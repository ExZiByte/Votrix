package votrix.Discord.listeners;

import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import votrix.Discord.utils.MemberCounter;

public class GuildMemberJoin extends ListenerAdapter {
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        MemberCounter memberCount = new MemberCounter();
        event.getGuild().getVoiceChannelById("579403072028016652").getManager().setName("Member Count: " + memberCount.getMemberCount(event)).queue();
    }
}