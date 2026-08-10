package com.projectlove.lovable_clone.repository;

import com.projectlove.lovable_clone.entity.ProjectMember;
import com.projectlove.lovable_clone.entity.ProjectMemberId;
import lombok.Getter;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.ArrayList;
import java.util.List;


public interface ProjectMemberRepository extends JpaRepository<ProjectMember, ProjectMemberId> {




    List<ProjectMember> findByProjectId(Long projectId);


}
