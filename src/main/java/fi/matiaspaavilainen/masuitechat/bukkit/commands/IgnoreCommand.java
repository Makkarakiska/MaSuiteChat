package fi.matiaspaavilainen.masuitechat.bukkit.commands;

import fi.matiaspaavilainen.masuitechat.bukkit.MaSuiteChat;
import fi.matiaspaavilainen.masuitecore.acf.BaseCommand;
import fi.matiaspaavilainen.masuitecore.acf.annotation.CommandAlias;
import fi.matiaspaavilainen.masuitecore.acf.annotation.CommandPermission;
import fi.matiaspaavilainen.masuitecore.acf.annotation.Description;
import fi.matiaspaavilainen.masuitecore.core.channels.BukkitPluginChannel;
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