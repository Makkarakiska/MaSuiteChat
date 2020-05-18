package dev.masa.masuitechat.bungee.events;

import dev.masa.masuitechat.bungee.MaSuiteChat;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class SwitchEvent implements Listener {

    private MaSuiteChat plugin;

    public SwitchEvent(MaSuiteChat plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onConnect(ServerConnectEvent e) {
        if (!(e.getPlayer().getServer() == null) && (e.getTarget() != null) && (!e.getPlayer().getServer().getInfo().equals(e.getTarget()))) {
            if (plugin.config.load("chat", "messages.yml").getBoolean("switch-message.enabled")) {
                String server = plugin.config.load("chat", "chat.yml").getString("channels." + e.getPlayer().getServer().getInfo().getName().toLowerCase() + ".prefix");
                plugin.utils.broadcast(plugin.config.load("chat", "messages.yml")
                        .getString("switch-message.message")
                        .replace("%player%", e.getPlayer().getName())
                        .replace("%nickname%", e.getPlayer().getDisplayName())
                        .replace("%server%", server)
                );
            }
            MaSuiteChat.players.put(e.getPlayer().getUniqueId(), plugin.config.load("chat", "chat.yml")
                    .getString("channels." + e.getTarget().getName().toLowerCase() + ".defaultChannel"));
        }
    }
}
