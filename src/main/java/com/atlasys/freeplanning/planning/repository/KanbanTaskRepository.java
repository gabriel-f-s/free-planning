package com.atlasys.freeplanning.planning.repository;

import com.atlasys.freeplanning.identity.model.User;
import com.atlasys.freeplanning.planning.model.KanbanColumn;
import com.atlasys.freeplanning.planning.model.KanbanTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface KanbanTaskRepository extends JpaRepository<KanbanTask, UUID> {
    @Query("""
        SELECT task FROM KanbanTask task 
        JOIN task.column column 
        JOIN column.project project 
        WHERE task.id = :taskId AND project.responsible = :loggedUser
    """)
    Optional<KanbanTask> findByIdAndUser(@Param("taskId") UUID taskId, @Param("loogedUser") User loggedUser);
}
