package fi.matiaspaavilainen.masuitechat.channels;

import fi.matiaspaavilainen.masuitechat.Utilities;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.chat.ComponentSerializer;

public class Local {
    public static String sendMessage(ProxiedPlayer p, String msg, String prefix, String suffix) {
        return ComponentSerializer.toString(Utilities.chatFormat(p, msg, prefix, suffix, "local"));
    }
}
