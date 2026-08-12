package com.projectlove.lovable_clone.repository;

import com.projectlove.lovable_clone.entity.ProjectMember;
import com.projectlove.lovable_clone.entity.ProjectMemberId;
import com.projectlove.lovable_clone.enums.ProjectMemberRole;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;


public interface ProjectMemberRepository extends JpaRepository<ProjectMember, ProjectMemberId> {




    List<ProjectMember> findByProjectId(Long projectId);


    @Query("""
            SELECT pm.projectMemberRole FROM ProjectMember pm
            WHERE pm.projectMemberId.projectId=:projectId AND pm.projectMemberId.userId=:userId
            """)
    Optional<ProjectMemberRole> findRoleByProjectIdAndUserId(@Param("projectId") Long projectId,@Param("userId") Long userId);
}
