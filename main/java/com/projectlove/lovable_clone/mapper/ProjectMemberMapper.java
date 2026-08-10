package com.projectlove.lovable_clone.mapper;


import com.projectlove.lovable_clone.dto.member.MemberResponse;
import com.projectlove.lovable_clone.entity.ProjectMember;
import com.projectlove.lovable_clone.entity.User;
import com.projectlove.lovable_clone.enums.ProjectMemberRole;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.boot.json.JsonWriter;


@Mapper(componentModel = "spring")
public interface ProjectMemberMapper {

    @Mapping(source = "id",target = "userId")
    @Mapping(target="projectMemberRole",constant = "OWNER")
    MemberResponse  toMemberResponseFromOwner(User owner);
    @Mapping(source = "user.id",target = "userId")
    @Mapping(source = "user.username",target = "username")
    @Mapping(source = "user.name",target = "name")
    @Mapping(source = "project.id",target = "projectId")
    @Mapping(source = "projectMemberRole",target = "projectMemberRole")
    MemberResponse toMemberResponseFromMember(ProjectMember member);



}
