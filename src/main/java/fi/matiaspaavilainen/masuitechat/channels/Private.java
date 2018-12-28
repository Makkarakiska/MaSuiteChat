package fi.matiaspaavilainen.masuitechat.channels;

import fi.matiaspaavilainen.masuitechat.managers.Group;
import fi.matiaspaavilainen.masuitecore.Utils;
import fi.matiaspaavilainen.masuitecore.chat.Date;
import fi.matiaspaavilainen.masuitecore.chat.Formator;
import fi.matiaspaavilainen.masuitecore.chat.MDChat;
import fi.matiaspaavilainen.masuitecore.config.Configuration;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.HashMap;
import java.util.UUID;


public class Private {

    public Private() { }

    public static HashMap<UUID, UUID> conversations = new HashMap<>();

    private Configuration config = new Configuration();
    private Formator formator = new Formator();
    private Utils utils = new Utils();

    public void sendMessage(ProxiedPlayer sender, ProxiedPlayer receiver, String msg) {

        if (utils.isOnline(receiver, sender)) {
            create(sender, receiver, msg);
            conversations.put(receiver.getUniqueId(), sender.getUniqueId());
            conversations.put(sender.getUniqueId(), receiver.getUniqueId());
        } else {
            sender.sendMessage(new TextComponent(new Formator().colorize(config.load(null, "messages.yml").getString("player-not-online"))));
        }
    }

    public void create(ProxiedPlayer sender, ProxiedPlayer receiver, String msg) {
        String senderFormat = config.load("chat", "chat.yml").getString("formats.private.sender");
        String receiverFormat = config.load("chat", "chat.yml").getString("formats.private.receiver");
        Group senderInfo = new Group().get(sender.getUniqueId());
        Group receiverInfo = new Group().get(receiver.getUniqueId());
        senderFormat = formator.colorize(senderFormat
                .replace("%sender_nickname%", sender.getDisplayName())
                .replace("%receiver_nickname%", receiver.getDisplayName())
                .replace("%sender_realname%", sender.getName())
                .replace("%receiver_realname%", receiver.getName())
                .replace("%sender_prefix%", senderInfo.getPrefix())
                .replace("%sender_suffix%", senderInfo.getSuffix())
                .replace("%receiver_prefix%", receiverInfo.getPrefix())
                .replace("%receiver_suffix%", receiverInfo.getSuffix())
        );
        receiverFormat = formator.colorize(receiverFormat
                .replace("%sender_nickname%", sender.getDisplayName())
                .replace("%receiver_nickname%", receiver.getDisplayName())
                .replace("%sender_realname%", sender.getName())
                .replace("%receiver_realname%", receiver.getName())
                .replace("%sender_prefix%", senderInfo.getPrefix())
                .replace("%sender_suffix%", senderInfo.getSuffix())
                .replace("%receiver_prefix%", receiverInfo.getPrefix())
                .replace("%receiver_suffix%", receiverInfo.getSuffix())
        );
        if (sender.hasPermission("masuitechat.chat.colors")) {
            senderFormat = formator.colorize(senderFormat.replace("%message%", msg));
            receiverFormat = formator.colorize(receiverFormat.replace("%message%", msg));
        } else {
            senderFormat = senderFormat.replace("%message%", msg);
            receiverFormat = receiverFormat.replace("%message%", msg);
        }

        HoverEvent he = new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder(formator.colorize(config.load("chat", "messages.yml")
                        .getString("message-hover-actions")
                        .replace("%timestamp%", new Date().getDate(new java.util.Date())))).create());
        if (sender.hasPermission("masuitechat.chat.colors")) {
            sender.sendMessage(new ComponentBuilder(MDChat.getMessageFromString(senderFormat)).event(he).create());
            receiver.sendMessage(new ComponentBuilder(MDChat.getMessageFromString(receiverFormat)).event(he).create());
        } else {
            sender.sendMessage(new ComponentBuilder(MDChat.getMessageFromString(senderFormat, false)).event(he).create());
            receiver.sendMessage(new ComponentBuilder(MDChat.getMessageFromString(receiverFormat, false)).event(he).create());
        }
    }
}
