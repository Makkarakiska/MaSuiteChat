package dev.masa.masuitechat.core.models;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@NoArgsConstructor
@RequiredArgsConstructor
@Data
@Table(name = "masuite_mail")
public class Mail {

    @DatabaseField(generatedId = true)
    private int id;

    @NonNull
    @DatabaseField(dataType = DataType.UUID)
    private UUID sender;

    @NonNull
    @DatabaseField(dataType = DataType.UUID)
    private UUID receiver;

    @NonNull
    @DatabaseField
    private String message;

    @NonNull
    @DatabaseField
    private Boolean seen = false;

    @NonNull
    @DatabaseField(dataType = DataType.LONG)
    private Long timestamp;

    public boolean isSeen() {
        return this.seen;
    }
}
