package fi.matiaspaavilainen.masuitechat.channels;

import fi.matiaspaavilainen.masuitechat.Utilities;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;


public class Global {
    public static void sendMessage(ProxiedPlayer p, String msg) {
        ProxyServer.getInstance().broadcast(new TextComponent(Utilities.chatFormat(p, msg, "global")));
    }
}
