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

    public void sendAll(String s, String message) {
        ProxiedPlayer sender = ProxyServer.getInstance().getPlayer(s);
        if (utils.isOnline(sender)) {

            Set<MaSuitePlayer> maSuitePlayers = new MaSuitePlayer().findAll();
            maSuitePlayers.forEach(msp -> {
                Mail mail = new Mail(sender.getUniqueId(), msp.getUUID(), message, System.currentTimeMillis() / 1000);
                // Notify player(s)
                if (mail.send()) {
                    formator.sendMessage(sender, config.load("chat", "messages.yml").getString("mail.sent").replace("%player%", msp.getUsername()));
                    if (utils.isOnline(ProxyServer.getInstance().getPlayer(msp.getUUID()))) {
                        formator.sendMessage(ProxyServer.getInstance().getPlayer(msp.getUUID()), config.load("chat", "messages.yml").getString("mail.received").replace("%player%", sender.getName()));
                    }
                }
            });
        }
    }

    public void read(String r) {
        ProxiedPlayer receiver = ProxyServer.getInstance().getPlayer(r);
        if (utils.isOnline(receiver)) {
            StringJoiner joiner = new StringJoiner("\n");

            Set<Mail> mailSet = new Mail().list(receiver.getUniqueId());

            if (mailSet.isEmpty()) {
                formator.sendMessage(receiver, config.load("chat", "messages.yml").getString("mail.empty"));
                return;
            }
            // Do some magic with mails
            mailSet.forEach(mail -> {
                MaSuitePlayer sender = new MaSuitePlayer().find(mail.getSender());
                joiner.add(config.load("chat", "chat.yml").getString("formats.mail")
                        .replace("%sender_realname%", sender.getUsername())
                        .replace("%sender_nickname%", sender.getNickname() != null ? sender.getNickname() : sender.getUsername())
                        .replace("%message%", mail.getMessage())
                );
                mail.read();
            });

            formator.sendMessage(receiver, joiner.toString());
        }
    }
}
