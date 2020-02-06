package fi.matiaspaavilainen.masuitechat.core.services;

import fi.matiaspaavilainen.masuitechat.bungee.MaSuiteChat;
import fi.matiaspaavilainen.masuitechat.core.models.Mail;
import fi.matiaspaavilainen.masuitecore.core.utils.HibernateUtil;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class MailService {

    private EntityManager entityManager = HibernateUtil.addClasses(Mail.class).getEntityManager();
    public HashMap<UUID, List<Mail>> mails = new HashMap<>();

    private MaSuiteChat plugin;

    public MailService(MaSuiteChat plugin) {
        this.plugin = plugin;
    }

    /**
     * Sends a new {@link Mail}
     *
     * @param mail mail to send
     */
    public Mail sendMail(Mail mail) {
        entityManager.getTransaction().begin();
        entityManager.persist(mail);
        entityManager.getTransaction().commit();
        mails.get(mail.getReceiver()).add(mail);

        return mail;
    }


    /**
     * Mark {@link Mail} as read
     */
    public Mail markAsRead(Mail mail) {
        mail.setSeen(true);
        entityManager.getTransaction().begin();
        entityManager.merge(mail);
        entityManager.getTransaction().commit();

        // Remove home from list and add new back
        List<Mail> homeList = mails.get(mail.getReceiver()).stream().filter(cachedMail -> cachedMail.getId() != mail.getId()).collect(Collectors.toList());
        homeList.add(mail);
        mails.put(mail.getReceiver(), homeList);
        return mail;
    }

    /**
     * Remove mail
     *
     * @param mail mail to remove
     */
    public void removeMail(Mail mail) {
        entityManager.getTransaction().begin();
        entityManager.remove(mail);
        entityManager.getTransaction().commit();

        // Update cache
        mails.put(mail.getReceiver(), mails.get(mail.getReceiver()).stream().filter(cachedMail -> cachedMail.getId() != mail.getId()).collect(Collectors.toList()));
    }

    /**
     * Gets list of player's mails
     *
     * @param uuid owner of mailbox
     * @return returns a list of mails
     */
    public List<Mail> getMails(UUID uuid) {
        if (mails.containsKey(uuid)) {
            return mails.get(uuid);
        }

        List<Mail> mailList = entityManager.createQuery(
                "SELECT m FROM Mail m WHERE m.receiver = :receiver AND m.seen = 0 ORDER BY m.timestamp", Mail.class)
                .setParameter("receiver", uuid).getResultList();
        mails.put(uuid, mailList);

        return mailList;
    }

    /**
     * Initializes mailbox for user
     *
     * @param uuid uuid of the owner
     */
    public void initializeMailbox(UUID uuid) {
        List<Mail> mailList = entityManager.createQuery(
                "SELECT m FROM Mail m WHERE m.receiver = :receiver AND m.seen = 0 ORDER BY m.timestamp", Mail.class)
                .setParameter("receiver", uuid).getResultList();
        mails.put(uuid, mailList);
    }
}
