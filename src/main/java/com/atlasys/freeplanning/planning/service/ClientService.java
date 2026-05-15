package com.atlasys.freeplanning.planning.service;

import com.atlasys.freeplanning.identity.model.User;
import com.atlasys.freeplanning.planning.dto.client.ClientDetailWithoutProjectsResponse;
import com.atlasys.freeplanning.planning.dto.client.ClientRequest;
import com.atlasys.freeplanning.planning.dto.client.ClientDetailWithProjectsResponse;
import com.atlasys.freeplanning.planning.dto.client.ClientSummaryResponse;
import com.atlasys.freeplanning.planning.mapper.ClientMapper;
import com.atlasys.freeplanning.planning.model.Client;
import com.atlasys.freeplanning.planning.model.Project;
import com.atlasys.freeplanning.planning.repository.ClientRepository;
import com.atlasys.freeplanning.planning.repository.ProjectRepository;
import jakarta.persistence.EntityExistsException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ClientService {

    private final ClientRepository repository;
    private final ProjectRepository projectRepository;
    private final ClientMapper mapper;

    public ClientService(ClientRepository repository, ProjectRepository projectRepository, ClientMapper mapper) {
        this.repository = repository;
        this.projectRepository = projectRepository;
        this.mapper = mapper;
    }

    public Page<ClientDetailWithoutProjectsResponse> findAll(User loggedUser, Pageable pageable) {
        Page<Client> clientsPage = repository.findAllByOwner(loggedUser, pageable);
        return clientsPage.map(ClientDetailWithoutProjectsResponse::new);
    }

    public Page<ClientDetailWithoutProjectsResponse> findByName(User loggedUser, String name, Pageable pageable) {
        Page<Client> clientPage = repository.findByOwnerAndNameContainingIgnoreCase(loggedUser, name, pageable);
        return clientPage
                .map(ClientDetailWithoutProjectsResponse::new);
    }

    public ClientDetailWithProjectsResponse findOne(User loggedUser, UUID id) {
        Client client = findClient(loggedUser, id);
        List<Project> projects = projectRepository.findByClient(client);
        client.addProjects(projects);
        return new ClientDetailWithProjectsResponse(client);
    }

    public ClientSummaryResponse create(User loggedUser, ClientRequest request) {
        if (repository.existsClientByEmailAndOwner(request.email(), loggedUser))
            throw new EntityExistsException("Client already exists");

        Client client = new Client();
        client.setName(request.name());
        client.setEmail(request.email());
        client.setPhone(request.phone());
        client.setOwner(loggedUser);
        return new ClientSummaryResponse(repository.save(client));
    }

    public ClientDetailWithoutProjectsResponse update(User loggedUser, ClientRequest request, UUID id) {
        Client client = findClient(loggedUser, id);
        mapper.updateEntityFromDto(request, client);
        return new ClientDetailWithoutProjectsResponse(repository.save(client));
    }

    public void delete(User loggedUser, UUID id) {
        Client client = findClient(loggedUser, id);
        List<Project> projects = projectRepository.findByClient(client);
        if (!projects.isEmpty())
            throw new EntityExistsException("Client has active projects");
        repository.delete(client);
    }

    private Client findClient(User loggedUser, UUID id) {
        return repository.findByOwnerAndId(loggedUser, id)
                .orElseThrow(() -> new EntityExistsException("Client not found"));
    }
}
