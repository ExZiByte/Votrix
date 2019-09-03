package votrix.Discord.listeners;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import votrix.Discord.utils.Data;
import votrix.Discord.utils.MemberCounter;

import java.awt.*;
import java.time.Instant;
import java.util.Random;

public class GuildMemberJoin extends ListenerAdapter {
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        EmbedBuilder eb = new EmbedBuilder();
        Data data = new Data();
        MemberCounter memberCount = new MemberCounter();
        event.getGuild().getVoiceChannelById("579403072028016652").getManager().setName("Member Count: " + memberCount.getMemberCount(event)).queue();

        String[] messages = {
            "[member] just joined the server - glhf!",
            "[member] just joined. Everyone, look busy!",
            "[member] just joined. Can I get a heal?",
            "[member] joined your party.",
            "[member] joined. You must construct additional pylons!",
            "Ermagherd. [member] is here!",
            "Welcome, [member]. Stay awhile and listen.",
            "Welcome, [member]. We were expecting you ( ͡° ͜ʖ ͡°)",
            "Welcome, [member]. We hope you brought pizza.",
            "Welcome [member]. Leave your weapons by the door.",
            "A wild [member] appeared.",
            "Swoooosh. [member] just landed.",
            "Brace yourselves. [member] just joined the server.",
            "[member] just joined. Hide your bananas.",
            "[member] just arrived. Seems OP - please nerf.",
            "[member] just slid into the server.",
            "A [member] has spawned in the server.",
            "Big [member] showed up!",
            "Where’s [member]? In the server!",
            "[member] hopped into the server. Kangaroo!!",
            "[member] just showed up. Hold my beer.",
            "Never gonna give [member] up, never gonna let [member] down!",
            "It's dangerous to go alone, take [member].",
            "It's [member]! Praise the sun!. [T]/",
            "Ha! [member] has joined. You activated my trap card!",
            "Hello is it [member] you're looking for?"
        };

        if(!event.getMember().getUser().isBot()) {
            Random random = new Random();
            int message = random.nextInt(messages.length);
            eb.setDescription(messages[message].replace("[member]", event.getMember().getAsMention()));
            eb.setColor(new Color(data.getColor()));
            eb.setTimestamp(Instant.now());

            event.getGuild().getTextChannelById("606703075310436403").sendMessage(eb.build()).queue((message1) -> {
                eb.clear();
            });
        }

    }
}
