package net.acecore.tuna;

import org.bukkit.Bukkit;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.*;
import java.util.logging.Level;

public final class Tuna extends JavaPlugin {
    public static Integer time;
    public static Integer respawn;
    public static Boolean no_move_reset;

    public static Map<Player,Integer> timers;
    private static Plugin plugin;
    public static Map<Player, BossBar> playerBossBars;
    public static Map<Player, List<Double>> coordinate;
    public static Map<Player, Boolean> set_uped;

    public static final String saveFolderPath = "plugins/Tuna";
    public static final String setting_filePath = saveFolderPath + "/setting.properties";
    public static Properties properties = new Properties();
    Properties load_properties = new Properties();
    @Override
    public void onEnable() {
        // Plugin startup logic
        File saveFolder = new File(saveFolderPath);
        saveFolder.mkdirs();
        File setting_dataFile = new File(setting_filePath);
        if (!setting_dataFile.exists()) {
            time = 10;
            respawn = 20;
            no_move_reset = true;
            try {
                setting_dataFile.createNewFile();
                getLogger().info("setting.properties ファイルを作成しました");
                properties.setProperty("time", String.valueOf(10));
                properties.setProperty("after_respawn", String.valueOf(20));
                properties.setProperty("no_move_reset", String.valueOf((true)));
                try (OutputStream out = new FileOutputStream(setting_filePath)) {
                    properties.store(out, "settings");
                }catch (IOException e){
                    System.err.println("not created setting file");
                }
            } catch (IOException e) {
                getLogger().severe("setting.properties ファイルの作成に失敗しました: " + e.getMessage());
            }
        }else{
            try (FileReader reader = new FileReader(setting_filePath)) {
                load_properties.load(reader);
                time = Integer.valueOf(load_properties.getProperty("time"));
                respawn = Integer.valueOf(load_properties.getProperty("after_respawn"));
                no_move_reset = Boolean.valueOf(load_properties.getProperty("no_move_reset"));
            }catch (IOException e) {
                time = 10;
                respawn = 20;
                no_move_reset = true;
                getLogger().log(Level.WARNING,"設定ファイルの値が不正のため、初期値を利用しています。"+setting_filePath+"をご確認ください。　time,after_respawnは正の整数を指定してください。");
            }
        }

        coordinate = new HashMap<>();
        set_uped = new HashMap<>();
        for(Player player: Bukkit.getOnlinePlayers()){
            set_uped.put(player,false);
        }

        plugin = this;
        timers = new HashMap<>();
        playerBossBars = new HashMap<>();
        Bukkit.getServer().getPluginManager().registerEvents(new main(), this);
        new system().runTaskTimer(Tuna.getPlugin(), 0L, 20L);
    }
    public static Plugin getPlugin() {
        return plugin;
    }
    @Override
    public void onDisable() {
        // Plugin shutdown logic
        for (BossBar bossBar : playerBossBars.values()) {
            if(!playerBossBars.isEmpty()){
                bossBar.removeAll();
            }
        }
        time = null;
        respawn = null;
        no_move_reset = null;

        if (timers != null) {
            timers.clear();
        }
        if (playerBossBars != null) {
            playerBossBars.clear();
        }
        if (coordinate != null) {
            coordinate.clear();
        }
        if(set_uped != null){
            set_uped.clear();
        }

        // プロパティをリセット
        properties.clear();
        load_properties.clear();
    }
}
