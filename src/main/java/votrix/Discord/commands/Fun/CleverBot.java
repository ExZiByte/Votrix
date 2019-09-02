package votrix.Discord.commands.Fun;

import com.michaelwflaherty.cleverbotapi.CleverBotQuery;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import votrix.Discord.utils.Data;

import java.awt.*;
import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.stream.Collectors;

public class CleverBot extends ListenerAdapter {

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split("\\s+");
        Data data = new Data();
        EmbedBuilder eb = new EmbedBuilder();
        if (args[0].equals(event.getGuild().getSelfMember().getAsMention())) {
            if (args.length > 1) {
                String seedText = Arrays.stream(args).skip(1).collect(Collectors.joining(" "));
                CleverBotQuery bot = new CleverBotQuery(System.getenv("CLEVERBOTAPIKEY"), seedText);
                String response;
                try {
                    bot.sendRequest();
                    response = bot.getResponse();
                    event.getChannel().sendMessage(response).queue();
                } catch (IOException e) {
                    e.printStackTrace();

                    eb.setDescription("An error has occured with the chatbot API \n\n```\n" + e.toString().substring(0, 1950) + "\n```");
                    eb.setColor(new Color(data.getColor()));
                    eb.setTimestamp(Instant.now());
                    eb.setFooter("Votrix Cleverbot API Error", data.getSelfAvatar(event));

                    data.getLogChannel(event).sendMessage(eb.build()).queue();
                }
            }
        }
    }
}
