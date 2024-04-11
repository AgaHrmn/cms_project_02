package fantastic.cms.resources;

import fantastic.cms.models.User;
import fantastic.cms.requests.UserRequest;
import fantastic.cms.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@Tag(name = "user", description = "User API")
public class UserResource {
    private final UserService userService;

    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/{id}")
    @Operation(summary = "Find user by id", description = "Find user by ID")
    @ApiResponse(responseCode = "404", description = "User not found")
    @ApiResponse(responseCode = "200", description = "User found")
    public ResponseEntity<User> findOne(@PathVariable("id") String id) {
        var user = userService.findOne(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping
    @Operation(summary = "List", description = "List all users")
    @ApiResponse(responseCode = "404", description = "Users not found")
    @ApiResponse(responseCode = "200", description = "Users found")
    public ResponseEntity<List<User>> findAll() {
        var users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    @PostMapping
    @Operation(summary = "Create", description = "Create a new user")
    @ApiResponse(responseCode = "400", description = "invalid request")
    @ApiResponse(responseCode = "201", description = "User created successfully")
    public ResponseEntity<User> newUser(UserRequest userRequest) {
        var user = userService.create(userRequest);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user by id", description = "Delete user by id")
    @ApiResponse(responseCode = "404", description = "User not found")
    @ApiResponse(responseCode = "200", description = "User deleted successfully")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeUser(@PathVariable("id") String id) {
        userService.delete(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user by id", description = "Update user by id")
    @ApiResponse(responseCode = "400", description = "Invalid request")
    @ApiResponse(responseCode = "404", description = "User not found")
    @ApiResponse(responseCode = "200", description = "User deleted successfully")
    public ResponseEntity<User> updateUser(@PathVariable("id") String id, UserRequest userRequest) {
        var user = userService.update(id, userRequest);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}


