package fi.matiaspaavilainen.masuitechat.bukkit.commands;

import fi.matiaspaavilainen.masuitechat.bukkit.MaSuiteChat;
import fi.matiaspaavilainen.masuitecore.acf.BaseCommand;
import fi.matiaspaavilainen.masuitecore.acf.annotation.CommandAlias;
import fi.matiaspaavilainen.masuitecore.acf.annotation.CommandCompletion;
import fi.matiaspaavilainen.masuitecore.acf.annotation.CommandPermission;
import fi.matiaspaavilainen.masuitecore.acf.annotation.Description;
import fi.matiaspaavilainen.masuitecore.core.channels.BukkitPluginChannel;
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
