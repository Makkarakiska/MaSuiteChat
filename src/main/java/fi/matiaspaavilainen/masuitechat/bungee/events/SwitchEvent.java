package fi.matiaspaavilainen.masuitechat.bungee.events;

import fi.matiaspaavilainen.masuitechat.bungee.MaSuiteChat;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class SwitchEvent implements Listener {

    private MaSuiteChat plugin;

    public SwitchEvent(MaSuiteChat plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onSwitch(net.md_5.bungee.api.event.ServerSwitchEvent event) {
        if (plugin.config.load("chat", "messages.yml").getBoolean("switch-message.enabled")) {
            plugin.utils.broadcast(plugin.config.load("chat", "messages.yml")
                    .getString("switch-message.message")
                    .replace("%player%", event.getPlayer().getName())
                    .replace("%server%", event.getPlayer().getServer().getInfo().getName())
            );
        }
    }
}
