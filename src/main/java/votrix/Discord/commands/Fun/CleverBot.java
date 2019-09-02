package votrix.Discord.commands.Fun;

import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class CleverBot extends ListenerAdapter {

    public void onGuildMessageReceived(GuildMessageReceivedEvent event){
        String[] args = event.getMessage().getContentRaw().split("\\s+");
        if(args[0].equals(event.getGuild().getSelfMember().getAsMention())){
            event.getChannel().sendMessage("hi").queue();
        }
    }
}
