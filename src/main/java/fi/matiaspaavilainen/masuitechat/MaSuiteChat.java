package fi.matiaspaavilainen.masuitechat;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fi.matiaspaavilainen.masuitechat.channels.*;
import fi.matiaspaavilainen.masuitechat.commands.ChatActions;
import fi.matiaspaavilainen.masuitechat.managers.ConfigManager;
import fi.matiaspaavilainen.masuitechat.managers.ServerManager;
import fi.matiaspaavilainen.masuitecore.MaSuiteCore;
import fi.matiaspaavilainen.masuitecore.chat.Formator;
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
import java.util.List;

public class MaSuiteChat extends Plugin implements Listener {

    MaSuiteCore core = new MaSuiteCore();
    private List<String> onlinePlayers = new ArrayList<>();
    public static List<String> playerActions = new ArrayList<>();
    public static List<String> staffActions = new ArrayList<>();
    @Override
    public void onEnable() {
        Configuration config = new Configuration();
        super.onEnable();
        getProxy().getPluginManager().registerListener(this, this);
        getProxy().getPluginManager().registerCommand(this, new ChatActions());
        config.create(this,"actions.yml");
        config.create(this,"messages.yml");
        config.create(this,"chat.yml");
        ConfigManager.getActions();
        ServerManager.loadServers();
    }

    @EventHandler
    public void onJoin(PostLoginEvent e){
        ProxiedPlayer p = e.getPlayer();
        onlinePlayers.add(p.getName());
    }

    @EventHandler
    public void onQuit(PlayerDisconnectEvent e){
        ProxiedPlayer p = e.getPlayer();
        onlinePlayers.remove(p.getName());
    }

    @Override
    public void onLoad() {
        super.onLoad();
        for(ProxiedPlayer p : getProxy().getPlayers()){
            onlinePlayers.add(p.getName());
        }
    }

    @EventHandler
    public void onPluginMessage(PluginMessageEvent e) throws IOException {
        if(e.getTag().equals("BungeeCord")){
            DataInputStream in = new DataInputStream(new ByteArrayInputStream(e.getData()));
            String subchannel = in.readUTF();
            if(subchannel.equals("GlobalChat")){
                Global.sendMessage(ProxyServer.getInstance().getPlayer(in.readUTF()), in.readUTF(), in.readUTF(), in.readUTF());
            }
            if(subchannel.equals("ServerChat")){
                Server.sendMessage(ProxyServer.getInstance().getPlayer(in.readUTF()), in.readUTF(), in.readUTF(), in.readUTF());
            }
            if(subchannel.equals("AdminChat")){
                Admin.sendMessage(ProxyServer.getInstance().getPlayer(in.readUTF()), in.readUTF(), in.readUTF(), in.readUTF());
            }
            if(subchannel.equals("PrivateChat")){
                String sender = in.readUTF();
                String receiver = in.readUTF();
                if(onlinePlayers.contains(receiver)){
                    Private.sendMessage(
                            ProxyServer.getInstance().getPlayer(sender),
                            ProxyServer.getInstance().getPlayer(receiver),
                            in.readUTF()
                    );
                }else{
                    ProxyServer.getInstance().getPlayer(sender).sendMessage(new Formator().colorize(new Configuration().load("messages.yml").getString("player-not-online")));
                }

            }
            if(subchannel.equals("LocalChat")){

                String p = in.readUTF();
                String msg = in.readUTF();
                String px = in.readUTF();
                String sx = in.readUTF();
                Configuration config = new Configuration();

                ProxiedPlayer player = ProxyServer.getInstance().getPlayer(p);
                String server = player.getServer().getInfo().getName().toLowerCase();
                Integer range = config.load("chat.yml").getInt("channels." + server + ".localRadius");

                ByteArrayDataOutput output = ByteStreams.newDataOutput();
                output.writeUTF("LocalChat");
                output.writeUTF(Local.sendMessage(player, msg, px, sx));
                output.writeUTF(String.valueOf(range));
                player.getServer().sendData("BungeeCord", output.toByteArray());
            }
        }
    }



}
