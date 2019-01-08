package fi.matiaspaavilainen.masuitechat.bukkit.commands;

import com.google.common.base.Joiner;
import fi.matiaspaavilainen.masuitechat.bukkit.MaSuiteChat;
import fi.matiaspaavilainen.masuitecore.core.objects.PluginChannel;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Reply implements CommandExecutor {

    private MaSuiteChat plugin;

    public Reply(MaSuiteChat plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        Player p = (Player) sender;
        if (args.length > 0) {
            new PluginChannel(plugin, p, new Object[]{"MaSuiteChat", "SendMessage", "reply", p.getUniqueId().toString(), Joiner.on(" ").join(args)}).send();
        } else {
            plugin.formator.sendMessage(p, plugin.config.load("chat", "syntax.yml").getString("private.reply"));
        }
        return true;
    }
}
