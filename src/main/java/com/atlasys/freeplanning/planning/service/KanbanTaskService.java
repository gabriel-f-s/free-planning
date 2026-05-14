package com.atlasys.freeplanning.planning.service;

import com.atlasys.freeplanning.identity.model.User;
import com.atlasys.freeplanning.planning.dto.kanban.KanbanTaskReorderRequest;
import com.atlasys.freeplanning.planning.dto.kanban.KanbanTaskUpdateRequest;
import com.atlasys.freeplanning.planning.dto.kanban.KanbanTaskResponse;
import com.atlasys.freeplanning.planning.mapper.KanbanMapper;
import com.atlasys.freeplanning.planning.model.KanbanColumn;
import com.atlasys.freeplanning.planning.model.KanbanTask;
import com.atlasys.freeplanning.planning.repository.KanbanColumnRepository;
import com.atlasys.freeplanning.planning.repository.KanbanTaskRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
    public KanbanTaskResponse moveTask(User loggedUser, UUID taskId, KanbanTaskReorderRequest request) {
        KanbanColumn column = findColumn(loggedUser, request.columnId());
        KanbanTask targetTask = findTask(loggedUser, taskId);
        int oldPos = targetTask.getPosition();
        int newPos = request.position();
        if (oldPos == newPos)
            return new KanbanTaskResponse(targetTask);
        List<KanbanTask> affectedTasks = repository.findAffectedTasks(column, Math.min(oldPos, newPos), Math.max(oldPos, newPos));
        for (KanbanTask task : affectedTasks) {
            if (oldPos < newPos) {
                task.setPosition(task.getPosition() - 1);
            } else {
                task.setPosition(task.getPosition() + 1);
            }
        }
        targetTask.setPosition(newPos);
        targetTask.setColumn(column);
        affectedTasks.add(targetTask);
        repository.saveAll(affectedTasks);
        return new KanbanTaskResponse(targetTask);
    }

    @Transactional
    public KanbanTaskResponse updateTask(User loggedUser, UUID taskId, KanbanTaskUpdateRequest request) {
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
