package htw.freiheit.user.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
class UserControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void registerWithValidDataCreatesUser() {
        RegisterRequest request = new RegisterRequest("erika", "erika@htw-berlin.de", "Sicher123", "user");

        ResponseEntity<UserResponse> response = restTemplate.postForEntity("/register", request, UserResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().username()).isEqualTo("erika");
        assertThat(response.getBody().email()).isEqualTo("erika@htw-berlin.de");
    }

    @Test
    void registerWithMissingUsernameReturnsBadRequest() {
        RegisterRequest request = new RegisterRequest("", "ohne-namen@htw-berlin.de", "Sicher123", "user");

        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity("/register", request, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void registerWithDuplicateUsernameReturnsConflict() {
        RegisterRequest first = new RegisterRequest("doppelt", "doppelt1@htw-berlin.de", "Sicher123", "user");
        RegisterRequest second = new RegisterRequest("doppelt", "doppelt2@htw-berlin.de", "Sicher123", "user");
        restTemplate.postForEntity("/register", first, UserResponse.class);

        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity("/register", second, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void registerWithDuplicateEmailReturnsConflict() {
        RegisterRequest first = new RegisterRequest("nutzerin1", "gleiche@htw-berlin.de", "Sicher123", "user");
        RegisterRequest second = new RegisterRequest("nutzerin2", "gleiche@htw-berlin.de", "Sicher123", "user");
        restTemplate.postForEntity("/register", first, UserResponse.class);

        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity("/register", second, ErrorResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }
}
