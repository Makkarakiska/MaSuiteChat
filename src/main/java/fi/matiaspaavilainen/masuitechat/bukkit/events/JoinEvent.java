package fi.matiaspaavilainen.masuitechat.bukkit.events;

import fi.matiaspaavilainen.masuitechat.bukkit.MaSuiteChat;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class JoinEvent implements Listener {

    private MaSuiteChat plugin;

    public JoinEvent(MaSuiteChat plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, () -> {
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(b);
            try {
                Player p = e.getPlayer();
                if (p == null) {
                    return;
                }
                out.writeUTF("MaSuiteChat");
                out.writeUTF("SetGroup");
                out.writeUTF(p.getUniqueId().toString());
                out.writeUTF(plugin.getPrefix(p));
                out.writeUTF(plugin.getSuffix(p));
                plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> p.sendPluginMessage(plugin, "BungeeCord", b.toByteArray()));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }, 10);
    }
}
