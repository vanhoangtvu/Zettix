package net.zettix.accountcenter.mapper;

import net.zettix.accountcenter.dto.request.UserCreationRequest;
import net.zettix.accountcenter.dto.request.UserUpdateRequest;
import net.zettix.accountcenter.dto.response.UserResponse;
import net.zettix.accountcenter.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest req);
    UserResponse toUserResponse(User user);

    // update in-place
    void updateUser(@MappingTarget User user, UserUpdateRequest req);
}