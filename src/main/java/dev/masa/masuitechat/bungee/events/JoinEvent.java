package dev.masa.masuitechat.bungee.events;

import dev.masa.masuitechat.bungee.MaSuiteChat;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class JoinEvent implements Listener {

    private MaSuiteChat plugin;

    public JoinEvent(MaSuiteChat plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onConnect(ServerConnectEvent e) {
        if (e.getReason().equals(ServerConnectEvent.Reason.JOIN_PROXY)) {
            String server = plugin.config.load("chat", "chat.yml").getString("channels." + e.getTarget().getName().toLowerCase() + ".prefix");
            if (plugin.config.load("chat", "messages.yml").getBoolean("connection-message.enabled")) {
                plugin.utils.broadcast(plugin.config.load("chat", "messages.yml")
                        .getString("connection-message.join")
                        .replace("%player%", e.getPlayer().getName())
                        .replace("%nickname%", e.getPlayer().getDisplayName())
                        .replace("%server%", server)
                );
            }
            MaSuiteChat.players.put(e.getPlayer().getUniqueId(), plugin.config.load("chat", "chat.yml")
                    .getString("channels." + e.getTarget().getName().toLowerCase() + ".defaultChannel"));

            if (plugin.config.load("chat", "messages.yml").getBoolean("first-join.enabled"))
                if (plugin.getApi().getPlayerService().getPlayer(e.getPlayer().getUniqueId()) == null) {
                    plugin.utils.broadcast(plugin.config.load("chat", "messages.yml")
                            .getString("first-join.message")
                            .replace("%player%", e.getPlayer().getName())
                            .replace("%server%", server)
                    );
                }

            if (plugin.config.load("chat", "messages.yml").getBoolean("motd.enabled")) {
                plugin.formator.sendMessage(e.getPlayer(), plugin.config.load("chat", "messages.yml")
                        .getString("motd.message")
                        .replace("%player%", e.getPlayer().getName())
                        .replace("%nickname%", e.getPlayer().getDisplayName()
                                .replace("%server%", server)
                        )
                );
            }
        }
    }
}
