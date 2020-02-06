package dev.masa.masuitechat.bungee;

import dev.masa.masuitechat.core.controllers.MailController;
import dev.masa.masuitechat.core.services.MailService;
import dev.masa.masuitechat.bungee.channels.*;
import dev.masa.masuitechat.bungee.events.JoinEvent;
import dev.masa.masuitechat.bungee.events.LeaveEvent;
import dev.masa.masuitechat.bungee.events.SwitchEvent;
import dev.masa.masuitechat.bungee.managers.ServerManager;
import dev.masa.masuitechat.bungee.objects.Group;
import dev.masa.masuitecore.bungee.Utils;
import dev.masa.masuitecore.bungee.chat.Formator;
import dev.masa.masuitecore.core.Updator;
import dev.masa.masuitecore.core.api.MaSuiteCoreAPI;
import dev.masa.masuitecore.core.configuration.BungeeConfiguration;
import dev.masa.masuitecore.core.models.MaSuitePlayer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class MaSuiteChat extends Plugin implements Listener {

    public static HashMap<UUID, String> players = new HashMap<>();
    public static HashMap<UUID, Set<UUID>> ignores = new HashMap<>();
    public static HashMap<UUID, Group> groups = new HashMap<>();
    public static boolean luckPermsApi = false;
    public Formator formator = new Formator();

    public Utils utils = new Utils();

    public BungeeConfiguration config = new BungeeConfiguration();

    public MaSuiteCoreAPI api = new MaSuiteCoreAPI();

    public MailService mailService;

    @Override
    public void onEnable() {
        getProxy().getPluginManager().registerListener(this, this);

        // Create configs
        config.create(this, "chat", "messages.yml");
        config.create(this, "chat", "chat.yml");

        mailService = new MailService(this);

        // Load actions, servers and channels
        ServerManager.loadServers();

        new Updator(getDescription().getVersion(), getDescription().getName(), "60039").checkUpdates();
        if (getProxy().getPluginManager().getPlugin("LuckPerms") != null) {
            luckPermsApi = true;
        }

        config.addDefault("chat/messages.yml", "ignore-channel.ignore", "&cYou are now ignoring that channel!");
        config.addDefault("chat/messages.yml", "ignore-channel.unignore", "&aYou are now seeing that channel again!");

        config.addDefault("chat/messages.yml", "switch-message.enabled", true);
        config.addDefault("chat/messages.yml", "switch-message.message", "&9%player% &7has moved to &9%server%&7!");

        config.addDefault("chat/messages.yml", "connection-message.enabled", true);
        config.addDefault("chat/messages.yml", "connection-message.join", "&7[&a+&7] &9%player% &7joined!");
        config.addDefault("chat/messages.yml", "connection-message.left", "&7[&c-&7] &9%player% &7left!");

        config.addDefault("chat/messages.yml", "motd.enabled", true);
        config.addDefault("chat/messages.yml", "motd.message", "&7Welcome to our server, &9%player%&7!");

        config.addDefault("chat/messages.yml", "first-join.enabled", true);
        config.addDefault("chat/messages.yml", "first-join.message", "9%player%&7 has joined for the first time!");

        config.addDefault("chat/messages.yml", "afk.on", "&9%player% &7is now afk.");
        config.addDefault("chat/messages.yml", "afk.off", "&9%player% &7is no longer afk.");

        config.addDefault("chat/messages.yml", "ignore.on", "&aYou are now ignoring %player%.");
        config.addDefault("chat/messages.yml", "ignore.off", "&cYou are not ignoring %player% anymore.");

        getProxy().getPluginManager().registerListener(this, new SwitchEvent(this));
        getProxy().getPluginManager().registerListener(this, new LeaveEvent(this));
        getProxy().getPluginManager().registerListener(this, new JoinEvent(this));
    }

    @Override
    public void onLoad() {
        getProxy().getPlayers().forEach(p -> {
            String channel = config.load("chat", "chat.yml")
                    .getString("channels." + p.getServer().getInfo().getName().toLowerCase() + ".defaultChannel");
            players.put(p.getUniqueId(), channel);
            formator.sendMessage(p, config.load("chat", "messages.yml").getString("channel-changed." + channel));
        });
    }

    private void chatLog(String channel, String sender, String message) {
        System.out.println("[Chat] [" + channel + "] " + sender + " > " + message);
    }

    @EventHandler
    public void onPluginMessage(PluginMessageEvent e) throws IOException {
        BungeeConfiguration config = new BungeeConfiguration();
        Local localChannel = new Local(this);
        Private privateChannel = new Private();
        if (e.getTag().equals("BungeeCord")) {
            DataInputStream in = new DataInputStream(new ByteArrayInputStream(e.getData()));
            String subchannel = in.readUTF();
            if (subchannel.equals("MaSuiteChat")) {
                String childchannel = in.readUTF();
                if (childchannel.equals("Chat")) {
                    ProxiedPlayer p = getProxy().getPlayer(UUID.fromString(in.readUTF()));
                    if (p == null) {
                        return;
                    }
                    if (players.containsKey(p.getUniqueId())) {
                        String msg = in.readUTF();
                        switch (players.get(p.getUniqueId())) {
                            case ("staff"):
                                Staff.sendMessage(p, msg);
                                chatLog("STAFF", p.getName(), msg);
                                break;
                            case ("global"):
                                Global.sendMessage(p, msg);
                                chatLog("GLOBAL", p.getName(), msg);
                                break;
                            case ("server"):
                                Server.sendMessage(p, msg);
                                chatLog("SERVER", p.getName(), msg);
                                break;
                            case ("local"):
                                localChannel.send(p, msg);
                                chatLog("LOCAL", p.getName(), msg);
                                break;
                        }
                    }
                }
                if (childchannel.equals("ToggleChannel")) {
                    String channel = in.readUTF();
                    ProxiedPlayer p = getProxy().getPlayer(UUID.fromString(in.readUTF()));
                    if (p != null) {
                        switch (channel) {
                            case ("staff"):
                                players.put(p.getUniqueId(), "staff");
                                formator.sendMessage(p, config.load("chat", "messages.yml").getString("channel-changed.staff"));
                                break;
                            case ("global"):
                                players.put(p.getUniqueId(), "global");
                                formator.sendMessage(p, config.load("chat", "messages.yml").getString("channel-changed.global"));
                                break;
                            case ("server"):
                                players.put(p.getUniqueId(), "server");
                                formator.sendMessage(p, config.load("chat", "messages.yml").getString("channel-changed.server"));
                                break;
                            case ("local"):
                                players.put(p.getUniqueId(), "local");
                                formator.sendMessage(p, config.load("chat", "messages.yml").getString("channel-changed.local"));
                                break;
                        }
                    }

                }
                if (childchannel.equals("SendMessage")) {
                    String channel = in.readUTF();
                    ProxiedPlayer p = getProxy().getPlayer(UUID.fromString(in.readUTF()));
                    String value = in.readUTF();
                    if (p != null) {
                        switch (channel) {
                            case ("staff"):
                                Staff.sendMessage(p, value);
                                chatLog("STAFF", p.getName(), value);
                                break;
                            case ("global"):
                                Global.sendMessage(p, value);
                                chatLog("GLOBAL", p.getName(), value);
                                break;
                            case ("server"):
                                Server.sendMessage(p, value);
                                chatLog("SERVER", p.getName(), value);
                                break;
                            case ("local"):
                                localChannel.send(p, value);
                                chatLog("LOCAL", p.getName(), value);
                                break;
                            case ("private"):
                                ProxiedPlayer receiver = getProxy().getPlayer(value);
                                if (utils.isOnline(receiver, p)) {
                                    privateChannel.sendMessage(p, receiver, in.readUTF());
                                }
                                break;
                            case ("reply"):
                                if (Private.conversations.containsKey(p.getUniqueId())) {
                                    ProxiedPlayer r = getProxy().getPlayer(Private.conversations.get(p.getUniqueId()));
                                    if (utils.isOnline(r, p)) {
                                        privateChannel.sendMessage(p, r, value);
                                    }
                                }
                                break;
                        }
                    }

                }
                if (childchannel.equals("Mail")) {
                    String superchildchannel = in.readUTF();
                    MailController mailController = new MailController(this);
                    switch (superchildchannel) {
                        case ("Send"):
                            mailController.sendMail(in.readUTF(), in.readUTF(), in.readUTF());
                            break;
                        case ("SendAll"):
                            mailController.sendAll(in.readUTF(), in.readUTF());
                            break;
                        case ("Read"):
                            mailController.read(in.readUTF());
                            break;
                    }

                }

                if (childchannel.equals("Nick")) {
                    ProxiedPlayer sender = getProxy().getPlayer(UUID.fromString(in.readUTF()));
                    String nick = in.readUTF();
                    if (utils.isOnline(sender)) {
                        sender.setDisplayName(nick);
                        MaSuitePlayer msp = api.getPlayerService().getPlayer(sender.getUniqueId());
                        msp.setNickname(nick);
                        api.getPlayerService().updatePlayer(msp);
                        formator.sendMessage(sender, config.load("chat", "messages.yml").getString("nickname-changed").replace("%nickname%", nick));
                    }
                }

                if (childchannel.equals("NickOther")) {
                    ProxiedPlayer sender = getProxy().getPlayer(UUID.fromString(in.readUTF()));
                    ProxiedPlayer target = getProxy().getPlayer(in.readUTF());
                    String nick = in.readUTF();
                    if (utils.isOnline(target, sender)) {
                        target.setDisplayName(nick);
                        MaSuitePlayer msp = api.getPlayerService().getPlayer(target.getUniqueId());
                        msp.setNickname(nick);
                        api.getPlayerService().updatePlayer(msp);
                        formator.sendMessage(sender, config.load("chat", "messages.yml").getString("nickname-changed").replace("%nickname%", nick));
                    }

                }
                if (childchannel.equals("ResetNick")) {
                    ProxiedPlayer sender = getProxy().getPlayer(UUID.fromString(in.readUTF()));
                    if (utils.isOnline(sender)) {
                        updateNick(config, sender);
                    }
                }

                if (childchannel.equals("ResetNickOther")) {
                    ProxiedPlayer sender = getProxy().getPlayer(UUID.fromString(in.readUTF()));
                    ProxiedPlayer target = getProxy().getPlayer(in.readUTF());
                    if (utils.isOnline(target, sender)) {
                        updateNick(config, target);
                    }
                }

                if (childchannel.equals("SetGroup")) {
                    UUID uuid = UUID.fromString(in.readUTF());
                    groups.put(uuid, new Group(uuid, in.readUTF(), in.readUTF()));
                }

                if (childchannel.equals("IgnoreChannel")) {
                    String c = in.readUTF();
                    ProxiedPlayer p = getProxy().getPlayer(UUID.fromString(in.readUTF()));
                    if (utils.isOnline(p)) {
                        if (c.equals("global")) {
                            if (Global.ignoredChannels.contains(p.getUniqueId())) {
                                Global.ignoredChannels.remove(p.getUniqueId());
                                formator.sendMessage(p, config.load("chat", "messages.yml").getString("ignore-channel.unignore"));
                            } else {
                                Global.ignoredChannels.add(p.getUniqueId());
                                formator.sendMessage(p, config.load("chat", "messages.yml").getString("ignore-channel.ignore"));
                            }
                        } else if (c.equals("server")) {
                            if (Server.ignoredChannels.contains(p.getUniqueId())) {
                                Server.ignoredChannels.remove(p.getUniqueId());
                                formator.sendMessage(p, config.load("chat", "messages.yml").getString("ignore-channel.unignore"));
                            } else {
                                Server.ignoredChannels.add(p.getUniqueId());
                                formator.sendMessage(p, config.load("chat", "messages.yml").getString("ignore-channel.ignore"));
                            }
                        }
                    }
                }

                if (childchannel.equals("Afk")) {
                    ProxiedPlayer p = getProxy().getPlayer(UUID.fromString(in.readUTF()));
                    boolean status = in.readBoolean();
                    if (utils.isOnline(p)) {
                        if (status) {
                            utils.broadcast(config.load("chat", "messages.yml").getString("afk.on").replace("%player%", p.getName()));
                        } else {
                            utils.broadcast(config.load("chat", "messages.yml").getString("afk.off").replace("%player%", p.getName()));
                        }
                    }
                }

                if (childchannel.equals("IgnorePlayer")) {
                    ProxiedPlayer sender = getProxy().getPlayer(UUID.fromString(in.readUTF()));
                    ProxiedPlayer target = getProxy().getPlayer(in.readUTF());
                    if (utils.isOnline(target, sender)) {
                        if (ignores.containsKey(sender.getUniqueId())) {
                            ignores.get(sender.getUniqueId()).remove(target.getUniqueId());
                            formator.sendMessage(sender, config.load("chat", "messages.yml").getString("ignore.off").replace("%player%", target.getName()));
                        } else {
                            if (!ignores.containsKey(sender.getUniqueId())) {
                                ignores.put(sender.getUniqueId(), new HashSet<>());
                            }
                            ignores.get(sender.getUniqueId()).add(target.getUniqueId());
                            formator.sendMessage(sender, config.load("chat", "messages.yml").getString("ignore.on").replace("%player%", target.getName()));
                        }
                    }
                }
            }
        }
    }

    private void updateNick(BungeeConfiguration config, ProxiedPlayer target) {
        target.setDisplayName(target.getName());
        MaSuitePlayer msp = api.getPlayerService().getPlayer(target.getUniqueId());
        msp.setNickname(null);
        api.getPlayerService().updatePlayer(msp);
        formator.sendMessage(target, config.load("chat", "messages.yml").getString("nickname-changed").replace("%nickname%", target.getName()));
    }
}
