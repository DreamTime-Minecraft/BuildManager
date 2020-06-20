package ru.buseso.dreamtime.buildmanager.Events;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import ru.buseso.dreamtime.buildmanager.BuildManager;
import ru.buseso.dreamtime.buildmanager.utils.BMWorld;
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
            }
        }

        if(bmw == null) {
            p.sendMessage(Utils.fixColor(BuildManager.prefix+"&4Мир не занесён в конфиг менеджера! " +
                    "Сообщите об этой ошибке &cАдминистрации &9Dream&bTime&4'а!"));
            return;
        }

        if(!bmw.getBuilders().contains(p.getName())) {
            p.sendMessage(Utils.fixColor(BuildManager.prefix+"&4Вы не являетесь строителем в этом мире!"));
            e.setCancelled(true);
            return;
        }
    }

    

}
