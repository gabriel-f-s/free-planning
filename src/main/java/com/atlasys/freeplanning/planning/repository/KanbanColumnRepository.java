package com.atlasys.freeplanning.planning.repository;

import com.atlasys.freeplanning.identity.model.User;
import com.atlasys.freeplanning.planning.model.KanbanColumn;
import com.atlasys.freeplanning.planning.model.Project;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface KanbanColumnRepository extends JpaRepository<KanbanColumn, UUID> {
    @EntityGraph(attributePaths = "tasks")
    List<KanbanColumn> findAllByProject(Project project);

    @Query("""
        SELECT column FROM KanbanColumn column
        JOIN column.project project
        WHERE column.id = :columnId AND project.responsible = :loggedUser
    """)
    Optional<KanbanColumn> findByIdAndUser(@Param("columnId") UUID columnId, @Param("loggedUser") User loggedUser);
}
