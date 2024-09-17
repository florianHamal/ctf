package at.flori4n.ctf.data;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
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

    @Getter
    private ArmorStand flag;

    @Getter
    @Setter
    private Location flagLocation;


    public CtfTeam(String name, int size, Location spawn, Location flagLocation) {
        this.spawn = spawn;
        this.size = size;
        this.flagLocation = flagLocation;
        setupTeam(name);
    }

    public CtfTeam(String name, int size, Location spawn){
        this.size = size;
        this.spawn = spawn;
        setupTeam(name);
    }

    private void setupTeam(String name){
        team = Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam(name);
        team.setPrefix(name+": ");
        team.setAllowFriendlyFire(false);
    }
    public void addPlayer(OfflinePlayer player){
        if (team.getSize()<size){
            team.addEntry(player.getName());
        }else {
            throw new RuntimeException("teamIsFull");
        }
    }
    public void setFlag(ArmorStand e){
        flag = e;
        e.setCustomName(team.getName()+ " Flag");
        e.setCustomNameVisible(true);
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
