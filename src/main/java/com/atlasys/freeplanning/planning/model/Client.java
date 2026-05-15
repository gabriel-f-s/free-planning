package com.atlasys.freeplanning.planning.model;

import com.atlasys.freeplanning.identity.model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "clients", schema = "planning")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    private String email;

    private String phone;

    @OneToMany(mappedBy = "client")
    private List<Project> projects = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_fk", nullable = false)
    private User owner;

    public void addProjects(List<Project> projectList) { projects.addAll(projectList); }
}
