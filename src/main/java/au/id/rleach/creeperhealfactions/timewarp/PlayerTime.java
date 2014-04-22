/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package au.id.rleach.creeperhealfactions.timewarp;

import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;


public class PlayerTime {
    Overshooter os;
    Player p;
    TimeManager tm;
    long originalTime;
    final static long ticksInDay = 24000;
    
    public PlayerTime(Player p, long goal, TimeManager tm){
        this.p = p;
        this.tm = tm;
        originalTime = p.getPlayerTime();
        long curTime = originalTime;
        //int maths applies, automatic rounding, yay!
        long curDate = (curTime / ticksInDay) * 24000;
        curTime = curTime % ticksInDay;
        
        final long a = goal - curTime;
        final long b = 24000-a;
        //Go the small way around.
        if(a<=b) os = new Overshooter(0,  a, this);
        else     os = new Overshooter(0, -b, this);
        
        
        
    }

    Player getPlayer() {
        return p;
    }

    void setGoal(long goal) {
        os.setGoal(goal);
    }

    void tick() {
        long offset = this.os.next();
        Bukkit.getLogger().log(Level.WARNING,"tock: "+ originalTime+offset);
        p.setPlayerTime(originalTime+offset, false);
    }

    void stop() {
        this.tm.stop(this);
    }
}
