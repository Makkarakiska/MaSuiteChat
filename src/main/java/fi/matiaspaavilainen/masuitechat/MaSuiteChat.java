package fi.matiaspaavilainen.masuitechat;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fi.matiaspaavilainen.masuitechat.channels.*;
import fi.matiaspaavilainen.masuitechat.commands.ChatActions;
import fi.matiaspaavilainen.masuitechat.commands.Message;
import fi.matiaspaavilainen.masuitechat.commands.Reply;
import fi.matiaspaavilainen.masuitechat.managers.ConfigManager;
import fi.matiaspaavilainen.masuitechat.managers.ServerManager;
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
        config.create(this, "chat", "messages.yml");
        config.create(this, "chat", "chat.yml");
        config.create(this, "chat", "syntax.yml");

        // Chat Actions for messages
        getProxy().getPluginManager().registerCommand(this, new ChatActions());

        // Private messaging
        getProxy().getPluginManager().registerCommand(this, new Message());
        getProxy().getPluginManager().registerCommand(this, new Reply());


        //Commannds
        getProxy().getPluginManager().registerCommand(this, new fi.matiaspaavilainen.masuitechat.commands.channels.Staff());
        getProxy().getPluginManager().registerCommand(this, new fi.matiaspaavilainen.masuitechat.commands.channels.Global());
        getProxy().getPluginManager().registerCommand(this, new fi.matiaspaavilainen.masuitechat.commands.channels.Server());
        getProxy().getPluginManager().registerCommand(this, new fi.matiaspaavilainen.masuitechat.commands.channels.Local());


        // Load actions, servers and channels
        ConfigManager.getActions();
        ServerManager.loadServers();
        loadChannels();
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
        loadChannels();
    }

    private void loadChannels(){
        Configuration config = new Configuration();
        channels.put("staff", new Channel("staff", "staff", "masuitechat.channel.staff", config.load("chat", "chat.yml").getString("formats.staff")));
        channels.put("global", new Channel("global", "global", "masuitechat.channel.global", config.load("chat", "chat.yml").getString("formats.global")));
        channels.put("server", new Channel("server", "server", "masuitechat.channel.server", config.load("chat", "chat.yml").getString("formats.server")));
        channels.put("local", new Channel("local", "local", "masuitechat.channel.global", config.load("chat", "chat.yml").getString("formats.local")));

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
                        String px = in.readUTF();
                        String sx = in.readUTF();

                        String server = p.getServer().getInfo().getName().toLowerCase();
                        Integer range = config.load(null, "chat.yml").getInt("channels." + server + ".localRadius");

                        ByteArrayDataOutput output = ByteStreams.newDataOutput();
                        output.writeUTF("LocalChat");
                        output.writeUTF(Local.sendMessage(p, msg));
                        output.writeUTF(String.valueOf(range));
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
