package votrix.Discord.commands.Miscellaneous;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.bson.Document;
import votrix.Discord.listeners.SuggestionReactAdd;
import votrix.Discord.utils.Data;
import votrix.Discord.utils.Database;
import votrix.Discord.utils.Webhooks;

import java.awt.*;
import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Suggest extends ListenerAdapter {
    Database db = new Database();
    Integer id;

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split("\\s+");
        db.connect();
        MongoCollection<Document> suggestions = db.getCollection("Suggestions");
        id = Integer.parseInt(suggestions.find().sort(new BasicDBObject("suggestionID", -1)).limit(1).first().getString("suggestionID"));
        db.close();
        Data data = new Data();
        EmbedBuilder eb = new EmbedBuilder();
        String[] images = {"https://quiver.nestedvar.dev/assets/huh.jpg", "https://quiver.nestedvar.dev/assets/jackie_chan_huh.jpg", "https://quiver.nestedvar.dev/assets/wat.png", "https://quiver.nestedvar.dev/assets/wat_magik.png"};
        if (args[0].equalsIgnoreCase(data.getPrefix() + "suggest") || args[0].equalsIgnoreCase(data.getPrefix() + "suggestion")) {
            event.getMessage().delete().queue();
            if (args.length < 2) {
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
            } else if (args.length > 1) {
                try {
                    String sug = Arrays.stream(args).skip(1).collect(Collectors.joining(" "));
                    Webhooks webhook = new Webhooks(System.getenv("VOTRIXSUGGESTIONWEBHOOK"));
                    webhook.setAvatarUrl(event.getMember().getUser().getEffectiveAvatarUrl());
                    webhook.setUsername(event.getMember().getUser().getName());
                    webhook.addEmbed(new Webhooks.EmbedObject()
                        .setTitle("New Suggestion | " + id + 1)
                        .setColor(new Color(data.getColor()))
                        .setDescription(sug)
                    );
                    webhook.execute();
                    new java.util.Timer().schedule(
                        new java.util.TimerTask() {
                            @Override
                            public void run() {
                                addSuggestion(event, eb, sug, SuggestionReactAdd.messageID);
                            }
                        },
                        250
                    );

                    eb.setDescription(":white_check_mark: Successfully sent the suggestion");
                    eb.setColor(new Color(data.getColor()));
                    eb.setTimestamp(Instant.now());
                    eb.setFooter("Votrix Suggestions", data.getSelfAvatar(event));

                    event.getChannel().sendMessage(eb.build()).queue((message) -> {
                        message.delete().queueAfter(15, TimeUnit.SECONDS);
                        eb.clear();
                    });
                } catch (IOException ex) {
                    event.getChannel().sendMessage("Well shit there was an error with this command tell " + event.getGuild().getMemberById("79693184417931264").getAsMention() + " he retarded").queue();
                    ex.printStackTrace();
                }
            }
        }
    }

    public void addSuggestion(GuildMessageReceivedEvent event, EmbedBuilder eb, String suggestion, String messageID) {
        db.connect();
        MongoCollection suggestions = db.getCollection("Suggestions");
        Document doc = new Document(id.toString(), new BasicDBObject().append("messageID", messageID).append("finished", false).append("author", event.getAuthor().getAsTag()).append("suggestion", suggestion).append("suggestionID", id + 1));
        suggestions.insertOne(doc);
        db.close();

        event.getChannel().sendMessage(eb.build()).queue((message) -> {
            message.delete().queueAfter(20, TimeUnit.SECONDS);
            eb.clear();
        });
    }

    public String getName() {
        return "Suggest";
    }

    public String getDescription() {
        return "Make a suggestion for a feature to be added to the server and or bot.";
    }

    public String getShortDescription() {
        return "Make a suggestion";
    }

    public String getRequiredRoles() {
        return "Everyone";
    }

    public String getCommandSyntax() {
        return "```\n" + Data.getPrefix() + "suggest {suggestion in as much detail as you can give}\n```";
    }

    public boolean isDisabled() {
        return false;
    }

}
