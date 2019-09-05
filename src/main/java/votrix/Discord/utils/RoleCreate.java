package votrix.Discord.utils;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.EnumSet;
import java.util.concurrent.TimeUnit;

public class RoleCreate {
    static Role muteRole;
    public static void createMutedRole(GuildMessageReceivedEvent event){
        muteRole = event.getGuild().getController().createRole().setName("Muted").setColor(0xffffff).setMentionable(false).complete();

        event.getGuild().getController().modifyRolePositions().selectPosition(event.getGuild().getRolesByName("Muted", true).get(0)).moveTo(event.getGuild().getRoles().size() - 3).queue();

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

    }

}
