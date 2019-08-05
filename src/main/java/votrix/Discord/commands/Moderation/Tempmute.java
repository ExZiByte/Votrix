package votrix.Discord.commands.Moderation;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import votrix.Discord.Data;
import votrix.Discord.Votrix;
import votrix.Discord.utils.RoleCheck;

import java.awt.*;
import java.time.Instant;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Tempmute extends ListenerAdapter {

    public void onGuildMessageReceived(GuildMessageReceivedEvent event){
        String[] args = event.getMessage().getContentRaw().split("\\s+");
        Data data = new Data();
        Votrix votrix = new Votrix();
        RoleCheck rc = new RoleCheck();
        EmbedBuilder eb = new EmbedBuilder();
        EmbedBuilder muted = new EmbedBuilder();
        Role muteRole;
        if(args[0].equalsIgnoreCase(votrix.prefix + "tempmute")){
            event.getMessage().delete().queue();
            if(rc.isOwner(event) || rc.isDeveloper(event) || rc.isAdministrator(event) || rc.isModerator(event)){
                if(args.length < 2) {
                    eb.setDescription("You didn't specify enough arguments. Please refer to " + votrix.prefix + "`tempmute help` for more information");
                    eb.setColor(new Color(data.getColor()));
                    eb.setTimestamp(Instant.now());
                    eb.setFooter("Insufficient Arguments", data.getSelfAvatar(event));

                    event.getChannel().sendMessage(eb.build()).queue((message) -> {
                        eb.clear();
                        message.delete().queueAfter(20, TimeUnit.SECONDS);
                    });
                } else if(args.length > 2 && args.length < 3){
                    if (args[1].equalsIgnoreCase("help")) {
                        eb.setDescription("Tempmute Help\n```\n~tempmute @member <length><length multiplier> [reason]\n<> | Required\n[] | Optional\n```");
                        eb.setColor(new Color(data.getColor()));
                        eb.setTimestamp(Instant.now());
                        eb.setFooter("Votrix Tempmute Help", data.getSelfAvatar(event));

                        event.getChannel().sendMessage(eb.build()).queue((message) -> {
                            eb.clear();
                            message.delete().queueAfter(20, TimeUnit.SECONDS);
                        });
                    }
                } else if(args.length < 4 && args.length > 2) {
                    Member mentioned = event.getMessage().getMentionedMembers().get(0);
                    
                }
            }
        }
    }

}
