package votrix.Discord;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import votrix.Discord.commands.Fun.CleverBot;
import votrix.Discord.commands.Fun.Screenshare;
import votrix.Discord.commands.Information.Help;
import votrix.Discord.commands.Miscellaneous.Suggest;
import votrix.Discord.commands.Moderation.*;
import votrix.Discord.commands.Settings.SetPrefix;
import votrix.Discord.listeners.*;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Votrix {

    public static void main(String[] args) throws LoginException, RateLimitedException, InterruptedException{

        //Disable MongoDB Logs
        Logger mongoLogger = Logger.getLogger( "org.mongodb.driver" );
        mongoLogger.setLevel(Level.OFF);

        final JDABuilder votrix = new JDABuilder(AccountType.BOT).setToken(System.getenv("VOTRIXTOKEN"));

        votrix.setGame(Game.watching("the loading bar!"));
        votrix.setStatus(OnlineStatus.DO_NOT_DISTURB);
        //Commands
        //Information
        votrix.addEventListener(new Help());

        //Fun
        votrix.addEventListener(new CleverBot());
        votrix.addEventListener(new Screenshare());

        //Miscellaneous
        votrix.addEventListener(new Suggest());

        //Moderation
        votrix.addEventListener(new RoleAdd());
        votrix.addEventListener(new RoleRemove());
        votrix.addEventListener(new Ban());
        votrix.addEventListener(new Clear());
        votrix.addEventListener(new Kick());
        votrix.addEventListener(new Mute());
        votrix.addEventListener(new Softban());
        votrix.addEventListener(new Tempmute());
        votrix.addEventListener(new Unmute());

        //Settings
        votrix.addEventListener(new SetPrefix());

        //Misc Event Listeners
        votrix.addEventListener(new Ready());
        votrix.addEventListener(new GuildMemberJoin());
        votrix.addEventListener(new GuildMemberLeave());
        votrix.addEventListener(new SuggestionReactAdd());

        //Developmental Listeners
        votrix.addEventListener(new AltReJoin());

        

        votrix.build();
        System.out.println("Loaded");
    }
}
