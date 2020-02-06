package dev.masa.masuitechat.bukkit.commands;

import dev.masa.masuitechat.bukkit.MaSuiteChat;
import dev.masa.masuitecore.acf.BaseCommand;
import dev.masa.masuitecore.acf.annotation.CommandAlias;
import dev.masa.masuitecore.acf.annotation.CommandPermission;
import dev.masa.masuitecore.acf.annotation.Description;
import dev.masa.masuitecore.core.channels.BukkitPluginChannel;
import org.bukkit.entity.Player;

public class AfkCommand extends BaseCommand {

    private MaSuiteChat plugin;

    public AfkCommand(MaSuiteChat plugin) {
        this.plugin = plugin;
    }

    @CommandAlias("afk|awayfromkeyboard")
    @CommandPermission("masuitechat.afk")
    @Description("Toggles player's afk state")
    public void afkCommand(Player player) {
        if (plugin.afkList.contains(player.getUniqueId())) {
            new BukkitPluginChannel(plugin, player, "MaSuiteChat", "Afk", player.getUniqueId().toString(), false).send();
            plugin.afkList.remove(player.getUniqueId());
            return;
        }
        new BukkitPluginChannel(plugin, player, "MaSuiteChat", "Afk", player.getUniqueId().toString(), true).send();
        plugin.afkList.add(player.getUniqueId());
    }
}
