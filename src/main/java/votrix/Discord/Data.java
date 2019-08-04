package votrix.Discord;

import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.Random;

public class Data {

    public final String prefix = "~";

    public static int getColor() {

        Random obj = new Random();
        int rand_num = obj.nextInt(0xffffff + 1);

        return rand_num;
    }

    public static String getSelfAvatar(GuildMessageReceivedEvent event){
        return event.getJDA().getSelfUser().getEffectiveAvatarUrl();
    }

}
