package fi.matiaspaavilainen.masuitechat;

import fi.matiaspaavilainen.masuitecore.chat.Date;
import fi.matiaspaavilainen.masuitecore.chat.Formator;
import fi.matiaspaavilainen.masuitecore.config.Configuration;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class Utilities {

    public static TextComponent chatFormat(ProxiedPlayer p, String msg, String px, String sx, String channel) {

        Formator formator = new Formator();
        Configuration config = new Configuration();

        String format = config.load("chat.yml").getString("formats." + channel);
        String server = config.load("chat.yml").getString("channels." + p.getServer().getInfo().getName().toLowerCase() + ".prefix");

        format = formator.colorize(format.replace("%server%", server)
                .replace("%prefix%", px)
                .replace("%nickname%", p.getDisplayName())
                .replace("%realname%", p.getName())
                .replace("%suffix%", sx));

        if (p.hasPermission("masuitechat.chat.colors")) {
            format = formator.colorize(format.replace("%message%", msg));
        } else {
            format = format.replace("%message%", msg);
        }
        TextComponent base = new TextComponent(format);
        base.setHoverEvent(
                new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder(formator.colorize(config.load("messages.yml")
                                .getString("message-hover-actions")
                                .replace("%timestamp%", new Date().getDate(new java.util.Date())))).create()));
        base.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/chatactions " + p.getName()));

        return base;
    }
}
