package fi.matiaspaavilainen.masuitechat.bukkit.commands;

import fi.matiaspaavilainen.masuitechat.bukkit.MaSuiteChat;
import fi.matiaspaavilainen.masuitecore.core.objects.PluginChannel;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Nick implements CommandExecutor {

    private MaSuiteChat plugin;

    public Nick(MaSuiteChat plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        Player p = (Player) sender;
        if (args.length == 1) {
            new PluginChannel(plugin, p, new Object[]{"MaSuiteChat", "Nick", p.getUniqueId().toString(), args[0]}).send();
            return true;
        } else if(args.length == 2){
            new PluginChannel(plugin, p, new Object[]{"MaSuiteChat", "NickOther", p.getUniqueId().toString(), args[0], args[1]}).send();
            return true;
        } else {
            plugin.formator.sendMessage(p, plugin.config.load("chat", "syntax.yml").getString("nick.set"));
        }
        return true;
    }
}
