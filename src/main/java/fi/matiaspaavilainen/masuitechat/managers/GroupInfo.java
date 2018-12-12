package fi.matiaspaavilainen.masuitechat.managers;

import fi.matiaspaavilainen.masuitechat.MaSuiteChat;
import me.lucko.luckperms.api.Contexts;
import me.lucko.luckperms.api.User;
import net.alpenblock.bungeeperms.BungeePerms;

import java.util.UUID;

public class GroupInfo {

    private String prefix;
    private String suffix;

    public GroupInfo(UUID uuid) {
        if (MaSuiteChat.luckPermsApi != null) {
            User user = MaSuiteChat.luckPermsApi.getUser(uuid);
            if (user != null) {
                setPrefix(user.getCachedData().getMetaData(Contexts.allowAll()).getPrefix());
                setSuffix(user.getCachedData().getMetaData(Contexts.allowAll()).getSuffix());
            }
        }
        if (MaSuiteChat.bungeePermsApi != null) {
            net.alpenblock.bungeeperms.User user = BungeePerms.getInstance().getPermissionsManager().getUser(uuid);
            if (user != null) {
                setPrefix(user.getPrefix());
                setSuffix(user.getSuffix());
            }
        }
    }

    public String getPrefix() {
        return prefix != null ? prefix : "";
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix != null ? prefix : "";
    }

    public String getSuffix() {
        return suffix != null ? suffix : "";
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix != null ? suffix : "";
    }
}
