package fi.matiaspaavilainen.masuitechat.bungee.objects;

import fi.matiaspaavilainen.masuitechat.bungee.MaSuiteChat;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.query.QueryOptions;
import net.md_5.bungee.api.ProxyServer;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class Group {

    private UUID uuid;
    private String prefix;
    private String suffix;

    /**
     * Initialize empty group
     */
    public Group() {
        this.uuid = null;
        this.prefix = "";
        this.suffix = "";
    }

    /**
     * Constructor for group info
     *
     * @param uuid   user's uuid
     * @param prefix user's prefix
     * @param suffix user's suffix
     */
    public Group(UUID uuid, String prefix, String suffix) {
        this.uuid = uuid;
        this.prefix = prefix;
        this.suffix = suffix;
    }

    /**
     * Loads user group info from LuckPerms or HashMap (Vault support)
     *
     * @param uuid user's uuid
     * @return user's group info
     */
    public Group get(UUID uuid) {
        if (MaSuiteChat.luckPermsApi) {
            LuckPerms api = LuckPermsProvider.get();
            if (ProxyServer.getInstance().getPlayer(uuid) == null) {
                api.getUserManager().loadUser(uuid);
            }
            User user = null;
            try {
                user = api.getUserManager().loadUser(uuid).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            if (user != null) {
                this.uuid = uuid;
                this.prefix = user.getCachedData().getMetaData(QueryOptions.defaultContextualOptions()).getPrefix();
                this.suffix = user.getCachedData().getMetaData(QueryOptions.defaultContextualOptions()).getSuffix();
                return this;
            }
        } else {
            return MaSuiteChat.groups.get(uuid);
        }
        return this;
    }

    /**
     * @return user's uuid
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     * @param uuid user's uuid
     */
    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    /**
     * @return user's prefix
     */
    public String getPrefix() {
        return prefix != null ? prefix : "";
    }

    /**
     * @param prefix user's prefix
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix != null ? prefix : "";
    }

    /**
     * @return user's suffix
     */
    public String getSuffix() {
        return suffix != null ? suffix : "";
    }

    /**
     * @param suffix user's suffix
     */
    public void setSuffix(String suffix) {
        this.suffix = suffix != null ? suffix : "";
    }


}
