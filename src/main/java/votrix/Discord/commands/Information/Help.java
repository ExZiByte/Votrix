package votrix.Discord.commands.Information;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import votrix.Discord.commands.Fun.CleverBot;
import votrix.Discord.commands.Fun.Screenshare;
import votrix.Discord.commands.Miscellaneous.Suggest;
import votrix.Discord.commands.Moderation.*;
import votrix.Discord.commands.Settings.SetPrefix;
import votrix.Discord.utils.Data;

import javax.management.relation.Role;
import java.awt.*;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

public class Help extends ListenerAdapter {

    public void onGuildMessageReceived(GuildMessageReceivedEvent event){
        String[] args = event.getMessage().getContentRaw().split("\\s+");
        Data data = new Data();
        EmbedBuilder eb = new EmbedBuilder();

        if(args[0].equalsIgnoreCase(data.getPrefix() + "help")){
            event.getMessage().delete().queue();
            if(args.length < 2){

                RoleAdd addrole = new RoleAdd();
                Ban ban = new Ban();
                Clear clear = new Clear();
                CleverBot cleverbot = new CleverBot();
                Kick kick = new Kick();
                Mute mute = new Mute();
                Screenshare screenshare = new Screenshare();
                SetPrefix setprefix = new SetPrefix();
                Softban softban = new Softban();
                Suggest suggest = new Suggest();
                Tempmute tempmute = new Tempmute();
                Unmute unmute = new Unmute();

                eb.setTitle("⚙ Help");
                eb.setDescription("Commands for Votrix!\n\n**General Commands**\n```\n" + data.getPrefix() + "suggest     --  " + suggest.getShortDescription() + "\n```\n**Fun Commands**\n```\n@Votrix msg  -- " + cleverbot.getShortDescription() + "\n" + data.getPrefix() + "screenshare -- " + screenshare.getShortDescription() + "\n```\n**Moderation Commands**\n```\n" + data.getPrefix() + "addrole     -- " + addrole.getShortDescription() + "\n" + data.getPrefix() + "ban         --  " + ban.getShortDescription() + "\n" + data.getPrefix() + "clear       --  " + clear.getShortDescription() + "\n" + data.getPrefix() + "kick        --  " + kick.getShortDescription() + "\n" + data.getPrefix() + "mute        --  " + mute.getShortDescription() + "\n" + data.getPrefix() + "softban    --  " + softban.getShortDescription() + "\n" + data.getPrefix() + "tempmute    --  " + tempmute.getShortDescription() + "\n" + data.getPrefix() + "unmute      --  " + unmute.getShortDescription() + "\n```\n**Setting Commands**\n```\n" + data.getPrefix() + "setprefix   --  " + setprefix.getShortDescription() + "\n```\n\n**For command syntax do**\n```\n" + data.getPrefix() + "help {command}\n```");
                eb.setColor(new Color(data.getColor()));
                eb.setTimestamp(Instant.now());
                eb.setFooter("Votrix Commands Help Menu", data.getSelfAvatar(event));

                event.getChannel().sendMessage(eb.build()).queue((message) -> {
                    message.delete().queueAfter(2, TimeUnit.MINUTES);
                    eb.clear();
                });

            } else if(args.length < 3){
                if(args[1].equalsIgnoreCase("addrole")){
                    RoleAdd command = new RoleAdd();
                    eb.setTitle(command.getName() + " Help");
                    eb.setColor(new Color(data.getColor()));
                    eb.setDescription(command.getDescription() +"\n\n **Required Roles:**\n" + command.getRequiredRoles()  + "\n\n **How to use the addrole command:**\n" + command.getCommandSyntax() + "\n**Is Disabled?** \n```\n" + command.isDisabled() + "\n```\n```\n{} | Required\n[] | Optional\n() | Optional unless previous item chosen to be used\n```");
                    eb.setFooter("Votrix Ban Command Help", data.getSelfAvatar(event));

                    event.getChannel().sendMessage(eb.build()).queue((message) -> {
                        message.delete().queueAfter(30, TimeUnit.SECONDS);
                        eb.clear();
                    });
                }
                else if(args[1].equalsIgnoreCase("ban")){
                    Ban command = new Ban();
                    eb.setTitle(command.getName() + " Help");
                    eb.setColor(new Color(data.getColor()));
                    eb.setDescription(command.getDescription() +"\n\n **Required Roles:**\n" + command.getRequiredRoles()  + "\n\n **How to use the ban command:**\n" + command.getCommandSyntax() + "\n**Is Disabled?** \n```\n" + command.isDisabled() + "\n```\n```\n{} | Required\n[] | Optional\n```");
                    eb.setFooter("Votrix Ban Command Help", data.getSelfAvatar(event));

                    event.getChannel().sendMessage(eb.build()).queue((message) -> {
                        message.delete().queueAfter(30, TimeUnit.SECONDS);
                        eb.clear();
                    });
                } else if(args[1].equalsIgnoreCase("clear")){
                    Clear command = new Clear();
                    eb.setTitle(command.getName() + " Help");
                    eb.setColor(new Color(data.getColor()));
                    eb.setDescription(command.getDescription() + "\n\n **Required Roles:**\n" + command.getRequiredRoles() + "\n\n **How to use the clear command:**\n" + command.getCommandSyntax() + "\n**Is Disabled?** \n```\n" + command.isDisabled() + "\n```\n```\n{} | Required\n[] | Optional\n```");                    eb.setFooter("Votrix Clear Command Help", data.getSelfAvatar(event));

                    event.getChannel().sendMessage(eb.build()).queue((message) -> {
                        message.delete().queueAfter(30, TimeUnit.SECONDS);
                        eb.clear();
                    });
                } else if(args[1].equalsIgnoreCase("cleverbot")){
                    CleverBot command = new CleverBot();
                    eb.setTitle(command.getName() + " Help");
                    eb.setColor(new Color(data.getColor()));
                    eb.setDescription(command.getDescription() + "\n\n **Required Roles:**\n" + command.getRequiredRoles() + "\n\n **How to use the unmute command:**\n" + command.getCommandSyntax() + "\n**Is Disabled?** \n```\n" + command.isDisabled() + "\n```\n```\n{} | Required\n[] | Optional\n```");
                    eb.setFooter("Votrix Unmute Command Help", data.getSelfAvatar(event));

                    event.getChannel().sendMessage(eb.build()).queue((message) -> {
                        message.delete().queueAfter(30, TimeUnit.SECONDS);
                        eb.clear();
                    });
                } else if(args[1].equalsIgnoreCase("kick")){
                    Kick command = new Kick();
                    eb.setTitle(command.getName() + " Help");
                    eb.setColor(new Color(data.getColor()));
                    eb.setDescription(command.getDescription() +"\n\n **Required Roles:**\n" + command.getRequiredRoles()  + "\n\n **How to use the kick command:**\n" + command.getCommandSyntax() + "\n**Is Disabled?** \n```\n" + command.isDisabled() + "\n```\n```\n{} | Required\n[] | Optional\n```");
                    eb.setFooter("Votrix Kick Command Help", data.getSelfAvatar(event));

                    event.getChannel().sendMessage(eb.build()).queue((message) -> {
                        message.delete().queueAfter(30, TimeUnit.SECONDS);
                        eb.clear();
                    });
                } else if(args[1].equalsIgnoreCase("mute")){
                    Mute command = new Mute();
                    eb.setTitle(command.getName() + " Help");
                    eb.setColor(new Color(data.getColor()));
                    eb.setDescription(command.getDescription() + "\n\n **Required Roles:**\n" + command.getRequiredRoles() + "\n\n **How to use the mute command:**\n" + command.getCommandSyntax() + "\n**Is Disabled?** \n```\n" + command.isDisabled() + "\n```\n```\n{} | Required\n[] | Optional\n```");
                    eb.setFooter("Votrix Mute Command Help", data.getSelfAvatar(event));

                    event.getChannel().sendMessage(eb.build()).queue((message) -> {
                        message.delete().queueAfter(30, TimeUnit.SECONDS);
                        eb.clear();
                    });
                } else if(args[1].equalsIgnoreCase("screenshare")){
                    Screenshare command = new Screenshare();
                    eb.setTitle(command.getName() + " Help");
                    eb.setColor(new Color(data.getColor()));
                    eb.setDescription(command.getDescription() + "\n\n **Required Roles:**\n" + command.getRequiredRoles() + "\n\n **How to use the screenshare command:**\n" + command.getCommandSyntax() + "\n**Is Disabled?** \n```\n" + command.isDisabled() + "\n```\n```\n{} | Required\n[] | Optional\n```");
                    eb.setFooter("Votrix Screenshare Command Help", data.getSelfAvatar(event));

                    event.getChannel().sendMessage(eb.build()).queue((message) -> {
                        message.delete().queueAfter(30, TimeUnit.SECONDS);
                        eb.clear();
                    });
                } else if(args[1].equalsIgnoreCase("setprefix")){
                    SetPrefix command = new SetPrefix();
                    eb.setTitle(command.getName() + " Help");
                    eb.setColor(new Color(data.getColor()));
                    eb.setDescription(command.getDescription() + "\n\n **Required Roles:**\n" + command.getRequiredRoles() + "\n\n **How to use the setprefix command:**\n" + command.getCommandSyntax() + "\n**Is Disabled?** \n```\n" + command.isDisabled() + "\n```\n```\n{} | Required\n[] | Optional\n```");
                    eb.setFooter("Votrix SetPrefix Command Help", data.getSelfAvatar(event));

                    event.getChannel().sendMessage(eb.build()).queue((message) -> {
                        message.delete().queueAfter(30, TimeUnit.SECONDS);
                        eb.clear();
                    });
                } else if(args[1].equalsIgnoreCase("softban")){
                    Softban command = new Softban();
                    eb.setTitle(command.getName() + " Help");
                    eb.setColor(new Color(data.getColor()));
                    eb.setDescription(command.getDescription() + "\n\n **Required Roles:**\n" + command.getRequiredRoles() + "\n\n **How to use the softban command:**\n" + command.getCommandSyntax() + "\n**Is Disabled?** \n```\n" + command.isDisabled() + "\n```\n```\n{} | Required\n[] | Optional\n```");
                    eb.setFooter("Votrix Softban Command Help", data.getSelfAvatar(event));

                    event.getChannel().sendMessage(eb.build()).queue((message) -> {
                        message.delete().queueAfter(30, TimeUnit.SECONDS);
                        eb.clear();
                    });
                } else if(args[1].equalsIgnoreCase("suggest")){
                    Suggest command = new Suggest();
                    eb.setTitle(command.getName() + " Help");
                    eb.setColor(new Color(data.getColor()));
                    eb.setDescription(command.getDescription() + "\n\n **Required Roles:**\n" + command.getRequiredRoles() + "\n\n **How to use the softban command:**\n" + command.getCommandSyntax() + "\n**Is Disabled?** \n```\n" + command.isDisabled() + "\n```\n```\n{} | Required\n[] | Optional\n```");
                    eb.setFooter("Votrix Suggest Command Help", data.getSelfAvatar(event));

                    event.getChannel().sendMessage(eb.build()).queue((message) -> {
                        message.delete().queueAfter(30, TimeUnit.SECONDS);
                        eb.clear();
                    });
                } else if(args[1].equalsIgnoreCase("tempmute")){
                    Tempmute command = new Tempmute();
                    eb.setTitle(command.getName() + " Help");
                    eb.setColor(new Color(data.getColor()));
                    eb.setDescription(command.getDescription() + "\n\n **Required Roles:**\n" + command.getRequiredRoles() + "\n\n **How to use the tempmute command:**\n" + command.getCommandSyntax() + "\n**Time Multipliers**\n```\nS | SECONDS\nM | MINUTES\nH | HOURS\nD | DAYS\n```\n**Is Disabled?** \n```\n" + command.isDisabled() + "\n```\n```\n{} | Required\n[] | Optional\n```");
                    eb.setFooter("Votrix Tempmute Command Help", data.getSelfAvatar(event));

                    event.getChannel().sendMessage(eb.build()).queue((message) -> {
                        message.delete().queueAfter(30, TimeUnit.SECONDS);
                        eb.clear();
                    });
                } else if(args[1].equalsIgnoreCase("unmute")){
                    Unmute command = new Unmute();
                    eb.setTitle(command.getName() + " Help");
                    eb.setColor(new Color(data.getColor()));
                    eb.setDescription(command.getDescription() + "\n\n **Required Roles:**\n" + command.getRequiredRoles() + "\n\n **How to use the unmute command:**\n" + command.getCommandSyntax() + "\n**Is Disabled?** \n```\n" + command.isDisabled() + "\n```\n```\n{} | Required\n[] | Optional\n```");
                    eb.setFooter("Votrix Unmute Command Help", data.getSelfAvatar(event));

                    event.getChannel().sendMessage(eb.build()).queue((message) -> {
                        message.delete().queueAfter(30, TimeUnit.SECONDS);
                        eb.clear();
                    });
                }

            }
        }
    }
}
