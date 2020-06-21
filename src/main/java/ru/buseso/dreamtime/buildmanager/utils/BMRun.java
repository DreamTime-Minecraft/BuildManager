package ru.buseso.dreamtime.buildmanager.utils;

import org.bukkit.scheduler.BukkitRunnable;
import ru.buseso.dreamtime.buildmanager.BuildManager;

import java.io.File;
import java.io.IOException;

public class BMRun extends BukkitRunnable {
    @Override
    public void run() {
        try {
            BuildManager.ins.getConfig().save(new File(BuildManager.ins.getDataFolder()+File.separator+"config.yml"));
            Utils.log(BuildManager.prefix+"&aКонфиг сохранён!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
