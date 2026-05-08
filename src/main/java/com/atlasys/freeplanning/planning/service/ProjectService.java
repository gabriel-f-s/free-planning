package com.atlasys.freeplanning.planning.service;

import com.atlasys.freeplanning.identity.model.User;
import com.atlasys.freeplanning.planning.dto.Note;
import com.atlasys.freeplanning.planning.dto.kanban.BoardResponse;
import com.atlasys.freeplanning.planning.dto.project.*;
import com.atlasys.freeplanning.planning.mapper.KanbanMapper;
import com.atlasys.freeplanning.planning.mapper.ProjectMapper;
import com.atlasys.freeplanning.planning.model.Client;
import com.atlasys.freeplanning.planning.model.KanbanColumn;
import com.atlasys.freeplanning.planning.model.Project;
import com.atlasys.freeplanning.planning.model.enums.Status;
import com.atlasys.freeplanning.planning.repository.ClientRepository;
import com.atlasys.freeplanning.planning.repository.KanbanColumnRepository;
import com.atlasys.freeplanning.planning.repository.ProjectRepository;
import com.atlasys.freeplanning.planning.repository.specifications.ProjectSpec;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


@Service
public class ProjectService {

    private final ProjectRepository repository;
    private final ClientRepository clientRepository;
    private final KanbanColumnRepository columnRepository;

    private final ProjectMapper mapper;
    private final KanbanMapper kanbanMapper;

    public ProjectService(ProjectRepository repository, ClientRepository clientRepository, KanbanColumnRepository columnRepository, ProjectMapper mapper, KanbanMapper kanbanMapper) {
        this.repository = repository;
        this.clientRepository = clientRepository;
        this.columnRepository = columnRepository;
        this.mapper = mapper;
        this.kanbanMapper = kanbanMapper;
    }

    public Page<ProjectSummaryResponse> findAll(User loggedUser, ProjectFilter filter, Pageable pageable) {
        Specification<Project> specification = ProjectSpec.filterBy(filter);
        Specification<Project> userSpecification =
                (root, query, criteriaBuilder)
                        -> criteriaBuilder.equal(root.get("responsible"), loggedUser);
        Specification<Project> finalSpecification = Specification.where(userSpecification).and(specification);

        Page<Project> projectPage = repository.findAll(finalSpecification, pageable);
        return projectPage.map(ProjectSummaryResponse::new);
    }

    public ProjectDetailResponse findOne(User loggedUser, UUID id) {
        Project project = findProject(loggedUser, id);
        return new ProjectDetailResponse(project);
    }

    @Transactional(readOnly = true)
    public BoardResponse findProjectBoard(User loggedUser, UUID projectId) {
        Project project = findProject(loggedUser, projectId);
        List<KanbanColumn> columns = columnRepository.findAllByProject(project);
        return kanbanMapper.toResponse(columns);
    }

    @Transactional
    public ProjectDetailResponse create(User loggedUser, ProjectCreateRequest request) {
        Client client = clientRepository.findById(request.clientId())
                .orElseThrow(() -> new EntityNotFoundException("Client not found"));

        if (request.deliveryForecast() != null && request.deliveryForecast().isBefore(LocalDate.now()))
            throw new IllegalArgumentException("Delivery forecast must be in the future");

        if (request.deliveryDate() != null && request.deliveryDate().isBefore(LocalDate.now()))
            throw new IllegalArgumentException("Delivery date must be in the future");

        Project project = new Project();
        project.setTitle(request.title());
        project.setDescription(request.description());
        project.setPlatform(request.platform());
        project.setStatus(Status.UNDER_NEGOTIATION);
        project.setType(request.type());
        project.setMinimumValue(request.minimumValue());
        project.setMaximumValue(request.maximumValue());
        project.setClosedValue(request.closedValue());
        project.setDeliveryForecast(request.deliveryForecast());
        project.setDeliveryDate(request.deliveryDate());
        project.setResponsible(loggedUser);
        project.setClient(client);
        repository.save(project);

        return new ProjectDetailResponse(project);
    }

    @Transactional
    public ProjectDetailResponse update(User loggedUser, ProjectUpdateRequest request, UUID id) {
        Project project = findProject(loggedUser, id);
        mapper.updateEntityFromDto(request, project);

        if (request.clientId() != null) {
            if (project.getClient() == null || !project.getClient().getId().equals(request.clientId())) {
                Client newClient = clientRepository.findById(request.clientId())
                        .orElseThrow(() -> new EntityNotFoundException("Client not found"));
                project.setClient(newClient);
            }
        }

        return new ProjectDetailResponse(repository.save(project));
    }

    @Transactional
    public Note updateNotes(User loggedUser, UUID id, Note annotation) {
        Project project = findProject(loggedUser, id);
        project.setAnnotation(annotation);
        repository.save(project);
        return annotation;
    }

    private Project findProject(User loggedUser, UUID id) {
        return repository.findByIdAndResponsible(id, loggedUser)
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));
    }
}
