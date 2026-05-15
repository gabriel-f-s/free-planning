package com.atlasys.freeplanning.planning.service;

import com.atlasys.freeplanning.identity.model.User;
import com.atlasys.freeplanning.planning.dto.kanban.*;
import com.atlasys.freeplanning.planning.model.KanbanColumn;
import com.atlasys.freeplanning.planning.model.KanbanTask;
import com.atlasys.freeplanning.planning.repository.KanbanColumnRepository;
import com.atlasys.freeplanning.planning.repository.KanbanTaskRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KanbanColumnService {

    private final KanbanColumnRepository columnRepository;
    private final KanbanTaskRepository taskRepository;

    @Transactional
    public KanbanColumnResponse renameColumn(User loggedUser, UUID id, KanbanColumnRenameRequest request) {
        KanbanColumn column = findColumn(loggedUser, id);
        column.setName(request.name());
        return new KanbanColumnResponse(columnRepository.save(column));
    }

    @Transactional
    public void deleteColumn(User loggedUser, UUID id) {
        KanbanColumn column = findColumn(loggedUser, id);
        columnRepository.delete(column);
    }

    @Transactional
    public KanbanTaskResponse addTask(User loggedUser, UUID id, KanbanTaskCreateRequest request) {
        KanbanColumn column = findColumn(loggedUser, id);
        KanbanTask task = new KanbanTask();
        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setPosition(request.position());
        task.setColumn(column);
        return new KanbanTaskResponse(taskRepository.save(task));
    }

    private KanbanColumn findColumn(User loggedUser, UUID id) {
        return columnRepository.findByIdAndUser(id, loggedUser)
                .orElseThrow(() -> new EntityNotFoundException("Column not found"));
    }
}
