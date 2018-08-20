package fi.matiaspaavilainen.masuitechat.channels;

import fi.matiaspaavilainen.masuitecore.chat.Formator;
import fi.matiaspaavilainen.masuitecore.config.Configuration;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;


public class Private {
    public static void sendMessage(ProxiedPlayer sender, ProxiedPlayer receiver, String msg) {
        Configuration config = new Configuration();
        Formator formator = new Formator();
        if (receiver != null) {
            String format = config.load(null,"chat.yml").getString("formats.private");
            format = formator.colorize(format
                    .replace("%sender_nickname%", sender.getDisplayName())
                    .replace("%receiver_nickname%", receiver.getDisplayName())
                    .replace("%sender_realname%", sender.getName())
                    .replace("%receiver_realname%", receiver.getName()));
            if (sender.hasPermission("masuitechat.chat.colors")) {
                format = formator.colorize(format.replace("%message%", msg));
            } else {
                format = format.replace("%message%", msg);
            }

            TextComponent message = new TextComponent(format);
            sender.sendMessage(message);
            receiver.sendMessage(message);
        } else {
            sender.sendMessage(new TextComponent(new Formator().colorize(config.load(null,"messages.yml").getString("player-not-online"))));
        }
    }
}
