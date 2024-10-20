package at.flori4n.ctf.data;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
public class ConfigCommands implements CommandExecutor {
    GameData gameData = GameData.getInstance();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        Player player = (Player) sender;
        CtfTeam team;
        if (!player.hasPermission("config"))return false;
        switch (strings[0]){
            case "setStart":
                gameData.setStart(Boolean.parseBoolean(strings[1]));
                player.sendMessage("value changed");
                break;
            case "addTeam":
                gameData.addTeam(
                        new CtfTeam(
                                strings[1],
                                Integer.parseInt(strings[2]),
                                player.getLocation()
                        )
                );
                //player.sendMessage("config saved");
                break;
            case "removeTeam":
                gameData.removeTeam(gameData.getTeamByName(strings[1]));
                break;
            case "setFlagLocation":
                team =gameData.getTeamByName(strings[1]);
                team.setFlagLocation(player.getLocation());
                team.setFlagBlock(player.getItemInHand());
                break;
            case "printTeams":
                gameData.printTeams(player);
                break;
            case "save":
                gameData.saveConfig();
                //player.sendMessage("config saved");
                break;
            case "setLobby":
                gameData.setLobbyLocation(player.getLocation());
                //player.sendMessage("value changed");
                break;
            case "setLobbyTime":
                gameData.setLobbyTime(Integer.parseInt(strings[1]));
                //player.sendMessage("value changed");
                break;
            case "setPlayersToStart":
                gameData.setPlayersToStart(Integer.parseInt(strings[1]));
                //player.sendMessage("value changed");
                break;
            default:
                player.sendMessage("Invalid Command");
                break;
        }
        return false;
    }
}
