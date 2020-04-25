package dev.masa.masuitechat.bukkit.commands;

import dev.masa.masuitechat.bukkit.MaSuiteChat;
import dev.masa.masuitecore.acf.BaseCommand;
import dev.masa.masuitecore.acf.annotation.CommandAlias;
import dev.masa.masuitecore.acf.annotation.CommandPermission;
import dev.masa.masuitecore.acf.annotation.Description;
import dev.masa.masuitecore.core.channels.BukkitPluginChannel;
import org.bukkit.entity.Player;

public class IgnoreCommand extends BaseCommand {

    private MaSuiteChat plugin;

    public IgnoreCommand(MaSuiteChat plugin) {
        this.plugin = plugin;
    }

    @CommandAlias("ignore|igplayer|ignorep")
    @CommandPermission("masuitechat.ignore.player")
    @Description("Ignores specific player")
    public void ignorePlayerCommand(Player player, String onlinePlayer) {
        new BukkitPluginChannel(plugin, player, "MaSuiteChat", "IgnorePlayer", player.getUniqueId().toString(), onlinePlayer).send();
    }

}