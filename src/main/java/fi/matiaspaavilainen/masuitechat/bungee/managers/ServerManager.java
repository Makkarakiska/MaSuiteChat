package fi.matiaspaavilainen.masuitechat.bungee.managers;

import fi.matiaspaavilainen.masuitecore.core.configuration.BungeeConfiguration;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.config.Configuration;


public class ServerManager {


    public static void loadServers() {
        Configuration config = new BungeeConfiguration().load("chat", "chat.yml");
        for (String server : ProxyServer.getInstance().getServers().keySet()) {
            if (!config.contains("channels." + server.toLowerCase())) {
                System.out.println("Added " + server + " to channels section!");
                config.set("channels." + server.toLowerCase() + ".prefix", server);
                config.set("channels." + server.toLowerCase() + ".localRadius", 100);
                config.set("channels." + server.toLowerCase() + ".defaultChannel", "global");
            }
        }
        new BungeeConfiguration().save(config, "chat/chat.yml");
    }
}
