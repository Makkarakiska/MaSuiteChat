package dev.masa.masuitechat.bungee.channels;

import dev.masa.masuitechat.bungee.MaSuiteChat;
import dev.masa.masuitechat.bungee.objects.Group;
import dev.masa.masuitecore.bungee.Utils;
import dev.masa.masuitecore.bungee.chat.Formator;
import dev.masa.masuitecore.bungee.chat.MDChat;
import dev.masa.masuitecore.core.configuration.BungeeConfiguration;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;
import java.util.UUID;


public class Private {

    public Private() {
    }

    public static HashMap<UUID, UUID> conversations = new HashMap<>();

    private BungeeConfiguration config = new BungeeConfiguration();
    private Formator formator = new Formator();
    private Utils utils = new Utils();

    public void sendMessage(ProxiedPlayer sender, ProxiedPlayer receiver, String msg) {

        if (utils.isOnline(receiver, sender)) {
            create(sender, receiver, msg);
            if(MaSuiteChat.ignores.get(receiver.getUniqueId()) == null || !MaSuiteChat.ignores.get(receiver.getUniqueId()).contains(sender.getUniqueId())) {
        		conversations.put(receiver.getUniqueId(), sender.getUniqueId());
        	}
        	conversations.put(sender.getUniqueId(), receiver.getUniqueId());
        } else {
            sender.sendMessage(new TextComponent(new Formator().colorize(config.load(null, "messages.yml").getString("player-not-online"))));
        }
    }

    public void create(ProxiedPlayer sender, ProxiedPlayer receiver, String msg) {
        String senderFormat = config.load("chat", "chat.yml").getString("formats.private.sender");
        String receiverFormat = config.load("chat", "chat.yml").getString("formats.private.receiver");
        SimpleDateFormat customDate = new SimpleDateFormat(config.load("chat", "messages.yml").getString("timestamp-format"));
        customDate.setTimeZone(TimeZone.getTimeZone(config.load("chat", "messages.yml").getString("timestamp-timezone")));
        String dateFormat  = customDate.format(new Date());
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
        HoverEvent he = new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder(formator.colorize(config.load("chat", "messages.yml")
                        .getString("message-hover-actions")
                        .replace("%timestamp%", dateFormat))).create());
        if(sender.hasPermission("masuitechat.chat.colors")) {
            sender.sendMessage(new ComponentBuilder(MDChat.getMessageFromString(formator.colorize(senderFormat.replace("%message%", msg)))).event(he).create());
            if(MaSuiteChat.ignores.get(receiver.getUniqueId()) == null || !MaSuiteChat.ignores.get(receiver.getUniqueId()).contains(sender.getUniqueId())) {
            	receiver.sendMessage(new ComponentBuilder(MDChat.getMessageFromString(formator.colorize(receiverFormat.replace("%message%", msg)))).event(he).create());
            }
        } else {
            sender.sendMessage(new ComponentBuilder(MDChat.getMessageFromString(senderFormat.replace("%message%", msg))).event(he).create());
            if(MaSuiteChat.ignores.get(receiver.getUniqueId()) == null || !MaSuiteChat.ignores.get(receiver.getUniqueId()).contains(sender.getUniqueId())) {
            	receiver.sendMessage(new ComponentBuilder(MDChat.getMessageFromString(receiverFormat.replace("%message%", msg))).event(he).create());
            }
        }
    }
}