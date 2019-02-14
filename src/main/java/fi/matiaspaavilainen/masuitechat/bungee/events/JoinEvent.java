package fi.matiaspaavilainen.masuitechat.bungee.events;

import fi.matiaspaavilainen.masuitechat.bungee.MaSuiteChat;
import fi.matiaspaavilainen.masuitecore.core.objects.MaSuitePlayer;
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
    public void onJoin(PostLoginEvent e) {
        if (plugin.config.load("chat", "messages.yml").getBoolean("connection-message.enabled")) {
            plugin.utils.broadcast(plugin.config.load("chat", "messages.yml")
                    .getString("connection-message.join")
                    .replace("%player%", e.getPlayer().getName()));
        }
        MaSuiteChat.players.put(e.getPlayer().getUniqueId(), "global");

        if (plugin.config.load("chat", "messages.yml").getBoolean("first-join.enabled"))
            if (new MaSuitePlayer().find(e.getPlayer().getUniqueId()).getUniqueId() == null) {
                plugin.utils.broadcast(plugin.config.load("chat", "messages.yml")
                        .getString("first-join.message").replace("%player%", e.getPlayer().getName()));
            }

        if (plugin.config.load("chat", "messages.yml").getBoolean("motd.enabled")) {
            plugin.formator.sendMessage(e.getPlayer(), plugin.config.load("chat", "messages.yml")
                    .getString("motd.message")
                    .replace("%player%", e.getPlayer().getName()));
        }
    }
}
