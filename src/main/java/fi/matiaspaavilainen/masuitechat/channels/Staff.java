package fi.matiaspaavilainen.masuitechat.channels;

import fi.matiaspaavilainen.masuitechat.Utilities;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class Staff {
    public static void sendMessage(ProxiedPlayer p, String msg) {
        TextComponent message = new TextComponent(Utilities.chatFormat(p, msg, "staff"));
        for (ProxiedPlayer players : ProxyServer.getInstance().getPlayers()) {
            if (players.hasPermission("masuitechat.channel.admin")) players.sendMessage(message);
        }

    }
}
