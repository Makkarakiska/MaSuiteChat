package dev.masa.masuitechat.core.services;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.table.TableUtils;
import dev.masa.masuitechat.bungee.MaSuiteChat;
import dev.masa.masuitechat.core.models.Mail;
import lombok.Getter;
import lombok.SneakyThrows;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class MailService {

    @Getter
    private HashMap<UUID, List<Mail>> mails = new HashMap<>();
    private Dao<Mail, Integer> mailDao;

    private MaSuiteChat plugin;

    @SneakyThrows
    public MailService(MaSuiteChat plugin) {
        this.plugin = plugin;
        this.mailDao = DaoManager.createDao(plugin.getApi().getDatabaseService().getConnection(), Mail.class);
        TableUtils.createTableIfNotExists(plugin.getApi().getDatabaseService().getConnection(), Mail.class);
    }

    /**
     * Sends a new {@link Mail}
     *
     * @param mail mail to send
     */
    @SneakyThrows
    public Mail sendMail(Mail mail) {
        mailDao.create(mail);
        mails.get(mail.getReceiver()).add(mail);

        return mail;
    }


    /**
     * Mark {@link Mail} as read
     */
    @SneakyThrows
    public Mail markAsRead(Mail mail) {
        mail.setSeen(true);
        mailDao.update(mail);
        // Remove home from list and add new back
        List<Mail> mailList = mails.get(mail.getReceiver()).stream().filter(cachedMail -> cachedMail.getId() != mail.getId()).collect(Collectors.toList());
        mailList.add(mail);
        mails.put(mail.getReceiver(), mailList);
        return mail;
    }

    /**
     * Remove mail
     *
     * @param mail mail to remove
     */
    @SneakyThrows
    public void removeMail(Mail mail) {
        mailDao.delete(mail);
        // Update cache
        mails.put(mail.getReceiver(), mails.get(mail.getReceiver()).stream().filter(cachedMail -> cachedMail.getId() != mail.getId()).collect(Collectors.toList()));
    }

    /**
     * Gets list of player's mails
     *
     * @param uuid owner of mailbox
     * @return returns a list of mails
     */
    @SneakyThrows
    public List<Mail> getMails(UUID uuid) {
        if (mails.containsKey(uuid)) {
            return mails.get(uuid);
        }


        List<Mail> mailList =  mailDao.queryBuilder().orderBy("timestamp", true).where().in("receiver", uuid).and().in("seen", 0).query();
        mails.put(uuid, mailList);

        return mailList;
    }

    /**
     * Initializes mailbox for user
     *
     * @param uuid uuid of the owner
     */
    @SneakyThrows
    public void initializeMailbox(UUID uuid) {
        List<Mail> mailList = mailDao.queryBuilder().orderBy("timestamp", true).where().in("receiver", uuid).and().in("seen", 0).query();
        mails.put(uuid, mailList);
    }
}
