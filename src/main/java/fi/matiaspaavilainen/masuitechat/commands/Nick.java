package fi.matiaspaavilainen.masuitechat.commands;

import fi.matiaspaavilainen.masuitecore.chat.Formator;
import fi.matiaspaavilainen.masuitecore.config.Configuration;
import fi.matiaspaavilainen.masuitecore.managers.MaSuitePlayer;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Nick extends Command {
    public Nick(String... aliases) {
        super("nick", "masuitechat.nick", aliases);
    }

    @Override
    public void execute(CommandSender cs, String[] args) {
        Formator formator = new Formator();
        Configuration config = new Configuration();
        if (args.length == 1) {
            if (!(cs instanceof ProxiedPlayer)) {
                return;
            }
            ProxiedPlayer p = (ProxiedPlayer) cs;
            p.setDisplayName(args[0]);
            MaSuitePlayer msp = new MaSuitePlayer();
            msp = msp.find(p.getUniqueId());
            msp.setNickname(args[0]);
            msp.update(msp);
            formator.sendMessage(p, config.load("chat", "messages.yml").getString("nickname-changed").replace("%nickname%", args[0]));
        } else if (args.length == 2) {
            if (cs.hasPermission("masuitechat.nick.others")) {
                ProxiedPlayer p = ProxyServer.getInstance().getPlayer(args[1]);
                if (p == null) {
                    cs.sendMessage(new TextComponent(formator.colorize(config.load(null, "messages.yml").getString("player-not-online"))));
                    return;
                }
                p.setDisplayName(args[0]);
                MaSuitePlayer msp = new MaSuitePlayer();
                msp = msp.find(p.getUniqueId());
                msp.setNickname(args[0]);
                msp.update(msp);
                formator.sendMessage(p, config.load("chat", "messages.yml").getString("nickname-changed").replace("%nickname%", args[0]));
            }
        } else {
            cs.sendMessage(new TextComponent(formator.colorize(config.load("chat", "syntax.yml").getString("nickname.set"))));
        }
    }
}
