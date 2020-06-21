package ru.buseso.dreamtime.buildmanager;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import ru.buseso.dreamtime.buildmanager.Events.BMCommand;
import ru.buseso.dreamtime.buildmanager.Events.BMListener;
import ru.buseso.dreamtime.buildmanager.utils.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class BuildManager extends JavaPlugin {

    public static String prefix = "&eBuild&cManager&8>> &7";

    public static List<BMWorld> worlds = new ArrayList<>();
    public static BuildManager ins;

    private BMRun run;

    @Override
    public void onEnable() {
        ins = this;
        long start = System.currentTimeMillis();
        Utils.log(prefix+"&aНачинаю запуск плагина...");

        Utils.log(prefix+"&aПровожу работы с конфигом...");
        saveDefaultConfig();

        Utils.log(prefix+"&aОжидаю загрузку миров для дополнительной настройки...");
        Bukkit.getScheduler().runTaskLater(this, () -> addAllFromConfig(), 60);

        Utils.log(prefix+"&aРегистрирую слушатели...");
        Bukkit.getPluginManager().registerEvents(new BMListener(), this);

        Utils.log(prefix+"&aРегистрирую команды...");
        Bukkit.getPluginCommand("buildmanager").setExecutor(new BMCommand());

        Utils.log(prefix+"&aЗапускаю таймер авто-сохранения конфига...");
        run = new BMRun();
        run.runTaskTimerAsynchronously(this, 20, 6000);

        Utils.log(prefix+"&aПлагин &eBuild&cManager &bv"+this.getDescription().getVersion()+" " +
                "&aуспешно загружен за &b"+ (System.currentTimeMillis()-start) +"&aмс");
    }

    @Override
    public void onDisable() {
        run.cancel();

        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            for(BMWorld bmw : BuildManager.worlds) {
                getConfig().set("worlds."+bmw.getId()+".owner",bmw.getOwner());
                getConfig().set("worlds."+bmw.getId()+".builders",bmw.getBuilders());
                getConfig().set("worlds."+bmw.getId()+".progress",bmw.getProgress().toString());
                getConfig().set("worlds."+bmw.getId()+".name",bmw.getName());
                getConfig().set("worlds."+bmw.getId()+".description",bmw.getDescription());
                getConfig().set("worlds."+bmw.getId()+".game",bmw.getGame().toString());
                getConfig().set("worlds."+bmw.getId()+".private",bmw.isPrivat());
            }
        });

        try {
            getConfig().save(new File(getDataFolder()+File.separator+"config.yml"));
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private void addAllFromConfig() {
        FileConfiguration cfg = getConfig();

        Set<String> sections = cfg.getConfigurationSection("worlds").getKeys(false);
        for(String s : sections) {
            if(Bukkit.getWorld(s) == null) {
                Utils.log(prefix+"&4Мир '"+s+"' не найден! Пропускаю его загрузку!");
                continue;
            }

            String owner = cfg.getString("worlds."+s+".owner");
            List<String> builders = cfg.getStringList("worlds."+s+".builders");
            Progress progress = Progress.valueOf(cfg.getString("worlds."+s+".progress"));
            String name = cfg.getString("worlds."+s+".name");
            String description = cfg.getString("worlds."+s+".description");
            Games game = Games.valueOf(cfg.getString("worlds."+s+".game"));
            boolean privat = Boolean.parseBoolean(cfg.getString("worlds."+s+".private"));

            BMWorld bmw = new BMWorld(s,owner,builders,progress,name,description,game,privat);
            worlds.add(bmw);
        }
    }
}
