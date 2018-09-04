package fi.matiaspaavilainen.masuitechat;

public class Channel {

    private String name;
    private String slug;
    private String permission;
    private String format;

    public Channel(){

    }

    public Channel(String name, String slug, String permission, String format) {
        this.name = name;
        this.slug = slug;
        this.permission = permission;
        this.format = format;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

}
