package fi.matiaspaavilainen.masuitechat.bukkit;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Config {

    private MaSuiteChat plugin;

    public Config(MaSuiteChat p) {
        plugin = p;
    }

    private FileConfiguration messages;
    private FileConfiguration syntaxes;

    public FileConfiguration getMessages() {
        return this.messages;
    }

    public FileConfiguration getSyntaxes() {
        return this.syntaxes;
    }

    public void createConfigs() {
        File messageFile = new File(plugin.getDataFolder(), "messages.yml");
        File syntaxFile = new File(plugin.getDataFolder(), "syntax.yml");
        if (!messageFile.exists()) {
            messageFile.getParentFile().mkdirs();
            plugin.saveResource("messages.yml", false);
        }
        if (!syntaxFile.exists()) {
            syntaxFile.getParentFile().mkdirs();
            plugin.saveResource("syntax.yml", false);
        }
        messages = new YamlConfiguration();
        syntaxes = new YamlConfiguration();
        try {
            messages.load(messageFile);
            syntaxes.load(syntaxFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
}
