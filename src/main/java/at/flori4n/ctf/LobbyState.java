package at.flori4n.ctf;

import at.flori4n.ctf.data.CtfTeam;
import at.flori4n.ctf.data.GameData;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import java.util.Iterator;

public class LobbyState implements State {

    private final LobbyListeners lobbyListeners = new LobbyListeners(this);
    private final GuiListeners guiListeners = new GuiListeners();
    private int taskId;
    private GameData gameData = GameData.getInstance();
    @Getter
    private boolean taskRunning = false;

    @Override
    public void preaction() {
        System.out.println("initLobbyState");
        Bukkit.getPluginManager().registerEvents(lobbyListeners, Ctf.getPlugin());
        Bukkit.getPluginManager().registerEvents(guiListeners, Ctf.getPlugin());

    }

    @Override
    public void action() {
        System.out.println("runningLobbyState");
    }

    @Override
    public void postAction() {
        System.out.println("stopLobbyState");
        HandlerList.unregisterAll(lobbyListeners);
        HandlerList.unregisterAll(guiListeners);
        stopCounter();
        putPlayersInTeams();
    }
    public void putPlayersInTeams(){
        Iterator<CtfTeam> teamsIterator = gameData.getTeams().iterator();
        Iterator<? extends Player> playerIterator = Bukkit.getOnlinePlayers().iterator();
        CtfTeam team = teamsIterator.next();
        Player player = playerIterator.next();
        while (true){
            System.out.println(team.getName());
            System.out.println(teamsIterator.hasNext());
            System.out.println(player.getDisplayName());
            System.out.println(playerIterator.hasNext());
            try {
                team.addPlayer(player);
                if (!playerIterator.hasNext())return;
                player = playerIterator.next();
                System.out.println("test");
            }catch (RuntimeException e){
                System.out.println("tes2");
                if (!teamsIterator.hasNext())return;
                System.out.println("tes3");
                team = teamsIterator.next();
            }
        }
    }

    public void startCounter(){
        taskRunning = true;
        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Ctf.getPlugin(), new Runnable() {
            int counter = GameData.getInstance().getLobbyTime();
            @Override
            public void run() {

                if (counter%10 == 0 && counter>0){
                    Bukkit.broadcastMessage("Runde startet in "+counter+" Sekunden");
                    playSound();
                }
                if (counter<5){
                    Bukkit.broadcastMessage("Runde startet in "+counter+" Sekunden");
                    playSound();
                }
                if (counter<=0){
                    Manager.getInstance().setState(new IngameState());
                }
                counter --;
            }
            public void playSound(){
                for (Player player:Bukkit.getOnlinePlayers()){
                    player.playSound(player.getLocation(), Sound.NOTE_BASS_DRUM,1,1);
                }
            }
        }, 0, 20);

    }

    public void stopCounter(){
        Bukkit.broadcastMessage("Start abgebrochen");
        Bukkit.broadcastMessage("Zu wenig Spieler");
        Bukkit.getScheduler().cancelTask(taskId);
        taskRunning = false;
    }


}
