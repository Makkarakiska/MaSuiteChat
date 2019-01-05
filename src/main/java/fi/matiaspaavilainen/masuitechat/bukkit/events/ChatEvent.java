package fi.matiaspaavilainen.masuitechat.bukkit.events;

import fi.matiaspaavilainen.masuitechat.bukkit.MaSuiteChat;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ChatEvent implements Listener {

    private MaSuiteChat plugin;

    public ChatEvent(MaSuiteChat plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onMessage(AsyncPlayerChatEvent e) {
        e.setCancelled(true);
        Player p = e.getPlayer();
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeUTF("MaSuiteChat");
            out.writeUTF("Chat");
            out.writeUTF(p.getUniqueId().toString());
            out.writeUTF(e.getMessage());
            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> p.sendPluginMessage(plugin, "BungeeCord", b.toByteArray()));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
