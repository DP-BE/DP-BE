package com.jambit.project.domain.repository;

import com.jambit.project.domain.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long>{
    List<Project> findTop5ByOrderByLikesCountDesc();
    List<Project> findByProjectManager(String nickname);
    List<Project> findByProjectNameContainingIgnoreCase(String title);

    @Modifying
    @Query("update Project p set p.viewCount = p.viewCount + 1 where p.id = :project_id")
    void incProjectViewCount(@Param("project_id") Long projectId);
}

