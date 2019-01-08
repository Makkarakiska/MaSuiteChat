package fi.matiaspaavilainen.masuitechat.bungee.channels;

import fi.matiaspaavilainen.masuitechat.bungee.Utilities;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Global {

    public static List<UUID> ignores = new ArrayList<>();
    public static void sendMessage(ProxiedPlayer p, String msg) {
        BaseComponent[] txt = Utilities.chatFormat(p, msg, "global");
        for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
            if(!ignores.contains(player.getUniqueId())){
                player.sendMessage(txt);
            }
        }
    }
}
