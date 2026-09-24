package htw.freiheit.user.web;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "Nutzername oder E-Mail ist erforderlich") String usernameOrEmail,
        @NotBlank(message = "Passwort ist erforderlich") String password
) {
}
