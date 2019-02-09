package fi.matiaspaavilainen.masuitechat.bukkit.commands;

import fi.matiaspaavilainen.masuitechat.bukkit.MaSuiteChat;
import fi.matiaspaavilainen.masuitecore.core.channels.BukkitPluginChannel;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Afk implements CommandExecutor {

    private MaSuiteChat plugin;

    public Afk(MaSuiteChat plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        Player p = (Player) sender;
        if (args.length == 0) {
            if(plugin.afkList.contains(p.getUniqueId())){
                new BukkitPluginChannel(plugin, p, new Object[]{"MaSuiteChat", "Afk", p.getUniqueId().toString(), false}).send();
                plugin.afkList.remove(p.getUniqueId());
            } else {
                new BukkitPluginChannel(plugin, p, new Object[]{"MaSuiteChat", "Afk", p.getUniqueId().toString(), true}).send();
                plugin.afkList.add(p.getUniqueId());
            }

        } else {
            plugin.formator.sendMessage(p, plugin.config.load("chat", "syntax.yml").getString("afk"));
        }
        return true;
    }
}
