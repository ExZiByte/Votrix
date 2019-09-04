package votrix.Discord.listeners;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.bson.Document;
import votrix.Discord.utils.Database;

public class SuggestionReactAdd extends ListenerAdapter {


    public static String messageID = "";
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        if (event.getChannel().getId().equals("598949933650804736")) {
            messageID = "";
            event.getMessage().addReaction("✅").queue((message) -> {
                event.getMessage().addReaction("❌").queue();
            });
            messageID = event.getMessageId();
            System.out.print(messageID + "\n");
        }
    }
}
