package ru.buseso.dreamtime.buildmanager.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;
import ru.buseso.dreamtime.buildmanager.BuildManager;

import java.io.File;
import java.io.IOException;

public class BMRun extends BukkitRunnable {
    @Override
    public void run() {
        FileConfiguration cfg = BuildManager.ins.getConfig();

        for(BMWorld bmw : BuildManager.worlds) {
            cfg.set("worlds."+bmw.getId()+".owner",bmw.getOwner());
            cfg.set("worlds."+bmw.getId()+".builders",bmw.getBuilders());
            cfg.set("worlds."+bmw.getId()+".progress",bmw.getProgress().toString());
            cfg.set("worlds."+bmw.getId()+".name",bmw.getName());
            cfg.set("worlds."+bmw.getId()+".description",bmw.getDescription());
            cfg.set("worlds."+bmw.getId()+".game",bmw.getGame().toString());
            cfg.set("worlds."+bmw.getId()+".private",bmw.isPrivat());
        }

        try {
            BuildManager.ins.getConfig().save(new File(BuildManager.ins.getDataFolder()+File.separator+"config.yml"));
        } catch(IOException e) {
            e.printStackTrace();
        }

        Utils.log(BuildManager.prefix+"&aКонфиг сохранён!");
    }
}
