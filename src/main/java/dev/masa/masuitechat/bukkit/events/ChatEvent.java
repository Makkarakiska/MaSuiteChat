package dev.masa.masuitechat.bukkit.events;

import dev.masa.masuitechat.bukkit.MaSuiteChat;
import dev.masa.masuitecore.core.channels.BukkitPluginChannel;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatEvent implements Listener {

    private MaSuiteChat plugin;

    public ChatEvent(MaSuiteChat plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onMessage(AsyncPlayerChatEvent e) {
        e.setCancelled(true);
        new BukkitPluginChannel(plugin, e.getPlayer(), "MaSuiteChat", "Chat", e.getPlayer().getUniqueId().toString(), e.getMessage()).send();
    }
}
