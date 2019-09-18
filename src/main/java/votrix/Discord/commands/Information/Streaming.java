package votrix.Discord.commands.Information;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import votrix.Discord.utils.ChannelCheck;
import votrix.Discord.utils.Data;
import votrix.Discord.utils.RoleCheck;

import java.awt.*;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

public class Streaming extends ListenerAdapter {

    public void onGuildMessageReceived(GuildMessageReceivedEvent event){
        String[] args = event.getMessage().getContentRaw().split("\\s+");
        Data data = new Data();
        RoleCheck rc = new RoleCheck();
        ChannelCheck cc = new ChannelCheck();
        EmbedBuilder eb = new EmbedBuilder();
        if(args[0].equalsIgnoreCase(data.getPrefix() + "streaming")){
            if(cc.isStreamerChannel(event)){
                if(rc.isStreamer(event)){
                    if(args.length < 2) {
                        
                    }
                } else {
                    eb.setDescription("You aren't an approved streamer. You'll need to have the " + event.getGuild().getRoleById("592222071132651523").getAsMention() + " role to use this command");
                    eb.setColor(new Color(data.getColor()));
                    eb.setTimestamp(Instant.now());
                    eb.setFooter("Insufficient Permissions", data.getSelfAvatar(event));

                    event.getChannel().sendMessage(eb.build()).queue((message) -> {
                        message.delete().queueAfter(15, TimeUnit.SECONDS);
                        eb.clear();
                    });
                }
            } else {
                eb.setDescription("You aren't using this command in the correct channel. You need to use this command in the " + event.getGuild().getTextChannelById("594757517536526366").getAsMention() + " channel.");
                eb.setColor(new Color(data.getColor()));
                eb.setTimestamp(Instant.now());
                eb.setFooter("Incorrect Channel", data.getSelfAvatar(event));

                event.getChannel().sendMessage(eb.build()).queue((message) -> {
                    message.delete().queueAfter(15, TimeUnit.SECONDS);
                    eb.clear();
                });
            }
        }
    }
}
