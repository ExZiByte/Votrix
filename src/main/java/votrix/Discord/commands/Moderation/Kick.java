package votrix.Discord.commands.Moderation;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import votrix.Discord.utils.Data;
import votrix.Discord.utils.RoleCheck;

import java.time.Instant;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Kick extends ListenerAdapter {

    public void onGuildMessageReceived(GuildMessageReceivedEvent event){
        String[] args = event.getMessage().getContentRaw().split("\\s+");
        Data data = new Data();
        RoleCheck rc = new RoleCheck();
        EmbedBuilder eb = new EmbedBuilder();
        EmbedBuilder success = new EmbedBuilder();
        EmbedBuilder kicked = new EmbedBuilder();
        if(args[0].equalsIgnoreCase(data.getPrefix() + "kick") || args[0].equalsIgnoreCase("***" + data.getPrefix() + "yeet***")){
            //event.getMessage().delete().queue();
            if(rc.isOwner(event) || rc.isDeveloper(event) || rc.isAdministrator(event)){
                if(args.length < 2){
                    eb.setDescription("You didn't specify enough arguments");
                    eb.setColor(0xff5555);
                    eb.setFooter("Insufficient Arguments", data.getSelfAvatar(event));
                    eb.setTimestamp(Instant.now());

                    event.getChannel().sendMessage(eb.build()).queue((message) -> {
                        message.delete().queueAfter(15, TimeUnit.SECONDS);
                        eb.clear();
                    });
                }else if (args.length < 3) {
                    Member mentioned = event.getMessage().getMentionedMembers().get(0);

                    kicked.setDescription("You've been kicked from: " + event.getGuild().getName()
                        + "\n\nReason: \n```\nThere was no reason specified\n```");
                    kicked.setColor(0xff5555);
                    kicked.setFooter(event.getJDA().getSelfUser().getName() + " Kicked",
                        data.getSelfAvatar(event));
                    kicked.setTimestamp(Instant.now());

                    eb.setDescription("You've kicked: " + mentioned.getAsMention() + "\n\nReason:\n```\nNo reason specified\n```");
                    eb.setColor(0x4fff45);
                    eb.setFooter(event.getJDA().getSelfUser().getName() + " Kick",
                        data.getSelfAvatar(event));
                    eb.setTimestamp(Instant.now());

                    mentioned.getUser().openPrivateChannel().queue((channel) -> {
                        channel.sendMessage(kicked.build()).queue();
                        kicked.clear();

                        event.getChannel().sendMessage(eb.build()).queue((message) -> {
                            message.delete().queueAfter(20, TimeUnit.SECONDS);
                            eb.clear();
                            event.getGuild().getController().kick(mentioned, "No reason specified").queue();
                        });
                    });

                } else {
                    Member mentioned = event.getMessage().getMentionedMembers().get(0);
                    String reason = Arrays.stream(args).skip(2).collect(Collectors.joining(" "));

                    kicked.setDescription("You've been kicked from: " + event.getGuild().getName() + "\n\nReason:\n```\n" + reason + "\n```");
                    kicked.setColor(0xff5555);
                    kicked.setFooter(event.getJDA().getSelfUser().getName() + " Kicked",
                        data.getSelfAvatar(event));
                    kicked.setTimestamp(Instant.now());

                    eb.setDescription("You've kicked: " + mentioned.getAsMention() + " \n\nReason: \n```\n" + reason + "\n```");
                    eb.setColor(0x4fff45);
                    eb.setFooter(event.getJDA().getSelfUser().getName() + " kicked",
                        data.getSelfAvatar(event));
                    eb.setTimestamp(Instant.now());

                    mentioned.getUser().openPrivateChannel().queue((channel) -> {
                        channel.sendMessage(kicked.build()).queue();
                        kicked.clear();

                        event.getChannel().sendMessage(eb.build()).queue((message) -> {
                            message.delete().queueAfter(20, TimeUnit.SECONDS);
                            eb.clear();
                            event.getGuild().getController().kick(mentioned, reason).queue();
                        });
                    });

                }
            } else {
                eb.setDescription(event.getMember().getAsMention()
                    + ", You dont have the permission to kick members from this guild.");
                eb.setColor(0xff5555);
                eb.setFooter("Insufficient Permissions", data.getSelfAvatar(event));
                eb.setTimestamp(Instant.now());
                event.getChannel().sendMessage(eb.build()).queue((message) -> {
                    message.delete().queueAfter(15, TimeUnit.SECONDS);
                    eb.clear();
                });
            }
        }
    }

    public String getName(){
        return "Kick";
    }

    public String getDescription() {
        return "Kicks the specified member for the specified reason. If no reason is specified then the member is kicked for \"No Reason Specified\".";
    }

    public String getShortDescription() {
        return "Kicks the specified member";
    }

    public String getRequiredRoles() {
        return "Owner, Developer, Administrator";
    }

    public String getCommandSyntax() {
        return "```\n" + Data.getPrefix() + "kick {@member} [reason]\n```";
    }

    public boolean isDisabled() {
        return false;
    }

}
