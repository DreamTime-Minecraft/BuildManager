package ru.buseso.dreamtime.buildmanager.Events;

import com.google.common.base.Strings;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.buseso.dreamtime.buildmanager.BuildManager;
import ru.buseso.dreamtime.buildmanager.utils.BMWorld;
import ru.buseso.dreamtime.buildmanager.utils.Utils;

import java.util.Arrays;

public class BMCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) { sender.sendMessage("А ПО ЖОПЕ?!!!!"); return false; }

        boolean help = false;
        Player p = (Player)sender;

        if(args.length == 0) {
            help = true;
        } else if(args.length == 1) {
            if(args[0].equalsIgnoreCase("info")) {
                String world = p.getWorld().getName();

                BMWorld bmw = null;
                for(BMWorld w : BuildManager.worlds) {
                    if(w.getId().equals(world)) {
                        bmw = w;
                        break;
                    }
                }

                if(bmw == null) {
                    p.sendMessage(Utils.fixColor(BuildManager.prefix+"&4Мир не занесён в конфиг менеджера! " +
                            "Сообщите об этой ошибке &cАдминистрации &9Dream&bTime&4'а!"));
                    return false;
                }

                sendInfo(p, bmw);
            } else {
                help = true;
            }
        }

        if(help) {
            sendHelp(p);
        }

        return true;
    }

    private void sendHelp(Player p) {
        p.sendMessage(Utils.fixColor("&7Все команды менеджера:"));
        p.sendMessage(Utils.fixColor("&a> &7/bm info - Информация о текущем мире"));
        p.sendMessage(Utils.fixColor("&a> &7/bm build builder add <ник> - добавить игрока в строителей в мире (String)"));
        p.sendMessage(Utils.fixColor("&a> &7/bm build builder remove <ник> - убрать игрока из строителей в мире (String)"));
        p.sendMessage(Utils.fixColor("&a> &7/bm build progress <прогресс> - установить прогресс в мире (Progress)"));
        p.sendMessage(Utils.fixColor("&a> &7/bm build name <название> - установить название для мира (String)"));
        p.sendMessage(Utils.fixColor("&a> &7/bm build desc <описание> - установить описание для мира (String)"));
        p.sendMessage(Utils.fixColor("&a> &7/bm build private <приватность> - установить приватность для мира (Boolean)"));
        p.sendMessage(Utils.fixColor("&a> &7/bm build game <игра> - установить для чего строится постройка (Games)"));
    }

    private void sendInfo(Player p, BMWorld world) {
        p.sendMessage(Utils.fixColor("&a> &7ID мира: &a"+world.getId()));
        p.sendMessage(Utils.fixColor("&a> &7Владелец: &a"+world.getOwner()));
        p.sendMessage(Utils.fixColor("&a> &7Строители: &a"+world.getBuilders().toString()));
        p.sendMessage(Utils.fixColor("&a> &7Прогресс: &a"+world.getProgress()));
        p.sendMessage(Utils.fixColor("&a> &7Название: &a"+world.getName()));
        p.sendMessage(Utils.fixColor("&a> &7Описание: &a"+world.getDescription()));
        p.sendMessage(Utils.fixColor("&a> &7Приватность: &a"+world.isPrivat()));
        p.sendMessage(Utils.fixColor("&a> &7Игра: &a"+world.getGame()));
    }
}
