package fi.matiaspaavilainen.masuitechat.bungee.events;

import fi.matiaspaavilainen.masuitechat.bungee.MaSuiteChat;
import net.md_5.bungee.api.event.PostLoginEvent;
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
    }
}