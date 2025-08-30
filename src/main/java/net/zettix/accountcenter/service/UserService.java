package net.zettix.accountcenter.service;

import jakarta.validation.Valid;
import net.zettix.accountcenter.dto.request.UserCreationRequest;
import net.zettix.accountcenter.dto.request.UserUpdateRequest;
import net.zettix.accountcenter.dto.response.UserResponse;
import net.zettix.accountcenter.entity.User;
import net.zettix.accountcenter.mapper.UserMapper;
import net.zettix.accountcenter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;

    public User createUser(UserCreationRequest request){
        if(userRepository.existsByUsername(request.getUsername()))
            throw new RuntimeException("user exitst try again");
        else if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("email exitst try again");
        }
        User user = userMapper.toUser(request);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        return userRepository.save(user);
    }

    public UserResponse updateUser(String id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // gọi đúng thứ tự tham số: entity trước, request sau
        userMapper.updateUser(user, request);

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public List<User> getUser(){
        return userRepository.findAll();
    }

    public UserResponse getUser(String id){
        return userMapper.toUserResponse(userRepository.findById(id)
                .orElseThrow(()->new RuntimeException("User not found")));
    }

    public void deleteUser(String id){
        userRepository.deleteById(id);
    }


}