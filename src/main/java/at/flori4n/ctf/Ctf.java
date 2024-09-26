package at.flori4n.ctf;

import at.flori4n.ctf.data.ConfigCommands;
import at.flori4n.ctf.data.GameData;
import at.flori4n.ctf.data.StartCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class Ctf extends JavaPlugin {
    private static Ctf instance;
    @Override
    public void onEnable(){
        instance = this;
        getCommand("ctf").setExecutor(new ConfigCommands());
        getCommand("start").setExecutor(new StartCommand());
        if (GameData.getInstance().isStart()){
            Manager.getInstance().setState(new LobbyState());
        }

    }
    public static Ctf getPlugin(){
        return instance;
    }

}
