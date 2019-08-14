package votrix.Discord.commands.Settings;

import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import votrix.Discord.utils.Data;

public class match extends ListenerAdapter {

    public void onGuildMessageReceived(GuildMessageReceivedEvent event){
        String[] args = event.getMessage().getContentRaw().split("\\s+");
        Data data = new Data(); //replace with the file you store your prefix in
        Role PC = event.getJDA().getRoleById("541091501908951041");
        Role PS4 = event.getJDA().getRoleById("606527283582468106");
        Role XBOX = event.getJDA().getRoleById("606527228074786851");
        Role MOBILE = event.getJDA().getRoleById("606531352690688035");

        if(args[0].equalsIgnoreCase(data.getPrefix()/*replace with how you grab your prefix*/ + "match")){
            if(args.length < 2){

            } else if(args.length == 2){
                if(args[1].equalsIgnoreCase("PC")) {
                    event.getChannel().sendMessage(event.getAuthor().getAsMention() + " is now looking to play! This user looking for matches on ***__COMPUTER__***! Come join, " + PC.getAsMention() + "!").queue();
                    event.getGuild().getController().addSingleRoleToMember(event.getMember(), PC).queue();
                }
                else if(args[1].equalsIgnoreCase("PS")) {
                    event.getChannel().sendMessage(event.getAuthor().getAsMention() + " is now looking to play! This user looking for matches on ***__PLAYSTATION__***! Come join, " + PS4.getAsMention() + "!").queue();
                    event.getGuild().getController().addSingleRoleToMember(event.getMember(), PS4).queue();
                }
                else if(args[1].equalsIgnoreCase("XBOX")) {
                    event.getChannel().sendMessage(event.getAuthor().getAsMention() + " is now looking to play! This user looking for matches on ***__XBOX__***! Come join, " + XBOX.getAsMention() + "!").queue();
                    event.getGuild().getController().addSingleRoleToMember(event.getMember(), XBOX).queue();
                }
                else if(args[1].equalsIgnoreCase("MOBILE")) {
                    event.getChannel().sendMessage(event.getAuthor().getAsMention() + " is now looking to play! This user looking for matches on ***__MOBILE__***! Come join, " + MOBILE.getAsMention() + "!").queue();
                    event.getGuild().getController().addSingleRoleToMember(event.getMember(), MOBILE).queue();
                }
            } else{
                for(int i = 1; i < args.length; i++){
                    if(args[i].equalsIgnoreCase("PC") || args[i].equalsIgnoreCase("PS") || args[i].equalsIgnoreCase("XBOX") || args[i].equalsIgnoreCase("MOBILE")) {
                        event.getGuild().getController().addSingleRoleToMember(event.getMember(), event.getGuild().getRolesByName(args[i], true).get(0)).queue();                    }
                }
            }
        }
    }
}
