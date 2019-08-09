package votrix.Discord.commands.Moderation;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import votrix.Discord.Data;
import votrix.Discord.utils.RoleCheck;
import votrix.Discord.utils.Time;

import java.awt.*;
import java.time.Instant;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Tempmute extends ListenerAdapter {

    public void onGuildMessageReceived(GuildMessageReceivedEvent event){
        String[] args = event.getMessage().getContentRaw().split("\\s+");
        Data data = new Data();
        RoleCheck rc = new RoleCheck();
        EmbedBuilder eb = new EmbedBuilder();
        EmbedBuilder muted = new EmbedBuilder();
        EmbedBuilder success = new EmbedBuilder();
        Role muteRole;
        if(args[0].equalsIgnoreCase(data.getPrefix() + "tempmute")){
            event.getMessage().delete().queue();
            if(rc.isOwner(event) || rc.isDeveloper(event) || rc.isAdministrator(event) || rc.isModerator(event)){
                if(args.length < 2) {
                    eb.setDescription("You didn't specify enough arguments. Please refer to " + data.getPrefix() + "`tempmute help` for more information");
                    eb.setColor(new Color(data.getColor()));
                    eb.setTimestamp(Instant.now());
                    eb.setFooter("Insufficient Arguments", data.getSelfAvatar(event));

                    event.getChannel().sendMessage(eb.build()).queue((message) -> {
                        eb.clear();
                        message.delete().queueAfter(20, TimeUnit.SECONDS);
                    });
                } else if(args.length < 3){
                    if (args[1].equalsIgnoreCase("help")) {
                        eb.setDescription("Tempmute Help\n```\n" + data.getPrefix() + "tempmute @member <length><length multiplier> [reason]\n<> | Required\n[] | Optional\n```");
                        eb.setColor(new Color(data.getColor()));
                        eb.setTimestamp(Instant.now());
                        eb.setFooter("Votrix Tempmute Help", data.getSelfAvatar(event));

                        event.getChannel().sendMessage(eb.build()).queue((message) -> {
                            eb.clear();
                            message.delete().queueAfter(20, TimeUnit.SECONDS);
                        });
                    } else {
                        eb.setDescription("You didn't specify enough arguments. Please refer to " + data.getPrefix() + "`tempmute help` for more information");
                        eb.setColor(new Color(data.getColor()));
                        eb.setTimestamp(Instant.now());
                        eb.setFooter("Insufficient Arguments", data.getSelfAvatar(event));

                        event.getChannel().sendMessage(eb.build()).queue((message) -> {
                            eb.clear();
                            message.delete().queueAfter(20, TimeUnit.SECONDS);
                        });
                    }
                } else if(args.length == 3) {
                    Member mentioned = event.getMessage().getMentionedMembers().get(0);
                    Time time = new Time();

                    eb.setDescription("You have tempmuted " + mentioned.getAsMention() + "\n\nReason: \n```\nNo reason specified\n```");
                    eb.setColor(new Color(data.getColor()));
                    eb.setTimestamp(Instant.now());
                    eb.setFooter("Votrix Tempmute", data.getSelfAvatar(event));

                    muted.setDescription("You have been tempmuted \n\nDetails: ```\nGuild: " + event.getGuild().getName() + "\nReason: No reason specified\nExecutor: " + event.getMember().getEffectiveName() + "\nTime: " + args[2].substring(0, args[2].length() - 1) + time.getTime(args[2]).name() + "\n```");
                    muted.setColor(new Color(data.getColor()));
                    muted.setTimestamp(Instant.now());
                    muted.setFooter("Tempmuted", data.getSelfAvatar(event));

                    success.setDescription(event.getMember().getAsMention() + " has tempmuted " + mentioned.getAsMention() + "\n\nDetails: ```\nReason: No reason specified\nTime: " + args[2].substring(0, args[2].length() - 1) + " " + time.getTime(args[2]).name() + "\n```");
                    success.setColor(new Color(data.getColor()));
                    success.setTimestamp(Instant.now());
                    success.setFooter("Tempmute", data.getSelfAvatar(event));

                    mentioned.getUser().openPrivateChannel().complete().sendMessage(muted.build()).queue((message) -> {
                       muted.clear();
                       mute(event, args[2], mentioned);
                       message.delete().queueAfter(20, TimeUnit.SECONDS);
                       event.getChannel().sendMessage(eb.build()).queue((message1) -> {
                           eb.clear();
                           data.getLogChannel(event).sendMessage(success.build()).queue((message2) -> {
                               success.clear();
                           });
                       });
                    });
                }
            }
        }
    }

    private static void mute(GuildMessageReceivedEvent event ,String args, Member mentioned){
        event.getGuild().getController().addSingleRoleToMember(mentioned, event.getGuild().getRolesByName("Muted", true).get(0)).queue();

        ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
        ses.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                event.getGuild().getController().removeSingleRoleFromMember(mentioned, event.getGuild().getRolesByName("Muted", true).get(0)).queue();
                mentioned.getUser().openPrivateChannel().complete().sendMessage("You have been unmuted").queue();
            }
        }, 0, Integer.parseInt(args.substring(0, args.length() - 1)), Time.getTime(args));
    }

}
