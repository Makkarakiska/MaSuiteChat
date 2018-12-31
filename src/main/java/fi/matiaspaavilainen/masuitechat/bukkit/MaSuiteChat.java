package fi.matiaspaavilainen.masuitechat.bukkit;

import fi.matiaspaavilainen.masuitechat.bukkit.commands.Nick;
import fi.matiaspaavilainen.masuitechat.bukkit.commands.Reply;
import fi.matiaspaavilainen.masuitechat.bukkit.commands.ResetNick;
import fi.matiaspaavilainen.masuitechat.bukkit.commands.channels.*;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class MaSuiteChat extends JavaPlugin implements Listener {

    public Config config = new Config(this);
    private static Chat chat = null;

    @Override
    public void onEnable() {
        super.onEnable();
        getServer().getPluginManager().registerEvents(this, this);
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new ChatMessagingChannel(this));

        registerCommands();

        config.createConfigs();
        setupChat();
        if (chat == null) {
            System.out.println("[MaSuite] [Chat] Vault not found... Disabling...");
            getServer().getPluginManager().disablePlugin(this);
        }
    }


    private void registerCommands() {
        // Channels
        getCommand("staff").setExecutor(new Staff(this));
        getCommand("global").setExecutor(new Global(this));
        getCommand("server").setExecutor(new Server(this));
        getCommand("local").setExecutor(new Local(this));
        getCommand("tell").setExecutor(new Private(this));
        getCommand("reply").setExecutor(new Reply(this));

        // Nick
        getCommand("nick").setExecutor(new Nick(this));
        getCommand("resetnick").setExecutor(new ResetNick(this));

        // Mail
        //getCommand("mail").setExecutor(new Mail(this));
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onMessage(AsyncPlayerChatEvent e) {
        e.setCancelled(true);
        Player p = e.getPlayer();
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeUTF("MaSuiteChat");
            out.writeUTF("Chat");
            out.writeUTF(p.getUniqueId().toString());
            out.writeUTF(e.getMessage());
            getServer().getScheduler().runTaskAsynchronously(this, () -> p.sendPluginMessage(this, "BungeeCord", b.toByteArray()));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private boolean setupChat() {
        RegisteredServiceProvider<Chat> chatProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
        if (chatProvider != null) {
            chat = chatProvider.getProvider();
        }

        return (chat != null);
    }

    public Chat getChat() {
        return chat;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        getServer().getScheduler().runTaskLaterAsynchronously(this, () -> {
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(b);
            try {
                Player p = e.getPlayer();
                if (p == null) {
                    return;
                }
                out.writeUTF("MaSuiteChat");
                out.writeUTF("SetGroup");
                out.writeUTF(p.getUniqueId().toString());
                out.writeUTF(getPrefix(p));
                out.writeUTF(getSuffix(p));
                getServer().getScheduler().runTaskAsynchronously(this, () -> p.sendPluginMessage(this, "BungeeCord", b.toByteArray()));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }, 10);
    }

    private String getPrefix(Player p) {
        if (getChat() != null) {
            if (getChat().getPlayerPrefix(p) != null) {
                return getChat().getPlayerPrefix(p);
            } else if (getChat().getGroupPrefix(p.getWorld(), getChat().getPrimaryGroup(p)) != null) {
                return getChat().getGroupPrefix(p.getWorld(), getChat().getPrimaryGroup(p));
            }
            return "";
        }
        return "";
    }

    private String getSuffix(Player p) {
        if (getChat() != null) {
            if (getChat().getPlayerSuffix(p) != null) {
                return getChat().getPlayerSuffix(p);
            } else if (getChat().getGroupSuffix(p.getWorld(), getChat().getPrimaryGroup(p)) != null) {
                return getChat().getGroupSuffix(p.getWorld(), getChat().getPrimaryGroup(p));
            }
            return "";
        }
        return "";
    }
}
