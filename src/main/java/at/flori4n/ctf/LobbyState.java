package at.flori4n.ctf;

import at.flori4n.ctf.data.CtfTeam;
import at.flori4n.ctf.data.GameData;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

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

        List<CtfTeam> teams = gameData.getTeams();
        List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
        while (!players.isEmpty()&&!teams.isEmpty()) {
            Player player = players.get(0);
            CtfTeam team = getLowestTeam(teams);
            if (team.isFull()) {
                teams.remove(team);
                continue;
            }
            if (gameData.getPlayerTeam(player) == null) team.addPlayer(player);
            players.remove(player);
        }
        System.out.println("playerS::");
        Bukkit.getOnlinePlayers().forEach(player -> {
            System.out.println(player.getName()+"\n");
        });
        System.out.println("playerS::");
    }
    public CtfTeam getLowestTeam(List<CtfTeam> teams){
        CtfTeam lowestTeam = teams.get(0);
        for (CtfTeam team: teams){
            if (team.getPlayers().size()<lowestTeam.getPlayers().size()){
                lowestTeam = team;
            }
        }
        return lowestTeam;
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
        Bukkit.getScheduler().cancelTask(taskId);
        taskRunning = false;
    }


}
