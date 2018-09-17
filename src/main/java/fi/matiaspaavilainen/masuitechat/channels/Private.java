package fi.matiaspaavilainen.masuitechat.channels;

import fi.matiaspaavilainen.masuitecore.chat.Formator;
import fi.matiaspaavilainen.masuitecore.config.Configuration;
import fi.matiaspaavilainen.masuitecore.managers.Group;
import fi.matiaspaavilainen.masuitecore.managers.MaSuitePlayer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.HashMap;
import java.util.UUID;


public class Private {
    public static HashMap<UUID, UUID> conversations = new HashMap<>();
    public static void sendMessage(ProxiedPlayer sender, ProxiedPlayer receiver, String msg) {
        Configuration config = new Configuration();
        Formator formator = new Formator();
        if (receiver != null) {
            String senderFormat = config.load("chat", "chat.yml").getString("formats.private.sender");
            String receiverFormat = config.load("chat", "chat.yml").getString("formats.private.receiver");
            MaSuitePlayer msp = new MaSuitePlayer();
            Group senderGroup = msp.getGroup(sender.getUniqueId());
            Group receiverGroup = msp.getGroup(receiver.getUniqueId());

            senderFormat = formator.colorize(senderFormat
                    .replace("%sender_nickname%", sender.getDisplayName())
                    .replace("%receiver_nickname%", receiver.getDisplayName())
                    .replace("%sender_realname%", sender.getName())
                    .replace("%receiver_realname%", receiver.getName())
                    .replace("%sender_prefix%", senderGroup.getPrefix() != null ? senderGroup.getPrefix() : "")
                    .replace("%sender_suffix%", senderGroup.getSuffix() != null ? senderGroup.getSuffix() : "")
                    .replace("%receiver_prefix%", receiverGroup.getPrefix() != null ? receiverGroup.getPrefix() : "")
                    .replace("%receiver_suffix%", receiverGroup.getSuffix() != null ? receiverGroup.getSuffix() : "")
            );
            receiverFormat = formator.colorize(receiverFormat
                    .replace("%sender_nickname%", sender.getDisplayName())
                    .replace("%receiver_nickname%", receiver.getDisplayName())
                    .replace("%sender_realname%", sender.getName())
                    .replace("%receiver_realname%", receiver.getName())
                    .replace("%sender_prefix%", senderGroup.getPrefix() != null ? senderGroup.getPrefix() : "")
                    .replace("%sender_suffix%", senderGroup.getSuffix() != null ? senderGroup.getSuffix() : "")
                    .replace("%receiver_prefix%", receiverGroup.getPrefix() != null ? receiverGroup.getPrefix() : "")
                    .replace("%receiver_suffix%", receiverGroup.getSuffix() != null ? receiverGroup.getSuffix() : "")
            );
            if (sender.hasPermission("masuitechat.chat.colors")) {
                senderFormat = formator.colorize(senderFormat.replace("%message%", msg.toString()));
                receiverFormat = formator.colorize(receiverFormat.replace("%message%", msg.toString()));
            } else {
                senderFormat = senderFormat.replace("%message%", msg);
                receiverFormat = receiverFormat.replace("%message%", msg);
            }
            sender.sendMessage(new TextComponent(senderFormat));
            receiver.sendMessage(new TextComponent(receiverFormat));
            conversations.put(receiver.getUniqueId(), sender.getUniqueId());
            conversations.put(sender.getUniqueId(), receiver.getUniqueId());
        } else {
            sender.sendMessage(new TextComponent(new Formator().colorize(config.load(null,"messages.yml").getString("player-not-online"))));
        }
    }
}
