package at.flori4n.ctf;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class IngameListeners implements Listener {
    @EventHandler
    public void onServerPing(ServerListPingEvent e){
        e.setMotd("Lobby");
    }
}
