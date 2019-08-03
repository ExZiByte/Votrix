package votrix.Discord;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import votrix.Discord.listeners.*;
import votrix.Discord.commands.*;

public class Votrix {
    public static void main(String[] args) throws LoginException, RateLimitedException, InterruptedException{
        final JDABuilder votrix = new JDABuilder(AccountType.BOT).setToken(System.getenv("VOTRIXTOKEN").toString());

        votrix.setGame(Game.watching("the loading bar!"));
        votrix.setStatus(OnlineStatus.DO_NOT_DISTURB);

        //Commands
        //Moderation
        votrix.addEventListener(new Ban());
        votrix.addEventListener(new Clear());
        votrix.addEventListener(new Mute());
        votrix.addEventListener(new Softban());


        //Misc Event Listeners
        votrix.addEventListener(new Ready());
        votrix.addEventListener(new GuildMemberJoin());
        votrix.addEventListener(new GuildMemberLeave());

        

        votrix.build();
    }
}
