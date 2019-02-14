package fi.matiaspaavilainen.masuitechat.bukkit.events;

import fi.matiaspaavilainen.masuitechat.bukkit.MaSuiteChat;
import fi.matiaspaavilainen.masuitecore.core.channels.BukkitPluginChannel;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.UUID;

public class AfkEvents implements Listener {

    private MaSuiteChat plugin;

    public AfkEvents(MaSuiteChat plugin) {
        this.plugin = plugin;
        afkDetector();
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        stopAFK(event.getPlayer());
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        stopAFK(event.getPlayer());
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        stopAFK(event.getPlayer());
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if (plugin.afkList.contains(event.getPlayer().getUniqueId()) && !event.getMessage().contains("/afk")) {
            new BukkitPluginChannel(plugin, event.getPlayer(), new Object[]{"MaSuiteChat", "Afk", event.getPlayer().getUniqueId().toString(), false}).send();
            plugin.afkList.remove(event.getPlayer().getUniqueId());
        }
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            if (plugin.afkList.contains(event.getDamager().getUniqueId())) {
                new BukkitPluginChannel(plugin, (Player) event.getDamager(), new Object[]{"MaSuiteChat", "Afk", event.getDamager().getUniqueId().toString(), false}).send();
                plugin.afkList.remove(event.getDamager().getUniqueId());
            }
        }

    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        stopAFK(event.getPlayer());
    }

    private void stopAFK(Player player) {
        if (plugin.afkList.contains(player.getUniqueId())) {
            new BukkitPluginChannel(plugin, player, new Object[]{"MaSuiteChat", "Afk", player.getUniqueId().toString(), false}).send();
            plugin.afkList.remove(player.getUniqueId());
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (plugin.afkList.contains(event.getPlayer().getUniqueId())) {
            new BukkitPluginChannel(plugin, event.getPlayer(), new Object[]{"MaSuiteChat", "Afk", event.getPlayer().getUniqueId().toString(), false}).send();
        }
    }

    @EventHandler
    public void onQuit(AsyncPlayerChatEvent event) {
        plugin.afkList.remove(event.getPlayer().getUniqueId());
        plugin.locations.remove(event.getPlayer().getUniqueId());
    }

    private void afkDetector() {
        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            for (Player player : plugin.getServer().getOnlinePlayers()) {
                plugin.locations.put(player.getUniqueId(), player.getLocation());
            }
            plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, () -> {
                for (UUID uuid : plugin.locations.keySet()) {
                    Player player = plugin.getServer().getPlayer(uuid);
                    if (player != null) {
                        if (plugin.locations.get(uuid).equals(player.getLocation())) {
                            if (!plugin.afkList.contains(uuid)) {
                                new BukkitPluginChannel(plugin, player, new Object[]{"MaSuiteChat", "Afk", uuid.toString(), true}).send();
                                plugin.afkList.add(uuid);
                            }
                        }
                    }
                }
            }, 20 * 2);
        }, 20 * 5, 20 * 300);
    }
}
