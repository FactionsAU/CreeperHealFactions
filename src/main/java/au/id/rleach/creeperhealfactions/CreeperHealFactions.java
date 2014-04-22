/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package au.id.rleach.creeperhealfactions;

import au.id.rleach.creeperhealfactions.timewarp.PlayerTime;
import au.id.rleach.creeperhealfactions.timewarp.TimeManager;
import com.massivecraft.factions.entity.BoardColls;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.UPlayer;
import com.massivecraft.factions.event.FactionsEventChunkChange;
import com.massivecraft.mcore.ps.PS;
import java.util.LinkedList;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

/**
 *
 * @author ja731j
 */
public class CreeperHealFactions extends JavaPlugin implements Listener{
    TimeManager tm;
    private int timeTaskId;
    @Override
    public void onEnable() {
        tm = new TimeManager(new LinkedList<PlayerTime>());
        
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        timeTaskId = scheduler.scheduleSyncRepeatingTask(this,tm.getRepeatingTask(),0,1);
        getServer().getPluginManager().registerEvents(this, this);
        
        
        getLogger().info("The CreeperHealFactions plugin has been loaded");
    }

    @Override
    public void onDisable() {
        Bukkit.getServer().getScheduler().cancelTask(timeTaskId);
        tm = null;
        HandlerList.unregisterAll((Plugin) this);
        getLogger().info("The CreeperHealFactions plugin has been disabled");
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerMoveEvent(PlayerMoveEvent e)
    {
        playerMove(e.getPlayer(), e.getTo(), e.getFrom());
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onVehicles(VehicleMoveEvent e){
        Entity pass = e.getVehicle().getPassenger();
        if(pass instanceof Player){
            playerMove((Player) pass, e.getTo(), e.getFrom());
        }
    }

    private void playerMove(Player player, Location to, Location from){
        final Chunk toChunk   = to  .getChunk();
        final Chunk fromChunk = from.getChunk();
        if ( ! toChunk.equals(fromChunk))
        {
            this.OnPlayerChunkChange(player, fromChunk, toChunk);
        }
    }
    
    private void OnPlayerChunkChange(Player player, Chunk fromChunk, Chunk toChunk) 
    {
        Bukkit.getLogger().log(Level.WARNING, "chunk");
       // UPlayer mover       = UPlayer   .get        (player)    ;
       // Faction ownFaction  = mover     .getFaction ()          ;
       // Faction to   = BoardColls.get().getFactionAt(PS.valueOf(toChunk  ));
       // Faction from = BoardColls.get().getFactionAt(PS.valueOf(fromChunk));
        
        long goal = (long) Math.random()*24000;
        tm.timewarp(player, goal);
    }
    
    //raining if raidable or if enemy raidable
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void OnPlayerQuit(PlayerQuitEvent p){
        tm.removePlayer(p.getPlayer());
    }
}
