package votrix.Discord.commands.Information;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import votrix.Discord.commands.Moderation.*;
import votrix.Discord.utils.Data;

import java.awt.*;
import java.util.concurrent.TimeUnit;

public class Help extends ListenerAdapter {

    public void onGuildMessageReceived(GuildMessageReceivedEvent event){
        String[] args = event.getMessage().getContentRaw().split("\\s+");
        Data data = new Data();
        EmbedBuilder eb = new EmbedBuilder();



        if(args[0].equalsIgnoreCase(data.getPrefix() + "help")){
            event.getMessage().delete().queue();
            if(args.length < 2){

            } else if(args.length < 3){
                if(args[1].equalsIgnoreCase("ban")){
                    Ban command = new Ban();
                    eb.setTitle(command.getName());
                    eb.setColor(new Color(data.getColor()));
                    eb.setDescription(command.getDescription() +"\n\n **Required Roles:**\n" + command.getRequiredRoles());
                    eb.setFooter("Votrix Ban Command Help", data.getSelfAvatar(event));

                    event.getChannel().sendMessage(eb.build()).queue((message) -> {
                        message.delete().queueAfter(15, TimeUnit.SECONDS);
                        eb.clear();
                    });
                } else if(args[1].equalsIgnoreCase("clear")){
                    Clear command = new Clear();
                    eb.setTitle(command.getName());
                    eb.setColor(new Color(data.getColor()));
                    eb.setDescription(command.getDescription() + "\n\n **Required Roles:**\n" + command.getRequiredRoles());
                    eb.setFooter("Votrix Clear Command Help", data.getSelfAvatar(event));

                    event.getChannel().sendMessage(eb.build()).queue((message) -> {
                        message.delete().queueAfter(15, TimeUnit.SECONDS);
                        eb.clear();
                    });
                } else if(args[1].equalsIgnoreCase("mute")){
                    Mute command = new Mute();
                    eb.setTitle(command.getName());
                    eb.setColor(new Color(data.getColor()));
                    eb.setDescription(command.getDescription() + "\n\n **Required Roles:**\n" + command.getRequiredRoles());
                    eb.setFooter("Votrix Mute Command Help", data.getSelfAvatar(event));

                    event.getChannel().sendMessage(eb.build()).queue((message) -> {
                        message.delete().queueAfter(15, TimeUnit.SECONDS);
                        eb.clear();
                    });
                } else if(args[1].equalsIgnoreCase("softban")){
                    Softban command = new Softban();
                    eb.setTitle(command.getName());
                    eb.setColor(new Color(data.getColor()));
                    eb.setDescription(command.getDescription() + "\n\n **Required Roles:**\n" + command.getRequiredRoles());
                    eb.setFooter("Votrix Softban Command Help", data.getSelfAvatar(event));

                    event.getChannel().sendMessage(eb.build()).queue((message) -> {
                        message.delete().queueAfter(15, TimeUnit.SECONDS);
                        eb.clear();
                    });
                } else if(args[1].equalsIgnoreCase("tempmute")){
                    Tempmute command = new Tempmute();
                    eb.setTitle(command.getName());
                    eb.setColor(new Color(data.getColor()));
                    eb.setDescription(command.getDescription() + "\n\n **Required Roles:**\n" + command.getRequiredRoles() + "\n\n **How to use the tempmute command:**\n" + command.getCommandSyntax() + "\n**Time Multipliers**\n```\nS | SECONDS\nM | MINUTES\nH | HOURS\nD | DAYS\n```\n```\n{} | Required\n[] | Optional\n```");
                    eb.setFooter("Votrix Tempmute Command Help", data.getSelfAvatar(event));

                    event.getChannel().sendMessage(eb.build()).queue((message) -> {
                        message.delete().queueAfter(15, TimeUnit.SECONDS);
                        eb.clear();
                    });
                }

            }
        }
    }
}
