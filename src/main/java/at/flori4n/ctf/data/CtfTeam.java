package at.flori4n.ctf.data;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CtfTeam {
    private Team team;
    @Getter
    @Setter
    private Location spawn;
    @Getter
    private int size;


    public CtfTeam(String name,int size,Location spawn){
        team = Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam(name);
        team.setPrefix(name+": ");
        team.setAllowFriendlyFire(false);
        this.size = size;
        this.spawn = spawn;
    }
    public void addPlayer(OfflinePlayer player){
        if (team.getSize()<size){
            team.addEntry(player.getName());
        }else {
            throw new RuntimeException("teamIsFull");
        }
    }
    public void removePlayer(OfflinePlayer player){
        team.removeEntry(player.getName());
    }
    public boolean hasPlayer(OfflinePlayer player){
        return team.hasEntry(player.getName());
    }
    public List<Player> getPlayers(){
        List<Player> players = new ArrayList<>();
        for (String name : team.getEntries()){
            System.out.println(name);
             players.add(Bukkit.getPlayer(name));
        }
        System.out.println(players.size());
        return players;
    }

    public String getName(){
        return team.getName();
    }

}
