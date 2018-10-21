package fi.matiaspaavilainen.masuitechat.managers;

import fi.matiaspaavilainen.masuitechat.Mail;
import fi.matiaspaavilainen.masuitecore.Utils;
import fi.matiaspaavilainen.masuitecore.chat.Formator;
import fi.matiaspaavilainen.masuitecore.config.Configuration;
import fi.matiaspaavilainen.masuitecore.managers.MaSuitePlayer;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Set;
import java.util.StringJoiner;

public class MailManager {

    private Configuration config = new Configuration();
    private Formator formator = new Formator();
    private Utils utils = new Utils();

    public void send(String s, String r, String message) {
        ProxiedPlayer sender = ProxyServer.getInstance().getPlayer(s);
        if (utils.isOnline(sender)) {

            MaSuitePlayer receiver = new MaSuitePlayer().find(r);
            if (receiver.getUUID() == null) {
                formator.sendMessage(sender, config.load("chat", "messages.yml").getString("mail.player-not-found"));
                return;
            }
            Mail mail = new Mail(sender.getUniqueId(), receiver.getUUID(), message, System.currentTimeMillis() / 1000);

            // Notify player(s)
            if (mail.send()) {
                formator.sendMessage(sender, config.load("chat", "messages.yml").getString("mail.sent").replace("%player%", receiver.getUsername()));
                if (utils.isOnline(ProxyServer.getInstance().getPlayer(r))) {
                    formator.sendMessage(ProxyServer.getInstance().getPlayer(r), config.load("chat", "messages.yml").getString("mail.received").replace("%player%", sender.getName()));
                }
            }
        }
    }

    public void read(String s){
        ProxiedPlayer sender = ProxyServer.getInstance().getPlayer(s);
        if(utils.isOnline(sender)){
            StringJoiner joiner = new StringJoiner("\n");
            new Mail().list(sender.getUniqueId()).forEach(mail -> joiner.add(new MaSuitePlayer().find(mail.getSender()).getUsername() + " > " + mail.getMessage()));
            formator.sendMessage(sender, joiner.toString());
        }
    }
}
