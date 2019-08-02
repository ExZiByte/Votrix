package votrix.Discord.listeners;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class GuildMemberJoin extends ListenerAdapter {
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        event.getJDA().getGuildById("578937882023034901").getVoiceChannelById("579403072028016652").getManager().setName("Member Count: " + ).queue();
    }
}