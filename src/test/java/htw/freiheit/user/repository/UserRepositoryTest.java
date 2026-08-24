package htw.freiheit.user.repository;

import htw.freiheit.user.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void create() {
        User user = new User("jane", "jane@example.com", "secret123", "USER");

        User saved = userRepository.save(user);

        assertThat(saved.getId()).isNotNull();
    }

    @Test
    void read() {
        userRepository.save(new User("john", "john@example.com", "secret123", "USER"));

        Optional<User> found = userRepository.findByUsername("john");

        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("john@example.com");
    }

    @Test
    void update() {
        User saved = userRepository.save(new User("mary", "mary@example.com", "secret123", "USER"));

        saved.setEmail("mary.new@example.com");
        userRepository.save(saved);

        Optional<User> updated = userRepository.findById(saved.getId());
        assertThat(updated).isPresent();
        assertThat(updated.get().getEmail()).isEqualTo("mary.new@example.com");
    }

    @Test
    void delete() {
        User saved = userRepository.save(new User("peter", "peter@example.com", "secret123", "USER"));

        userRepository.deleteById(saved.getId());

        assertThat(userRepository.findById(saved.getId())).isEmpty();
    }
}
