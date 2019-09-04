package votrix.Discord.commands.Fun;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import votrix.Discord.utils.Data;

import java.awt.*;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

public class Screenshare extends ListenerAdapter {

    public void onGuildMessageReceived(GuildMessageReceivedEvent event){
        String[] args = event.getMessage().getContentRaw().split("\\s+");
        Data data = new Data();
        EmbedBuilder eb = new EmbedBuilder();
        EmbedBuilder success = new EmbedBuilder();
        if(args[0].equalsIgnoreCase(data.getPrefix() + "screenshare") || args[0].equalsIgnoreCase(data.getPrefix() + "ss")){
            if(!event.getMember().getVoiceState().inVoiceChannel()){
                eb.setDescription(event.getMember().getAsMention() + " you'll need to join a voice channel before using this command");
                eb.setColor(new Color(data.getColor()));
                eb.setTimestamp(Instant.now());
                eb.setFooter("Votrix Screenshare", data.getSelfAvatar(event));

                event.getChannel().sendMessage(eb.build()).queue((message) -> {
                    message.delete().queueAfter(10, TimeUnit.SECONDS);
                    eb.clear();
                });
            } else {
                // Screen share link https://www.discordapp.com/channels/578937882023034901/
                eb.setDescription("You've created a screenshare link for " + event.getMember().getVoiceState().getChannel().getName() + "\n\n[Screenshare Link](https://www.discordapp.com/channels/578937882023034901/" + event.getMember().getVoiceState().getChannel().getId() + "\"Click to join the screenshare for the voice channel " + event.getMember().getVoiceState().getChannel().getName() +"\")");
                eb.setColor(new Color(data.getColor()));
                eb.setTimestamp(Instant.now());
                eb.setFooter("Votrix Screenshare", data.getSelfAvatar(event));

                success.setDescription(event.getMember().getAsMention() + " has created a screenshare link for the voice channel " + event.getMember().getVoiceState().getChannel().getName());
                success.setColor(new Color(data.getColor()));
                success.setTimestamp(Instant.now());
                success.setFooter("Votrix Screenshare Log", data.getSelfAvatar(event));

                event.getChannel().sendMessage(eb.build()).queue((message1) -> {
                    data.getLogChannel(event).sendMessage(success.build()).queue((message2) -> {
                        success.clear();
                    });
                    eb.clear();
                });
            }
        }
    }
}
