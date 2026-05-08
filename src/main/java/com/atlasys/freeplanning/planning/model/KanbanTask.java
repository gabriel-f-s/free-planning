package com.atlasys.freeplanning.planning.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "kanban_tasks", schema = "planning")
public class KanbanTask {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String title;
    private String description;
    private Integer position;

    @ManyToOne
    @JoinColumn(name = "column_fk")
    private KanbanColumn column;

    @Version
    private Long version;
}
