package fantastic.cms.services;

import fantastic.cms.constant.UserType;
import fantastic.cms.repositories.CategoryRepository;
import fantastic.cms.repositories.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import fantastic.cms.models.User;
import fantastic.cms.models.VerificationToken;
import fantastic.cms.repositories.UserRepository;
import fantastic.cms.repositories.VerificationTokenRepository;
import fantastic.cms.requests.UserRequest;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final VerificationTokenRepository tokenRepository;
    private final EmailService emailService;
    private final BCryptPasswordEncoder passwordEncoder;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final CategoryRepository categoryRepository;
    private final NewsRepository newsRepository;

    @Autowired
    public UserService(UserRepository userRepository, VerificationTokenRepository tokenRepository, EmailService emailService, BCryptPasswordEncoder passwordEncoder, CategoryRepository categoryRepository, NewsRepository newsRepository) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.categoryRepository = categoryRepository;
        this.newsRepository = newsRepository;
    }

    public User update(String id, UserRequest userRequest) {
        User user = this.userRepository.findById(id).orElseThrow();
        user.setName(userRequest.getName());
        return this.userRepository.save(user);
    }

    public User create(UserRequest userRequest) {
        User user = new User();
        user.setName(userRequest.getName());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setEnabled(false);
        user.setType(UserType.STANDARD_USER);
        logger.info("Creating user with username: {}", userRequest.getName());
        return this.userRepository.save(user);
    }

    public void delete(String id) {
        this.userRepository.deleteById(id);
    }

    public List<User> findAll() {
        return this.userRepository.findAll();
    }

    public User findOne(String id) {
        return this.userRepository.findById(id).orElseThrow();
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }


    public User getCurrentAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username);
    }

    @Transactional
    public void registerNewUser(User user) {
        // Log the registration attempt for better debugging
        logger.info("Attempting to register new user: {}", user.getUsername());

        // Check if username or email already exists
        if (userRepository.existsByUsername(user.getUsername())) {
            logger.error("Username already exists: {}", user.getUsername());
            throw new IllegalArgumentException("Username already exists");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            logger.error("Email already exists: {}", user.getEmail());
            throw new IllegalArgumentException("Email already exists");
        }

        // Encrypt password and save user
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setType(UserType.STANDARD_USER);
        userRepository.save(user);

        logger.info("User successfully registered: {}", user.getUsername());

        // Create verification token
        String token = UUID.randomUUID().toString();
        createVerificationToken(user, token);

        // Send verification email (uncomment during testing)
//        String recipientAddress = user.getEmail();
//        String subject = "Registration Confirmation";
//        String confirmationUrl = "http://localhost:8080/confirm?token=" + token;
//        String message = "Please click the following link to verify your email address: " + confirmationUrl;
//        emailService.sendVerificationEmail(recipientAddress, subject, message);
    }

    private void createVerificationToken(User user, String token) {
        VerificationToken verificationToken = new VerificationToken(token, user, calculateExpiryDate(24 * 60));
        tokenRepository.save(verificationToken);
        logger.info("Verification token created for user: {}", user.getUsername());
    }

    @Transactional
    public void delete(String userId, String currentPrincipalName) {
        User user = userRepository.findById(userId).orElseThrow();
        var type = userRepository.findByUsername(currentPrincipalName).getType();
        String currentPrincipalId = userRepository.findByUsername(currentPrincipalName).getId();
        boolean isAdmin = type == UserType.ADMIN;
        boolean isAuthor = (type == UserType.STANDARD_USER || type == UserType.MODERATOR) && user.getId().equals(currentPrincipalId);
        if (isAdmin || isAuthor) {
            var categories = categoryRepository.findByAuthorId(currentPrincipalId);
            for (var category: categories){
                category.setAuthor(null);
            }
            categoryRepository.saveAllAndFlush(categories);
            newsRepository.deleteAll(newsRepository.findByAuthorId(currentPrincipalId));
            userRepository.delete(user);
        } else {
            throw new AccessDeniedException("Użytkownik nie posiada uprawnień do usunięcia użytkownika");
        }
    }


    @Transactional
    public boolean verifyUser(String token) {
        VerificationToken verificationToken = tokenRepository.findByToken(token);
        if (verificationToken == null || verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            logger.error("Invalid or expired token: {}", token);
            return false;
        }

        User user = verificationToken.getUser();
        user.setEnabled(true);
        userRepository.save(user);

        logger.info("User verified successfully: {}", user.getUsername());

        tokenRepository.delete(verificationToken);

        return true;
    }

    private Date calculateExpiryDate(int expiryTimeInMinutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(cal.getTime().getTime());
    }

    public boolean usernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }
}
