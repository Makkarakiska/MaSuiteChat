package fi.matiaspaavilainen.masuitechat.bukkit;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class ChatMessagingChannel implements PluginMessageListener {

    private MaSuiteChat plugin;

    ChatMessagingChannel(MaSuiteChat p) {
        plugin = p;
    }

    public void onPluginMessageReceived(String channel, Player p, byte[] message) {
        if (!channel.equals("BungeeCord")) {
            return;
        }

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));
        String subchannel = null;
        try {
            subchannel = in.readUTF();
            if (subchannel.equals("LocalChat")) {
                String inMsg = in.readUTF();
                int range = in.readInt();
                Location loc = p.getLocation();
                for (Player pl : Bukkit.getOnlinePlayers()) {
                    if (pl.getWorld().equals(p.getWorld())) {
                        if (pl.getLocation().distance(loc) <= range) {
                            TextComponent msg = new TextComponent(ComponentSerializer.parse(inMsg));
                            pl.spigot().sendMessage(msg);
                        }
                    }
                }
            }
            if (subchannel.equals("StaffChat")) {
                String inMsg = in.readUTF();
                for (Player pl : Bukkit.getOnlinePlayers()) {
                    if (pl.hasPermission("masuitechat.channel.staff")) {
                        TextComponent msg = new TextComponent(ComponentSerializer.parse(inMsg));
                        pl.spigot().sendMessage(msg);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
