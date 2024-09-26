package at.flori4n.ctf;

import at.flori4n.ctf.data.CtfTeam;
import at.flori4n.ctf.data.GameData;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class IngameState implements State {
    private final IngameListeners ingameListeners = new IngameListeners();
    private GameData gameData = GameData.getInstance();
    private int taskId;
    @Override
    public void preaction() {
        System.out.println("initIngameState");
        Bukkit.getPluginManager().registerEvents(ingameListeners, Ctf.getPlugin());



        for (Player player:Bukkit.getOnlinePlayers()){
            player.setGameMode(GameMode.SPECTATOR);
            player.getInventory().clear();
        }

        for (CtfTeam team:gameData.getTeams()){
            Location loc = team.getFlagLocation();
            ArmorStand flag = (ArmorStand) loc.getWorld().spawnEntity(loc,EntityType.ARMOR_STAND);
            flag.getEquipment().setHelmet(new ItemStack(Material.REDSTONE_BLOCK));
            flag.setVisible(false);
            flag.setMetadata("flori4n.ctf.flag",new FixedMetadataValue(Ctf.getPlugin(),"flori4n.ctf.flag"));
            team.setFlag(flag);
            for (Player p :team.getPlayers()){
                p.setGameMode(GameMode.SURVIVAL);
                p.teleport(team.getSpawn());
                p.setBedSpawnLocation(team.getSpawn());
            }
        }
    }

    @Override
    public void action() {
        System.out.println("runningIngameState");
        //flag Effects
        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Ctf.getPlugin(), new Runnable() {
            int counter = 0;
            @Override
            public void run() {

                for (CtfTeam team:gameData.getTeams()){
                    ArmorStand armorStand = team.getFlag();
                    armorStand.getLocation().getWorld().playEffect(armorStand.getLocation(),Effect.FLAME,1,1);
                    if (counter%20==0)armorStand.getLocation().getWorld().playSound(armorStand.getLocation(),Sound.FIREWORK_LAUNCH,1,1);
                }
                counter ++;
                if (counter >20)counter=0;
            }
        }, 0, 0);
    }

    @Override
    public void postAction() {
        System.out.println("stoppingIngameState");
        Bukkit.getScheduler().cancelTask(taskId);
        HandlerList.unregisterAll(ingameListeners);
    }

    public void equipPlayer(Player p){
        p.getInventory().setHelmet(new ItemStack(Material.CHAINMAIL_HELMET));
        p.getInventory().setBoots(new ItemStack(Material.CHAINMAIL_BOOTS));
        p.getInventory().setChestplate(new ItemStack(Material.CHAINMAIL_CHESTPLATE));
        p.getInventory().setLeggings(new ItemStack(Material.CHAINMAIL_LEGGINGS));
        p.getInventory().addItem(new ItemStack(Material.STONE_SWORD));
        p.getInventory().addItem(new ItemStack(Material.BOW));

        ItemStack itemStack = new ItemStack(Material.ARROW);
        itemStack.setAmount(10);
        p.getInventory().addItem(itemStack);
    }
}
