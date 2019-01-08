package fi.matiaspaavilainen.masuitechat.bukkit.commands.channels;

import fi.matiaspaavilainen.masuitechat.bukkit.MaSuiteChat;
import fi.matiaspaavilainen.masuitecore.core.objects.PluginChannel;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Private implements CommandExecutor {

    private MaSuiteChat plugin;

    public Private(MaSuiteChat plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        Player p = (Player) sender;
        if (args.length > 1) {
            StringBuilder msg = new StringBuilder();
            int i;
            for (i = 1; i < args.length; i++) {
                msg.append(args[i]).append(" ");
            }
            new PluginChannel(plugin, p, new Object[]{"MaSuiteChat", "SendMessage", "private", p.getUniqueId().toString(), args[0], msg.toString()}).send();
        } else {
           plugin.formator.sendMessage(p, plugin.config.load("chat", "syntax.yml").getString("private.send"));
        }
        return true;
    }
}
