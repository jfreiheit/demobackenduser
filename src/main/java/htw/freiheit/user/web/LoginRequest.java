package htw.freiheit.user.web;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @Schema(example = "erika") @NotBlank(message = "Nutzername oder E-Mail ist erforderlich") String usernameOrEmail,
        @Schema(example = "einSicheresPasswort1") @NotBlank(message = "Passwort ist erforderlich") String password
) {
}
