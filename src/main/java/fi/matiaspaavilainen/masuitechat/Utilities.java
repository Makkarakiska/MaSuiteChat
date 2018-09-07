package fi.matiaspaavilainen.masuitechat;

import fi.matiaspaavilainen.masuitecore.chat.Date;
import fi.matiaspaavilainen.masuitecore.chat.Formator;
import fi.matiaspaavilainen.masuitecore.config.Configuration;
import fi.matiaspaavilainen.masuitecore.managers.MaSuitePlayer;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.chat.ComponentSerializer;

public class Utilities {

    public static TextComponent chatFormat(ProxiedPlayer p, String msg, String channel) {

        Formator formator = new Formator();
        Configuration config = new Configuration();

        MaSuitePlayer msp = new MaSuitePlayer();
        String format = config.load(null,"chat.yml").getString("formats." + channel);
        String server = config.load(null,"chat.yml").getString("channels." + p.getServer().getInfo().getName().toLowerCase() + ".prefix");

        format = formator.colorize(format.replace("%server%", server)
                .replace("%prefix%", msp.getGroup(p.getUniqueId()).getPrefix())
                .replace("%nickname%", p.getDisplayName())
                .replace("%realname%", p.getName())
                .replace("%suffix%", msp.getGroup(p.getUniqueId()).getSuffix()));
        if (p.hasPermission("masuitechat.chat.colors")) {
            format = formator.colorize(format.replace("%message%", msg));
        } else {
            format = format.replace("%message%", msg);
        }
        /*TextComponent message = new TextComponent();
        if(p.hasPermission("masuitechat.chat.link")){
            if (p.hasPermission("masuitechat.chat.colors")) {
                message.setText(formator.colorize(format.replace("%message%", msg)));
            } else {
                message.setText(format.replace("%message%", msg));
            }
        }else{
            if (p.hasPermission("masuitechat.chat.colors")) {
                message.setText(formator.colorize(format.replace("%message%", msg)));
                message = new TextComponent(message.getText().replace(".", " . "));
            } else {
                message.setText(format.replace("%message%", msg));
            }
        }
        message.setClickEvent( new ClickEvent( ClickEvent.Action.OPEN_URL, "https://www.spigotmc.org" ) );
        message.setHoverEvent(null);*/
        TextComponent base = new TextComponent(format);
        base.setHoverEvent(
                new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder(formator.colorize(config.load("chat","messages.yml")
                                .getString("message-hover-actions")
                                .replace("%timestamp%", new Date().getDate(new java.util.Date())))).create()));
        base.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/chatactions " + p.getName()));
        return base;
    }
}
