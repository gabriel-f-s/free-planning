package com.atlasys.freeplanning.planning.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "kanban_columns", schema = "planning")
@RequiredArgsConstructor
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
    private List<KanbanTask> tasks = new ArrayList<>();

    @Version
    private Long version;

    public KanbanColumn(String name, int position, Project project) {
        this.name = name;
        this.position = position;
        this.project = project;
    }
}
