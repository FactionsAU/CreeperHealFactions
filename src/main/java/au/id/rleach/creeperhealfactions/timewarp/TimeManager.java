/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package au.id.rleach.creeperhealfactions.timewarp;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class TimeManager {

    List<PlayerTime> active;
    List<PlayerTime> toRemove = new LinkedList<PlayerTime>();
    HashMap <Player,PlayerTime> times;
    
    //Times
    final static long dawn             =      0;
    final static long day              =   1000;
    final static long noon             =   6000;
    final static long dusk             =  12000;
    final static long night            =  14000;
    final static long midnight         =  18000;
    final static long Morning_twilight =  22009;
    final static long BURN             =  23459;
    //DAY OF
    final static long Full_Moon        =      0;
    final static long Waning_Gibbous   =  24000;
    final static long Last_Quarter     =  48000;
    final static long Waning_Crescent  =  72000;
    final static long New_Moon         =  96000;
    final static long Waxing_Crescent  = 120000;
    final static long First_Quarter    = 144000;
    final static long Waxing_Gibbous   = 168000;

    
    public TimeManager(List<PlayerTime> active){
        this.active = active;
        times = new HashMap<Player,PlayerTime>();
    }
    /**
     goal is a time, not a datetime. values between >=0 and <24000 only
     **/
    public void timewarp(Player p, long goal){
        PlayerTime pt = contains(p);
        if(pt == null){
            pt = new PlayerTime(p, goal, this);
            times.put(p, pt);
        } else {
            pt.setGoal(goal);
        }
        active.add(pt);
    }
    
    void stop(PlayerTime pt){
        //This is called inside tick(), we can't remove from active if we use a foreach.
        toRemove.add(pt);
    }
    
    void tick(){
        Bukkit.getLogger().log(Level.WARNING, "tick");
        for(PlayerTime pt: active){
            pt.tick();
        }
        active.removeAll(toRemove);
        toRemove.clear();
    }
    
    PlayerTime contains(Player p){
        for(PlayerTime pt : active){
            if(pt.getPlayer() == p) return pt;
        }
        return times.get(p);
    }
    
    public void removePlayer(Player p){
        //Call it twice, to remove from active and hashmap
        PlayerTime found = contains(p);
        active.remove(found);
        found = contains(p);
        active.remove(found);
    }

    public Runnable getRepeatingTask() {
        return new BukkitRunnable(){
            public void run() {
                tick();
            }
        };
    }
}
