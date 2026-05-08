package com.atlasys.freeplanning.planning.repository;

import com.atlasys.freeplanning.identity.model.User;
import com.atlasys.freeplanning.planning.model.Client;
import com.atlasys.freeplanning.planning.model.Project;
import com.atlasys.freeplanning.planning.model.enums.Status;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID>, JpaSpecificationExecutor<Project> {
    Optional<Project> findByIdAndResponsible(UUID id, User responsible);
    
    Page<Project> findAll(@NonNull Specification<Project> specification, @NonNull Pageable pageable);

    List<Project> findAllByStatusInAndResponsible(Collection<Status> statuses, User responsible);

    long countByStatusInAndResponsible(Collection<Status> statuses, User responsible);

    @Query("SELECT SUM(p.closedValue) FROM Project p " +
            "WHERE p.responsible = :user " +
            "AND p.status = :status " +
            "AND p.deliveryDate BETWEEN :start AND :end")
    BigDecimal sumBillingByMonth(@Param("user") User user,
                                 @Param("status") Status status,
                                 @Param("start") LocalDate start,
                                 @Param("end") LocalDate end);

    long countProjectsByDeliveryDateBetweenAndResponsible(LocalDate start, LocalDate end, User responsible);

    List<Project> findByClient(Client client);

    List<Project> findAllByResponsibleAndDeliveryDateBetween(User responsible, LocalDate start, LocalDate end);
}
