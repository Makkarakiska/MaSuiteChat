package fi.matiaspaavilainen.masuitechat.bukkit;

import fi.matiaspaavilainen.masuitechat.bukkit.commands.*;
import fi.matiaspaavilainen.masuitechat.bukkit.commands.channels.*;
import fi.matiaspaavilainen.masuitechat.bukkit.events.AfkEvents;
import fi.matiaspaavilainen.masuitechat.bukkit.events.ChatEvent;
import fi.matiaspaavilainen.masuitechat.bukkit.events.JoinEvent;
import fi.matiaspaavilainen.masuitechat.bukkit.events.LeaveEvent;
import fi.matiaspaavilainen.masuitecore.bukkit.chat.Formator;
import fi.matiaspaavilainen.masuitecore.core.configuration.BukkitConfiguration;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MaSuiteChat extends JavaPlugin implements Listener {

    private static Chat chat = null;

    public BukkitConfiguration config = new BukkitConfiguration();
    public Formator formator = new Formator();
    public List<UUID> afkList = new ArrayList<>();

    @Override
    public void onEnable() {

        config.create(this, "chat", "syntax.yml");

        // Load listeners
        getServer().getPluginManager().registerEvents(new JoinEvent(this), this);
        getServer().getPluginManager().registerEvents(new LeaveEvent(this), this);
        getServer().getPluginManager().registerEvents(new ChatEvent(this), this);
        getServer().getPluginManager().registerEvents(new AfkEvents(this), this);
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new ChatMessagingChannel(this));

        registerCommands();

        setupChat();
        if (chat == null) {
            System.out.println("[MaSuite] [Chat] Vault not found... Disabling...");
            getServer().getPluginManager().disablePlugin(this);
        }

        config.addDefault("chat/syntax.yml", "ignore-channel", "&cCorrect syntax: /ignorechannel <global/server>");
    }


    private void registerCommands() {
        // Channels
        getCommand("staff").setExecutor(new Staff(this));
        getCommand("global").setExecutor(new Global(this));
        getCommand("server").setExecutor(new Server(this));
        getCommand("local").setExecutor(new Local(this));
        getCommand("tell").setExecutor(new Private(this));
        getCommand("reply").setExecutor(new Reply(this));
        getCommand("ignorechannel").setExecutor(new IgnoreChannel(this));
        // Nick
        getCommand("nick").setExecutor(new Nick(this));
        getCommand("resetnick").setExecutor(new ResetNick(this));

        // Mail
        getCommand("mail").setExecutor(new Mail(this));

        //Afk
        getCommand("afk").setExecutor(new Afk(this));
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
