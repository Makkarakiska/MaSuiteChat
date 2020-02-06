package fi.matiaspaavilainen.masuitechat.core.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@NoArgsConstructor
@RequiredArgsConstructor
@Data
@Entity
@Table(name = "masuite_mail")
public class Mail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NonNull
    @Column(name = "sender")
    @Type(type = "uuid-char")
    private UUID sender;

    @NonNull
    @Column(name = "receiver")
    @Type(type = "uuid-char")
    private UUID receiver;

    @NonNull
    @Column(name = "message")
    private String message;

    @NonNull
    @Column(name = "seen")
    private Boolean seen = false;

    @NonNull
    @Column(name = "timestamp")
    private Long timestamp;

    public boolean isSeen() {
        return this.seen;
    }
}
