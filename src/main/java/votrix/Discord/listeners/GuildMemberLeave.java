package votrix.Discord.listeners;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class GuildMemberLeave extends ListenerAdapter{

    public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
        int memberCount = 0;
        for (Member member : event.getJDA().getGuildById("578937882023034901").getMembers()) {
            if (!member.getUser().isBot()) {
                memberCount =+ 1;
            }
        }
        event.getJDA().getGuildById("578937882023034901").getVoiceChannelById("579403072028016652").getManager().setName("Member Count: " + memberCount).queue();
    }

}