package com.projectlove.lovable_clone.repository;

import com.projectlove.lovable_clone.entity.ProjectMember;
import com.projectlove.lovable_clone.entity.ProjectMemberId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, ProjectMemberId> {
}
