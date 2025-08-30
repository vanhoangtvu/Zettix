package net.zettix.accountcenter.controller;

import jakarta.validation.Valid;
import net.zettix.accountcenter.dto.request.UserUpdateRequest;
import net.zettix.accountcenter.dto.response.ApiResponse;
import net.zettix.accountcenter.dto.request.UserCreationRequest;
import net.zettix.accountcenter.dto.response.UserResponse;
import net.zettix.accountcenter.entity.User;
import net.zettix.accountcenter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    ApiResponse<User> createUser(@RequestBody @Valid UserCreationRequest request){
        ApiResponse<User> apiResponse =new ApiResponse<>();
        apiResponse.setResult(userService.createUser(request));
        return apiResponse;
    }

    @GetMapping
    List<User> getUsers(){
        return userService.getUser();
    }

    @GetMapping("/{id}")
    UserResponse getUSer(@PathVariable String id){
        return userService.getUser(id);
    }

    @PutMapping("/{id}")
    public UserResponse updateUser(@PathVariable String id,@RequestBody UserUpdateRequest request){
        return userService.updateUser(id, request);
    }

    @DeleteMapping("/{id}")
    String deleteUser(@PathVariable String id){
        userService.deleteUser(id);
        return "deleted";
    }


}
