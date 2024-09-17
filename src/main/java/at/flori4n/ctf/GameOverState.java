package at.flori4n.ctf;

import at.flori4n.ctf.data.CtfTeam;
import at.flori4n.ctf.data.GameData;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.scoreboard.Team;
import org.github.paperspigot.Title;

public class GameOverState implements State{

    private final GameOverListeners gameOverListeners = new GameOverListeners();

    private CtfTeam winner;
    public GameOverState(CtfTeam winner){
        this.winner = winner;
    }
    @Override
    public void preaction() {
        Bukkit.getPluginManager().registerEvents(gameOverListeners, Ctf.getPlugin());
    }

    @Override
    public void action() {
        String players ="";
        for (Player player: winner.getPlayers()){
            players+= player.getName()+" ";
        }
        for (Player player: Bukkit.getOnlinePlayers()){
            player.sendTitle(new Title("§2Team "+winner.getName()+"§2 hat gewonnen",players,1*20,10*20,1*20));
        }

        Bukkit.getScheduler().scheduleSyncRepeatingTask(Ctf.getPlugin(), new Runnable() {
            int counter = 20;
            @Override
            public void run() {
                counter --;
                if (counter<=0){
                    Bukkit.shutdown();
                }
            }
        }, 0, 20);

    }

    @Override
    public void postAction() {
        HandlerList.unregisterAll(gameOverListeners);
    }
}
