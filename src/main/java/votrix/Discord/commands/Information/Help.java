package votrix.Discord.commands.Information;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import votrix.Discord.commands.Moderation.Ban;
import votrix.Discord.commands.Moderation.Clear;
import votrix.Discord.utils.Data;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public class Help extends ListenerAdapter {

    public void onGuildMessageReceived(GuildMessageReceivedEvent event){
        String[] args = event.getMessage().getContentRaw().split("\\s+");
        Data data = new Data();
        EmbedBuilder eb = new EmbedBuilder();
        Ban ban = new Ban();
        Clear clear = new Clear();
        if(args[0].equalsIgnoreCase(data.getPrefix() + "help")){
            event.getMessage().delete().queue();
            if(args.length < 2){

            } else if(args.length < 3){
                if(args[1].equalsIgnoreCase("ban")){
                    eb.setTitle(ban.getName());
                    eb.setColor(new Color(data.getColor()));
                    eb.setDescription(ban.getDescription() +"\n\n **Required Roles:**\n" + ban.getRequiredRoles());
                    eb.setFooter("Votrix Ban Command Help", data.getSelfAvatar(event));

                    event.getChannel().sendMessage(eb.build()).queue((message) -> {
                        message.delete().queueAfter(15, TimeUnit.SECONDS);
                        eb.clear();
                    });
                }
                else if(args[1].equalsIgnoreCase("clear")){
                    eb.setTitle(clear.getName());
                    eb.setColor(new Color(data.getColor()));
                    eb.setDescription(clear.getDescription() + "\n\n **Required Roles:**\n" + clear.getRequiredRoles());
                    eb.setFooter("Votrix Clear Command Help", data.getSelfAvatar(event));

                    event.getChannel().sendMessage(eb.build()).queue((message) -> {
                        message.delete().queueAfter(15, TimeUnit.SECONDS);
                        eb.clear();
                    });
                }

            }
        }
    }
}
