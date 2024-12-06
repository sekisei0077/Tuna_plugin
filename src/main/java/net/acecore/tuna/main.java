package net.acecore.tuna;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.ArrayList;
import java.util.Arrays;

public class main implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        //TODO:鯖再起時にこれらを何とかするreset処理を記述
        Tuna.set_uped.put(event.getPlayer(), true);
        system.join(event.getPlayer());
    }



    @EventHandler
    public void onPlayerRespawnEvent(PlayerRespawnEvent event) {
        system.respawn_reset_timer(event.getPlayer());
    }
    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent event){
        if(!Tuna.set_uped.get(event.getPlayer())){
            system.join(event.getPlayer());
            Tuna.set_uped.put(event.getPlayer(), true);
        }
        Double befor_x = Tuna.coordinate.get(event.getPlayer()).get(0);
        Double befor_y = Tuna.coordinate.get(event.getPlayer()).get(1);
        Double befor_z = Tuna.coordinate.get(event.getPlayer()).get(2);
        Location now_loc = event.getPlayer().getLocation();
        if((befor_x != now_loc.getX() || befor_y != now_loc.getY() || befor_z != now_loc.getZ()) || Tuna.no_move_reset){
            system.reset_timer(event.getPlayer());
        }
    }
    @EventHandler void onPlayerPostRespawnEvent(PlayerRespawnEvent event){
        system.respawn_reset_timer(event.getPlayer());
    }


    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event){
        //TODO:退出時にこれらを消すreset処理を記述
        system.quit(event.getPlayer());
    }
}
