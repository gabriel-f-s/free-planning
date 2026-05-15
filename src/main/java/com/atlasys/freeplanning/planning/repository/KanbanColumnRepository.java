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
    @Query("""
        SELECT c FROM KanbanColumn c
        LEFT JOIN FETCH c.tasks
        WHERE c.project = :projectId
        ORDER BY c.position ASC
    """)
    List<KanbanColumn> findAllByProjectOrderedByPosition(@Param("projectId") Project project);

    @Query("""
        SELECT c FROM KanbanColumn c
        JOIN c.project p
        WHERE c.id = :columnId AND p.responsible = :loggedUser
    """)
    Optional<KanbanColumn> findByIdAndUser(
            @Param("columnId") UUID columnId,
            @Param("loggedUser") User loggedUser
    );

    @Query("""
        SELECT c FROM KanbanColumn c
        WHERE c.project = :projectId AND c.position BETWEEN :min AND :max
    """)
    List<KanbanColumn> findAffectedColumns(
            @Param("projectId") Project project,
            @Param("min") int min,
            @Param("max") int max
    );
}
