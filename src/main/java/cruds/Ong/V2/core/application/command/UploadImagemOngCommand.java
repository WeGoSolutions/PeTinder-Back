package cruds.Ong.V2.core.application.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class UploadImagemOngCommand {
    private UUID ongId;
    private byte[] imagemBytes;
}

