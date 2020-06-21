package ru.buseso.dreamtime.buildmanager.Events;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import ru.buseso.dreamtime.buildmanager.BuildManager;
import ru.buseso.dreamtime.buildmanager.utils.BMWorld;
import ru.buseso.dreamtime.buildmanager.utils.Progress;
import ru.buseso.dreamtime.buildmanager.utils.Utils;

public class BMListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onChageWorld(PlayerTeleportEvent e) {
        Player p = e.getPlayer();

        if(e.getFrom().getWorld().equals(e.getTo().getWorld())) { return; }

        World world = p.getWorld();

        BMWorld bmw = null;
        for(BMWorld w : BuildManager.worlds) {
            if(w.getId().equals(world.getName())) {
                bmw = w;
                break;
            }
        }

        if(bmw == null) {
            p.sendMessage(Utils.fixColor(BuildManager.prefix+"&4Мир не занесён в конфиг менеджера! " +
                    "Сообщите об этой ошибке &cАдминистрации &9Dream&bTime&4'а!"));
            return;
        }

        Progress prog = bmw.getProgress();
        if(prog.equals(Progress.NOTSTARTED) || prog.equals(Progress.INPROGRESS)) {
            if(bmw.isPrivat()) {
                if (!bmw.getBuilders().contains(p.getName())) {
                    p.sendMessage(Utils.fixColor(BuildManager.prefix + "&4Вы не являетесь строителем в данном мире!"));
                    e.setCancelled(true);
                    return;
                } else {
                    p.removePotionEffect(PotionEffectType.NIGHT_VISION);
                    p.setGlowing(false);
                    p.setGameMode(GameMode.CREATIVE);
                }
            }
        }

        p.setGameMode(GameMode.SPECTATOR);
        p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION,9999,50,true));
        p.setGlowing(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void playerChangeGm(PlayerGameModeChangeEvent e) {
        Player p = e.getPlayer();
        World world = p.getWorld();

        BMWorld bmw = null;
        for(BMWorld w : BuildManager.worlds) {
            if(w.getId().equals(world.getName())) {
                bmw = w;
                break;
            }
        }

        if(bmw == null) {
            p.sendMessage(Utils.fixColor(BuildManager.prefix+"&4Мир не занесён в конфиг менеджера! " +
                    "Сообщите об этой ошибке &cАдминистрации &9Dream&bTime&4'а!"));
            return;
        }

        Progress prog = bmw.getProgress();
        if(prog.equals(Progress.FINISHED) || prog.equals(Progress.FROZEN)) {
            if(!p.hasPermission("buildmanager.admin")) {
                p.setGameMode(GameMode.SPECTATOR);
            }
        }
    }

    @EventHandler
    public void inventoryClick(InventoryClickEvent e) {
        Player p = (Player)e.getWhoClicked();
        int slot = e.getSlot();

        if(e.getView().getItem(slot) != null) {
            if(slot > BuildManager.worlds.size()) return;
            BMWorld bmw = BuildManager.worlds.get(slot);
            BMCommand.sendInfo(p, bmw);
            World world = Bukkit.getWorld(bmw.getId());
            p.teleport(world.getSpawnLocation());
            if(bmw.getProgress().equals(Progress.FINISHED) || bmw.getProgress().equals(Progress.FROZEN)) {
                p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 9999, 2));
                p.setGlowing(true);
                p.setGameMode(GameMode.SPECTATOR);
            }
        }
    }

}
