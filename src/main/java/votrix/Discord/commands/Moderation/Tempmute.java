package votrix.Discord.commands.Moderation;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import votrix.Discord.utils.*;

import java.awt.*;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Tempmute extends ListenerAdapter {

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split("\\s+");
        Data data = new Data();
        RoleCheck rc = new RoleCheck();
        RoleCreate create = new RoleCreate();
        Embeds embeds = new Embeds();
        EmbedBuilder eb = new EmbedBuilder();
        if (args[0].equalsIgnoreCase(data.getPrefix() + "tempmute")) {
            event.getMessage().delete().queue();
            if (rc.isOwner(event) || rc.isDeveloper(event) || rc.isAdministrator(event) || rc.isModerator(event)) {
                if (args.length < 2) {
                    eb.setDescription("You didn't specify enough arguments. Please refer to " + data.getPrefix() + "`tempmute help` for more information");
                    eb.setColor(new Color(data.getColor()));
                    eb.setTimestamp(Instant.now());
                    eb.setFooter("Insufficient Arguments", data.getSelfAvatar(event));
                    event.getChannel().sendMessage(eb.build()).queue((message) -> {
                        eb.clear();
                        message.delete().queueAfter(20, TimeUnit.SECONDS);
                    });
                } else if (args.length < 3) {
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
                } else if (args.length == 3) {
                    Member mentioned = event.getMessage().getMentionedMembers().get(0);
                    List<Role> roles = event.getGuild().getRolesByName("Muted", true);
                    if (roles.size() < 1) {
                        create.createMutedRole(event);
                        event.getChannel().sendMessage("Your server didn't have a Muted role so I went ahead and created one for you and set the correct required permissions to each text channel").queue((message) -> {
                            message.delete().queueAfter(15, TimeUnit.SECONDS);
                        });
                        Time time = new Time();
                        String reason = "No reason specified";
                        mentioned.getUser().openPrivateChannel().complete().sendMessage(embeds.getMutedEmbed(event, args, reason).build()).queue((message) -> {
                            mute(event, args[2], mentioned);
                        });
                        event.getChannel().sendMessage(embeds.getTempmuteEmbed(event, args, reason).build()).queue((message1) -> {
                            message1.delete().queueAfter(20, TimeUnit.SECONDS);
                        });
                        data.getLogChannel(event).sendMessage(embeds.getMuteSuccessEmbed(event, args, reason).build()).queue();
                    } else {
                        if (mentioned.getRoles().contains(event.getGuild().getRolesByName("Muted", false).get(0))) {
                            event.getChannel().sendMessage(embeds.getAlreadyMutedEmbed(event).build()).queue((message) -> {
                                message.delete().queueAfter(20, TimeUnit.SECONDS);
                            });
                        } else {
                            Time time = new Time();
                            String reason = "No reason specified";
                            mentioned.getUser().openPrivateChannel().complete().sendMessage(embeds.getMutedEmbed(event, args, reason).build()).queue((message) -> {
                                mute(event, args[2], mentioned);
                            });
                            event.getChannel().sendMessage(embeds.getTempmuteEmbed(event, args, reason).build()).queue((message1) -> {
                                message1.delete().queueAfter(20, TimeUnit.SECONDS);
                            });
                            data.getLogChannel(event).sendMessage(embeds.getMuteSuccessEmbed(event, args, reason).build()).queue();
                        }
                    }
                } else if (args.length > 3) {
                    Member mentioned = event.getMessage().getMentionedMembers().get(0);
                    if (args[3].equalsIgnoreCase("-s")) {
                        String reason = Arrays.stream(args).skip(4).collect(Collectors.joining(" "));
                        List<Role> roles = event.getGuild().getRolesByName("Muted", true);
                        if (roles.size() < 1) {
                            create.createMutedRole(event);
                            event.getChannel().sendMessage("Your server didn't have a Muted role so I went ahead and created one for you and set the correct required permissions to each text channel").queue((message) -> {
                                message.delete().queueAfter(15, TimeUnit.SECONDS);
                            });
                            Time time = new Time();
                            mentioned.getUser().openPrivateChannel().complete().sendMessage(embeds.getMutedEmbed(event, args, reason).build()).queue((message) -> {
                                mute(event, args[2], mentioned);
                            });
                            event.getChannel().sendMessage(embeds.getTempmuteEmbed(event, args, reason).build()).queue((message1) -> {
                                message1.delete().queueAfter(20, TimeUnit.SECONDS);
                            });
                            reason = "";
                        } else {
                            if (mentioned.getRoles().contains(event.getGuild().getRolesByName("Muted", false).get(0))) {
                                event.getChannel().sendMessage(embeds.getAlreadyMutedEmbed(event).build()).queue((message) -> {
                                    message.delete().queueAfter(20, TimeUnit.SECONDS);
                                });
                            } else {
                                Time time = new Time();
                                mentioned.getUser().openPrivateChannel().complete().sendMessage(embeds.getMutedEmbed(event, args, reason).build()).queue((message) -> {
                                    mute(event, args[2], mentioned);
                                });
                                event.getChannel().sendMessage(embeds.getTempmuteEmbed(event, args, reason).build()).queue((message1) -> {
                                    message1.delete().queueAfter(20, TimeUnit.SECONDS);
                                });
                                reason = "";
                            }
                        }
                    } else {
                        String reason = Arrays.stream(args).skip(3).collect(Collectors.joining(" "));
                        List<Role> roles = event.getGuild().getRolesByName("Muted", true);
                        if (roles.size() < 1) {
                            create.createMutedRole(event);
                            event.getChannel().sendMessage("Your server didn't have a Muted role so I went ahead and created one for you and set the correct required permissions to each text channel").queue((message) -> {
                                message.delete().queueAfter(15, TimeUnit.SECONDS);
                            });
                            Time time = new Time();
                            mentioned.getUser().openPrivateChannel().complete().sendMessage(embeds.getMutedEmbed(event, args, reason).build()).queue((message) -> {
                                mute(event, args[2], mentioned);
                            });
                            event.getChannel().sendMessage(embeds.getTempmuteEmbed(event, args, reason).build()).queue((message1) -> {
                                message1.delete().queueAfter(20, TimeUnit.SECONDS);
                            });
                            data.getLogChannel(event).sendMessage(embeds.getMuteSuccessEmbed(event, args, reason).build()).queue();
                            reason = "";
                        } else {
                            if (mentioned.getRoles().contains(event.getGuild().getRolesByName("Muted", false).get(0))) {
                                event.getChannel().sendMessage(embeds.getAlreadyMutedEmbed(event).build()).queue((message) -> {
                                    message.delete().queueAfter(20, TimeUnit.SECONDS);
                                });
                            } else {
                                mentioned.getUser().openPrivateChannel().complete().sendMessage(embeds.getMutedEmbed(event, args, reason).build()).queue((message) -> {
                                        mute(event, args[2], mentioned);
                                });
                                event.getChannel().sendMessage(embeds.getTempmuteEmbed(event, args, reason).build()).queue((message1) -> {
                                    message1.delete().queueAfter(20, TimeUnit.SECONDS);
                                });
                                data.getLogChannel(event).sendMessage(embeds.getMuteSuccessEmbed(event, args, reason).build()).queue();
                                reason = "";
                            }
                        }
                    }
                }
            }
        }
    }
    private static void mute(GuildMessageReceivedEvent event, String args, Member mentioned) {
        event.getGuild().getController().addSingleRoleToMember(mentioned, event.getGuild().getRolesByName("Muted", true).get(0)).queue();
        event.getGuild().getController().removeSingleRoleFromMember(mentioned, event.getGuild().getRolesByName("Muted", true).get(0)).queueAfter(Integer.parseInt(args.substring(0, args.length() - 1)), Time.getTime(args));
        mentioned.getUser().openPrivateChannel().complete().sendMessage("You have been unmuted").queueAfter(Integer.parseInt(args.substring(0, args.length() - 1)), Time.getTime(args));
    }

    public String getName() {
        return "Tempmute";
    }

    public String getDescription() {
        return "Temporarily mutes the specified member for the specified amount of time and the specified reason. If no reason is specifed then the member is muted for \"No Reason Specified\"";
    }

    public String getRequiredRoles() {
        return "Owner, Developer, Administrator, Moderator";
    }

    public String getCommandSyntax() {
        return "```\n" + Data.getPrefix() + "tempmute {@member} {time}{time multiplier} [reason]\n```";
    }

}
