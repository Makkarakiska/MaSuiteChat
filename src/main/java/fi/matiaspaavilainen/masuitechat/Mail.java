package fi.matiaspaavilainen.masuitechat;

import fi.matiaspaavilainen.masuitechat.database.Database;
import fi.matiaspaavilainen.masuitecore.config.Configuration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class Mail {
    private Database db = MaSuiteChat.db;
    private Connection connection = null;
    private PreparedStatement statement = null;
    private Configuration config = new Configuration();
    private String tablePrefix = config.load(null, "config.yml").getString("database.table-prefix");

    // Info
    private UUID sender;
    private UUID receiver;
    private String message;
    private Boolean seen;
    private Long timestamp;

    public Mail() {}

    public Mail(UUID sender, UUID receiver, String message, Long timestamp) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.timestamp = timestamp;
    }

    public void send(Mail mail){
        String insert = "INSERT INTO " + tablePrefix +
                "mail (sender, receiver, message, read, timestamp) VALUES (?, ?, ?, ?, ?) ;";
        try {
            connection = db.hikari.getConnection();
            statement = connection.prepareStatement(insert);
            statement.setString(1, mail.getSender().toString());
            statement.setString(2, mail.getReceiver().toString());
            statement.setString(3, mail.getMessage());
            statement.setBoolean(4, mail.isSeen());
            statement.setLong(5, mail.getTimestamp());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
            if(statement != null){
                try {
                    statement.close();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        }
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
