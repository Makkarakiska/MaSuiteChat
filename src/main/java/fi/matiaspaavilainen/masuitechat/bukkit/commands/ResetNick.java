package fi.matiaspaavilainen.masuitechat.bukkit.commands;

import fi.matiaspaavilainen.masuitechat.bukkit.MaSuiteChat;
import fi.matiaspaavilainen.masuitecore.core.objects.PluginChannel;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ResetNick implements CommandExecutor {

    private MaSuiteChat plugin;

    public ResetNick(MaSuiteChat plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        Player p = (Player) sender;

        if (args.length == 0) {
            new PluginChannel(plugin, p, new Object[]{"MaSuiteChat", "ResetNick", p.getUniqueId().toString()}).send();
            return true;
        } else if(args.length == 1){
            new PluginChannel(plugin, p, new Object[]{"MaSuiteChat", "ResetNickOther", p.getUniqueId().toString(), args[0]}).send();
            return true;
        } else{
            plugin.formator.sendMessage(p, plugin.config.load("chat", "syntax.yml").getString("nick.reset"));
        }
        return true;
    }
}
