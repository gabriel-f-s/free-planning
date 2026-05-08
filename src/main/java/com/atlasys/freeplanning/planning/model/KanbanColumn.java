package com.atlasys.freeplanning.planning.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "kanban_columns", schema = "planning")
public class KanbanColumn {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;
    private Integer position;

    @ManyToOne
    @JoinColumn(name = "project_fk")
    private Project project;

    @OneToMany(mappedBy = "column", cascade = CascadeType.ALL)
    @OrderBy("position ASC")
    private List<KanbanTask> tasks;

    @Version
    private Long version;

}
