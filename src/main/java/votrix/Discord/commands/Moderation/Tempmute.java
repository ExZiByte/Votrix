package votrix.Discord.commands.Moderation;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Channel;
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
import java.util.EnumSet;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Tempmute extends ListenerAdapter {

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split("\\s+");
        Data data = new Data();
        RoleCheck rc = new RoleCheck();
        EmbedBuilder eb = new EmbedBuilder();
        EmbedBuilder muted = new EmbedBuilder();
        EmbedBuilder success = new EmbedBuilder();
        Role muteRole;
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

                        muteRole = event.getGuild().getController().createRole().setName("Muted").setColor(0xffffff).setMentionable(false).complete();

                        muteRole.getManager().revokePermissions(Permission.MESSAGE_TTS, Permission.MESSAGE_WRITE,
                            Permission.VOICE_DEAF_OTHERS, Permission.VOICE_MOVE_OTHERS,
                            Permission.VOICE_MUTE_OTHERS, Permission.VOICE_SPEAK, Permission.VOICE_USE_VAD,
                            Permission.NICKNAME_CHANGE, Permission.MESSAGE_ADD_REACTION).queue();

                        for (Channel channel : event.getGuild().getTextChannels()) {
                            if (!channel.getParent().getId().equals("579392397189054465")) {
                                channel.getManager().putPermissionOverride(muteRole, EnumSet.of(Permission.MESSAGE_HISTORY, Permission.MESSAGE_READ), EnumSet.of(Permission.MESSAGE_WRITE)).queue();
                            }
                        }

                        event.getChannel().sendMessage("Your server didn't have a Muted role so I went ahead and created one for you and set the correct required permissions to each text channel").queue((message) -> {
                            message.delete().queueAfter(15, TimeUnit.SECONDS);
                        });
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
                        success.setFooter("Votrix Tempmute Log", data.getSelfAvatar(event));

                        mentioned.getUser().openPrivateChannel().complete().sendMessage(muted.build()).queue((message) -> {
                            muted.clear();
                            mute(event, args[2], mentioned);
                            event.getChannel().sendMessage(eb.build()).queue((message1) -> {
                                eb.clear();
                                message1.delete().queueAfter(20, TimeUnit.SECONDS);
                                data.getLogChannel(event).sendMessage(success.build()).queue((message2) -> {
                                    success.clear();
                                });
                            });
                        });

                    } else {

                        if (mentioned.getRoles().contains(event.getGuild().getRolesByName("Muted", false).get(0))) {
                            eb.setDescription(mentioned.getAsMention() + " is already muted");
                            eb.setColor(new Color(data.getColor()));
                            eb.setTimestamp(Instant.now());
                            eb.setFooter("Already Muted", data.getSelfAvatar(event));

                            event.getChannel().sendMessage(eb.build()).queue((message) -> {
                                message.delete().queueAfter(20, TimeUnit.SECONDS);
                            });

                        } else {
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
                            success.setFooter("Votrix Tempmute Log", data.getSelfAvatar(event));

                            mentioned.getUser().openPrivateChannel().complete().sendMessage(muted.build()).queue((message) -> {
                                muted.clear();
                                mute(event, args[2], mentioned);
                                event.getChannel().sendMessage(eb.build()).queue((message1) -> {
                                    eb.clear();
                                    message1.delete().queueAfter(20, TimeUnit.SECONDS);
                                    data.getLogChannel(event).sendMessage(success.build()).queue((message2) -> {
                                        success.clear();
                                    });
                                });
                            });
                        }
                    }
                } else if (args.length > 3) {
                    if (args[3].equalsIgnoreCase("-s")) {
                        String reason = Arrays.stream(args).skip(4).collect(Collectors.joining(" "));
                        List<Role> roles = event.getGuild().getRolesByName("Muted", true);
                        Member mentioned = event.getMessage().getMentionedMembers().get(0);
                        if (roles.size() < 1) {

                            muteRole = event.getGuild().getController().createRole().setName("Muted").setColor(0xffffff).setMentionable(false).complete();

                            muteRole.getManager().revokePermissions(Permission.MESSAGE_TTS, Permission.MESSAGE_WRITE,
                                Permission.VOICE_DEAF_OTHERS, Permission.VOICE_MOVE_OTHERS,
                                Permission.VOICE_MUTE_OTHERS, Permission.VOICE_SPEAK, Permission.VOICE_USE_VAD,
                                Permission.NICKNAME_CHANGE, Permission.MESSAGE_ADD_REACTION).queue();

                            for (Channel channel : event.getGuild().getTextChannels()) {
                                if (!channel.getParent().getId().equals("579392397189054465")) {
                                    channel.getManager().putPermissionOverride(muteRole, EnumSet.of(Permission.MESSAGE_HISTORY, Permission.MESSAGE_READ), EnumSet.of(Permission.MESSAGE_WRITE)).queue();
                                }
                            }

                            event.getChannel().sendMessage("Your server didn't have a Muted role so I went ahead and created one for you and set the correct required permissions to each text channel").queue((message) -> {
                                message.delete().queueAfter(15, TimeUnit.SECONDS);
                            });
                            Time time = new Time();

                            eb.setDescription("You have tempmuted " + mentioned.getAsMention() + "\n\nReason: \n```\n" + reason + "\n```");
                            eb.setColor(new Color(data.getColor()));
                            eb.setTimestamp(Instant.now());
                            eb.setFooter("Votrix Tempmute", data.getSelfAvatar(event));

                            muted.setDescription("You have been tempmuted \n\nDetails: ```\nGuild: " + event.getGuild().getName() + "\nReason: " + reason + "\nExecutor: " + event.getMember().getEffectiveName() + "\nTime: " + args[2].substring(0, args[2].length() - 1) + time.getTime(args[2]).name() + "\n```");
                            muted.setColor(new Color(data.getColor()));
                            muted.setTimestamp(Instant.now());
                            muted.setFooter("Tempmuted", data.getSelfAvatar(event));

                            mentioned.getUser().openPrivateChannel().complete().sendMessage(muted.build()).queue((message) -> {
                                muted.clear();
                                mute(event, args[2], mentioned);
                                event.getChannel().sendMessage(eb.build()).queue((message1) -> {
                                    eb.clear();
                                    message1.delete().queueAfter(20, TimeUnit.SECONDS);
                                });
                            });

                            reason = "";

                        } else {
                            if (mentioned.getRoles().contains(event.getGuild().getRolesByName("Muted", false).get(0))) {
                                eb.setDescription(mentioned.getAsMention() + " is already muted");
                                eb.setColor(new Color(data.getColor()));
                                eb.setTimestamp(Instant.now());
                                eb.setFooter("Already Muted", data.getSelfAvatar(event));

                                event.getChannel().sendMessage(eb.build()).queue((message) -> {
                                    message.delete().queueAfter(20, TimeUnit.SECONDS);
                                });

                            } else {
                                Time time = new Time();

                                eb.setDescription("You have tempmuted " + mentioned.getAsMention() + "\n\nReason: \n```\n" + reason + "\n```");
                                eb.setColor(new Color(data.getColor()));
                                eb.setTimestamp(Instant.now());
                                eb.setFooter("Votrix Tempmute", data.getSelfAvatar(event));

                                muted.setDescription("You have been tempmuted \n\nDetails: ```\nGuild: " + event.getGuild().getName() + "\nReason: " + reason + "\nExecutor: " + event.getMember().getEffectiveName() + "\nTime: " + args[2].substring(0, args[2].length() - 1) + time.getTime(args[2]).name() + "\n```");
                                muted.setColor(new Color(data.getColor()));
                                muted.setTimestamp(Instant.now());
                                muted.setFooter("Tempmuted", data.getSelfAvatar(event));

                                mentioned.getUser().openPrivateChannel().complete().sendMessage(muted.build()).queue((message) -> {
                                    muted.clear();
                                    mute(event, args[2], mentioned);
                                    event.getChannel().sendMessage(eb.build()).queue((message1) -> {
                                        eb.clear();
                                        message1.delete().queueAfter(20, TimeUnit.SECONDS);
                                    });
                                });
                                reason = "";
                            }
                        }

                    } else {
                        String reason = Arrays.stream(args).skip(3).collect(Collectors.joining(" "));
                        List<Role> roles = event.getGuild().getRolesByName("Muted", true);
                        if (roles.size() < 1) {

                            muteRole = event.getGuild().getController().createRole().setName("Muted").setColor(0xffffff).setMentionable(false).complete();

                            muteRole.getManager().revokePermissions(Permission.MESSAGE_TTS, Permission.MESSAGE_WRITE,
                                Permission.VOICE_DEAF_OTHERS, Permission.VOICE_MOVE_OTHERS,
                                Permission.VOICE_MUTE_OTHERS, Permission.VOICE_SPEAK, Permission.VOICE_USE_VAD,
                                Permission.NICKNAME_CHANGE, Permission.MESSAGE_ADD_REACTION).queue();

                            for (Channel channel : event.getGuild().getTextChannels()) {
                                if (!channel.getParent().getId().equals("579392397189054465")) {
                                    channel.getManager().putPermissionOverride(muteRole, EnumSet.of(Permission.MESSAGE_HISTORY, Permission.MESSAGE_READ), EnumSet.of(Permission.MESSAGE_WRITE)).queue();
                                }
                            }

                            event.getChannel().sendMessage("Your server didn't have a Muted role so I went ahead and created one for you and set the correct required permissions to each text channel").queue((message) -> {
                                message.delete().queueAfter(15, TimeUnit.SECONDS);
                            });

                            Member mentioned = event.getMessage().getMentionedMembers().get(0);
                            Time time = new Time();

                            eb.setDescription("You have tempmuted " + mentioned.getAsMention() + "\n\nReason: \n```\n" + reason + "\n```");
                            eb.setColor(new Color(data.getColor()));
                            eb.setTimestamp(Instant.now());
                            eb.setFooter("Votrix Tempmute", data.getSelfAvatar(event));

                            muted.setDescription("You have been tempmuted \n\nDetails: ```\nGuild: " + event.getGuild().getName() + "\nReason: " + reason + "\nExecutor: " + event.getMember().getEffectiveName() + "\nTime: " + args[2].substring(0, args[2].length() - 1) + time.getTime(args[2]).name() + "\n```");
                            muted.setColor(new Color(data.getColor()));
                            muted.setTimestamp(Instant.now());
                            muted.setFooter("Tempmuted", data.getSelfAvatar(event));

                            success.setDescription(event.getMember().getAsMention() + " has tempmuted " + mentioned.getAsMention() + "\n\nDetails: ```\nReason: " + reason + "\nTime: " + args[2].substring(0, args[2].length() - 1) + " " + time.getTime(args[2]).name() + "\n```");
                            success.setColor(new Color(data.getColor()));
                            success.setTimestamp(Instant.now());
                            success.setFooter("Votrix Tempmute Log", data.getSelfAvatar(event));

                            mentioned.getUser().openPrivateChannel().complete().sendMessage(muted.build()).queue((message) -> {
                                muted.clear();
                                mute(event, args[2], mentioned);
                                event.getChannel().sendMessage(eb.build()).queue((message1) -> {
                                    eb.clear();
                                    message1.delete().queueAfter(20, TimeUnit.SECONDS);
                                    data.getLogChannel(event).sendMessage(success.build()).queue((message2) -> {
                                        success.clear();
                                    });
                                });
                            });

                            reason = "";

                        } else {

                            Member mentioned = event.getMessage().getMentionedMembers().get(0);

                            if (mentioned.getRoles().contains(event.getGuild().getRolesByName("Muted", false).get(0))) {
                                eb.setDescription(mentioned.getAsMention() + " is already muted");
                                eb.setColor(new Color(data.getColor()));
                                eb.setTimestamp(Instant.now());
                                eb.setFooter("Already Muted", data.getSelfAvatar(event));

                                event.getChannel().sendMessage(eb.build()).queue((message) -> {
                                    message.delete().queueAfter(20, TimeUnit.SECONDS);
                                });

                            } else {
                                Time time = new Time();

                                eb.setDescription("You have tempmuted " + mentioned.getAsMention() + "\n\nReason: \n```\n" + reason + "\n```");
                                eb.setColor(new Color(data.getColor()));
                                eb.setTimestamp(Instant.now());
                                eb.setFooter("Votrix Tempmute", data.getSelfAvatar(event));

                                muted.setDescription("You have been tempmuted \n\nDetails: ```\nGuild: " + event.getGuild().getName() + "\nReason: " + reason + "\nExecutor: " + event.getMember().getEffectiveName() + "\nTime: " + args[2].substring(0, args[2].length() - 1) + time.getTime(args[2]).name() + "\n```");
                                muted.setColor(new Color(data.getColor()));
                                muted.setTimestamp(Instant.now());
                                muted.setFooter("Tempmuted", data.getSelfAvatar(event));

                                success.setDescription(event.getMember().getAsMention() + " has tempmuted " + mentioned.getAsMention() + "\n\nDetails: ```\nReason: " + reason + "\nTime: " + args[2].substring(0, args[2].length() - 1) + " " + time.getTime(args[2]).name() + "\n```");
                                success.setColor(new Color(data.getColor()));
                                success.setTimestamp(Instant.now());
                                success.setFooter("Votrix Tempmute Log", data.getSelfAvatar(event));

                                mentioned.getUser().openPrivateChannel().complete().sendMessage(muted.build()).queue((message) -> {
                                    muted.clear();
                                    mute(event, args[2], mentioned);
                                    event.getChannel().sendMessage(eb.build()).queue((message1) -> {
                                        eb.clear();
                                        message1.delete().queueAfter(20, TimeUnit.SECONDS);
                                        data.getLogChannel(event).sendMessage(success.build()).queue((message2) -> {
                                            success.clear();
                                        });
                                    });
                                });
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

}
