package ch.fincons.library;

import ch.fincons.library.entities.User;
import ch.fincons.library.repository.ArticoliRepository;
import ch.fincons.library.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableCaching
@ServletComponentScan
@AllArgsConstructor
@Slf4j
public class Application implements CommandLineRunner {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		User firstUser = new User();
		firstUser.setUsername("antonio");
		firstUser.setEmail("antonio@gmail.com");
		firstUser.setPassword(passwordEncoder.encode("123Stella"));
		firstUser.setRoles("USER");

		log.info("Saving first user::: {}", userRepository.save(firstUser));

		User secondUser = new User();
		secondUser.setUsername("marco");
		secondUser.setEmail("marco@gmail.com");
		secondUser.setPassword(passwordEncoder.encode("123Stella"));
		secondUser.setRoles("ADMIN");

		log.info("Saving second user::: {}", userRepository.save(secondUser));
	}
}
