package dev.masa.masuitechat.bukkit;

import dev.masa.masuitechat.bukkit.commands.*;
import dev.masa.masuitechat.bukkit.events.AfkEvents;
import dev.masa.masuitechat.bukkit.events.ChatEvent;
import dev.masa.masuitechat.bukkit.events.JoinEvent;
import dev.masa.masuitechat.bukkit.events.LeaveEvent;
import dev.masa.masuitecore.acf.PaperCommandManager;
import dev.masa.masuitecore.bukkit.chat.Formator;
import dev.masa.masuitecore.core.configuration.BukkitConfiguration;
import dev.masa.masuitecore.core.utils.CommandManagerUtil;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class MaSuiteChat extends JavaPlugin implements Listener {

    private static Chat chat = null;

    public BukkitConfiguration config = new BukkitConfiguration();
    public List<UUID> afkList = new ArrayList<>();

    public HashMap<UUID, Location> locations = new HashMap<>();

    @Override
    public void onEnable() {
        config.create(this, "chat", "config.yml");

        // Load listeners
        getServer().getPluginManager().registerEvents(new JoinEvent(this), this);
        getServer().getPluginManager().registerEvents(new LeaveEvent(this), this);
        if (config.load("chat", "config.yml").getBoolean("afk-listener")) {
            getServer().getPluginManager().registerEvents(new AfkEvents(this), this);
        }
        getServer().getPluginManager().registerEvents(new ChatEvent(this), this);

        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new ChatMessagingChannel(this));

        registerCommands();


        setupChat();
        if (chat == null) {
            System.out.println("[MaSuite] [Chat] Vault chat hook not found (Does your permission plugin support Vault?)... Disabling...");
            getServer().getPluginManager().disablePlugin(this);
        }
    }


    private void registerCommands() {
        PaperCommandManager manager = new PaperCommandManager(this);
        manager.registerCommand(new AfkCommand(this));
        manager.registerCommand(new ChannelCommands(this));
        manager.registerCommand(new IgnoreCommand(this));
        manager.registerCommand(new NickCommand(this));
        manager.registerCommand(new PrivateMessageCommand(this));
        manager.registerCommand(new ReplyCommand(this));
        manager.registerCommand(new MailCommand(this));
        CommandManagerUtil.registerMaSuitePlayerCommandCompletion(manager);
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


    public String getPrefix(Player p) {
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

    public String getSuffix(Player p) {
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
