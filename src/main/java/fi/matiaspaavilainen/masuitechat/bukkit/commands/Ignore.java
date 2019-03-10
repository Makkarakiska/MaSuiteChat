package fi.matiaspaavilainen.masuitechat.bukkit.commands;

import fi.matiaspaavilainen.masuitechat.bukkit.MaSuiteChat;
import fi.matiaspaavilainen.masuitecore.core.channels.BukkitPluginChannel;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Ignore implements CommandExecutor {

    private MaSuiteChat plugin;

    public Ignore(MaSuiteChat plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        Player p = (Player) sender;
        if (args.length == 1) {
            new BukkitPluginChannel(plugin, p, new Object[]{
                    "MaSuiteChat",
                    "IgnorePlayer",
                    p.getUniqueId().toString(),
                    args[0]
            }).send();
        } else {
            plugin.formator.sendMessage(p, plugin.config.load("chat", "syntax.yml").getString("ignore"));
        }

        return true;
    }
}