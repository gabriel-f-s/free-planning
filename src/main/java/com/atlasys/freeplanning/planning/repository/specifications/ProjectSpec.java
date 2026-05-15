package com.atlasys.freeplanning.planning.repository.specifications;

import com.atlasys.freeplanning.planning.dto.project.ProjectFilter;
import com.atlasys.freeplanning.planning.model.Project;
import com.atlasys.freeplanning.planning.model.enums.Platform;
import com.atlasys.freeplanning.planning.model.enums.ProjectType;
import com.atlasys.freeplanning.planning.model.enums.Status;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProjectSpec {

    public static Specification<Project> filterBy(ProjectFilter filter) {
        return ((root, query, criteriaBuilder) -> {
           List<Predicate> predicates = new ArrayList<>();

           if (StringUtils.hasText(filter.title())) {
               predicates.add(criteriaBuilder.like(
                       criteriaBuilder.lower(root.get("title")),
                       "%" + filter.title().toLowerCase() + "%"
               ));
           }

           if (StringUtils.hasText(filter.platform())) {
               predicates.add(criteriaBuilder.equal(
                       root.get("platform"),
                       Platform.valueOf(filter.platform())
               ));
           }

           if (StringUtils.hasText(filter.status())) {
               predicates.add(criteriaBuilder.equal(
                       root.get("status"),
                       Status.valueOf(filter.status())
               ));
           }

           if (StringUtils.hasText(filter.type())) {
               predicates.add(criteriaBuilder.equal(
                       root.get("type"),
                       ProjectType.valueOf(filter.type())
               ));
           }

           if (StringUtils.hasText(filter.deliveryForecast())) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        root.get("deliveryForecast"),
                        LocalDate.parse(filter.deliveryForecast())
                ));
           }

           if (StringUtils.hasText(filter.deliveryDate())) {
               predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                       root.get("deliveryDate"),
                       LocalDate.parse(filter.deliveryDate())
               ));
           }

           return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }
}
