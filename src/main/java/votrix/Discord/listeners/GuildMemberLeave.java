package votrix.Discord.listeners;

import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import votrix.Discord.utils.MemberCounter;

public class GuildMemberLeave extends ListenerAdapter{

    public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
        MemberCounter memberCount = new MemberCounter();
        event.getGuild().getVoiceChannelById("579403072028016652").getManager().setName("Member Count: " + memberCount.getMemberCount(event)).queue();
    }

}