package ru.buseso.dreamtime.buildmanager;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import ru.buseso.dreamtime.buildmanager.Events.BMCommand;
import ru.buseso.dreamtime.buildmanager.Events.BMListener;
import ru.buseso.dreamtime.buildmanager.utils.*;

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
        addAllFromConfig();

        Utils.log(prefix+"&aРегистрирую слушатели...");
        Bukkit.getPluginManager().registerEvents(new BMListener(), this);

        Utils.log(prefix+"&aРегистрирую команды...");
        Bukkit.getPluginCommand("buildmanager").setExecutor(new BMCommand());

        Utils.log(prefix+"&aЗапускаю таймер авто-сохранения конфига...");
        run = new BMRun();
        run.runTaskTimer(this, 20, 6000);

        Utils.log(prefix+"&aПлагин &eBuild&cManager &bv"+this.getDescription().getVersion()+" " +
                "&aуспешно загружен за &b"+ (System.currentTimeMillis()-start) +"&aмс");
    }

    @Override
    public void onDisable() {
        run.cancel();
        saveConfig();
    }

    private void addAllFromConfig() {
        FileConfiguration cfg = getConfig();

        Set<String> sections = cfg.getConfigurationSection("worlds").getKeys(false);
        for(String s : sections) {
            if(Bukkit.getWorld(s) == null) {
                Utils.log(prefix+"&4Мир '"+s+"' не найден! Пропускаю его загрузку!");
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
