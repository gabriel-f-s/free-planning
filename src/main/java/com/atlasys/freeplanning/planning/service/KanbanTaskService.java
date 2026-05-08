package com.atlasys.freeplanning.planning.service;

import com.atlasys.freeplanning.identity.model.User;
import com.atlasys.freeplanning.planning.dto.kanban.KanbanTaskRequest;
import com.atlasys.freeplanning.planning.dto.kanban.KanbanTaskResponse;
import com.atlasys.freeplanning.planning.mapper.KanbanMapper;
import com.atlasys.freeplanning.planning.model.KanbanColumn;
import com.atlasys.freeplanning.planning.model.KanbanTask;
import com.atlasys.freeplanning.planning.repository.KanbanColumnRepository;
import com.atlasys.freeplanning.planning.repository.KanbanTaskRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class KanbanTaskService {

    private final KanbanTaskRepository repository;
    private final KanbanColumnRepository columnRepository;
    private final KanbanMapper mapper;

    public KanbanTaskService(KanbanTaskRepository repository, KanbanColumnRepository columnRepository, KanbanMapper mapper) {
        this.repository = repository;
        this.columnRepository = columnRepository;
        this.mapper = mapper;
    }

    @Transactional
    public KanbanTaskResponse createTask(User loggedUser, UUID columnId, KanbanTaskRequest request) {
        KanbanColumn column = findColumn(loggedUser, columnId);
        KanbanTask task = new KanbanTask();
        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setPosition(request.position());
        task.setColumn(column);
        return new KanbanTaskResponse(repository.save(task));
    }

    @Transactional
    public KanbanTaskResponse updateTask(User loggedUser, UUID taskId, KanbanTaskRequest request) {
        KanbanTask task = findTask(loggedUser, taskId);
        mapper.updateEntityFromDto(request, task);
        return new KanbanTaskResponse(repository.save(task));
    }

    @Transactional
    public void deleteTask(User loggedUser, UUID taskId) {
        KanbanTask task = findTask(loggedUser, taskId);
        repository.delete(task);
    }

    private KanbanColumn findColumn(User loggedUser, UUID columnId) {
        return columnRepository.findByIdAndUser(columnId, loggedUser)
                .orElseThrow(() -> new EntityNotFoundException("Column not found"));
    }

    private KanbanTask findTask(User loggedUser, UUID taskId) {
        return repository.findByIdAndUser(taskId, loggedUser)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));
    }
}
