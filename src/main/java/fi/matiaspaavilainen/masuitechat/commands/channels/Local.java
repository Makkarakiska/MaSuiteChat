package fi.matiaspaavilainen.masuitechat.commands.channels;

import com.google.common.base.Joiner;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fi.matiaspaavilainen.masuitechat.MaSuiteChat;
import fi.matiaspaavilainen.masuitecore.chat.Formator;
import fi.matiaspaavilainen.masuitecore.config.Configuration;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Local extends Command {
    public Local() {
        super("localchat", "masuitechat.channel.local", "local", "l", "localchannel");
    }

    @Override
    public void execute(CommandSender cs, String[] args) {
        if(!(cs instanceof ProxiedPlayer)){
            return;
        }
        ProxiedPlayer p = (ProxiedPlayer) cs;
        Formator formator = new Formator();
        Configuration config = new Configuration();
        if(args.length == 0){
            MaSuiteChat.players.put(p.getUniqueId(), "local");
            formator.sendMessage(p, config.load("chat", "messages.yml").getString("channel-changed.local"));
        }else{
            String server = p.getServer().getInfo().getName().toLowerCase();
            int range = config.load("chat", "chat.yml").getInt("channels." + server + ".localRadius");
            ByteArrayDataOutput output = ByteStreams.newDataOutput();
            output.writeUTF("LocalChat");
            output.writeUTF(fi.matiaspaavilainen.masuitechat.channels.Local.sendMessage(p, Joiner.on(" ").join(args)));
            output.writeInt(range);
            p.getServer().sendData("BungeeCord", output.toByteArray());
        }
    }
}
