package at.flori4n.ctf;
import at.flori4n.ctf.data.CtfTeam;
import at.flori4n.ctf.data.GameData;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class LobbyListeners implements Listener {
    private int taskId;
    private boolean taskRunning = false;
    private GameData gameData = GameData.getInstance();
    private LobbyState lobbyState;


    public LobbyListeners(LobbyState lobbyState){
        this.lobbyState = lobbyState;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        p.teleport(gameData.getLobbyLocation());
        p.getInventory().clear();
        p.setGameMode(GameMode.SURVIVAL);

        ItemStack teamSelector = new ItemStack(Material.BED);
        ItemMeta teamSelectorMeta = teamSelector.getItemMeta();
        teamSelectorMeta.setDisplayName("Team Selector");
        teamSelector.setItemMeta(teamSelectorMeta);
        p.getInventory().addItem(teamSelector);

        if (!taskRunning&&Bukkit.getOnlinePlayers().size()>=gameData.getPlayersToStart()){
            lobbyState.startCounter();
        }else if (taskRunning&&Bukkit.getOnlinePlayers().size()<gameData.getPlayersToStart()){
            lobbyState.stopCounter();
        }

    }
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player p = event.getPlayer();
        CtfTeam t = gameData.getPlayerTeam(p);
        if (t!=null) t.removePlayer(p);
    }

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
    }
    @EventHandler
    public void onServerPing(ServerListPingEvent e){
        e.setMotd("Lobby");
    }
}
