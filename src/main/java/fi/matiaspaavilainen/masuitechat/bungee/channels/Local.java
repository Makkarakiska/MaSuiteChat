package fi.matiaspaavilainen.masuitechat.bungee.channels;

import fi.matiaspaavilainen.masuitechat.bungee.MaSuiteChat;
import fi.matiaspaavilainen.masuitechat.bungee.Utilities;
import fi.matiaspaavilainen.masuitecore.core.configuration.BungeeConfiguration;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.chat.ComponentSerializer;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Local {

    private MaSuiteChat plugin;
    private BungeeConfiguration config = new BungeeConfiguration();

    public Local(MaSuiteChat p) {
        plugin = p;
    }

    public String buildMessage(ProxiedPlayer p, String msg) {
        return ComponentSerializer.toString(Utilities.chatFormat(p, msg, "local"));
    }

    public void send(ProxiedPlayer p, String msg) {
        String server = p.getServer().getInfo().getName().toLowerCase();
        int range = config.load("chat", "bungee/chat.yml").getInt("channels." + server + ".localRadius");

        try (ByteArrayOutputStream b = new ByteArrayOutputStream();
             DataOutputStream out = new DataOutputStream(b)) {
            out.writeUTF("LocalChat");
            out.writeUTF(buildMessage(p, msg));
            out.writeInt(range);
            p.getServer().sendData("BungeeCord", b.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
