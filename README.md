# Freeplanning

API REST para gestão de projetos freelancer. O **Freeplanning** centraliza clientes, propostas, pipeline comercial, valores e entregas em um único lugar — com quadro Kanban por projeto para acompanhar o trabalho do dia a dia.

Desenvolvido para profissionais autônomos (desenvolvedores, designers e afins) que precisam organizar negociações, prazos e faturamento sem depender de planilhas ou ferramentas genéricas.

## Funcionalidades

- **Autenticação** — registro, login e sessão stateless com JWT
- **Perfil do usuário** — nome, ocupação, valor/hora e anotações gerais
- **Clientes** — cadastro, busca por nome, edição e exclusão
- **Projetos** — criação com tipo, plataforma de origem, faixa de valores, previsão e data de entrega; filtros e paginação
- **Pipeline** — status do projeto (negociação, em andamento, entregue, cancelado, perdido, em espera)
- **Kanban** — colunas e tarefas por projeto, com reordenação e edição
- **Dashboard** — resumo de projetos ativos, faturamento do mês, entregas da semana, pipeline e notas rápidas

## Stack

| Tecnologia | Uso |
|------------|-----|
| Java 25 | Linguagem |
| Spring Boot 4 | API REST |
| Spring Security | Autenticação e autorização |
| Spring Data JPA | Persistência |
| PostgreSQL | Banco de dados |
| Flyway | Migrações de schema |
| MapStruct | Mapeamento DTO ↔ entidade |
| Lombok | Redução de boilerplate |
| Auth0 Java JWT | Tokens de acesso |
| Docker Compose | PostgreSQL em desenvolvimento |

## Arquitetura

O código segue uma organização por domínio em `com.atlasys.freeplanning`:

```
src/main/java/com/atlasys/freeplanning/
├── identity/          # Usuários e autenticação
├── planning/          # Clientes, projetos, Kanban e dashboard
└── infra/             # Segurança, CORS e tratamento global de erros
```

O banco utiliza dois schemas PostgreSQL:

- `identity` — usuários
- `planning` — clientes, projetos, colunas e tarefas Kanban

## Pré-requisitos

- JDK 25
- Maven 3.9+ (ou use o wrapper `./mvnw`)
- Docker e Docker Compose (para o PostgreSQL local)

## Configuração

### Variáveis de ambiente

| Variável | Descrição |
|----------|-----------|
| `SUN_DATABASE_USERNAME` | Usuário do PostgreSQL |
| `SUN_DATABASE_PASSWORD` | Senha do PostgreSQL |
| `RSA_PRIVATE_KEY` | Chave secreta para assinatura dos tokens JWT |

O `compose.yaml` sobe o PostgreSQL na porta **5435** (mapeada para `5432` no container), com banco `freeplanning`.

O Spring Boot Docker Compose detecta o serviço e configura o datasource automaticamente em desenvolvimento.

### CORS

Por padrão, requisições do front-end em `http://localhost:4200` são permitidas.

## Executando localmente

1. Defina as variáveis de ambiente (exemplo com valores de desenvolvimento):

```bash
export SUN_DATABASE_USERNAME=postgres
export SUN_DATABASE_PASSWORD=postgres
export RSA_PRIVATE_KEY=sua-chave-secreta-jwt
```

2. Suba o banco (opcional se o Spring Boot Compose já iniciar o serviço):

```bash
docker compose up -d
```

3. Execute a aplicação:

```bash
./mvnw spring-boot:run
```

A API ficará disponível em `http://localhost:8080` (porta padrão do Spring Boot).

### Build

```bash
./mvnw clean package
java -jar target/freeplanning-0.0.1-SNAPSHOT.jar
```

### Testes

```bash
./mvnw test
```

## Autenticação

Rotas públicas:

- `POST /auth/register` — cadastro
- `POST /auth/login` — login

As demais rotas exigem o header:

```
Authorization: Bearer <token>
```

## API

### Autenticação (`/auth`)

| Método | Rota | Descrição |
|--------|------|-----------|
| POST | `/auth/register` | Registrar usuário |
| POST | `/auth/login` | Autenticar e obter token |

### Usuário (`/user`)

| Método | Rota | Descrição |
|--------|------|-----------|
| GET | `/user` | Dados do usuário logado |
| PATCH | `/user/update` | Atualizar perfil |
| PUT | `/user/change-email` | Alterar e-mail |
| PUT | `/user/change-password` | Alterar senha |

### Clientes (`/clients`)

| Método | Rota | Descrição |
|--------|------|-----------|
| GET | `/clients` | Listar (paginado; filtro opcional `?name=`) |
| GET | `/clients/{id}` | Detalhe com projetos |
| POST | `/clients` | Criar cliente |
| PUT | `/clients/{id}` | Atualizar cliente |
| DELETE | `/clients/{id}` | Excluir cliente |

### Projetos (`/projects`)

| Método | Rota | Descrição |
|--------|------|-----------|
| GET | `/projects` | Listar com filtros e paginação |
| GET | `/projects/{id}` | Detalhe do projeto |
| POST | `/projects` | Criar projeto |
| PUT | `/projects/{id}` | Atualizar projeto |
| PUT | `/projects/{id}/notes` | Atualizar anotações |
| PUT | `/projects/{id}/status` | Alterar status |
| GET | `/projects/{id}/columns` | Quadro Kanban |
| POST | `/projects/{id}/columns` | Adicionar coluna |
| PATCH | `/projects/{id}/columns/{columnId}/move` | Reordenar coluna |

Filtros em `GET /projects`: `title`, `platform`, `status`, `type`, `deliveryForecast`, `deliveryDate`.

### Kanban

**Colunas** (`/columns`)

| Método | Rota | Descrição |
|--------|------|-----------|
| PATCH | `/columns/{id}/rename` | Renomear coluna |
| DELETE | `/columns/{id}` | Excluir coluna |
| POST | `/columns/{id}/tasks` | Criar tarefa na coluna |

**Tarefas** (`/tasks`)

| Método | Rota | Descrição |
|--------|------|-----------|
| PATCH | `/tasks/{id}` | Atualizar tarefa |
| PATCH | `/tasks/{id}/move` | Mover tarefa entre colunas |
| DELETE | `/tasks/{id}` | Excluir tarefa |

### Dashboard (`/dashboard`)

| Método | Rota | Descrição |
|--------|------|-----------|
| GET | `/dashboard/summary` | Projetos ativos, faturamento do mês e entregas da semana |
| GET | `/dashboard/pipeline` | Visão do pipeline por status |
| GET | `/dashboard/notes` | Anotações gerais do usuário |
| PUT | `/dashboard/notes` | Atualizar anotações |
| GET | `/dashboard/hourly-rate` | Valor/hora cadastrado |

## Domínio

### Status do projeto

`UNDER_NEGOTIATION` · `IN_PROGRESS` · `DELIVERED` · `CANCELED` · `LOSS` · `ON_HOLD`

### Tipo de projeto

`LOGO_CREATION` · `BRAND_CREATION` · `UIUX` · `MARKETING` · `DESIGN` · `WEB_DEVELOPMENT` · `DESKTOP_DEVELOPMENT` · `MOBILE_APP_DEVELOPMENT` · `AUTOMATION` · `TECHNICAL_SUPPORT`

### Plataforma de origem

`GETNINJAS` · `WORKANA` · `NINETY_NINE_FREELAS` · `DIRECT`

### Ocupação do usuário

`DEVELOPER` · `DESIGNER`

## Licença

Este projeto está licenciado sob a [MIT License](LICENSE).
