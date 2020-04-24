package dev.masa.masuitechat.core.controllers;

import dev.masa.masuitechat.bungee.MaSuiteChat;
import dev.masa.masuitechat.core.models.Mail;
import dev.masa.masuitecore.core.models.MaSuitePlayer;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;

public class MailController {

    private MaSuiteChat plugin;

    public MailController(MaSuiteChat plugin) {
        this.plugin = plugin;
    }

    public void sendMail(String senderName, String receiverName, String message) {
        ProxiedPlayer sender = ProxyServer.getInstance().getPlayer(senderName);
        if (plugin.utils.isOnline(sender)) {

            MaSuitePlayer receiver = plugin.getApi().getPlayerService().getPlayer(receiverName);
            if (receiver == null) {
                plugin.formator.sendMessage(sender, plugin.config.load("chat", "messages.yml").getString("mail.player-not-found"));
                return;
            }
            Mail mail = new Mail(sender.getUniqueId(), receiver.getUniqueId(), message, System.currentTimeMillis() / 1000);

            plugin.getMailService().sendMail(mail);
            plugin.formator.sendMessage(sender, plugin.config.load("chat", "messages.yml").getString("mail.sent").replace("%player%", receiver.getUsername()));
            if (plugin.utils.isOnline(ProxyServer.getInstance().getPlayer(receiverName))) {
                plugin.formator.sendMessage(ProxyServer.getInstance().getPlayer(receiverName), plugin.config.load("chat", "messages.yml").getString("mail.received").replace("%player%", sender.getName()));
            }
        }
    }

    public void sendAll(String senderName, String message) {
        ProxiedPlayer sender = ProxyServer.getInstance().getPlayer(senderName);
        if (plugin.utils.isOnline(sender)) {

            List<MaSuitePlayer> maSuitePlayers = plugin.getApi().getPlayerService().getAllPlayers(false);

            maSuitePlayers.forEach(msp -> {
                Mail mail = new Mail(sender.getUniqueId(), msp.getUniqueId(), message, System.currentTimeMillis() / 1000);
                // Notify player(s)
                plugin.getProxy().getScheduler().runAsync(plugin, () -> {
                    plugin.getMailService().sendMail(mail);
                    plugin.formator.sendMessage(sender, plugin.config.load("chat", "messages.yml").getString("mail.sent").replace("%player%", msp.getUsername()));
                    if (plugin.utils.isOnline(ProxyServer.getInstance().getPlayer(msp.getUniqueId()))) {
                        plugin.formator.sendMessage(ProxyServer.getInstance().getPlayer(msp.getUniqueId()), plugin.config.load("chat", "messages.yml").getString("mail.received").replace("%player%", sender.getName()));
                    }
                });
            });
        }
    }

    public void read(String receiverName) {
        ProxiedPlayer receiver = ProxyServer.getInstance().getPlayer(receiverName);
        if (plugin.utils.isOnline(receiver)) {

            List<Mail> mails = plugin.getMailService().getMails(receiver.getUniqueId());

            if (mails.isEmpty()) {
                plugin.formator.sendMessage(receiver, plugin.config.load("chat", "messages.yml").getString("mail.empty"));
                return;
            }
            // Do some magic with mails
            mails.forEach(mail -> {
                MaSuitePlayer sender = plugin.getApi().getPlayerService().getPlayer(mail.getSender());
                plugin.formator.sendMessage(receiver, plugin.config.load("chat", "chat.yml").getString("formats.mail")
                        .replace("%sender_realname%", sender.getUsername())
                        .replace("%sender_nickname%", sender.getNickname() != null ? sender.getNickname() : sender.getUsername())
                        .replace("%message%", mail.getMessage()));
                plugin.getMailService().markAsRead(mail);
            });
        }
    }
}
