package votrix.Discord.commands;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import votrix.Discord.Data;
import votrix.Discord.utils.RoleCheck;

import java.awt.*;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

public class Tempmute extends ListenerAdapter {

    public void onGuildMessageReceived(GuildMessageReceivedEvent event){
        String[] args = event.getMessage().getContentRaw().split("\\s+");
        Data data = new Data();
        RoleCheck rc = new RoleCheck();
        EmbedBuilder eb = new EmbedBuilder();
        EmbedBuilder muted = new EmbedBuilder();
        Role muteRole;
        if(args[0].equalsIgnoreCase(data.prefix + "tempmute")){
            event.getMessage().delete().queue();
            if(rc.isOwner(event) || rc.isDeveloper(event) || rc.isAdministrator(event) || rc.isModerator(event)){
                if(args[1].equalsIgnoreCase("help")){
                    eb.setDescription("Tempmute Help\n```\n~tempmute @member <length><length multiplier> [reason]\n```");
                    eb.setColor(new Color(data.getColor()));
                    eb.setTimestamp(Instant.now());
                    eb.setFooter("Votrix Tempmute Help", event.getJDA().getSelfUser().getEffectiveAvatarUrl());

                    event.getChannel().sendMessage(eb.build()).queue((message) -> {
                       eb.clear();
                       message.delete().queueAfter(10, TimeUnit.SECONDS);
                    });
                }
                else if(args.length < 3){
                    eb.setDescription("You didn't specify enough arguments");
                }
            }
        }
    }

}
