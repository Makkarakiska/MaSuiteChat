package fi.matiaspaavilainen.masuitechat.core.objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Channel {
    private String name;
    private String slug;
    private String permission;
    private String format;
}