package votrix.Discord.commands;

import java.time.Instant;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Invite;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import votrix.Discord.Data;
import votrix.Discord.utils.RoleCheck;
// Lines 41 and 70 need special attention in this class

public class Softban extends ListenerAdapter {

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split("\\s+");
        Data data = new Data();
        RoleCheck rc = new RoleCheck();
        EmbedBuilder eb = new EmbedBuilder();
        EmbedBuilder banned = new EmbedBuilder();

        if (args[0].equalsIgnoreCase(data.prefix + "softban")) {
            event.getMessage().delete().queue();
            if(rc.isOwner(event) || rc.isDeveloper(event) || rc.isAdministrator(event)){
                if (args.length < 2) {
                    eb.setDescription("You didn't specify enough arguments \n" + data.prefix + "softban @<member>");
                    eb.setColor(0xff5555);
                    eb.setFooter("Insufficient Arguments", event.getJDA().getSelfUser().getEffectiveAvatarUrl());
                    eb.setTimestamp(Instant.now());
    
                    event.getChannel().sendMessage(eb.build()).queue((message) -> {
                        message.delete().queueAfter(15, TimeUnit.SECONDS);
                        eb.clear();
                    });
                } else if(args.length < 3){
                    Invite inviteLink = event.getGuild().getTextChannelById("594722722903490573").createInvite().setMaxUses(1).setMaxAge(null).complete();
                    Member mentioned = event.getMessage().getMentionedMembers().get(0);

                    banned.setDescription("You've been softbanned from: " + event.getGuild().getName()
                            + "\n\n Reason:\n```\nThere was no reason specified\n```\nHere is an invite link to get back on the server: \n[" + event.getGuild().getName() + " Invite](https://discord.gg/" + inviteLink.toString().replace("Invite(", "").replace(")","") + " \"Invite Link for " + event.getGuild().getName() + "\")");
                    banned.setColor(0xff5555);
                    banned.setFooter(event.getJDA().getSelfUser().getName() + " Softban",
                            event.getJDA().getSelfUser().getEffectiveAvatarUrl());
                    banned.setTimestamp(Instant.now());

                    eb.setDescription("You've softbanned: " + mentioned.getAsMention() + " \n\nReason: \n```\nNo reason specified\n```");
                    eb.setColor(0x4fff45);
                    eb.setFooter(event.getJDA().getSelfUser().getName() + " Softban",
                            event.getJDA().getSelfUser().getEffectiveAvatarUrl());
                    eb.setTimestamp(Instant.now());

                    mentioned.getUser().openPrivateChannel().queue((channel) -> {
                        channel.sendMessage(banned.build()).queue();
                        banned.clear();

                        event.getChannel().sendMessage(eb.build()).queue((message) -> {
                            message.delete().queueAfter(20, TimeUnit.SECONDS);
                            eb.clear();
                            event.getGuild().getController().ban(mentioned, 7, "No Reason Specified").queue();
                            event.getGuild().getController().unban(mentioned.getUser().getId()).queueAfter(2, TimeUnit.SECONDS);
                        });
                    });

                } else {
                    Invite inviteLink = event.getGuild().getTextChannelById("594722722903490573").createInvite().setMaxUses(1).setMaxAge(null).complete();
                    Member mentioned = event.getMessage().getMentionedMembers().get(0);
                    String reason = Arrays.stream(args).skip(2).collect(Collectors.joining(" "));

                    banned.setDescription("You've been softbanned from: " + event.getGuild().getName() + " \n\nReason: \n```\n" + reason + "\n```\nHere is an invite link to get back on the server: \n[" + event.getGuild().getName().toString() + " Link](https://discord.gg/" + inviteLink.toString().replace("Invite(", "").replace(")","") + " \"Invite Link for " + event.getGuild().getName() + "\")");
                    banned.setColor(0xff5555);
                    banned.setFooter(event.getJDA().getSelfUser().getName() + " Softbanned",
                            event.getJDA().getSelfUser().getEffectiveAvatarUrl());
                    banned.setTimestamp(Instant.now());

                    eb.setDescription("You've softbanned: " + mentioned.getAsMention() + " \n\nReason:\n```\n" + reason + "\n```");
                    eb.setColor(0x4fff45);
                    eb.setFooter(event.getJDA().getSelfUser().getName() + " Softban",
                            event.getJDA().getSelfUser().getEffectiveAvatarUrl());
                    eb.setTimestamp(Instant.now());

                    mentioned.getUser().openPrivateChannel().queue((channel) -> {
                        channel.sendMessage(banned.build()).queue();
                        banned.clear();

                        event.getChannel().sendMessage(eb.build()).queue((message) -> {
                            message.delete().queueAfter(20, TimeUnit.SECONDS);
                            eb.clear();
                            event.getGuild().getController().ban(mentioned, 7, reason).queue();
                            event.getGuild().getController().unban(mentioned.getUser().getId()).queueAfter(2, TimeUnit.SECONDS);
                        });
                    });

                }
            } else {
                eb.setDescription(event.getMember().getAsMention()
                            + ", You don't have the permission to ban members from this guild.");
                    eb.setColor(0xff5555);
                    eb.setFooter("Insufficient Permissions", event.getJDA().getSelfUser().getEffectiveAvatarUrl());
                    eb.setTimestamp(Instant.now());
                    event.getChannel().sendMessage(eb.build()).queue((message) -> {
                        message.delete().queueAfter(15, TimeUnit.SECONDS);
                        eb.clear();
                    });
            }
        }
    }

}
