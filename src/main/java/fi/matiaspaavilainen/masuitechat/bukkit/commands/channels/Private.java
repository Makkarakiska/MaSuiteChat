package fi.matiaspaavilainen.masuitechat.bukkit.commands.channels;

import fi.matiaspaavilainen.masuitechat.bukkit.MaSuiteChat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

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
            try (ByteArrayOutputStream b = new ByteArrayOutputStream();
                 DataOutputStream out = new DataOutputStream(b)) {
                out.writeUTF("MaSuiteChat");
                out.writeUTF("SendMessage");
                out.writeUTF("private");
                out.writeUTF(p.getUniqueId().toString());
                out.writeUTF(args[0]);
                out.writeUTF(msg.toString());
                plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> p.sendPluginMessage(plugin, "BungeeCord", b.toByteArray()));
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
           plugin.formator.sendMessage(p, plugin.config.load("chat", "syntax.yml").getString("private.send"));
        }
        return true;
    }
}
