package fi.matiaspaavilainen.masuitechat;

import fi.matiaspaavilainen.masuitecore.database.Database;
import fi.matiaspaavilainen.masuitecore.MaSuiteCore;
import fi.matiaspaavilainen.masuitecore.config.Configuration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Mail {
    private Database db = MaSuiteCore.db;
    private Connection connection = null;
    private PreparedStatement statement = null;
    private Configuration config = new Configuration();
    private String tablePrefix = config.load(null, "config.yml").getString("database.table-prefix");

    // Info
    private UUID sender;
    private UUID receiver;
    private String message;
    private Boolean seen = false;
    private Long timestamp;

    public Mail() {
    }

    public Mail(UUID sender, UUID receiver, String message, Long timestamp) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.timestamp = timestamp;
    }

    public boolean send() {
        String insert = "INSERT INTO " + tablePrefix +
                "mail (sender, receiver, message, seen, timestamp) VALUES (?, ?, ?, ?, ?) ;";
        try {
            connection = db.hikari.getConnection();
            statement = connection.prepareStatement(insert);
            statement.setString(1, getSender().toString());
            statement.setString(2, getReceiver().toString());
            statement.setString(3, getMessage());
            statement.setBoolean(4, isSeen());
            statement.setLong(5, getTimestamp());
            statement.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public Set<Mail> list(UUID uuid) {
        Set<Mail> mails = new HashSet<>();
        ResultSet rs = null;

        try {
            connection = db.hikari.getConnection();
            statement = connection.prepareStatement("SELECT * FROM " + tablePrefix + "mail WHERE receiver = ?;");
            statement.setString(1, uuid.toString());
            rs = statement.executeQuery();
            while (rs.next()) {
                if (!rs.getBoolean("seen")) {
                    Mail mail = new Mail();
                    mail.setSender(UUID.fromString(rs.getString("sender")));
                    mail.setReceiver(UUID.fromString(rs.getString("receiver")));
                    mail.setMessage(rs.getString("message"));
                    mail.setTimestamp(rs.getLong("timestamp"));
                    mails.add(mail);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return mails;
    }

    public UUID getSender() {
        return sender;
    }

    public void setSender(UUID sender) {
        this.sender = sender;
    }

    public UUID getReceiver() {
        return receiver;
    }

    public void setReceiver(UUID receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean isSeen() {
        return seen;
    }

    public void setSeen(Boolean seen) {
        this.seen = seen;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
