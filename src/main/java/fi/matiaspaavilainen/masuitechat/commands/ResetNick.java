package fi.matiaspaavilainen.masuitechat.commands;

import fi.matiaspaavilainen.masuitecore.chat.Formator;
import fi.matiaspaavilainen.masuitecore.config.Configuration;
import fi.matiaspaavilainen.masuitecore.managers.MaSuitePlayer;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;


public class ResetNick extends Command {
    public ResetNick(String... aliases) {
        super("resetnick", "masuitechat.nick", aliases);
    }

    @Override
    public void execute(CommandSender cs, String[] args) {
        Formator formator = new Formator();
        Configuration config = new Configuration();
        if (args.length == 0) {
            if (!(cs instanceof ProxiedPlayer)) {
                return;
            }
            ProxiedPlayer p = (ProxiedPlayer) cs;
            p.setDisplayName(p.getName());
            MaSuitePlayer msp = new MaSuitePlayer();
            msp = msp.find(p.getUniqueId());
            msp.setNickname(null);
            msp.update(msp);
            formator.sendMessage(p, config.load("chat", "messages.yml").getString("nickname-changed").replace("%nickname%", p.getName()));
        } else if (args.length == 1) {
            if (cs.hasPermission("masuitechat.nick.others")) {
                ProxiedPlayer p = ProxyServer.getInstance().getPlayer(args[0]);
                if (p == null) {
                    cs.sendMessage(new TextComponent(formator.colorize(config.load(null, "messages.yml").getString("player-not-online"))));
                    return;
                }
                p.setDisplayName(null);
                MaSuitePlayer msp = new MaSuitePlayer();
                msp = msp.find(p.getUniqueId());
                msp.setNickname(null);
                msp.update(msp);
                formator.sendMessage(p, config.load("chat", "messages.yml").getString("nickname-changed").replace("%nickname%", p.getName()));
            }
        } else {
            cs.sendMessage(new TextComponent(formator.colorize(config.load("chat", "syntax.yml").getString("nickname.reset"))));
        }
    }
}
