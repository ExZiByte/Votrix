package votrix.Discord.commands.Moderation;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import votrix.Discord.utils.Data;
import votrix.Discord.utils.RoleCheck;
import votrix.Discord.utils.Time;

import java.awt.*;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

public class RoleAdd extends ListenerAdapter {

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split("\\s+");
        Data data = new Data();
        RoleCheck rc = new RoleCheck();
        EmbedBuilder eb = new EmbedBuilder();
        EmbedBuilder success = new EmbedBuilder();
        EmbedBuilder grantee = new EmbedBuilder();
        if (args[0].equalsIgnoreCase(data.getPrefix() + "addrole")) {
            event.getMessage().delete().queue();
            if (rc.isOwner(event) || rc.isDeveloper(event)) {
                if (args.length < 3) {
                    eb.setDescription("You didn't specify enough arguments. Please refer to " + data.getPrefix() + "help addrole.");
                    eb.setColor(0xff5555);
                    eb.setFooter("Insufficient Arguments", data.getSelfAvatar(event));
                    eb.setTimestamp(Instant.now());

                    event.getChannel().sendMessage(eb.build()).queue((message) -> {
                        message.delete().queueAfter(15, TimeUnit.SECONDS);
                        eb.clear();
                    });
                } else if (args.length > 2) {
                    Member mentioned = event.getMessage().getMentionedMembers().get(0);
                    Role role = event.getGuild().getRolesByName(args[1], true).get(0);

                    eb.setDescription("Successfully added the role " + role.getAsMention() + " to the member " + mentioned.getAsMention());
                    eb.setColor(new Color(data.getColor()));
                    eb.setTimestamp(Instant.now());
                    eb.setFooter("Votrix RoleAdd", data.getSelfAvatar(event));

                    success.setDescription(event.getMember().getAsMention() + " added the role " + role.getAsMention() + " to the member " + mentioned.getAsMention());
                    success.setColor(new Color(data.getColor()));
                    success.setTimestamp(Instant.now());
                    success.setFooter("Votrix RoleAdd Log", data.getSelfAvatar(event));

                    grantee.setDescription("You've been granted the role " + role.getAsMention() + " by " + event.getMember().getAsMention());
                    grantee.setColor(new Color(data.getColor()));
                    grantee.setTimestamp(Instant.now());
                    grantee.setFooter("Votrix Role Granted", data.getSelfAvatar(event));

                    mentioned.getUser().openPrivateChannel().complete().sendMessage(grantee.build()).queue((message) -> {
                        grantee.clear();
                        event.getChannel().sendMessage(eb.build()).queue((message1) -> {
                            message1.delete().queueAfter(15, TimeUnit.SECONDS);
                            eb.clear();
                            data.getLogChannel(event).sendMessage(success.build()).queue((message2) -> {
                                success.clear();
                            });
                        });
                    });

                    addRole(event, role, mentioned);
                } else if (args.length > 3) {
                    Member mentioned = event.getMessage().getMentionedMembers().get(0);
                    Role role = event.getGuild().getRolesByName(args[1], true).get(0);

                    eb.setDescription("Successfully added the role " + role.getAsMention() + " to the member " + mentioned.getAsMention() + "\n**Expires in:** " + Integer.parseInt(args[3].substring(0, args[3].length() - 1)) + Time.getTime(args[3]).name());
                    eb.setColor(new Color(data.getColor()));
                    eb.setTimestamp(Instant.now());
                    eb.setFooter("Votrix RoleAdd", data.getSelfAvatar(event));

                    success.setDescription(event.getMember().getAsMention() + " added the role " + role.getAsMention() + " to the member " + mentioned.getAsMention() + "\n**Will expire in:** " + Integer.parseInt(args[3].substring(0, args[3].length() - 1)) + Time.getTime(args[3]).name());
                    success.setColor(new Color(data.getColor()));
                    success.setTimestamp(Instant.now());
                    success.setFooter("Votrix RoleAdd Log", data.getSelfAvatar(event));

                    grantee.setDescription("You've been granted the role " + role.getAsMention() + " by " + event.getMember().getAsMention() + "\nThis role will be removed in " + Integer.parseInt(args[3].substring(0, args[3].length() - 1)) + Time.getTime(args[3]).name());
                    grantee.setColor(new Color(data.getColor()));
                    grantee.setTimestamp(Instant.now());
                    grantee.setFooter("Votrix Role Granted", data.getSelfAvatar(event));

                    mentioned.getUser().openPrivateChannel().complete().sendMessage(grantee.build()).queue((message) -> {
                        grantee.clear();
                        event.getChannel().sendMessage(eb.build()).queue((message1) -> {
                            message1.delete().queueAfter(15, TimeUnit.SECONDS);
                            eb.clear();
                            data.getLogChannel(event).sendMessage(success.build()).queue((message2) -> {
                                success.clear();
                            });
                        });
                    });

                    addRole(event, role, mentioned);
                    removeRoleAfterTimesUp(event, role, mentioned, args[3]);
                }
            }
        }
    }

    private static void addRole(GuildMessageReceivedEvent event, Role role, Member mentioned) {
        event.getGuild().getController().addSingleRoleToMember(mentioned, role).queue();
    }

    private static void removeRoleAfterTimesUp(GuildMessageReceivedEvent event, Role role, Member mentioned, String args) {
        event.getGuild().getController().removeSingleRoleFromMember(mentioned, event.getGuild().getRolesByName("Muted", true).get(0)).queueAfter(Integer.parseInt(args.substring(0, args.length() - 1)), Time.getTime(args));
        mentioned.getUser().openPrivateChannel().complete().sendMessage("The " + role.getAsMention() + "has been removed from you.").queueAfter(Integer.parseInt(args.substring(0, args.length() - 1)), Time.getTime(args));
    }

    public String getName() {
        return "Addrole";
    }

    public String getDescription() {
        return "Adds the specified role to the mentioned member. If you specify a time length the role will be removed after the specified amount of time.";
    }

    public String getShortDescription() {
        return "Adds a role to a mentioned member";
    }

    public String getCommandSyntax() {
        return "```\n" + Data.getPrefix() + "addrole {role} {@member} [time](time multiplier)\n```";
    }

    public String getRequiredRoles() {
        return "Owner, Developer";
    }

    public boolean isDisabled() {
        return false;
    }
}
