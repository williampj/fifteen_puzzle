# Fifteen Puzzle API

This project is a Spring Boot-based REST API for playing and managing 15-puzzle games. 
It supports user registration and login, token-based authentication, game state management, 
and admin operations including batch deletions and user notifications via RabbitMQ.

## Features

- JWT authentication (access and refresh tokens)
- Game creation, move validation, and game state tracking
- Admin Server-Sent Event notifications using RabbitMQ
- In-memory storage of users and games
- Admin functionality to remove complete or stale games, or inactive users
- Functional service layer, implementing documented interfaces
- Unit test coverage at the Service and Service Store (in-memory data layer)

## Technologies

- Java 22
- Spring Boot 3.4
- JSON Web Tokens
- RabbitMQ (AMQP)
- Maven
- Docker and Docker Compose
- JUnit & Mockito
- Lombok

## Project Structure

com.william.puzzle
- controller: REST controllers
- service: Business logic 
  - service.store: In-memory persistence layer
- dto: request/response objects
- model: core domain models (Game, Board, User)
- exception: custom exceptions
- constants: Static strings (log messages, error messages etc.)
  

## Running Locally

### Prerequisites

- Java 22
- Docker + Docker Compose
- Maven (or use the provided wrapper)

### Clone the Repository

```bash
git clone https://github.com/williampj/fifteen_puzzle.git
cd fifteen_puzzle
```

### Run the program

Remove the `.example` extensions of `.env.properties.example` and `env.example` and update values as needed. 

```bash
docker-compose up --build
```

API available at: http://localhost:8080

### Gameplay 
#### Player
2. Start with the signup endpoint (POST /users) to register a user
2. Then request login (POST /auth/login) with user and password from previous step. Use the returned bearer (Access) 
token as a header in further game-related requests.
3. The zero represents the empty square. Adjacent empty squares can be moved into the empty square with a POST call to /move with the square to move from. The game is solved when the numbers are ordered ascendingly from 1-15, with 0 on the final 16th square. For every move, the current board structure will figure in the response, including a "isSolved" property whose value is false until the game is solved.  

#### Admin
An admin controller lays out the admin's actions, which include deleting users and games based on identifiers, or based on inactivity. 


### RabbitMQ Management UI: 
http://localhost:15672

Default credentials: 
- user: `broker_user` 
- password: `broker_password`

### Author
@williampj