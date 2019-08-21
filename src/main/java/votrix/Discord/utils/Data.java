package votrix.Discord.utils;

import com.mongodb.Mongo;
import com.mongodb.client.MongoCollection;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import org.bson.Document;

import java.util.Random;

import static com.mongodb.client.model.Filters.eq;

public class Data {

    static Database db = new Database();

    public static TextChannel getLogChannel(GuildMessageReceivedEvent event) {
        return event.getGuild().getTextChannelById("598948078741094400");
    }

    public static String getPrefix() {
        String prefix;

        db.connect();
        MongoCollection<Document> guild = db.getCollection("guild");
        prefix = guild.find().first().getString("prefix");
        db.close();
        return prefix;
    }

    public static void setPrefix(String prefix) {
        db.connect();
        MongoCollection guild = db.getCollection("guild");
        guild.findOneAndUpdate(eq("prefix", getPrefix()), eq("$set", prefix));
    }

    public static int getColor() {

        Random obj = new Random();
        int rand_num = obj.nextInt(0xffffff + 1);

        return rand_num;
    }

    public static String getSelfAvatar(GuildMessageReceivedEvent event) {
        return event.getJDA().getSelfUser().getEffectiveAvatarUrl();
    }

}
