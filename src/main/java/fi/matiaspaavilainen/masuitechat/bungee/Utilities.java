package fi.matiaspaavilainen.masuitechat.bungee;

import fi.matiaspaavilainen.masuitechat.bungee.objects.Group;
import fi.matiaspaavilainen.masuitecore.bungee.chat.Formator;
import fi.matiaspaavilainen.masuitecore.bungee.chat.MDChat;
import fi.matiaspaavilainen.masuitecore.core.configuration.BungeeConfiguration;
import fi.matiaspaavilainen.masuitecore.core.utils.Date;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class Utilities {

    public static BaseComponent[] chatFormat(ProxiedPlayer p, String msg, String channel) {

        Formator formator = new Formator();
        BungeeConfiguration config = new BungeeConfiguration();

        String format = config.load("chat", "bungee/chat.yml").getString("formats." + channel);
        String server = config.load("chat", "bungee/chat.yml").getString("channels." + p.getServer().getInfo().getName().toLowerCase() + ".prefix");

        Group group = new Group().get(p.getUniqueId());
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
