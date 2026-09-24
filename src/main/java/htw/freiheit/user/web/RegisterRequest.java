package htw.freiheit.user.web;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(
        @NotBlank(message = "Nutzername ist erforderlich") String username,
        @NotBlank(message = "E-Mail ist erforderlich")
        @Email(message = "Bitte eine gültige E-Mail-Adresse angeben") String email,
        @NotBlank(message = "Passwort ist erforderlich") String password,
        @NotBlank(message = "Rolle ist erforderlich") String role
) {
}
