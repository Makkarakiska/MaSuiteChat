package fi.matiaspaavilainen.masuitechat;

import fi.matiaspaavilainen.masuitechat.managers.GroupInfo;
import fi.matiaspaavilainen.masuitecore.chat.Date;
import fi.matiaspaavilainen.masuitecore.chat.Formator;
import fi.matiaspaavilainen.masuitecore.chat.MDChat;
import fi.matiaspaavilainen.masuitecore.config.Configuration;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class Utilities {

    public static BaseComponent[] chatFormat(ProxiedPlayer p, String msg, String channel) {

        Formator formator = new Formator();
        Configuration config = new Configuration();

        String format = config.load("chat", "chat.yml").getString("formats." + channel);
        String server = config.load("chat", "chat.yml").getString("channels." + p.getServer().getInfo().getName().toLowerCase() + ".prefix");

        GroupInfo group = new GroupInfo(p.getUniqueId());
        format = formator.colorize(
                format.replace("%server%", server)
                        .replace("%prefix%", group.getPrefix())
                        .replace("%nickname%", p.getDisplayName())
                        .replace("%realname%", p.getName())
                        .replace("%suffix%", group.getSuffix()));
        if (p.hasPermission("masuitechat.chat.colors")) {
            format = formator.colorize(format.replace("%message%", msg));
        } else {
            format = format.replace("%message%", msg);
        }

        TextComponent message = MDChat.getMessageFromString(format);
        return new ComponentBuilder(message).event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder(formator.colorize(config.load("chat", "messages.yml")
                        .getString("message-hover-actions")
                        .replace("%timestamp%", new Date().getDate(new java.util.Date())))).create())).create();
    }
}
