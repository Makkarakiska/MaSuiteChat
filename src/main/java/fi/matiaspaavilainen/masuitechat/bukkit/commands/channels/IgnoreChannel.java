package fi.matiaspaavilainen.masuitechat.bukkit.commands.channels;

import fi.matiaspaavilainen.masuitechat.bukkit.MaSuiteChat;
import fi.matiaspaavilainen.masuitecore.core.objects.PluginChannel;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class IgnoreChannel implements CommandExecutor {

    private MaSuiteChat plugin;

    public IgnoreChannel(MaSuiteChat plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        Player p = (Player) sender;
        if (args.length == 1) {
            if(args[0].equalsIgnoreCase("server") || args[0].equalsIgnoreCase("global")){
                new PluginChannel(plugin, p, new Object[]{"MaSuiteChat", "IgnoreChannel", args[0].toLowerCase(), p.getUniqueId().toString()}).send();
                return true;
            } else {
                plugin.formator.sendMessage(p, plugin.config.load("chat", "syntax.yml").getString("ignore-channel"));
            }
        } else {
            plugin.formator.sendMessage(p, plugin.config.load("chat", "syntax.yml").getString("ignore-channel"));
        }
        return false;
    }
}
