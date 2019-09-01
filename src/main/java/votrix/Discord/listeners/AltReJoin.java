package votrix.Discord.listeners;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.EnumSet;

public class AltReJoin extends ListenerAdapter {

    public void onGuildMemberJoin(GuildMemberJoinEvent event){
        if(event.getMember().getUser().getId().equals("483616844415238144")){
            EnumSet<Permission> allowed = EnumSet.of(Permission.VIEW_CHANNEL, Permission.MESSAGE_WRITE, Permission.MESSAGE_READ, Permission.MESSAGE_HISTORY, Permission.MESSAGE_MANAGE, Permission.MESSAGE_ATTACH_FILES, Permission.MESSAGE_EMBED_LINKS);
            event.getGuild().getTextChannelById("609207997499179008").getManager().putPermissionOverride(event.getMember(), allowed, null).queue();
        }
    }

}
