package ru.buseso.dreamtime.buildmanager.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class Utils {

    public static String fixColor(String string) {
        return ChatColor.translateAlternateColorCodes('&',string);
    }

    public static void log(String message) {
        Bukkit.getConsoleSender().sendMessage(fixColor(message));
    }

}
