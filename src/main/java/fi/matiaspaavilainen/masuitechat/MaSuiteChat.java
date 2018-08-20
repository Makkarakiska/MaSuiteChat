package fi.matiaspaavilainen.masuitechat;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fi.matiaspaavilainen.masuitechat.channels.*;
import fi.matiaspaavilainen.masuitechat.commands.ChatActions;
import fi.matiaspaavilainen.masuitechat.commands.Reply;
import fi.matiaspaavilainen.masuitechat.managers.ConfigManager;
import fi.matiaspaavilainen.masuitechat.managers.ServerManager;
import fi.matiaspaavilainen.masuitecore.MaSuiteCore;
import fi.matiaspaavilainen.masuitecore.chat.Formator;
import fi.matiaspaavilainen.masuitecore.config.Configuration;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
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

    public static List<String> playerActions = new ArrayList<>();
    public static List<String> staffActions = new ArrayList<>();
    @Override
    public void onEnable() {
        Configuration config = new Configuration();
        super.onEnable();
        getProxy().getPluginManager().registerListener(this, this);
        getProxy().getPluginManager().registerCommand(this, new ChatActions());
        getProxy().getPluginManager().registerCommand(this, new Reply());
        config.create(this,"chat","actions.yml");
        config.create(this,"chat","messages.yml");
        config.create(this,null,"chat.yml");
        ConfigManager.getActions();
        ServerManager.loadServers();
    }

    @EventHandler
    public void onJoin(PostLoginEvent e){
        ProxiedPlayer p = e.getPlayer();
    }

    @EventHandler
    public void onQuit(PlayerDisconnectEvent e){
        ProxiedPlayer p = e.getPlayer();
    }

    @Override
    public void onLoad() {
        super.onLoad();
    }

    @EventHandler
    public void onPluginMessage(PluginMessageEvent e) throws IOException {
        Configuration config = new Configuration();
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
                ProxiedPlayer sender = ProxyServer.getInstance().getPlayer(in.readUTF());
                ProxiedPlayer receiver = ProxyServer.getInstance().getPlayer(in.readUTF());
                if (receiver != null) {
                    Private.sendMessage(
                            sender,
                            receiver,
                            in.readUTF()
                    );
                }else{
                    String offline = new Formator().colorize(config.load(null,"messages.yml").getString("player-not-online"));
                    sender.sendMessage(new TextComponent(offline));
                }

            }
            if(subchannel.equals("LocalChat")){

                String p = in.readUTF();
                String msg = in.readUTF();
                String px = in.readUTF();
                String sx = in.readUTF();

                ProxiedPlayer player = ProxyServer.getInstance().getPlayer(p);
                String server = player.getServer().getInfo().getName().toLowerCase();
                Integer range = config.load(null,"chat.yml").getInt("channels." + server + ".localRadius");

                ByteArrayDataOutput output = ByteStreams.newDataOutput();
                output.writeUTF("LocalChat");
                output.writeUTF(Local.sendMessage(player, msg, px, sx));
                output.writeUTF(String.valueOf(range));
                player.getServer().sendData("BungeeCord", output.toByteArray());
            }
        }
    }



}
