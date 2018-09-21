package fi.matiaspaavilainen.masuitechat.commands;

import fi.matiaspaavilainen.masuitechat.channels.Private;
import fi.matiaspaavilainen.masuitecore.chat.Formator;
import fi.matiaspaavilainen.masuitecore.config.Configuration;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;


public class Message extends Command {
    public Message(String... aliases) {
        super("msg", "masuitechat.channel.private", aliases);
    }

    @Override
    public void execute(CommandSender cs, String[] args) {
        if(!(cs instanceof ProxiedPlayer)){
            return;
        }
        Configuration config = new Configuration();
        Formator formator = new Formator();
        if(args.length > 1){
            ProxiedPlayer sender = (ProxiedPlayer) cs;
            ProxiedPlayer receiver = ProxyServer.getInstance().getPlayer(args[0]);
            if (receiver != null) {
                StringBuilder msg = new StringBuilder();
                int i;
                for (i = 1; i < args.length; i++) {
                    msg.append(args[i]).append(" ");
                }
                Private.sendMessage(
                        sender,
                        receiver,
                        msg.toString()

                );
            } else {
                formator.sendMessage((ProxiedPlayer) cs, config.load(null, "messages.yml").getString("player-not-online"));
            }
        }else{
            formator.sendMessage((ProxiedPlayer) cs, config.load("chat", "syntax.yml").getString("private-message"));
        }

    }
}
