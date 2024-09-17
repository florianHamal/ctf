package at.flori4n.ctf;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;

public class IngameState implements State {
    private final IngameListeners ingameListeners = new IngameListeners();
    @Override
    public void preaction() {
        System.out.println("initIngameState");
        Bukkit.getPluginManager().registerEvents(ingameListeners, Ctf.getPlugin());
    }

    @Override
    public void action() {
        System.out.println("runningIngameState");
    }

    @Override
    public void postAction() {
        System.out.println("stoppingIngameState");
        HandlerList.unregisterAll(ingameListeners);
    }
}
