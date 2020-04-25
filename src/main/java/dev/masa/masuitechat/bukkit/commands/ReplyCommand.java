package dev.masa.masuitechat.bukkit.commands;

import dev.masa.masuitechat.bukkit.MaSuiteChat;
import dev.masa.masuitecore.acf.BaseCommand;
import dev.masa.masuitecore.acf.annotation.CommandAlias;
import dev.masa.masuitecore.acf.annotation.CommandPermission;
import dev.masa.masuitecore.acf.annotation.Description;
import dev.masa.masuitecore.core.channels.BukkitPluginChannel;
import org.bukkit.entity.Player;

public class ReplyCommand extends BaseCommand {

    private MaSuiteChat plugin;

    public ReplyCommand(MaSuiteChat plugin) {
        this.plugin = plugin;
    }

    @CommandAlias("r|reply")
    @CommandPermission("masuitechat.channel.private")
    @Description("Replies to a private message")
    public void replyCommand(Player player, String message) {
        new BukkitPluginChannel(plugin, player, "MaSuiteChat", "SendMessage", "reply", player.getUniqueId().toString(), message).send();
    }
}
