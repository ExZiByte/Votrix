package votrix.Discord.listeners;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import votrix.Discord.Data;

public class Ready extends ListenerAdapter {

    public void onReady(ReadyEvent event){
        Data data = new Data();
        //Game[] games = { Game.playing("with Somato Setchup"), Game.playing("with Biscuits"), Game.watching("Toe Tucks"), Game.watching("https://twitch.tv/kylaak_") };
        Game[] games = {Game.playing("on the developmentally delayed track")};
        ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
        ses.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                Random random = new Random();
                int game = random.nextInt(games.length);
                event.getJDA().getPresence().setGame(games[game]);
            }
        }, 0, 2, TimeUnit.MINUTES);
    }

}
