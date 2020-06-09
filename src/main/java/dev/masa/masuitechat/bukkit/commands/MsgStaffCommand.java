package dev.masa.masuitechat.bukkit.commands;

import dev.masa.masuitechat.bukkit.MaSuiteChat;
import dev.masa.masuitecore.acf.BaseCommand;
import dev.masa.masuitecore.acf.annotation.CommandAlias;
import dev.masa.masuitecore.acf.annotation.CommandPermission;
import dev.masa.masuitecore.acf.annotation.Description;
import dev.masa.masuitecore.core.channels.BukkitPluginChannel;
import org.bukkit.entity.Player;

public class MsgStaffCommand extends BaseCommand {

    private MaSuiteChat plugin;

    public MsgStaffCommand(MaSuiteChat plugin) {
        this.plugin = plugin;
    }

    @CommandAlias("msgstaff|helpop")
    @CommandPermission("masuitechat.msgstaff")
    @Description("Messages all staff with the permission 'masuitechat.msgstaff.receive'")
    public void msgStaffCommand(Player player, String message) {
        new BukkitPluginChannel(plugin, player, "MaSuiteChat", "MsgStaff", player.getUniqueId().toString(), message).send();
    }
}
