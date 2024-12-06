package net.acecore.tuna;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.logging.Level;

import static net.acecore.tuna.Tuna.playerBossBars;

public class system extends BukkitRunnable {
    //private static Map<Player,Integer> timers;
    @Override
    public void run() {
        if(Tuna.timers!=null){
            for(Player player: Bukkit.getOnlinePlayers()) {
                if (!Tuna.set_uped.get(player)) {
                    system.join(player);
                    Tuna.set_uped.put(player, true);
                }
            }
            if(!(Tuna.timers.isEmpty())) {
                for (Map.Entry<Player, Integer> entry : Tuna.timers.entrySet()) {
                    int now = entry.getValue();
                    now -= 1;
                    if (entry.getValue() <= 0 && (entry.getKey().getGameMode() == GameMode.SURVIVAL || entry.getKey().getGameMode() == GameMode.ADVENTURE )) {
                        if(entry.getKey().getHealth()!=0){
                            entry.getKey().setHealth(0);
                            return;
                        }
                    }
                    entry.setValue(now);
                    if(now<0){
                        now=0;
                    }
                    setBossBar(entry.getKey(),now);
                }
            }
        }
    }
    public static void reset_timer(Player player) {
        Tuna.timers.put(player,Tuna.time);
        Tuna.coordinate.put(player,Arrays.asList(player.getLocation().getX(),player.getLocation().getY(),player.getLocation().getZ()));
    }
    public static void respawn_reset_timer(Player player) {
        Tuna.timers.put(player,Tuna.respawn);
    }
    public static void join(Player player){
        System.out.println(player.getDisplayName()+"のreset処理を行いました");
        try {
            Tuna.coordinate.put(player, Arrays.asList(0.0, 0.0, 0.0));
            Tuna.timers.put(player, 0);
            BossBar mybar = Bukkit.createBossBar(player.getName(), BarColor.WHITE, BarStyle.SOLID);
            mybar.setProgress(1);
            mybar.addPlayer(player);
            Tuna.playerBossBars.put(player, mybar);
            system.respawn_reset_timer(player);
        } catch (Exception e){
            Bukkit.getLogger().log(Level.WARNING,"error:"+e);
        }
    }
    public static void quit(Player player){
        try {
            Tuna.timers.remove(player);
            BossBar delete_bar = Tuna.playerBossBars.remove(player);
            delete_bar.removeAll();
            Tuna.coordinate.remove(player);
        } catch (Exception e){
            Bukkit.getLogger().log(Level.WARNING,"error:"+e);
        }
    }

    private static void setBossBar(Player player,Integer now){
        BossBar bossBar = Tuna.playerBossBars.get(player);
        if (bossBar != null) {
            double per;
            per = (double) now / Tuna.time;
            if (per > 1.0) {
                per=1.0;
            }
            if (per>0.5){
                bossBar.setColor(BarColor.WHITE);
            } else if (per>0.25) {
                bossBar.setColor(BarColor.YELLOW);
            }else {
                bossBar.setColor(BarColor.RED);
            }
            bossBar.setProgress(per);
            bossBar.setTitle("残り"+now+"秒");
        }
    }
}