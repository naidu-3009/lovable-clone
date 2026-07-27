package com.projectlove.lovable_clone.entity;

import com.projectlove.lovable_clone.enums.ProjectMemberRole;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class ProjectMember {
     Preview.ProjectMemberId projectMemberId;
     ProjectMemberRole projectMemberRole;
     User user;
     Project project;

     Instant invitedAt;
     Instant acceptedAt;

}
