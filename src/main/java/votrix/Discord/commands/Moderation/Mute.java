package votrix.Discord.commands.Moderation;

import java.awt.*;
import java.time.Instant;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import votrix.Discord.Data;
import votrix.Discord.utils.RoleCheck;

public class Mute extends ListenerAdapter {

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split("\\s+");
        Data data = new Data();
        RoleCheck rc = new RoleCheck();
        EmbedBuilder eb = new EmbedBuilder();

        EmbedBuilder muted = new EmbedBuilder();
        EmbedBuilder success = new EmbedBuilder();
        Role muteRole;

        if (args[0].equalsIgnoreCase(data.getPrefix(event) + "mute")) {
                event.getMessage().delete().queue();
            if (rc.isOwner(event) || rc.isDeveloper(event) || rc.isAdministrator(event) || rc.isModerator(event)) {
                if (args.length < 2) {
                    eb.setDescription("You didn't specify enough arguments \n" + data.getPrefix(event) + "mute @<member>");
                    eb.setColor(new Color(data.getColor()));
                    eb.setFooter("Insufficient Arguments", data.getSelfAvatar(event));
                    eb.setTimestamp(Instant.now());

                    event.getChannel().sendMessage(eb.build()).queue((message) -> {
                        message.delete().queueAfter(15, TimeUnit.SECONDS);
                        eb.clear();
                    });
                } else if (args.length < 3) {

                    List<Role> roles = event.getGuild().getRolesByName("Muted", true);

                    if (roles.size() < 1) {

                        muteRole = event.getGuild().getController().createRole().setName("Muted").setColor(0xffffff).setMentionable(false).complete();

                        muteRole.getManager().revokePermissions(Permission.MESSAGE_TTS, Permission.MESSAGE_WRITE,
                                        Permission.VOICE_DEAF_OTHERS, Permission.VOICE_MOVE_OTHERS,
                                        Permission.VOICE_MUTE_OTHERS, Permission.VOICE_SPEAK, Permission.VOICE_USE_VAD,
                                        Permission.NICKNAME_CHANGE, Permission.MESSAGE_ADD_REACTION).queue();

                        for (Channel channel : event.getGuild().getTextChannels()) {
                            if(!    channel.getParent().getId().equals("579392397189054465")){
                                channel.getManager().putPermissionOverride(muteRole, EnumSet.of(Permission.MESSAGE_HISTORY, Permission.MESSAGE_READ), EnumSet.of(Permission.MESSAGE_WRITE)).queue();
                            }
                        }

                        event.getChannel().sendMessage("Your server didn't have a Muted role so I went ahead and created one for you and set the correct required permissions to each text channel").queue((message) -> {
                                    message.delete().queueAfter(15, TimeUnit.SECONDS);
                                });

                        Member mentioned = event.getMessage().getMentionedMembers().get(0);
                        // Build Information Embed to be sent to muted user
                        muted.setDescription("You've been muted on: " + event.getGuild().getName()
                                + "\n\nReason: \n```\nThere was no reason specified\n```");
                        muted.setColor(new Color(data.getColor()));
                        muted.setFooter(event.getJDA().getSelfUser().getName() + " Muted",
                                data.getSelfAvatar(event));
                        muted.setTimestamp(Instant.now());

                        // Build Information Embed to be sent to server channel
                        eb.setDescription("You've muted: " + mentioned.getAsMention() + "\n\nReason:\n```\nNo reason specified\n```");
                        eb.setColor(0x4fff45);
                        eb.setFooter(event.getJDA().getSelfUser().getName() + " Mute",
                                data.getSelfAvatar(event));
                        eb.setTimestamp(Instant.now());

                        success.setDescription(event.getMember().getAsMention() + " muted " + mentioned.getAsMention() + "\n\nReason: \n```No reason specified\n```");
                        success.setColor(new Color(data.getColor()));
                        success.setFooter(event.getJDA().getSelfUser().getName() + " Mute", data.getSelfAvatar(event));
                        success.setTimestamp(Instant.now());

                        mentioned.getUser().openPrivateChannel().queue((channel) -> {
                            channel.sendMessage(muted.build()).queue();
                            muted.clear();

                            event.getChannel().sendMessage(eb.build()).queue((message) -> {
                                message.delete().queueAfter(20, TimeUnit.SECONDS);
                                eb.clear();
                                data.getLogChannel(event).sendMessage(success.build()).queue((message2) -> {
                                    success.clear();
                                });
                                event.getGuild().getController().addSingleRoleToMember(mentioned, muteRole).queue();
                            });
                        });
                    } else {

                        muteRole = event.getGuild().getRolesByName("Muted", true).get(0);

                        for (Channel channel : event.getGuild().getTextChannels()) {
                            if(!channel.getParent().getId().equals("579392397189054465")){
                                channel.getManager().putPermissionOverride(muteRole, EnumSet.of(Permission.MESSAGE_HISTORY, Permission.MESSAGE_READ), EnumSet.of(Permission.MESSAGE_WRITE)).queue();
                            }
                        }
                        Member mentioned = event.getMessage().getMentionedMembers().get(0);
                        if (!mentioned.getRoles().contains(event.getGuild().getRoleById(muteRole.getId()))) {

                            // Build Information Embed to be sent to kicked user
                            muted.setDescription("You've been muted on: " + event.getGuild().getName()
                                    + "\n\nReason: \n```\nThere was no reason specified\n```");
                            muted.setColor(new Color(data.getColor()));
                            muted.setFooter(event.getJDA().getSelfUser().getName() + " Muted",
                                    data.getSelfAvatar(event));
                            muted.setTimestamp(Instant.now());

                            // Build Information Embed to be to server channel
                            eb.setDescription(
                                    "You've muted: " + mentioned.getAsMention() + "\n\nReason:\n```\nNo reason specified\n```");
                            eb.setColor(0x4fff45);
                            eb.setFooter(event.getJDA().getSelfUser().getName() + " Mute",
                                    data.getSelfAvatar(event));
                            eb.setTimestamp(Instant.now());

                            success.setDescription(event.getMember().getAsMention() + " muted " + mentioned.getAsMention() + "\n\nReason: \n```No reason specified\n```");
                            success.setColor(new Color(data.getColor()));
                            success.setFooter(event.getJDA().getSelfUser().getName() + " Mute", data.getSelfAvatar(event));
                            success.setTimestamp(Instant.now());

                            mentioned.getUser().openPrivateChannel().queue((channel) -> {
                                channel.sendMessage(muted.build()).queue();
                                muted.clear();

                                event.getChannel().sendMessage(eb.build()).queue((message) -> {
                                    message.delete().queueAfter(20, TimeUnit.SECONDS);
                                    eb.clear();
                                    data.getLogChannel(event).sendMessage(success.build()).queue((message2) -> {
                                        success.clear();
                                    });
                                    event.getGuild().getController().addSingleRoleToMember(mentioned, muteRole).queue();
                                });
                            });
                        } else {
                            eb.setDescription(mentioned.getAsMention() + " is already muted!");
                            eb.setColor(new Color(data.getColor()));
                            eb.setFooter(event.getJDA().getSelfUser().getName() + " Mute",
                                    data.getSelfAvatar(event));
                            eb.setTimestamp(Instant.now());

                            event.getChannel().sendMessage(eb.build()).queue((message) -> {
                                message.delete().queueAfter(20, TimeUnit.SECONDS);
                                eb.clear();
                            });
                        }

                    }
                } else if(args.length >= 3){
                    List<Role> roles = event.getGuild().getRolesByName("Muted", true);
                    String reason = Arrays.stream(args).skip(2).collect(Collectors.joining(" "));

                    if (roles.size() < 1) {

                        muteRole = event.getGuild().getController().createRole().setName("Muted").setColor(0xffffff).setMentionable(false).complete();

                        muteRole.getManager().revokePermissions(Permission.MESSAGE_TTS, Permission.MESSAGE_WRITE,
                            Permission.VOICE_DEAF_OTHERS, Permission.VOICE_MOVE_OTHERS,
                            Permission.VOICE_MUTE_OTHERS, Permission.VOICE_SPEAK, Permission.VOICE_USE_VAD,
                            Permission.NICKNAME_CHANGE, Permission.MESSAGE_ADD_REACTION).queue();

                        for (Channel channel : event.getGuild().getTextChannels()) {
                            if(!channel.getParent().getId().equals("579392397189054465")){
                                channel.getManager().putPermissionOverride(muteRole, EnumSet.of(Permission.MESSAGE_HISTORY, Permission.MESSAGE_READ), EnumSet.of(Permission.MESSAGE_WRITE)).queue();
                            }
                        }

                        event.getChannel().sendMessage("Your server didn't have a Muted role so I went ahead and created one for you and set the correct required permissions to each text channel").queue((message) -> {
                            message.delete().queueAfter(15, TimeUnit.SECONDS);
                        });

                        Member mentioned = event.getMessage().getMentionedMembers().get(0);
                        // Build Information Embed to be sent to muted user
                        muted.setDescription("You've been muted on: " + event.getGuild().getName()
                            + "\n\nReason: \n```\n" + reason + "\n```");
                        muted.setColor(new Color(data.getColor()));
                        muted.setFooter(event.getJDA().getSelfUser().getName() + " Muted",
                            data.getSelfAvatar(event));
                        muted.setTimestamp(Instant.now());

                        // Build Information Embed to be sent to server channel
                        eb.setDescription("You've muted: " + mentioned.getAsMention() + "\n\nReason:\n```\n" + reason + "\n```");
                        eb.setColor(0x4fff45);
                        eb.setFooter(event.getJDA().getSelfUser().getName() + " Mute",
                            data.getSelfAvatar(event));
                        eb.setTimestamp(Instant.now());

                        success.setDescription(event.getMember().getAsMention() + " muted " + mentioned.getAsMention() + "\n\nReason: \n```" + reason + "\n```");
                        success.setColor(new Color(data.getColor()));
                        success.setFooter(event.getJDA().getSelfUser().getName() + " Mute", data.getSelfAvatar(event));
                        success.setTimestamp(Instant.now());

                        mentioned.getUser().openPrivateChannel().queue((channel) -> {
                            channel.sendMessage(muted.build()).queue();
                            muted.clear();

                            event.getChannel().sendMessage(eb.build()).queue((message) -> {
                                message.delete().queueAfter(20, TimeUnit.SECONDS);
                                eb.clear();
                                data.getLogChannel(event).sendMessage(success.build()).queue((message2) -> {
                                    success.clear();
                                });
                                event.getGuild().getController().addSingleRoleToMember(mentioned, muteRole).queue();
                            });
                        });
                    } else {

                        muteRole = event.getGuild().getRolesByName("Muted", true).get(0);

                        for (Channel channel : event.getGuild().getTextChannels()) {
                            if(!channel.getParent().getId().equals("579392397189054465")){
                                channel.getManager().putPermissionOverride(muteRole, EnumSet.of(Permission.MESSAGE_HISTORY, Permission.MESSAGE_READ), EnumSet.of(Permission.MESSAGE_WRITE)).queue();
                            }
                        }
                        Member mentioned = event.getMessage().getMentionedMembers().get(0);
                        if (!mentioned.getRoles().contains(event.getGuild().getRoleById(muteRole.getId()))) {

                            // Build Information Embed to be sent to kicked user
                            muted.setDescription("You've been muted on: " + event.getGuild().getName()
                                + "\n\nReason: \n```\n" + reason + "\n```");
                            muted.setColor(new Color(data.getColor()));
                            muted.setFooter(event.getJDA().getSelfUser().getName() + " Muted",
                                data.getSelfAvatar(event));
                            muted.setTimestamp(Instant.now());

                            // Build Information Embed to be to server channel
                            eb.setDescription(
                                "You've muted: " + mentioned.getAsMention() + "\n\nReason:\n```\n" + reason + "\n```");
                            eb.setColor(0x4fff45);
                            eb.setFooter(event.getJDA().getSelfUser().getName() + " Mute",
                                data.getSelfAvatar(event));
                            eb.setTimestamp(Instant.now());

                            success.setDescription(event.getMember().getAsMention() + " muted " + mentioned.getAsMention() + "\n\nReason: \n```" + reason + "\n```");
                            success.setColor(new Color(data.getColor()));
                            success.setFooter(event.getJDA().getSelfUser().getName() + " Mute", data.getSelfAvatar(event));
                            success.setTimestamp(Instant.now());

                            mentioned.getUser().openPrivateChannel().queue((channel) -> {
                                channel.sendMessage(muted.build()).queue();
                                muted.clear();

                                event.getChannel().sendMessage(eb.build()).queue((message) -> {
                                    message.delete().queueAfter(20, TimeUnit.SECONDS);
                                    eb.clear();
                                    data.getLogChannel(event).sendMessage(success.build()).queue((message2) -> {
                                        success.clear();
                                    });
                                    event.getGuild().getController().addSingleRoleToMember(mentioned, muteRole).queue();
                                });
                            });
                        } else {
                            eb.setDescription(mentioned.getAsMention() + " is already muted!");
                            eb.setColor(new Color(data.getColor()));
                            eb.setFooter(event.getJDA().getSelfUser().getName() + " Mute",
                                data.getSelfAvatar(event));
                            eb.setTimestamp(Instant.now());

                            event.getChannel().sendMessage(eb.build()).queue((message) -> {
                                message.delete().queueAfter(20, TimeUnit.SECONDS);
                                eb.clear();
                            });
                        }

                    }
                }
            } else {
                eb.setDescription(event.getMember().getAsMention()
                        + ", You dont have the permission to mute members on this guild.");
                eb.setFooter("Insufficient Permissions", data.getSelfAvatar(event));
                eb.setTimestamp(Instant.now());
                event.getChannel().sendMessage(eb.build()).queue((message) -> {
                    message.delete().queueAfter(15, TimeUnit.SECONDS);
                    eb.clear();
                });
            }
        }
    }

}


