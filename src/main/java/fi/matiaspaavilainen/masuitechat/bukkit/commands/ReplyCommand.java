package fi.matiaspaavilainen.masuitechat.bukkit.commands;

import fi.matiaspaavilainen.masuitechat.bukkit.MaSuiteChat;
import fi.matiaspaavilainen.masuitecore.acf.BaseCommand;
import fi.matiaspaavilainen.masuitecore.acf.annotation.CommandAlias;
import fi.matiaspaavilainen.masuitecore.acf.annotation.CommandPermission;
import fi.matiaspaavilainen.masuitecore.acf.annotation.Description;
import fi.matiaspaavilainen.masuitecore.core.channels.BukkitPluginChannel;
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
