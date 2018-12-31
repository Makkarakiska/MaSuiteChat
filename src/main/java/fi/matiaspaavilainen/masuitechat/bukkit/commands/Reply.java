package fi.matiaspaavilainen.masuitechat.bukkit.commands;

import com.google.common.base.Joiner;
import fi.matiaspaavilainen.masuitechat.bukkit.MaSuiteChat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

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
            try (ByteArrayOutputStream b = new ByteArrayOutputStream();
                 DataOutputStream out = new DataOutputStream(b)) {
                out.writeUTF("MaSuiteChat");
                out.writeUTF("SendMessage");
                out.writeUTF("reply");
                out.writeUTF(p.getUniqueId().toString());
                out.writeUTF(Joiner.on(" ").join(args));
                plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () ->
                        p.sendPluginMessage(plugin, "BungeeCord", b.toByteArray()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}
