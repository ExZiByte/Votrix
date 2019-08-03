package votrix.Discord.utils;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;

public class MemberCounter {

    public int getMemberCount(GuildMemberJoinEvent event){

        int memberCount = 0;

        for(Member member: event.getGuild().getMembers()){
            if(member.getUser().isBot()){}
            else {
                memberCount = memberCount + 1;
            }
        }

        return memberCount;
    }

    public int getMemberCount(GuildMemberLeaveEvent event){

        int memberCount = 0;

        for(Member member: event.getGuild().getMembers()){
            if(member.getUser().isBot()){}
            else {
                memberCount = memberCount + 1;
            }
        }

        return memberCount;
    }

}