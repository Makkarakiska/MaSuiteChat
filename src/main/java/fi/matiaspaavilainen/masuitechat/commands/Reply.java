package fi.matiaspaavilainen.masuitechat.commands;

import fi.matiaspaavilainen.masuitechat.channels.Private;
import fi.matiaspaavilainen.masuitecore.chat.Formator;
import fi.matiaspaavilainen.masuitecore.config.Configuration;
import fi.matiaspaavilainen.masuitecore.managers.Group;
import fi.matiaspaavilainen.masuitecore.managers.MaSuitePlayer;
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

                String senderFormat = config.load("chat", "chat.yml").getString("formats.private.sender");
                String receiverFormat = config.load("chat", "chat.yml").getString("formats.private.receiver");
                MaSuitePlayer msp = new MaSuitePlayer();
                Group senderGroup = msp.getGroup(p.getUniqueId());
                Group receiverGroup = msp.getGroup(receiver.getUniqueId());

                senderFormat = formator.colorize(senderFormat
                        .replace("%sender_nickname%", p.getDisplayName())
                        .replace("%receiver_nickname%", receiver.getDisplayName())
                        .replace("%sender_realname%", p.getName())
                        .replace("%receiver_realname%", receiver.getName())
                        .replace("%sender_prefix%", senderGroup.getPrefix() != null ? senderGroup.getPrefix() : "")
                        .replace("%sender_suffix%", senderGroup.getSuffix() != null ? senderGroup.getSuffix() : "")
                        .replace("%receiver_prefix%", receiverGroup.getPrefix() != null ? receiverGroup.getPrefix() : "")
                        .replace("%receiver_suffix%", receiverGroup.getSuffix() != null ? receiverGroup.getSuffix() : "")
                );
                receiverFormat = formator.colorize(receiverFormat
                        .replace("%sender_nickname%", p.getDisplayName())
                        .replace("%receiver_nickname%", receiver.getDisplayName())
                        .replace("%sender_realname%", p.getName())
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
            } else{
                sender.sendMessage(new TextComponent(new Formator().colorize(config.load(null,"messages.yml").getString("player-not-online"))));
            }
        } else {
            sender.sendMessage(new TextComponent(new Formator().colorize(config.load("chat","messages.yml").getString("no-player-to-reply"))));
        }
    }
}
