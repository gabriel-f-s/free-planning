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

    List<KanbanTask> findByColumnAndPositionGreaterThan(KanbanColumn oldColumn, int oldPos);

    List<KanbanTask> findByColumnAndPositionGreaterThanEqual(KanbanColumn newColumn, int newPos);

    List<KanbanTask> findByColumnAndPositionBetween(KanbanColumn newColumn, int min, int max);
}
