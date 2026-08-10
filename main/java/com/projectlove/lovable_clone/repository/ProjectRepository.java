package com.projectlove.lovable_clone.repository;

import com.projectlove.lovable_clone.dto.projects.ProjectResponse;
import com.projectlove.lovable_clone.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project,Long> {

    @Query("""
        SELECT p from Project p
        WHERE p.deletedAt is NULL
        ORDER BY p.updatedAt DESC
""")
    List<Project> findAllAccessibleByUser(@Param("userId")Long userId);

@Query("""
   SELECT p from Project p 
   WHERE p.id=:id
     AND p.deletedAt is NULL
""")
Optional<Project> findAllAccessibleByUserId(@Param("id") Long id, @Param("userId") Long userId);
}
