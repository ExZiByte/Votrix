package votrix.Discord.commands.Settings;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import votrix.Discord.Data;
import votrix.Discord.Votrix;
import votrix.Discord.utils.RoleCheck;

import java.awt.*;
import java.time.Instant;

public class SetPrefix extends ListenerAdapter {

    public void onGuildMessageReceived(GuildMessageReceivedEvent event){
        String[] args = event.getMessage().getContentRaw().split("\\s+");
        Data data = new Data();
        Votrix votrix = new Votrix();
        RoleCheck rc = new RoleCheck();
        EmbedBuilder eb = new EmbedBuilder();
        EmbedBuilder success = new EmbedBuilder();
        if(args[0].equals(votrix.prefix + "setprefix")) {
            if (rc.isOwner(event) || rc.isDeveloper(event)) {
                data.setPrefix(args[1]);
                eb.setDescription("Successfully set the prefix to `" + args[1] + "`");
                eb.setColor(new Color(data.getColor()));
                eb.setFooter("Votrix Set Prefix", data.getSelfAvatar(event));
                eb.setTimestamp(Instant.now());

                success.setDescription(event.getMember().getAsMention() + " set the prefix to `" + args[1] + "`");
                success.setColor(new Color(data.getColor()));
                success.setFooter("Votrix Set Prefix Log", data.getSelfAvatar(event));
                success.setTimestamp(Instant.now());

                event.getChannel().sendMessage(eb.build()).queue((message) -> {
                    eb.clear();
                    data.getLogChannel(event).sendMessage(success.build()).queue((message2) -> {
                        success.clear();
                    });
                });
            }
        }
    }
}
