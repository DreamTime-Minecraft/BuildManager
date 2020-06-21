package ru.buseso.dreamtime.buildmanager.utils;

import org.bukkit.scheduler.BukkitRunnable;
import ru.buseso.dreamtime.buildmanager.BuildManager;

public class BMRun extends BukkitRunnable {
    @Override
    public void run() {
        BuildManager.ins.saveConfig();
        Utils.log(BuildManager.prefix+"&aКонфиг сохранён!");
    }
}
