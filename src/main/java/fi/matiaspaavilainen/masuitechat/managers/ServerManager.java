package fi.matiaspaavilainen.masuitechat.managers;

import fi.matiaspaavilainen.masuitecore.config.Configuration;
import net.md_5.bungee.api.ProxyServer;

public class ServerManager {

    public static void loadServers() {
        net.md_5.bungee.config.Configuration config = new Configuration().load("chat","chat.yml");
        for (String server : ProxyServer.getInstance().getServers().keySet()) {
            if(!config.contains("channels." + server.toLowerCase())){
                System.out.println("Added " + server + " to channels section!");
                config.set("channels." + server.toLowerCase() + ".prefix", server);
                config.set("channels." + server.toLowerCase() + ".localRadius", 100);
            }
        }
        new Configuration().save(config, "chat/chat.yml");
    }
}
