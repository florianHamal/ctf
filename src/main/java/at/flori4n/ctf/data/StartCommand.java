package at.flori4n.ctf.data;

import at.flori4n.ctf.IngameState;
import at.flori4n.ctf.Manager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StartCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player) commandSender;
        if (player.hasPermission("game.start")) Manager.getInstance().setState(new IngameState());
        return false;
    }
}
