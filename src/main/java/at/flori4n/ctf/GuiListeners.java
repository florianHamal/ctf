package at.flori4n.ctf;
import at.flori4n.ctf.data.CtfTeam;
import at.flori4n.ctf.data.GameData;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class GuiListeners implements Listener {

    private GameData gameData =GameData.getInstance();
    private static String GUI_NAME = "Select Team";

    public void openGUI(Player player){

        Inventory inventory = Bukkit.createInventory(null,
                ((int) (gameData.getTeams().size()/9)+1)*9,
                GUI_NAME);

        int i = 0;

        for (CtfTeam team : gameData.getTeams()){
            ItemStack itemStack = new ItemStack(Material.BED);
            List<String> lore = new ArrayList<>();
            lore.add(team.getPlayers().size()+"/"+team.getSize());
            lore.add("Members:");
            for (Player p:team.getPlayers()){
                lore.add("   "+p.getName());
            }
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setLore(lore);
            itemMeta.setDisplayName(team.getName());
            itemStack.setItemMeta(itemMeta);
            inventory.setItem(i,itemStack);
            i++;
        }
        player.openInventory(inventory);
    }

    @EventHandler
    public void handleOpener(PlayerInteractEvent event){

        if (event.getItem() == null || event.getItem().getType() != Material.BED) return;
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        openGUI(event.getPlayer());
    }

    @EventHandler
    public void blockPlaceEvent(BlockPlaceEvent event){
        event.setCancelled(true);
    }

    @EventHandler
    public void handleSelectorGUIClick(InventoryClickEvent event){
        event.setCancelled(true);
        if (!(event.getWhoClicked()instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        if (!event.getInventory().getName().equals(GUI_NAME))return;
        if (event.getCurrentItem()==null || event.getCurrentItem().getType()!=Material.BED)return;
        String itemName = event.getCurrentItem().getItemMeta().getDisplayName();
        try {
            gameData.getTeamByName(itemName).addPlayer(player);
            //player gets removed from old team automatically
        }catch (RuntimeException ex){
           player.sendMessage(ex.getMessage());
        }
        player.closeInventory();
    }
    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event){
        event.setCancelled(true);
    }
}
