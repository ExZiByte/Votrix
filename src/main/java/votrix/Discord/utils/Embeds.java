package votrix.Discord.utils;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.time.Instant;

public class Embeds {

    private static final Data data = new Data();
    private static final Time time = new Time();

    public static EmbedBuilder getAlreadyMutedEmbed(GuildMessageReceivedEvent event){
        Member mentioned = event.getMessage().getMentionedMembers().get(0);
        EmbedBuilder eb = new EmbedBuilder();
        eb.setDescription(mentioned.getAsMention() + " is already muted");
        eb.setColor(new Color(data.getColor()));
        eb.setTimestamp(Instant.now());
        eb.setFooter("Already Muted", data.getSelfAvatar(event));
        return eb;
    }

    public static EmbedBuilder getTempmuteEmbed(GuildMessageReceivedEvent event, String[] args, String reason) {
        Member mentioned = event.getMessage().getMentionedMembers().get(0);
        EmbedBuilder eb = new EmbedBuilder();
        eb.setDescription("You have tempmuted " + mentioned.getAsMention() + "\n\nReason: \n```\n" + reason + "\n```");
        eb.setColor(new Color(data.getColor()));
        eb.setTimestamp(Instant.now());
        eb.setFooter("Votrix Tempmute", data.getSelfAvatar(event));
        return eb;
    }
    public static EmbedBuilder getMutedEmbed(GuildMessageReceivedEvent event, String[] args, String reason) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setDescription("You have been tempmuted \n\nDetails: ```\nGuild: " + event.getGuild().getName() + "\nReason: " + reason + "\nExecutor: " + event.getMember().getEffectiveName() + "\nTime: " + args[2].substring(0, args[2].length() - 1) + " " + time.getTime(args[2]).name() + "\n```");
        eb.setColor(new Color(data.getColor()));
        eb.setTimestamp(Instant.now());
        eb.setFooter("Tempmuted", data.getSelfAvatar(event));
        return eb;
    }
    public static EmbedBuilder getMuteSuccessEmbed(GuildMessageReceivedEvent event, String[] args, String reason){
        Member mentioned = event.getMessage().getMentionedMembers().get(0);
        EmbedBuilder eb = new EmbedBuilder();
        eb.setDescription(event.getMember().getAsMention() + " has tempmuted " + mentioned.getAsMention() + "\n\nDetails: ```\nReason: " + reason + "\nTime: " + args[2].substring(0, args[2].length() - 1) + " " + time.getTime(args[2]).name() + "\n```");
        eb.setColor(new Color(data.getColor()));
        eb.setTimestamp(Instant.now());
        eb.setFooter("Votrix Tempmute Log", data.getSelfAvatar(event));
        return eb;
    }
}
