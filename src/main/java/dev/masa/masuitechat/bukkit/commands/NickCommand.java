package dev.masa.masuitechat.bukkit.commands;

import dev.masa.masuitechat.bukkit.MaSuiteChat;
import dev.masa.masuitecore.acf.BaseCommand;
import dev.masa.masuitecore.acf.annotation.*;
import dev.masa.masuitecore.core.channels.BukkitPluginChannel;
import org.bukkit.entity.Player;

public class NickCommand extends BaseCommand {

    private MaSuiteChat plugin;

    public NickCommand(MaSuiteChat plugin) {
        this.plugin = plugin;
    }

    @CommandAlias("nick|nickname")
    @CommandPermission("masuitechat.nick")
    @Description("Give a nick name for player")
    @CommandCompletion("* @masuite_players")
    public void nickCommand(Player player, String nick, @Optional @CommandPermission("masuitechat.nick.other") String onlinePlayer) {
        if(nick.contains("&") && !player.hasPermission("masuitechat.nick.color")) {
            plugin.formator.sendMessage(player, plugin.config.load(null, "messages.yml").getString("no-permission"));
            return;
        }
        if (onlinePlayer == null) {
            new BukkitPluginChannel(plugin, player, "MaSuiteChat", "Nick", player.getUniqueId().toString(), nick).send();
            return;
        }
        new BukkitPluginChannel(plugin, player, "MaSuiteChat", "NickOther", player.getUniqueId().toString(), onlinePlayer, nick).send();
    }

    @CommandAlias("resetnick|resetnickname")
    @CommandPermission("masuitechat.nick")
    @Description("Reset player's nick name")
    @CommandCompletion("@masuite_players")
    public void resetNickCommand(Player player, @Optional @CommandPermission("masuitechat.nick.other") String onlinePlayer) {
        if (onlinePlayer == null) {
            new BukkitPluginChannel(plugin, player, "MaSuiteChat", "ResetNick", player.getUniqueId().toString()).send();
            return;
        }
        new BukkitPluginChannel(plugin, player, "MaSuiteChat", "ResetNickOther", player.getUniqueId().toString(), onlinePlayer).send();
    }
}
