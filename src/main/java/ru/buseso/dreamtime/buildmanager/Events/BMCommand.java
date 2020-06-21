package ru.buseso.dreamtime.buildmanager.Events;

import com.google.common.base.Strings;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.buseso.dreamtime.buildmanager.BuildManager;
import ru.buseso.dreamtime.buildmanager.utils.*;

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
            if (args[0].equalsIgnoreCase("info")) {
                String world = p.getWorld().getName();

                BMWorld bmw = null;
                for (BMWorld w : BuildManager.worlds) {
                    if (w.getId().equals(world)) {
                        bmw = w;
                        break;
                    }
                }

                if (bmw == null) {
                    p.sendMessage(Utils.fixColor(BuildManager.prefix + "&4Мир не занесён в конфиг менеджера! " +
                            "Сообщите об этой ошибке &cАдминистрации &9Dream&bTime&4'а!"));
                    return false;
                }

                sendInfo(p, bmw);
            } else if(args[0].equalsIgnoreCase("gui")) {
                p.openInventory(BMInv.createMainMenu());
            } else {
                help = true;
            }
        } else if(args.length == 2) { help = true; }
        else if(args.length == 3) {
            if(args[0].equalsIgnoreCase("build")) {
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

                if(!p.getName().equals(bmw.getOwner()) || !p.hasPermission("buildmanager.admin")) {
                    p.sendMessage(Utils.fixColor(BuildManager.prefix+"&4Вы не являетесь владельцем данного мира!"));
                    return false;
                }

                if(args[1].equalsIgnoreCase("name")) {
                    String name = args[2];
                    bmw.setName(name);
                    p.sendMessage(Utils.fixColor(BuildManager.prefix+"&aНазвание мира изменено на &b"+name+"&a!"));
                } else if(args[1].equalsIgnoreCase("desc")) {
                    String desc = args[2].replace('_', ' ');
                    bmw.setDescription(desc);
                    p.sendMessage(Utils.fixColor(BuildManager.prefix+"&aОписание мира изменено на &b"+desc+"&a!"));
                } else if(args[1].equalsIgnoreCase("private")) {
                    String privat = args[2];
                    boolean priv = Boolean.parseBoolean(privat);
                    bmw.setPrivat(priv);
                    p.sendMessage(Utils.fixColor(BuildManager.prefix+"&aТеперь мир " +
                            "" + (priv ? "&cзакрыт" : "&2открыт") + "&a для общего посещения"));
                } else if(args[1].equalsIgnoreCase("game")) {
                    String game = args[2].toUpperCase();
                    Games games = null;
                    try {
                        games = Games.valueOf(game);
                    }catch (IllegalArgumentException e) {
                        p.sendMessage(Utils.fixColor(BuildManager.prefix+"&4Доступные цели постройки: "+Arrays.toString(Games.values())));
                        return false;
                    }

                    bmw.setGame(games);
                    p.sendMessage(Utils.fixColor(BuildManager.prefix+"&aЦель постройки изменена на "+game));
                } else if(args[1].equalsIgnoreCase("progress")) {
                    String progress = args[2].toUpperCase();
                    Progress prog = null;
                    try {
                        prog = Progress.valueOf(progress);
                    }catch (IllegalArgumentException e) {
                        p.sendMessage(Utils.fixColor(BuildManager.prefix+"&4Доступные прогрессы постройки: "+Arrays.toString(Progress.values())));
                        return false;
                    }

                    bmw.setProgress(prog);
                    p.sendMessage(Utils.fixColor(BuildManager.prefix+"&aПрогресс постройки изменен на "+progress));
                } else {
                    help = true;
                }
            } else {
                help = true;
            }
        }
        else if(args.length == 4) {
            if(args[0].equalsIgnoreCase("build")) {
                if(args[1].equalsIgnoreCase("builder")) {
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

                    if(!p.getName().equals(bmw.getOwner()) || !p.hasPermission("buildmanager.admin")) {
                        p.sendMessage(Utils.fixColor(BuildManager.prefix+"&4Вы не являетесь владельцем данного мира!"));
                        return false;
                    }

                    if(args[2].equalsIgnoreCase("add")) {
                        String name = args[3];
                        bmw.addBuilder(name);
                        p.sendMessage(Utils.fixColor(BuildManager.prefix+"&aИгрок &b"+name+" &aдобавлен в строителей в этот мир!"));
                    } else if(args[2].equalsIgnoreCase("remove")) {
                        String name = args[3];
                        bmw.removeBuilder(name);
                        p.sendMessage(Utils.fixColor(BuildManager.prefix+"&aИгрок &b"+name+" &aубран из строителей в этот мир!"));
                    } else {
                        help = true;
                    }
                } else {
                    help = true;
                }
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
        p.sendMessage(Utils.fixColor("&a> &7/bm gui - Меню со всеми мирами и информация о них"));
        p.sendMessage(Utils.fixColor("&a> &7/bm build builder add <ник> - добавить игрока в строителей в мире (String)"));
        p.sendMessage(Utils.fixColor("&a> &7/bm build builder remove <ник> - убрать игрока из строителей в мире (String)"));
        p.sendMessage(Utils.fixColor("&a> &7/bm build progress <прогресс> - установить прогресс в мире (Progress)"));
        p.sendMessage(Utils.fixColor("&a> &7/bm build name <название> - установить название для мира (String)"));
        p.sendMessage(Utils.fixColor("&a> &7/bm build desc <описание> - установить описание для мира. _ ставит пробел (String)"));
        p.sendMessage(Utils.fixColor("&a> &7/bm build private <приватность> - установить приватность для мира (Boolean)"));
        p.sendMessage(Utils.fixColor("&a> &7/bm build game <игра> - установить для чего строится постройка (Games)"));
    }

    public static void sendInfo(Player p, BMWorld world) {
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
