package dev.masa.masuitechat.bukkit.commands;

import dev.masa.masuitechat.bukkit.MaSuiteChat;
import dev.masa.masuitecore.acf.BaseCommand;
import dev.masa.masuitecore.acf.annotation.*;
import dev.masa.masuitecore.core.channels.BukkitPluginChannel;
import org.bukkit.entity.Player;

@CommandAlias("mail")
public class MailCommand extends BaseCommand {

    private MaSuiteChat plugin;

    public MailCommand(MaSuiteChat plugin) {
        this.plugin = plugin;
    }

    @Default
    @CommandPermission("masuitechat.mail.send")
    @CommandCompletion("@masuite_players *")
    public void sendMailToPlayerCommand(Player player, String receiver, String message) {
        new BukkitPluginChannel(plugin, player, "MaSuiteChat", "Mail", "Send", player.getName(), receiver, message).send();
    }

    @Subcommand("sendall")
    @CommandPermission("masuitechat.mail.sendall")
    public void sendMailToAllCommand(Player player, String message) {
        new BukkitPluginChannel(plugin, player, "MaSuiteChat", "Mail", "SendAll", player.getName(), message).send();
    }

    @Subcommand("read")
    @CommandPermission("masuitechat.mail.read")
    public void readMailCommand(Player player) {
        new BukkitPluginChannel(plugin, player, "MaSuiteChat", "Mail", "Read", player.getName()).send();
    }
}
