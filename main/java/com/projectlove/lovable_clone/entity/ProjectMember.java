package com.projectlove.lovable_clone.entity;

import com.projectlove.lovable_clone.enums.ProjectMemberRole;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "project_member")
public class ProjectMember {
    @EmbeddedId
     ProjectMemberId projectMemberId;

    @Enumerated(EnumType.STRING)
     @Column(nullable = false)
     ProjectMemberRole projectMemberRole;


    @ManyToOne
    @MapsId("userId")
     User user;

     @ManyToOne
     @MapsId("projectId")
     Project project;

     Instant invitedAt;
     Instant acceptedAt;

}
