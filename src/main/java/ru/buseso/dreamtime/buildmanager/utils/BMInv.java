package ru.buseso.dreamtime.buildmanager.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.buseso.dreamtime.buildmanager.BuildManager;
import ru.buseso.dreamtime.buildmanager.utils.holders.BMMainHolder;

import java.util.ArrayList;
import java.util.List;

public class BMInv {
    public static Inventory createMainMenu() {
        Inventory inv = Bukkit.createInventory(new BMMainHolder(), 54, Utils.fixColor("&eB&cM&8>> &6Главное меню"));
        for(int i = 0; i < 53 && i < BuildManager.worlds.size(); i++) {
            ItemStack item = new ItemStack(Material.MAP, i);
            ItemMeta meta = item.getItemMeta();
            BMWorld bmw = BuildManager.worlds.get(i);
            meta.setDisplayName(Utils.fixColor("&8["+bmw.getId()+"&8] &a"+bmw.getName()));
            List<String> lore = new ArrayList<>();
            lore.add(Utils.fixColor("&a> &7Владелец: &e"+bmw.getOwner()));
            lore.add(Utils.fixColor("&a> &7Строители:"));
            lore.add(Utils.fixColor("&7"+bmw.getBuilders().toString()));
            lore.add(Utils.fixColor("&a> &7Прогресс: &e"+bmw.getProgress().toString()));
            lore.add(Utils.fixColor("&a> &7Цель: &e"+bmw.getGame()));
            lore.add(Utils.fixColor("&a> &7Описание:"));
            lore.add(Utils.fixColor("&7"+bmw.getDescription()));
            meta.setLore(lore);

            if(bmw.getProgress().equals(Progress.FINISHED)) {
                item.addUnsafeEnchantment(Enchantment.LUCK, 1);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }

            item.setItemMeta(meta);
            inv.setItem(i, item);
        }
        return inv;
    }
}
