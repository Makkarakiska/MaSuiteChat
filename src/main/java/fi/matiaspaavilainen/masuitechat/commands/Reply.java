package fi.matiaspaavilainen.masuitechat.commands;

import fi.matiaspaavilainen.masuitechat.channels.Private;
import fi.matiaspaavilainen.masuitecore.chat.Formator;
import fi.matiaspaavilainen.masuitecore.config.Configuration;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Reply extends Command {

    public Reply() {
        super("r", "", "reply");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Configuration config = new Configuration();
        ProxiedPlayer p = (ProxiedPlayer) sender;
        if (Private.conversations.containsKey(p.getUniqueId())) {
            Formator formator = new Formator();
            ProxiedPlayer receiver = ProxyServer.getInstance().getPlayer(Private.conversations.get(p.getUniqueId()));
            if (receiver != null) {
                StringBuilder msg = new StringBuilder();
                int i;
                for (i = 0; i < args.length; i++) {
                    msg.append(args[i]).append(" ");
                }

                String format = config.load("chat", "chat.yml").getString("formats.private");
                format = formator.colorize(format
                        .replace("%sender_nickname%", p.getDisplayName())
                        .replace("%receiver_nickname%", receiver.getDisplayName())
                        .replace("%sender_realname%", p.getName())
                        .replace("%receiver_realname%", receiver.getName()));
                if (sender.hasPermission("masuitechat.chat.colors")) {
                    format = formator.colorize(format.replace("%message%", msg.toString()));
                } else {
                    format = format.replace("%message%", msg);
                }
                TextComponent message = new TextComponent(format);
                sender.sendMessage(message);
                receiver.sendMessage(message);
            } else{
                sender.sendMessage(new TextComponent(new Formator().colorize(config.load(null,"messages.yml").getString("player-not-online"))));
            }
        } else {
            sender.sendMessage(new TextComponent(new Formator().colorize(config.load("chat","messages.yml").getString("no-player-to-reply"))));
        }
    }
}
