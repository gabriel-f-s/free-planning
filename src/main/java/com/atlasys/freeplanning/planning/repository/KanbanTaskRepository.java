package com.atlasys.freeplanning.planning.repository;

import com.atlasys.freeplanning.identity.model.User;
import com.atlasys.freeplanning.planning.model.KanbanColumn;
import com.atlasys.freeplanning.planning.model.KanbanTask;
import com.atlasys.freeplanning.planning.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface KanbanTaskRepository extends JpaRepository<KanbanTask, UUID> {
    @Query("""
        SELECT t FROM KanbanTask t
        JOIN t.column c
        JOIN c.project p
        WHERE t.id = :taskId AND p.responsible = :loggedUser
    """)
    Optional<KanbanTask> findByIdAndUser(@Param("taskId") UUID taskId, @Param("loggedUser") User loggedUser);

    @Query("""
        SELECT t FROM KanbanTask t
        WHERE t.column = :columnId AND t.position BETWEEN :min AND :max
    """)
    List<KanbanTask> findAffectedTasks(
            @Param("columnId") KanbanColumn column,
            @Param("min") int min,
            @Param("max") int max
    );
}
