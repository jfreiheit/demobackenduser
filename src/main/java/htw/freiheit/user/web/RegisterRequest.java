package htw.freiheit.user.web;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(
        @Schema(example = "erika") @NotBlank(message = "Nutzername ist erforderlich") String username,
        @Schema(example = "erika@example.com") @NotBlank(message = "E-Mail ist erforderlich")
        @Email(message = "Bitte eine gültige E-Mail-Adresse angeben") String email,
        @Schema(example = "einSicheresPasswort1") @NotBlank(message = "Passwort ist erforderlich") String password,
        @Schema(example = "user") @NotBlank(message = "Rolle ist erforderlich") String role
) {
}
