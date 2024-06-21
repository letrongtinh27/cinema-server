package com.edu.hcmuaf.springserver.controller;

import com.edu.hcmuaf.springserver.auth.AuthenticationResponse;
import com.edu.hcmuaf.springserver.auth.RegisterAdminRequest;
import com.edu.hcmuaf.springserver.dto.request.UserRequest;
import com.edu.hcmuaf.springserver.dto.response.UserResponse;
import com.edu.hcmuaf.springserver.entity.User;
import com.edu.hcmuaf.springserver.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@RestController
@RequestMapping("api/users")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {
    @Autowired
    private UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(Authentication authentication) {
        logger.info("Fetching profile for user...");

        if (authentication == null) {
            logger.warn("Unauthorized access to profile endpoint");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        Object principal = authentication.getPrincipal();
        String username;

        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else if (principal instanceof OAuth2User) {
            username = ((OAuth2User) principal).getAttribute("email"); // assuming email is the username
        } else if (principal instanceof String) {
            username = principal.toString();
        } else {
            logger.error("Unexpected principal type encountered");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected principal type");
        }

        User user = userService.getUserProfileByUsername(username);
        if (user == null) {
            logger.warn("User profile not found for username: {}", username);
            return ResponseEntity.notFound().build();
        }

        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setUsername(user.getUsername());
        userResponse.setEmail(user.getEmail());
        userResponse.setPhone(user.getPhone_number());
        userResponse.setFullName(user.getFull_name());
        userResponse.setGender(user.getGender());
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        userResponse.setBirthday(sdf.format(user.getBirthday()));
        userResponse.setRole(user.getRole());

        logger.info("User profile retrieved successfully for username: {}", username);
        return ResponseEntity.ok(userResponse);
    }


//    @GetMapping("/all")
//    public ResponseEntity<?> getListUser() {
//        List<User> listUser = userService.getListUser();
//        if (listUser != null) {
//            return ResponseEntity.ok(listUser);
//        } else return ResponseEntity.badRequest().body(null);
//    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable int id) {
        logger.info("Fetching user by id: {}", id);

        User user = userService.findUserById(id);
        if (user != null) {
            logger.info("User found with id: {}", id);
            return ResponseEntity.ok(user);
        } else {
            logger.warn("User not found with id: {}", id);
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/edit")
    public ResponseEntity<?> updateUser(@RequestBody UserRequest.EditUser userRequest, Authentication authentication) throws ParseException {
        logger.info("Updating user profile...");

        try {
            AuthenticationResponse update = userService.updateUser(userRequest);
            if (update != null && update.getCode() == 200) {
                logger.info("User profile updated successfully for user: {}", userRequest.getUsername());
                return getProfile(authentication);
            } else {
                logger.warn("User with username {} not found. Update failed.", userRequest.getUsername());
                return ResponseEntity.badRequest().body(update);
            }
        } catch (ParseException e) {
            logger.error("Error parsing date while updating user profile", e);
            return ResponseEntity.badRequest().body("Error parsing date");
        }
    }

    @PostMapping ("/admin_create")
    public ResponseEntity<?> createUser(@RequestBody RegisterAdminRequest adminRequest) throws ParseException {
        logger.info("Creating new admin user...");

        AuthenticationResponse authenticationResponse = userService.createUser(adminRequest);
        if (authenticationResponse != null) {
            logger.info("Admin user {} created successfully", adminRequest.getUsername());
            return ResponseEntity.ok(authenticationResponse);
        } else {
            logger.warn("Failed to create admin user {}", adminRequest.getUsername());
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable long id) {
        logger.info("Deleting user with id: {}", id);

        userService.deleteById(id);
        logger.info("User deleted successfully with id: {}", id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<User>> getAllShowTime(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "{}") String filter,
                                                     @RequestParam(defaultValue = "16") int perPage,
                                                     @RequestParam(defaultValue = "title") String sort,
                                                     @RequestParam(defaultValue = "DESC") String order) {
        Page<User> users = userService.getAllwithSort(filter, page, perPage, sort, order);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsersByRole(@RequestParam(defaultValue = "{}") String filter) {
        logger.info("Fetching all users by role with filter: {}", filter);

        List<User> users = userService.getAllUsersByRole(filter);
        logger.info("Found {} users matching the filter", users.size());
        return ResponseEntity.ok(users);
    }
}
