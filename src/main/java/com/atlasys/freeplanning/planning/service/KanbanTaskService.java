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

import java.util.ArrayList;
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
        KanbanColumn newColumn = findColumn(loggedUser, request.columnId());
        KanbanTask targetTask = findTask(loggedUser, taskId);
        KanbanColumn oldColumn = targetTask.getColumn();

        int oldPos = targetTask.getPosition();
        int newPos = request.position();

        boolean isSameColumn = oldColumn.getId().equals(newColumn.getId());

        if (isSameColumn && oldPos == newPos) return new KanbanTaskResponse(targetTask);

        List<KanbanTask> tasksToSave = new ArrayList<>();
        if (isSameColumn) {
            int min = Math.min(oldPos, newPos);
            int max = Math.max(oldPos, newPos);
            List<KanbanTask> affectedTasks = repository.findByColumnAndPositionBetween(newColumn, min, max);
            for (KanbanTask task : affectedTasks) {
                if (task.getId().equals(targetTask.getId())) continue;
                if (oldPos < newPos) {
                    task.setPosition(task.getPosition() - 1);
                } else {
                    task.setPosition(task.getPosition() + 1);
                }
                tasksToSave.add(task);
            }
        } else {
            List<KanbanTask> oldColumnTasks = repository.findByColumnAndPositionGreaterThan(oldColumn, oldPos);
            for (KanbanTask task : oldColumnTasks) {
                task.setPosition(task.getPosition() - 1);
                tasksToSave.add(task);
            }
            List<KanbanTask> newColumnTasks = repository.findByColumnAndPositionGreaterThanEqual(newColumn, newPos);
            for (KanbanTask task : newColumnTasks) {
                task.setPosition(task.getPosition() + 1);
                tasksToSave.add(task);
            }
        }
        targetTask.setPosition(newPos);
        targetTask.setColumn(newColumn);
        tasksToSave.add(targetTask);
        repository.saveAll(tasksToSave);
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
