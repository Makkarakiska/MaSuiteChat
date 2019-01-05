package fi.matiaspaavilainen.masuitechat.bukkit.commands;

import fi.matiaspaavilainen.masuitechat.bukkit.MaSuiteChat;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

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
            try (ByteArrayOutputStream b = new ByteArrayOutputStream();
                 DataOutputStream out = new DataOutputStream(b)) {
                out.writeUTF("MaSuiteChat");
                out.writeUTF("ResetNick");
                out.writeUTF(p.getUniqueId().toString());
                p.sendPluginMessage(plugin, "BungeeCord", b.toByteArray());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (args.length == 1) {
            try (ByteArrayOutputStream b = new ByteArrayOutputStream();
                 DataOutputStream out = new DataOutputStream(b)) {
                out.writeUTF("MaSuiteChat");
                out.writeUTF("ResetNickOther");
                out.writeUTF(p.getUniqueId().toString());
                out.writeUTF(args[0]);
                p.sendPluginMessage(plugin, "BungeeCord", b.toByteArray());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else{
            plugin.formator.sendMessage(p, plugin.config.load("chat", "syntax.yml").getString("nick.reset"));
        }
        return true;
    }
}
