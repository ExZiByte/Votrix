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
            CleverBotQuery bot = new CleverBotQuery(System.getenv("CLEVERBOTAPIKEY"), "hi");
            if (args.length < 2) {
               String seedText = "Hi, how are you?";
               String response;
               try{
                   bot.setPhrase(seedText);
                   bot.sendRequest();
                   response = bot.getResponse();
                   event.getChannel().sendMessage(response).queue();
               } catch (IOException e){
                   e.printStackTrace();

                   eb.setDescription("An error has occured with the chatbot API \n\n```\n" + e.toString().substring(0, 1950) + "\n```");
                   eb.setColor(new Color(data.getColor()));
                   eb.setTimestamp(Instant.now());
                   eb.setFooter("Votrix Cleverbot API Error", data.getSelfAvatar(event));

                   data.getLogChannel(event).sendMessage(eb.build()).queue();
               }
            }
            if (args.length > 1) {
                String seedText = Arrays.stream(args).skip(1).collect(Collectors.joining(" "));
                String response;
                try {
                    bot.setPhrase(seedText);
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

    public String getName() {
        return "Votrix CleverBot";
    }

    public String getDescription() {
        return "Talk to a chatbot from the clever bot API";
    }

    public String getShortDescription() {
        return "Talk to a chatbot";
    }

    public String getCommandSyntax() {
        return "```\n@Votrix {starting message}\n```";
    }

    public boolean isDisabled() {
        return false;
    }

    public String getRequiredRoles() {
        return "Everyone";
    }

}
