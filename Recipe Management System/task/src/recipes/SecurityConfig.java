package recipes;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // Disable CSRF for API endpoints
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/actuator/shutdown", "/api/register").permitAll()
                        .requestMatchers("/api/recipe/new", "/api/recipe/{id}", "/api/recipe/search/**").authenticated()
                        .requestMatchers("/api/recipe/{id}/delete", "/api/recipe/{id}/update").hasRole("USER") // Fix ROLE_USER issue
                        .anyRequest().authenticated()
                )
                .httpBasic(basic -> {});  // Enable HTTP Basic Authentication

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.withUsername("user@example.com")
                .password(passwordEncoder().encode("password"))  // Use bcrypt encoded password
                .roles("USER")  // Assign USER role
                .build();

        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
