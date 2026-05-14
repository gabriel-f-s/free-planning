package com.atlasys.freeplanning.planning.service;

import com.atlasys.freeplanning.identity.model.User;
import com.atlasys.freeplanning.identity.repository.UserRepository;
import com.atlasys.freeplanning.planning.dto.AnnotationDTO;
import com.atlasys.freeplanning.planning.dto.dashboard.DashboardPipelineResponse;
import com.atlasys.freeplanning.planning.dto.dashboard.DashboardSummaryResponse;
import com.atlasys.freeplanning.planning.dto.project.ProjectPipelineResponse;
import com.atlasys.freeplanning.planning.model.Project;
import com.atlasys.freeplanning.planning.model.enums.Status;
import com.atlasys.freeplanning.planning.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class DashboardService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    private final static List<Status> activeStatus = List.of(
            Status.UNDER_NEGOTIATION,
            Status.IN_PROGRESS,
            Status.ON_HOLD
    );

    public DashboardService(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    public DashboardSummaryResponse summary(User loggedUser) {
        return new DashboardSummaryResponse(
                countAllProjectsActive(loggedUser),
                findMonthBilling(loggedUser),
                countDeliveriesThisWeek(loggedUser)
        );
    }

    public DashboardPipelineResponse pipeline(User loggedUser) {
        List<Project> projects = projectRepository.findAllByStatusInAndResponsible(activeStatus, loggedUser);

        Map<Status, List<ProjectPipelineResponse>> groupedProjects = projects.stream()
                .collect(Collectors.groupingBy(
                        Project::getStatus,
                        Collectors.mapping(
                                ProjectPipelineResponse::new,
                                Collectors.toList()
                        )
                ));

        return new DashboardPipelineResponse(
                groupedProjects.getOrDefault(Status.IN_PROGRESS, List.of()),
                groupedProjects.getOrDefault(Status.UNDER_NEGOTIATION, List.of()),
                groupedProjects.getOrDefault(Status.ON_HOLD, List.of())
        );
    }

    public AnnotationDTO findAnnotation(User loggedUser) {
        return new AnnotationDTO(loggedUser.getAnnotation());
    }

    public AnnotationDTO updateAnnotation(User loggedUser, AnnotationDTO annotation) {
        loggedUser.setAnnotation(annotation.content());
        userRepository.save(loggedUser);
        return new AnnotationDTO(loggedUser.getAnnotation());
    }

    public BigDecimal findUserHourlyRate(User loggedUser) {
        return loggedUser.getHourlyRate();
    }

    private long countAllProjectsActive(User loggedUser) {
        return projectRepository.countByStatusInAndResponsible(activeStatus, loggedUser);
    }

    private BigDecimal findMonthBilling(User loggedUser) {
        LocalDate now = LocalDate.now();
        LocalDate start = now.withDayOfMonth(1);
        LocalDate end = now.with(TemporalAdjusters.lastDayOfMonth());

        BigDecimal total = projectRepository.sumBillingByMonth(loggedUser, Status.DELIVERED, start, end);

        return total != null ? total : BigDecimal.ZERO;
    }

    private long countDeliveriesThisWeek(User loggedUser) {
        LocalDate now = LocalDate.now();
        LocalDate startWeek = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endWeek = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        return projectRepository.countProjectsByDeliveryDateBetweenAndResponsible(startWeek, endWeek, loggedUser);
    }
}
