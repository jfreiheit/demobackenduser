package htw.freiheit.user.web;

import htw.freiheit.user.model.User;
import htw.freiheit.user.security.JwtService;
import htw.freiheit.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    public UserController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @Operation(summary = "Neuen Nutzer registrieren",
            description = "Legt einen neuen User an. Das Passwort wird vor dem Speichern mit BCrypt gehasht.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Registrierung erfolgreich"),
            @ApiResponse(responseCode = "400", description = "Ungültige Eingabe (Pflichtfeld fehlt oder ungültige E-Mail)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Nutzername oder E-Mail bereits vergeben",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        User user = userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.from(user));
    }

    @Operation(summary = "Anmelden und JWT erhalten",
            description = "Prüft Nutzername/E-Mail und Passwort gegen die gehashten Zugangsdaten und liefert bei Erfolg ein signiertes JWT.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Anmeldung erfolgreich, JWT im Response-Body"),
            @ApiResponse(responseCode = "401", description = "Nutzername/E-Mail oder Passwort ist falsch",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        User user = userService.authenticate(request);
        String token = jwtService.generateToken(user);
        return ResponseEntity.ok(new LoginResponse(token, user.getUsername(), user.getRole()));
    }
}
