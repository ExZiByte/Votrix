package votrix.Discord.commands.Moderation;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import votrix.Discord.utils.Data;
import votrix.Discord.utils.RoleCheck;

import java.awt.*;
import java.time.Instant;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Unmute extends ListenerAdapter {

    public void onGuildMessageReceived(GuildMessageReceivedEvent event){
        String[] args = event.getMessage().getContentRaw().split("\\s+");
        Data data = new Data();
        RoleCheck rc = new RoleCheck();
        EmbedBuilder eb = new EmbedBuilder();
        EmbedBuilder unmuted = new EmbedBuilder();
        EmbedBuilder success = new EmbedBuilder();

        if(args[0].equalsIgnoreCase(data.getPrefix() + "unmute")){
            if(rc.isOwner(event) || rc.isDeveloper(event) || rc.isAdministrator(event) || rc.isModerator(event)){
                if(args.length < 2) {
                    eb.setDescription("You didn't specify enough arguments \n" + data.getPrefix() + "unmute @<member>");
                    eb.setColor(new Color(data.getColor()));
                    eb.setFooter("Insufficient Arguments", data.getSelfAvatar(event));
                    eb.setTimestamp(Instant.now());

                    event.getChannel().sendMessage(eb.build()).queue((message) -> {
                        message.delete().queueAfter(15, TimeUnit.SECONDS);
                        eb.clear();
                    });
                } else if(args.length >= 2){
                    Member mentioned = event.getMessage().getMentionedMembers().get(0);

                    eb.setDescription("Successfully unmuted " + mentioned.getAsMention());
                    eb.setColor(new Color(data.getColor()));
                    eb.setTimestamp(Instant.now());
                    eb.setFooter("Votrix Unmute", data.getSelfAvatar(event));

                    success.setDescription(event.getMember().getAsMention() + " unmuted " + mentioned.getAsMention());
                    success.setColor(new Color(data.getColor()));
                    success.setTimestamp(Instant.now());
                    success.setFooter("Votrix Unmute Log", data.getSelfAvatar(event));

                    unmuted.setDescription("You've been unmuted");
                    unmuted.setColor(new Color(data.getColor()));
                    unmuted.setTimestamp(Instant.now());
                    unmuted.setFooter("Votrix Unmute", data.getSelfAvatar(event));

                    event.getGuild().getController().removeSingleRoleFromMember(mentioned, event.getGuild().getRolesByName("Muted", true).get(0)).queue();

                    mentioned.getUser().openPrivateChannel().complete().sendMessage(unmuted.build()).queue((message) -> {
                        event.getChannel().sendMessage(eb.build()).queue((message1) -> {
                            eb.clear();
                            message.delete().queueAfter(15, TimeUnit.SECONDS);
                            data.getLogChannel(event).sendMessage(success.build()).queue((message2) -> {
                                success.clear();
                            });
                        });
                    });

                }
            }
        }
    }

    public String getName() {
        return "Unmute";
    }

    public String getDescription() {
        return "Unmutes the specified member";
    }

    public String getRequiredRoles() {
        return "Owner, Developer, Administrator, Moderator";
    }

    public String getCommandSyntax() {
        return "```\n" + Data.getPrefix() + "unmute {@member}\n```";
    }

}
