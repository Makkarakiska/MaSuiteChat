package fi.matiaspaavilainen.masuitechat.channels;

import fi.matiaspaavilainen.masuitechat.Utilities;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class Global {
    public static void sendMessage(ProxiedPlayer p, String msg) {
        ProxyServer.getInstance().broadcast(Utilities.chatFormat(p, msg, "global"));
    }
}
