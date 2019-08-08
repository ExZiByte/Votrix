package votrix.Discord;

import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import votrix.Discord.utils.SQLDriver;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

public class Data {

    public static TextChannel getLogChannel(GuildMessageReceivedEvent event) {
        return event.getGuild().getTextChannelById("598948078741094400");
    }

    public static String getPrefix() {
        String prefix;
        SQLDriver sql = new SQLDriver();
        try {
            Connection conn = sql.getConn();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM `information`");

            while (rs.next()) {
                prefix = rs.getString("prefix");
                return prefix;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void setPrefix(String args) {
        SQLDriver sql = new SQLDriver();
        try {
            sql.getConn().createStatement().execute("UPDATE `information` SET 'prefix' = " + args);
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
