package at.flori4n.ctf;

import at.flori4n.ctf.data.CtfTeam;
import at.flori4n.ctf.data.GameData;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.github.paperspigot.Title;
import org.spigotmc.event.entity.EntityDismountEvent;

public class IngameListeners implements Listener {

    private GameData gameData = GameData.getInstance();
    @EventHandler
    public void onServerPing(ServerListPingEvent e){
        e.setMotd("Ingame");
    }
    @EventHandler
    public void onEntityDamageEvent(EntityDamageByEntityEvent e){
        if (!(e.getDamager() instanceof Player))return;
        Player player = (Player) e.getDamager();
        Entity entity = e.getEntity();
        if (entity.getType() != EntityType.ARMOR_STAND)return;
        e.setCancelled(true);
        CtfTeam flagTeam = gameData.getTeamByFlag((LivingEntity) entity);
        CtfTeam playerTeam = gameData.getPlayerTeam((Player) player);
        if (flagTeam==null)return;
        if (playerTeam==null)return;
        if (playerTeam==flagTeam){
            if (entity.getLocation().equals(flagTeam.getFlagLocation()))return;
            entity.teleport(playerTeam.getFlagLocation());
            Bukkit.getOnlinePlayers().forEach(p ->
                    p.sendTitle(new Title(player.getName(),"§ahat die Fahne von "+flagTeam.getName() +"§a zurück gebracht",1*20,1*20,1*20)));
        }else {
            ArmorStand armorStand = (ArmorStand) e.getEntity();
            armorStand.setMarker(true);
            armorStand.setCustomNameVisible(false);
            e.getDamager().setPassenger(armorStand);
            Bukkit.getOnlinePlayers().forEach(p ->
                    p.sendTitle(new Title(player.getName(),"§4hat die Fahne von "+flagTeam.getName() +"§4 gestohlem",1*20,1*20,1*20)));
        }

    }

    @EventHandler
    public void onPLayerDeath(PlayerDeathEvent e){
        ArmorStand flag = (ArmorStand) e.getEntity().getPassenger();
        if (flag==null||flag.getType()!=EntityType.ARMOR_STAND)return;
        CtfTeam ctfTeam = gameData.getTeamByFlag(flag);
        if (ctfTeam==null)return;
        flag.setMarker(false);
        flag.setCustomNameVisible(true);
    }
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e){
        Player player = e.getPlayer();
        LivingEntity flag = (LivingEntity) player.getPassenger();
        if (flag==null||flag.getType()!=EntityType.ARMOR_STAND)return;
        CtfTeam team = gameData.getPlayerTeam(player);
        if (team==null)return;
        if (isInSpawn(player.getLocation(),team.getSpawn())) Manager.getInstance().setState(new GameOverState(team));
    }
    private boolean isInSpawn(Location playerLoc,Location spawnLoc){
        int offset = 2;
        if (playerLoc.getX()>spawnLoc.getX()-offset && playerLoc.getX()<spawnLoc.getX()+offset){
            if (playerLoc.getY()>spawnLoc.getY()-offset && playerLoc.getY()<spawnLoc.getY()+offset){
                if (playerLoc.getZ()>spawnLoc.getZ()-offset && playerLoc.getZ()<spawnLoc.getZ()+offset){
                    return true;
                }
            }
        }
        return false;
    }
    @EventHandler
    public void onPlayerInteract(PlayerInteractAtEntityEvent e){
        if (e.getRightClicked().getType()==EntityType.ARMOR_STAND)e.setCancelled(true);
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
        Player player = e.getPlayer();
        player.setGameMode(GameMode.SPECTATOR);
        player.teleport(gameData.getLobbyLocation());
        player.setBedSpawnLocation(gameData.getLobbyLocation());
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
    public void onVehicleExit(EntityDismountEvent e){
        e.setCancelled(true);
    }

}
