package dev.masa.masuitechat.bukkit.commands;

import dev.masa.masuitechat.bukkit.MaSuiteChat;
import dev.masa.masuitecore.acf.BaseCommand;
import dev.masa.masuitecore.acf.annotation.CommandAlias;
import dev.masa.masuitecore.acf.annotation.CommandCompletion;
import dev.masa.masuitecore.acf.annotation.CommandPermission;
import dev.masa.masuitecore.acf.annotation.Description;
import dev.masa.masuitecore.core.channels.BukkitPluginChannel;
import org.bukkit.entity.Player;

public class PrivateMessageCommand extends BaseCommand {

    private MaSuiteChat plugin;

    public PrivateMessageCommand(MaSuiteChat plugin) {
        this.plugin = plugin;
    }

    @CommandAlias("tell|msg|whisper|w")
    @CommandPermission("masuitechat.channel.private")
    @Description("Sends private message to a player")
    @CommandCompletion("@masuite_players")
    public void privateMessageCommand(Player player, String onlinePlayer, String message){
        new BukkitPluginChannel(plugin, player, "MaSuiteChat", "SendMessage", "private", player.getUniqueId().toString(), onlinePlayer, message).send();
    }
}
