package at.flori4n.ctf.data;

import at.flori4n.ctf.Ctf;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class GameData {

    private static GameData instance = null;
    @Getter
    List<CtfTeam> teams = new ArrayList<>();
    @Getter
    @Setter
    private boolean start;
    @Getter
    @Setter
    private Location lobbyLocation;
    @Getter
    @Setter
    private int lobbyTime;
    @Getter
    @Setter
    private int playersToStart;


    private GameData(){
        Bukkit.getScoreboardManager().getMainScoreboard().getTeams().forEach(Team::unregister);
        setDefaultConf();
        loadConf();
    }

    private void loadConf(){
        FileConfiguration config = Ctf.getPlugin().getConfig();

        //singleValues
        start = config.getBoolean("start");
        lobbyLocation = (Location) config.get("lobby");
        lobbyTime = config.getInt("lobbyTime");
        playersToStart = config.getInt("playersToStart");


        //teams
        ConfigurationSection section = config.getConfigurationSection("teams");
        if (section==null) return;
        for (String s:section.getKeys(false)){
            ConfigurationSection teamSection = section.getConfigurationSection(s);
            System.out.println(teamSection.getName());
            CtfTeam team = new CtfTeam(
                    teamSection.getName(),
                    teamSection.getInt("size"),
                    (Location) teamSection.get("spawn"),
                    (Location) teamSection.get("flagLocation")
            );
            teams.add(team);
        }


    }
    public void setDefaultConf(){
        FileConfiguration config =Ctf.getPlugin().getConfig();
        config.addDefault("start",false);
        config.addDefault("playersToStart",2);
        config.addDefault("lobbyTime",60);
        Ctf.getPlugin().saveConfig();
    }

    public void printTeams(Player player){
        teams.forEach(t->
            player.sendMessage(t.getName()+"_"+t.getSize()+"_"+t.getSpawn().serialize()+"\n")
        );
    }

    public void saveConfig(){
        FileConfiguration config = Ctf.getPlugin().getConfig();

        //general config
        config.set("start",start);
        config.set("lobby",lobbyLocation);
        config.set("lobbyTime",lobbyTime);
        config.set("playersToStart",playersToStart);


        //teams
        ConfigurationSection section = config.createSection("teams");
        for (CtfTeam team:teams){
            ConfigurationSection teamSection = section.createSection(team.getName());
            teamSection.set("size",team.getSize());
            teamSection.set("spawn",team.getSpawn());
            teamSection.set("flagLocation",team.getFlagLocation());
        }
        Ctf.getPlugin().saveConfig();
    }

    public void addTeam(CtfTeam team){
        teams.add(team);
    }
    public CtfTeam getTeamByFlag(LivingEntity flag){
        for (CtfTeam t:teams){
            if (t.getFlag() == flag) return t;
        }
        return null;
    }

    public CtfTeam getPlayerTeam(Player player){
        for (CtfTeam t :teams){
            if (t.hasPlayer(player))return t;
        }
        return null;
    }
    public CtfTeam getTeamByName(String name){
        for (CtfTeam t :teams){
            if (Objects.equals(t.getName(), name))return t;
        }
        return null;
    }

    public static GameData getInstance(){
        if (instance==null)instance=new GameData();
        return instance;
    }

}
