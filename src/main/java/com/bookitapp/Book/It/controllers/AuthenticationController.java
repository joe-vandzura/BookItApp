package com.bookitapp.Book.It.controllers;

import com.bookitapp.Book.It.models.EmailDetails;
import com.bookitapp.Book.It.models.Token;
import com.bookitapp.Book.It.models.User;
import com.bookitapp.Book.It.repositories.TokenRepository;
import com.bookitapp.Book.It.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;


@Controller
@RequiredArgsConstructor
public class AuthenticationController {

    private final UserRepository userRepo;
    private final EmailController emailController;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepo;

    @GetMapping("/login")
    String loginPage() {
        return "login";
    }

    @GetMapping(value = "/username-check/{usernameInput}", produces = "application/json")
    @ResponseBody
    public boolean checkIfUsernameExists(@PathVariable(name = "usernameInput") String usernameInput) {
        boolean usernameExists = userRepo.findByUsername(usernameInput) != null;
        return usernameExists;
    }

    @GetMapping(value = "/email-check/{emailInput}", produces = "application/json")
    @ResponseBody
    public boolean checkIfEmailAlreadyUsed(@PathVariable(name = "emailInput") String emailInput) {
        boolean emailBeingUsed = userRepo.findUserByEmail(emailInput) != null;
        return emailBeingUsed;
    }

    @GetMapping("/forgot-password")
    String showForgotPasswordPage() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    void submitForgotPasswordForm(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String email = request.get("email");
        User user = userRepo.findByUsername(username);

        if (user != null && user.getEmail().equals(email)) {
            user.getEmail();
            EmailDetails details = new EmailDetails();
            details.setRecipient(user.getEmail());
            details.setUserId((user.getId()));
            String securityToken = UUID.randomUUID().toString();
            Token token = new Token();
            token.setUser(user);
            token.setToken(securityToken);
            token.setTimestamp(LocalDateTime.now());
            tokenRepo.save(token);
            details.setSecurityToken(securityToken);
            details.setSubject("Reset Your Password");
            emailController.sendResetPasswordEmail(details);
        }
    }

    @GetMapping("/reset-password/{userId}")
    String showResetPasswordPage(
            Model model,
            @PathVariable(name = "userId") Long userId,
            @RequestParam(name = "secToken") String securityTokenString) {
        Token token = tokenRepo.findByToken(securityTokenString);
        if (token == null) {
            return "/error"; // need to add custom error page
        } else if (token.getTimestamp().isBefore(LocalDateTime.now().minusMinutes(15))) {
            tokenRepo.delete(token);
            return "link-expired";
        }
        model.addAttribute("userId", userId);
        return "reset-password";
    }

    @PostMapping("/reset-password")
    String resetPassword(
            @RequestParam(name = "user-id") Long userId,
            @RequestParam(name = "password") String password,
            @RequestParam(name = "confirm-password") String confirmPassword,
            @RequestParam(name = "is-logged-in") @Nullable Boolean isLoggedIn) {

        if (password.equals(confirmPassword)) {
            User user = userRepo.findById(userId).get();
            String hash = passwordEncoder.encode(password);
            user.setPassword(hash);
            userRepo.save(user);
            if (isLoggedIn != null && isLoggedIn == true) {
                return "redirect:/my-profile/account?saved";
            }
        }
        return "redirect:/login?passwordreset";
    }

}
