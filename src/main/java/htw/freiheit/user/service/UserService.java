package htw.freiheit.user.service;

import htw.freiheit.user.model.User;
import htw.freiheit.user.repository.UserRepository;
import htw.freiheit.user.web.LoginRequest;
import htw.freiheit.user.web.RegisterRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new DuplicateUserException("Nutzername ist bereits vergeben");
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new DuplicateUserException("E-Mail-Adresse ist bereits registriert");
        }

        User user = new User(
                request.username(),
                request.email(),
                passwordEncoder.encode(request.password()),
                request.role());
        return userRepository.save(user);
    }

    public User authenticate(LoginRequest request) {
        User user = userRepository.findByUsernameOrEmail(request.usernameOrEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Nutzername/E-Mail oder Passwort ist falsch"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new InvalidCredentialsException("Nutzername/E-Mail oder Passwort ist falsch");
        }

        return user;
    }
}
