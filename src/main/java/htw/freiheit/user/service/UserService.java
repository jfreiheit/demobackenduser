package htw.freiheit.user.service;

import htw.freiheit.user.model.User;
import htw.freiheit.user.repository.UserRepository;
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
        User user = new User(
                request.username(),
                request.email(),
                passwordEncoder.encode(request.password()),
                request.role());
        return userRepository.save(user);
    }
}
