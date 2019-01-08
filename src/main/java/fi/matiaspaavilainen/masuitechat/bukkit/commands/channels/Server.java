package fi.matiaspaavilainen.masuitechat.bukkit.commands.channels;

import com.google.common.base.Joiner;
import fi.matiaspaavilainen.masuitechat.bukkit.MaSuiteChat;
import fi.matiaspaavilainen.masuitecore.core.objects.PluginChannel;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Server implements CommandExecutor {

    private MaSuiteChat plugin;

    public Server(MaSuiteChat plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }

        Player p = (Player) sender;

        if (args.length == 0) {
            new PluginChannel(plugin, p, new Object[]{"MaSuiteChat", "ToggleChannel", "server", p.getUniqueId().toString()}).send();
            return true;
        } else {
            new PluginChannel(plugin, p, new Object[]{"MaSuiteChat", "SendMessage", "server", p.getUniqueId().toString(), Joiner.on(" ").join(args)}).send();
            return true;

        }
    }
}
