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

public class RoleRemove extends ListenerAdapter {

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split("\\s+");
        Data data = new Data();
        RoleCheck rc = new RoleCheck();
        EmbedBuilder eb = new EmbedBuilder();
        EmbedBuilder success = new EmbedBuilder();
        EmbedBuilder grantee = new EmbedBuilder();
        if (args[0].equalsIgnoreCase(data.getPrefix() + "removerole")) {
            event.getMessage().delete().queue();
            if (rc.isOwner(event) || rc.isDeveloper(event)) {
                if (args.length < 3) {
                    eb.setDescription("You didn't specify enough arguments. Please refer to " + data.getPrefix() + "help removerole.");
                    eb.setColor(0xff5555);
                    eb.setFooter("Insufficient Arguments", data.getSelfAvatar(event));
                    eb.setTimestamp(Instant.now());

                    event.getChannel().sendMessage(eb.build()).queue((message) -> {
                        message.delete().queueAfter(15, TimeUnit.SECONDS);
                        eb.clear();
                    });
                } else if (args.length > 2 && args.length < 4) {
                    Member mentioned = event.getMessage().getMentionedMembers().get(0);
                    Role role = event.getGuild().getRolesByName(args[1], true).get(0);

                    eb.setDescription("Successfully removed the role " + role.getAsMention() + " from the member " + mentioned.getAsMention());
                    eb.setColor(new Color(data.getColor()));
                    eb.setTimestamp(Instant.now());
                    eb.setFooter("Votrix Role Remove", data.getSelfAvatar(event));

                    success.setDescription(event.getMember().getAsMention() + " removed the role " + role.getAsMention() + " from the member " + mentioned.getAsMention());
                    success.setColor(new Color(data.getColor()));
                    success.setTimestamp(Instant.now());
                    success.setFooter("Votrix Role Remove Log", data.getSelfAvatar(event));

                    grantee.setDescription("You've been removed from the role " + role.getAsMention() + " by " + event.getMember().getAsMention());
                    grantee.setColor(new Color(data.getColor()));
                    grantee.setTimestamp(Instant.now());
                    grantee.setFooter("Votrix Role Removed", data.getSelfAvatar(event));

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

                    removeRole(event, role, mentioned);
                } else if (args.length > 3) {
                    Member mentioned = event.getMessage().getMentionedMembers().get(0);
                    Role role = event.getGuild().getRolesByName(args[1], true).get(0);

                    eb.setDescription("Successfully removed the role " + role.getAsMention() + " from the member " + mentioned.getAsMention() + "\n**Expires in:** " + Integer.parseInt(args[3].substring(0, args[3].length() - 1)) + " " + Time.getTime(args[3]).name());
                    eb.setColor(new Color(data.getColor()));
                    eb.setTimestamp(Instant.now());
                    eb.setFooter("Votrix Role Remove", data.getSelfAvatar(event));

                    success.setDescription(event.getMember().getAsMention() + " removed the role " + role.getAsMention() + " from the member " + mentioned.getAsMention() + "\n**Will expire in:** " + Integer.parseInt(args[3].substring(0, args[3].length() - 1)) + " " + Time.getTime(args[3]).name());
                    success.setColor(new Color(data.getColor()));
                    success.setTimestamp(Instant.now());
                    success.setFooter("Votrix Role Remove Log", data.getSelfAvatar(event));

                    grantee.setDescription("You've been removed from the role " + role.getAsMention() + " by " + event.getMember().getAsMention() + "\nThis role will be readded in " + Integer.parseInt(args[3].substring(0, args[3].length() - 1)) + " " + Time.getTime(args[3]).name());
                    grantee.setColor(new Color(data.getColor()));
                    grantee.setTimestamp(Instant.now());
                    grantee.setFooter("Votrix Role Removed", data.getSelfAvatar(event));

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

                    removeRole(event, role, mentioned);
                    reAddRoleAfterTimesUp(event, role, mentioned, args[3]);
                }
            } else {
                eb.setDescription(event.getMember().getAsMention() + " you don't have the permissions to edit roles on this guild");
                eb.setColor(new Color(data.getColor()));
                eb.setTimestamp(Instant.now());
                eb.setFooter("Insufficient Permissions", data.getSelfAvatar(event));

                event.getChannel().sendMessage(eb.build()).queue((message) -> {
                    message.delete().queueAfter(15, TimeUnit.SECONDS);
                    eb.clear();
                });
            }
        }
    }

    private static void removeRole(GuildMessageReceivedEvent event, Role role, Member mentioned) {
        event.getGuild().getController().removeSingleRoleFromMember(mentioned, role).queue();
    }

    private static void reAddRoleAfterTimesUp(GuildMessageReceivedEvent event, Role role, Member mentioned, String args) {
        event.getGuild().getController().removeSingleRoleFromMember(mentioned, event.getGuild().getRolesByName("Muted", true).get(0)).queueAfter(Integer.parseInt(args.substring(0, args.length() - 1)), Time.getTime(args));
        mentioned.getUser().openPrivateChannel().complete().sendMessage("The " + role.getAsMention() + "has been removed from you.").queueAfter(Integer.parseInt(args.substring(0, args.length() - 1)), Time.getTime(args));
    }

    public String getName() {
        return "Removerole";
    }

    public String getDescription() {
        return "Removes the specified role from the mentioned member. If you specify a time length the role will be readded after the specified amount of time.";
    }

    public String getShortDescription() {
        return "Removes a role from a mentioned member";
    }

    public String getCommandSyntax() {
        return "```\n" + Data.getPrefix() + "removerole {role} {@member} [time](time multiplier)\n```";
    }

    public String getRequiredRoles() {
        return "Owner, Developer";
    }

    public boolean isDisabled() {
        return false;
    }
}
