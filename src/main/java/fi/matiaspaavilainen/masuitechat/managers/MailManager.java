package fi.matiaspaavilainen.masuitechat.managers;

import fi.matiaspaavilainen.masuitechat.Mail;
import fi.matiaspaavilainen.masuitecore.managers.MaSuitePlayer;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class MailManager {

    public void handle(String s, String r, String message) {
        ProxiedPlayer sender = ProxyServer.getInstance().getPlayer(s);
        if (sender == null) {
            return;
        }

        MaSuitePlayer receiver = new MaSuitePlayer();
        receiver = receiver.find(r);
        if (receiver.getUUID() == null) {
            // Message: Player not found from database
            return;
        }
        Mail mail = new Mail(sender.getUniqueId(), receiver.getUUID(), message, System.currentTimeMillis() / 1000);
        mail.send(mail);
    }
}
