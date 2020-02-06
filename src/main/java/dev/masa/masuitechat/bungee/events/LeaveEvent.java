package dev.masa.masuitechat.bungee.events;

import dev.masa.masuitechat.bungee.MaSuiteChat;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class LeaveEvent implements Listener {

    private MaSuiteChat plugin;

    public LeaveEvent(MaSuiteChat plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerDisconnectEvent e) {
        if (plugin.config.load("chat", "messages.yml").getBoolean("connection-message.enabled")) {
            plugin.utils.broadcast(
                    plugin.config.load("chat", "messages.yml")
                    .getString("connection-message.left")
                    .replace("%player%", e.getPlayer().getName()));
        }
        MaSuiteChat.players.remove(e.getPlayer().getUniqueId());
    }
}