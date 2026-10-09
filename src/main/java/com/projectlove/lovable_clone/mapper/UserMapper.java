package com.projectlove.lovable_clone.mapper;

import com.projectlove.lovable_clone.dto.auth.SignUpRequest;
import com.projectlove.lovable_clone.dto.auth.UserProfileResponse;
import com.projectlove.lovable_clone.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUserEntity(SignUpRequest signUpRequest);

    @Mapping(target = "userId", source = "id")
    UserProfileResponse toUserProfileResponse(User user);
}
