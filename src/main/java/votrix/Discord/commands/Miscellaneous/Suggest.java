package votrix.Discord.commands.Miscellaneous;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import votrix.Discord.utils.Data;
import votrix.Discord.utils.Webhooks;

import java.awt.*;
import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Suggest extends ListenerAdapter {

    public void onGuildMessageReceived(GuildMessageReceivedEvent event){
        String[] args = event.getMessage().getContentRaw().split("\\s+");
        Data data = new Data();
        EmbedBuilder eb = new EmbedBuilder();
        String[] images = {"https://github.com/ExZiByte/Votrix/blob/master/huh.jpg", "https://github.com/ExZiByte/Votrix/blob/master/jackie_chan_huh.jpg", "https://github.com/ExZiByte/Votrix/blob/master/wat.png", "https://github.com/ExZiByte/Votrix/blob/master/wat_magik.png"};
        if(args[0].equalsIgnoreCase(data.getPrefix() + "suggest") || args[0].equalsIgnoreCase(data.getPrefix() + "suggestion")){
            event.getMessage().delete().queue();
            if(args.length < 2){
                Random rand = new Random();
                int image = rand.nextInt(images.length);
                eb.setDescription("I can't read your mind reeeeeeeeeeeee");
                eb.setColor(new Color(data.getColor()));
                eb.setImage(images[image]);
                eb.setFooter("Votrix Suggestions", data.getSelfAvatar(event));

                event.getChannel().sendMessage(eb.build()).queue((message) -> {
                    message.delete().queueAfter(30, TimeUnit.SECONDS);
                    eb.clear();
                });
            } else if(args.length > 1){
                try{
                    String sug = Arrays.stream(args).skip(1).collect(Collectors.joining(" "));
                    Webhooks webhook = new Webhooks(System.getenv("VOTRIXSUGGESTIONWEBHOOK"));
                    webhook.setAvatarUrl(event.getMember().getUser().getEffectiveAvatarUrl());
                    webhook.setUsername(event.getMember().getUser().getName());
                    webhook.addEmbed(new Webhooks.EmbedObject()
                        .setTitle("New Suggestion")
                        .setColor(new Color(data.getColor()))
                        .setDescription(sug)
                    );
                    webhook.execute();


                    eb.setDescription(":white_check_mark: Successfully sent the suggestion");
                    eb.setColor(new Color(data.getColor()));
                    eb.setTimestamp(Instant.now());
                    eb.setFooter("Votrix Suggestions", data.getSelfAvatar(event));

                    event.getChannel().sendMessage(eb.build()).queue((message) -> {
                        message.delete().queueAfter(20, TimeUnit.SECONDS);
                        eb.clear();
                    });
                } catch(IOException ex){
                    event.getChannel().sendMessage("Well shit there was an error with this command tell " + event.getGuild().getMemberById("79693184417931264").getAsMention() + " he retarded").queue();
                    ex.printStackTrace();
                }
            }
        }
    }

    public String getName() {
        return "Suggest";
    }

    public String getDescription() {
        return "Make a suggestion for a feature to be added to the server and or bot.";
    }

    public String getRequiredRoles() {
        return "Everyone";
    }

    public String getCommandSyntax() {
        return "```\n" + Data.getPrefix() + "suggest {suggestion in as much detail as you can give}\n```";
    }

}
