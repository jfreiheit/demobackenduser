package htw.freiheit.user.web;

import htw.freiheit.user.security.JwtService;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
class UserLoginControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private JwtService jwtService;

    private void register(String username, String email, String password) {
        restTemplate.postForEntity(
                "/register",
                new RegisterRequest(username, email, password, "user"),
                UserResponse.class);
    }

    @Test
    void loginWithValidCredentialsReturnsValidToken() {
        register("fritz", "fritz@htw-berlin.de", "Sicher123");

        ResponseEntity<LoginResponse> response = restTemplate.postForEntity(
                "/login", new LoginRequest("fritz", "Sicher123"), LoginResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().username()).isEqualTo("fritz");

        Claims claims = jwtService.parseToken(response.getBody().token());
        assertThat(claims.getSubject()).isEqualTo("fritz");
        assertThat(claims.get("role", String.class)).isEqualTo("user");
    }

    @Test
    void loginWithEmailInsteadOfUsernameSucceeds() {
        register("greta", "greta@htw-berlin.de", "Sicher123");

        ResponseEntity<LoginResponse> response = restTemplate.postForEntity(
                "/login", new LoginRequest("greta@htw-berlin.de", "Sicher123"), LoginResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void loginWithWrongPasswordReturnsUnauthorized() {
        register("heinz", "heinz@htw-berlin.de", "Sicher123");

        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(
                "/login", new LoginRequest("heinz", "falsch"), ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void loginWithUnknownUserReturnsUnauthorized() {
        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(
                "/login", new LoginRequest("unbekannt", "irgendwas"), ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }
}
