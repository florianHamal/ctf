package at.flori4n.ctf;

import at.flori4n.ctf.data.GameData;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

public class GameOverListeners implements Listener {
    private GameData gameData = GameData.getInstance();
    @EventHandler
    public void foodListener(FoodLevelChangeEvent e){
        e.setCancelled(true);
    }

    @EventHandler
    public void damageListener(EntityDamageEvent e){
        e.setCancelled(true);
    }
    @EventHandler
    public void blockBreakListener(BlockBreakEvent e){
        e.setCancelled(true);
    }
    @EventHandler
    public void weatherChangeListener(WeatherChangeEvent e){
        e.setCancelled(true);
        e.getWorld().setThundering(false);
    }
    @EventHandler
    public void playerJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        p.setGameMode(GameMode.SPECTATOR);
        p.teleport(gameData.getLobbyLocation());
        p.setBedSpawnLocation(gameData.getLobbyLocation());
    }
    @EventHandler
    public void onServerPing(ServerListPingEvent e){
        e.setMotd("Lobby");
    }
}
