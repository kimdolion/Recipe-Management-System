package recipes;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

    public void registerUser(String email, String password) {
        if (userRepository.findByEmail(email) != null) {
            throw new RuntimeException("AppUser already exists");
        }

        AppUser appUser = new AppUser();
        appUser.setEmail(email);
        appUser.setPassword(passwordEncoder.encode(password));
        userRepository.save(appUser);
    }

    public boolean isUserRegistered(String email) {
        return userRepository.findByEmail(email) != null;
    }
}
