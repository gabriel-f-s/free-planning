package com.atlasys.freeplanning.planning.model;

import com.atlasys.freeplanning.identity.model.User;
import com.atlasys.freeplanning.planning.model.enums.Platform;
import com.atlasys.freeplanning.planning.model.enums.ProjectType;
import com.atlasys.freeplanning.planning.model.enums.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "projects", schema = "planning")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    private Platform platform;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private ProjectType type;

    @Column(name = "minimum_value")
    private BigDecimal minimumValue;

    @Column(name = "maximum_value")
    private BigDecimal maximumValue;

    @Column(name = "closed_value")
    private BigDecimal closedValue;

    @Column(name = "delivery_forecast")
    private LocalDate deliveryForecast;

    @Column(name = "delivery_date")
    private LocalDate deliveryDate;

    @Column(columnDefinition = "TEXT")
    private String annotation;

    private Boolean isPersonalProject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_fk", nullable = false)
    private User responsible;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_fk", nullable = false)
    private Client client;
}
