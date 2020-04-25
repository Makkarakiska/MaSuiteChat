package dev.masa.masuitechat.bungee.channels;

import dev.masa.masuitechat.bungee.Utilities;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.chat.ComponentSerializer;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;

public class Staff {

    public static void sendMessage(ProxiedPlayer p, String msg) {
        for (Map.Entry<String, ServerInfo> entry : ProxyServer.getInstance().getServers().entrySet()) {
            ServerInfo serverInfo = entry.getValue();
            serverInfo.ping((result, error) -> {
                if (error == null) {
                    if (serverInfo.getPlayers().size() > 0) {
                        try (ByteArrayOutputStream b = new ByteArrayOutputStream();
                             DataOutputStream out = new DataOutputStream(b)) {
                            out.writeUTF("StaffChat");
                            out.writeUTF(ComponentSerializer.toString(Utilities.chatFormat(p, msg, "staff")));
                            serverInfo.sendData("BungeeCord", b.toByteArray());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }


                }
            });
        }

    }
}
