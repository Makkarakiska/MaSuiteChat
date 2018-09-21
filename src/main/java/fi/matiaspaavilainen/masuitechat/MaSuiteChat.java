package fi.matiaspaavilainen.masuitechat;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fi.matiaspaavilainen.masuitechat.channels.*;
import fi.matiaspaavilainen.masuitechat.commands.ChatActions;
import fi.matiaspaavilainen.masuitechat.commands.Message;
import fi.matiaspaavilainen.masuitechat.commands.Reply;
import fi.matiaspaavilainen.masuitechat.managers.ConfigManager;
import fi.matiaspaavilainen.masuitechat.managers.ServerManager;
import fi.matiaspaavilainen.masuitecore.Updator;
import fi.matiaspaavilainen.masuitecore.config.Configuration;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class MaSuiteChat extends Plugin implements Listener {

    public static List<String> playerActions = new ArrayList<>();
    public static List<String> staffActions = new ArrayList<>();
    public static HashMap<UUID, String> players = new HashMap<>();
    public static HashMap<String, Channel> channels = new HashMap<>();

    @Override
    public void onEnable() {
        super.onEnable();
        Configuration config = new Configuration();
        getProxy().getPluginManager().registerListener(this, this);

        // Create configs
        config.create(this, "chat", "actions.yml");
        config.create(this, "chat", "aliases.yml");
        config.create(this, "chat", "messages.yml");
        config.create(this, "chat", "chat.yml");
        config.create(this, "chat", "syntax.yml");

        // Chat Actions for messages
        getProxy().getPluginManager().registerCommand(this, new ChatActions());

        // Private messaging
        getProxy().getPluginManager().registerCommand(this, new Message(config.load("chat", "aliases.yml").getStringList("channels.private").toArray(new String[0])));
        getProxy().getPluginManager().registerCommand(this, new Reply(config.load("chat", "aliases.yml").getStringList("channels.reply").toArray(new String[0])));


        //Commands
        getProxy().getPluginManager().registerCommand(this, new fi.matiaspaavilainen.masuitechat.commands.channels.Staff(config.load("chat", "aliases.yml").getStringList("channels.staff").toArray(new String[0])));
        getProxy().getPluginManager().registerCommand(this, new fi.matiaspaavilainen.masuitechat.commands.channels.Global(config.load("chat", "aliases.yml").getStringList("channels.global").toArray(new String[0])));
        getProxy().getPluginManager().registerCommand(this, new fi.matiaspaavilainen.masuitechat.commands.channels.Server(config.load("chat", "aliases.yml").getStringList("channels.server").toArray(new String[0])));
        getProxy().getPluginManager().registerCommand(this, new fi.matiaspaavilainen.masuitechat.commands.channels.Local(config.load("chat", "aliases.yml").getStringList("channels.local").toArray(new String[0])));


        // Load actions, servers and channels
        ConfigManager.getActions();
        ServerManager.loadServers();

        new Updator().checkVersion(this.getDescription(), "60039");
    }

    @EventHandler
    public void onJoin(PostLoginEvent e) {
        ProxiedPlayer p = e.getPlayer();
        // Add player to global channel on join
        players.put(p.getUniqueId(), "global");
    }

    @EventHandler
    public void onQuit(PlayerDisconnectEvent e) {
        ProxiedPlayer p = e.getPlayer();
        // Remove player from channels on leave
        players.remove(p.getUniqueId());
    }

    @Override
    public void onLoad() {
        super.onLoad();
    }

    @EventHandler
    public void onPluginMessage(PluginMessageEvent e) throws IOException {
        Configuration config = new Configuration();
        if (e.getTag().equals("BungeeCord")) {
            DataInputStream in = new DataInputStream(new ByteArrayInputStream(e.getData()));
            String subchannel = in.readUTF();
            if (subchannel.equals("MaSuiteChat")) {
                ProxiedPlayer p = ProxyServer.getInstance().getPlayer(in.readUTF());
                if (p == null) {
                    return;
                }
                switch (players.get(p.getUniqueId())) {
                    case ("global"):
                        Global.sendMessage(p, in.readUTF());
                        break;
                    case ("server"):
                        Server.sendMessage(p, in.readUTF());
                        break;
                    case ("staff"):
                        Staff.sendMessage(p, in.readUTF());
                        break;
                    case ("local"):
                        String msg = in.readUTF();

                        String server = p.getServer().getInfo().getName().toLowerCase();
                        int range = config.load("chat", "chat.yml").getInt("channels." + server + ".localRadius");

                        ByteArrayDataOutput output = ByteStreams.newDataOutput();
                        output.writeUTF("LocalChat");
                        output.writeUTF(Local.sendMessage(p, msg));
                        output.writeInt(range);
                        p.getServer().sendData("BungeeCord", output.toByteArray());
                        break;
                    default:
                        System.out.println("Player " + p.getName() + " does not have channel for some reason! Please relog!");
                        break;
                }
            }
        }
    }


}
