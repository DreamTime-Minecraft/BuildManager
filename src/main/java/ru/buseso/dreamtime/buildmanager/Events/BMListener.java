package ru.buseso.dreamtime.buildmanager.Events;

import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
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
            if(!bmw.getBuilders().contains(p.getName())) {
                p.sendMessage(Utils.fixColor(BuildManager.prefix + "&4Вы не являетесь строителем в данном мире!"));
                e.setCancelled(true);
                return;
            } else {
                p.removePotionEffect(PotionEffectType.NIGHT_VISION);
                p.setGlowing(false);
                p.setGameMode(GameMode.CREATIVE);
            }
        }

        p.setGameMode(GameMode.SPECTATOR);
        p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION,9999,50,true));
        p.setGlowing(true);
    }

    @EventHandler
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

}
