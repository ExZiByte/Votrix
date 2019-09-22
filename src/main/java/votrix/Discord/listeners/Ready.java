package votrix.Discord.listeners;

import java.awt.*;
import java.time.Instant;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import votrix.Discord.utils.Data;

public class Ready extends ListenerAdapter {

    public void onReady(ReadyEvent event){
        Data data = new Data();
        EmbedBuilder eb = new EmbedBuilder();
        Game[] games = { Game.playing("with Somato Setchup"), Game.playing("with Biscuits"), Game.watching("Toe Tucks"), Game.watching("https://twitch.tv/kylaak_") };
        ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
        ses.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                Random random = new Random();
                int game = random.nextInt(games.length);
                event.getJDA().getPresence().setGame(games[game]);
            }
        }, 0, 2, TimeUnit.MINUTES);

        eb.setDescription("Restarting... Be back up in 30 - 45 seconds");
        eb.setColor(new Color(data.getColor()));
        eb.setTimestamp(Instant.now());
        eb.setFooter("Bot Restart", data.getSelfAvatar(event));
        new java.util.Timer().schedule(
            new java.util.TimerTask() {
                @Override
                public void run() {
                    data.getLogChannel(event).sendMessage(eb.build()).queue((message) -> {
                        event.getJDA().getGuildById("578937882023034901").getTextChannelsByName("general", true).get(0).sendMessage(eb.build()).queue((message1) -> {
                            event.getJDA().shutdownNow();
                            System.exit(1);
                        });
                    });
                }
            },  180000
        );
    }

}
