package fi.matiaspaavilainen.masuitechat.bukkit.commands;

import fi.matiaspaavilainen.masuitechat.bukkit.MaSuiteChat;
import fi.matiaspaavilainen.masuitecore.acf.BaseCommand;
import fi.matiaspaavilainen.masuitecore.acf.annotation.*;
import fi.matiaspaavilainen.masuitecore.core.channels.BukkitPluginChannel;
import org.bukkit.entity.Player;

public class ChannelCommands extends BaseCommand {

    private MaSuiteChat plugin;

    public ChannelCommands(MaSuiteChat plugin) {
        this.plugin = plugin;
    }

    @CommandAlias("g|global|globalchannel")
    @CommandPermission("masuitechat.channel.global")
    @Description("Send message or toggle global channel")
    public void globalChannelCommand(Player player, @Optional String message) {
        if (message == null) {
            new BukkitPluginChannel(plugin, player, "MaSuiteChat", "ToggleChannel", "global", player.getUniqueId().toString()).send();
            return;
        }
        new BukkitPluginChannel(plugin, player, "MaSuiteChat", "SendMessage", "global", player.getUniqueId().toString(), message).send();
    }

    @CommandAlias("s|serverchannel")
    @CommandPermission("masuitechat.channel.server")
    @Description("Send message or toggle server channel")
    public void serverChannelCommand(Player player, @Optional String message) {
        if (message == null) {
            new BukkitPluginChannel(plugin, player, "MaSuiteChat", "ToggleChannel", "server", player.getUniqueId().toString()).send();
            return;
        }
        new BukkitPluginChannel(plugin, player, "MaSuiteChat", "SendMessage", "server", player.getUniqueId().toString(), message).send();
    }

    @CommandAlias("l|local|localchannel")
    @CommandPermission("masuitechat.channel.local")
    @Description("Send message or toggle local channel")
    public void localChannelCommand(Player player, @Optional String message) {
        if (message == null) {
            new BukkitPluginChannel(plugin, player, "MaSuiteChat", "ToggleChannel", "local", player.getUniqueId().toString()).send();
            return;
        }
        new BukkitPluginChannel(plugin, player, "MaSuiteChat", "SendMessage", "local", player.getUniqueId().toString(), message).send();
    }

    @CommandAlias("a|ac|sc|adminchat|admin|staff")
    @CommandPermission("masuitechat.channel.staff")
    @Description("Send message or toggle staff channel")
    public void staffChannelCommand(Player player, @Optional String message) {
        if (message == null) {
            new BukkitPluginChannel(plugin, player, "MaSuiteChat", "ToggleChannel", "staff", player.getUniqueId().toString()).send();
            return;
        }
        new BukkitPluginChannel(plugin, player, "MaSuiteChat", "SendMessage", "staff", player.getUniqueId().toString(), message).send();
    }

    @CommandAlias("a|ac|sc|adminchat|admin|staff")
    @CommandPermission("masuitechat.ignore.channel")
    @Description("Ignore specific channel")
    @CommandCompletion("global|server")
    public void ignoreChannelCommand(Player player, String channel) {
        new BukkitPluginChannel(plugin, player, "MaSuiteChat", "IgnoreChannel", channel.toLowerCase(), player.getUniqueId().toString()).send();
    }
}
