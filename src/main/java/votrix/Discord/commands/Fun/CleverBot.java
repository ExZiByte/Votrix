package votrix.Discord.commands.Fun;

import com.google.code.chatterbotapi.ChatterBot;
import com.google.code.chatterbotapi.ChatterBotFactory;
import com.google.code.chatterbotapi.ChatterBotSession;
import com.google.code.chatterbotapi.ChatterBotType;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import votrix.Discord.utils.Data;

import java.awt.*;
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
                ChatterBotFactory factory = new ChatterBotFactory();
                try {
                    ChatterBot bot = factory.create(ChatterBotType.CLEVERBOT);
                    ChatterBotSession session = bot.createSession();
                    while (true) {
                        event.getChannel().sendMessage(seedText).queue();
                        seedText = session.think(seedText);
                        event.getChannel().sendMessage(seedText).queue();
                        seedText = session.think(seedText);
                    }
                } catch (Exception e) {
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
