package fi.matiaspaavilainen.masuitechat.managers;

import fi.matiaspaavilainen.masuitechat.MaSuiteChat;
import fi.matiaspaavilainen.masuitecore.config.Configuration;

public class ConfigManager {

    public static void getActions() {
        net.md_5.bungee.config.Configuration config = null;
        try {
            config = new Configuration().load("chat","actions.yml");
            MaSuiteChat.staffActions.addAll(config.getStringList("staff"));
            MaSuiteChat.playerActions.addAll(config.getStringList("player"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
