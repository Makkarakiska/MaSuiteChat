package fi.matiaspaavilainen.masuitechat;

import fi.matiaspaavilainen.masuitechat.utils.MDChat;
import fi.matiaspaavilainen.masuitecore.chat.Date;
import fi.matiaspaavilainen.masuitecore.chat.Formator;
import fi.matiaspaavilainen.masuitecore.config.Configuration;
import fi.matiaspaavilainen.masuitecore.managers.Group;
import fi.matiaspaavilainen.masuitecore.managers.MaSuitePlayer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class Utilities {

    public static BaseComponent[] chatFormat(ProxiedPlayer p, String msg, String channel) {

        Formator formator = new Formator();
        Configuration config = new Configuration();

        String format = config.load("chat", "chat.yml").getString("formats." + channel);
        String server = config.load("chat", "chat.yml").getString("channels." + p.getServer().getInfo().getName().toLowerCase() + ".prefix");

        Group group = new MaSuitePlayer().find(p.getUniqueId()).getGroup();


        format = formator.colorize(
                format.replace("%server%", server)
                        .replace("%prefix%", group.getPrefix() != null ? group.getPrefix() : "")
                        .replace("%nickname%", p.getDisplayName())
                        .replace("%realname%", p.getName())
                        .replace("%suffix%", group.getSuffix() != null ? group.getSuffix() : ""));
        if (p.hasPermission("masuitechat.chat.colors")) {
            format = formator.colorize(format.replace("%message%", msg));
        } else {
            format = format.replace("%message%", msg);
        }

        return new ComponentBuilder(MDChat.getMessageFromString(format)).event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder(formator.colorize(config.load("chat", "messages.yml")
                        .getString("message-hover-actions")
                        .replace("%timestamp%", new Date().getDate(new java.util.Date())))).create())).create();
    }
}
