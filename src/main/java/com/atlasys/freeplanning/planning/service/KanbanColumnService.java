package com.atlasys.freeplanning.planning.service;

import com.atlasys.freeplanning.identity.model.User;
import com.atlasys.freeplanning.planning.dto.kanban.*;
import com.atlasys.freeplanning.planning.mapper.KanbanMapper;
import com.atlasys.freeplanning.planning.model.KanbanColumn;
import com.atlasys.freeplanning.planning.model.Project;
import com.atlasys.freeplanning.planning.repository.KanbanColumnRepository;
import com.atlasys.freeplanning.planning.repository.KanbanTaskRepository;
import com.atlasys.freeplanning.planning.repository.ProjectRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class KanbanColumnService {

    private final KanbanColumnRepository columnRepository;
    private final ProjectRepository projectRepository;
    private final KanbanMapper mapper;

    public KanbanColumnService(
            KanbanColumnRepository columnRepository,
            ProjectRepository projectRepository,
            KanbanMapper mapper
    ) {
        this.columnRepository = columnRepository;
        this.projectRepository = projectRepository;
        this.mapper = mapper;
    }

    @Transactional
    public KanbanColumnResponse createColumn(User loggedUser, UUID projectId, KanbanColumnRequest request) {
        Project project = findProject(loggedUser, projectId);
        KanbanColumn column = new KanbanColumn();
        column.setName(request.name());
        column.setPosition(request.position());
        column.setProject(project);
        return new KanbanColumnResponse(columnRepository.save(column));
    }

    @Transactional
    public KanbanColumnResponse updateColumn(User loggedUser, UUID columnId, KanbanColumnRequest request) {
        KanbanColumn column = findColumn(loggedUser, columnId);
        mapper.updateEntityFromDto(request, column);
        return new KanbanColumnResponse(columnRepository.save(column));
    }

    @Transactional
    public void deleteColumn(User loggedUser, UUID columnId) {
        KanbanColumn column = findColumn(loggedUser, columnId);
        columnRepository.delete(column);
    }

    private Project findProject(User loggedUser, UUID id) {
        return projectRepository.findByIdAndResponsible(id, loggedUser)
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));
    }

    private KanbanColumn findColumn(User loggedUser, UUID columnId) {
        return columnRepository.findByIdAndUser(columnId, loggedUser)
                .orElseThrow(() -> new EntityNotFoundException("Column not found"));
    }
}
