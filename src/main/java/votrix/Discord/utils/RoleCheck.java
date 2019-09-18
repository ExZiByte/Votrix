package votrix.Discord.utils;

import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class RoleCheck {

    public static boolean isOwner(GuildMessageReceivedEvent event){
        return event.getMember().getRoles().contains(event.getGuild().getRoleById("579403923685638145"));
    }

    public static boolean isDeveloper(GuildMessageReceivedEvent event){
        if(event.getMember().getUser().getId().equals("79693184417931264")){
            return true;
        }
        return false;
    }

    public static boolean isAdministrator(GuildMessageReceivedEvent event){
        return event.getMember().getRoles().contains(event.getGuild().getRoleById("594923361075855372"));
    }

    public static boolean isModerator(GuildMessageReceivedEvent event){
        return event.getMember().getRoles().contains(event.getGuild().getRoleById("579409550818672670"));
    }

    public static boolean isStreamer(GuildMessageReceivedEvent event){
        return event.getMember().getRoles().contains(event.getGuild().getRoleById("592222071132651523"));
    }


}
