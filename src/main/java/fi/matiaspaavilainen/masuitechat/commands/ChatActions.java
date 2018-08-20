package fi.matiaspaavilainen.masuitechat.commands;

import fi.matiaspaavilainen.masuitechat.MaSuiteChat;
import fi.matiaspaavilainen.masuitecore.chat.Formator;
import fi.matiaspaavilainen.masuitecore.config.Configuration;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Listener;


public class ChatActions extends Command implements Listener {
    public ChatActions() {
        super("chatactions");
    }

    private Formator formator = new Formator();

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (!(sender instanceof ProxiedPlayer)) {
            System.out.println("Only players can execute this command!");
            return;
        }
        if (args.length == 1) {
            if (sender.hasPermission("masuitechat.action.player")) {
                TextComponent c = new TextComponent(formator.colorize(new Configuration().load("chat","messages.yml").getString("available-player-commands")));
                sender.sendMessage(c);
                MaSuiteChat.playerActions
                        .forEach(cmd -> {
                                    sender.sendMessage(action(args[0], cmd));
                                }
                        );
            }
            if (sender.hasPermission("masuitechat.action.admin")) {
                TextComponent s = new TextComponent(formator.colorize(new Configuration().load("chat","messages.yml").getString("available-staff-commands")));
                sender.sendMessage(s);
                MaSuiteChat.staffActions
                        .forEach(cmd -> {
                                    sender.sendMessage(action(args[0], cmd));
                                }
                        );
            }

        }
    }

    private TextComponent action(String p, String cmd) {
        String player = cmd.replace("%player%", p);
        String format = new Configuration().load(null,"chat.yml").getString("formats.actions");
        format = formator.colorize(format.replace("%action%", cmd).replace("%player%", p));
        TextComponent command = new TextComponent(format);
        command.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, player));
        return command;
    }
}
