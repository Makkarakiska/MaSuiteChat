package fi.matiaspaavilainen.masuitechat.commands.channels;

import com.google.common.base.Joiner;
import fi.matiaspaavilainen.masuitechat.MaSuiteChat;
import fi.matiaspaavilainen.masuitecore.chat.Formator;
import fi.matiaspaavilainen.masuitecore.config.Configuration;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Server extends Command {
    public Server() {
        super("serverchat", "masuitechat.channel.server", "server", "s", "serverchannel");
    }

    @Override
    public void execute(CommandSender cs, String[] args) {
        if(!(cs instanceof ProxiedPlayer)){
            return;
        }
        ProxiedPlayer p = (ProxiedPlayer) cs;
        Formator formator = new Formator();
        Configuration config = new Configuration();
        if(args.length == 0){
            MaSuiteChat.players.put(p.getUniqueId(), "server");
            formator.sendMessage(p, config.load("chat", "messages.yml").getString("channel-changed.server"));
        }else{
            fi.matiaspaavilainen.masuitechat.channels.Server.sendMessage(p, Joiner.on(" ").join(args));
        }
    }
}
