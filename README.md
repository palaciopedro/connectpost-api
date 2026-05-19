# ConnectPost API - MongoDB

API REST desenvolvida com Java, Spring Boot e MongoDB para gerenciamento de usuários, posts e comentários, aplicando conceitos de modelagem NoSQL, DTOs, consultas avançadas e arquitetura em camadas.

---

# Tecnologias utilizadas

<p align="left">
  <img src="https://img.shields.io/badge/Java-25-red?style=for-the-badge&logo=openjdk">
  <img src="https://img.shields.io/badge/Spring_Boot-4.0-green?style=for-the-badge&logo=springboot">
  <img src="https://img.shields.io/badge/MongoDB-NoSQL-darkgreen?style=for-the-badge&logo=mongodb">
  <img src="https://img.shields.io/badge/Maven-Build-blue?style=for-the-badge&logo=apachemaven">
</p>

---

# Objetivo do projeto

O projeto foi desenvolvido com o objetivo de praticar:

- criação de APIs REST
- integração com MongoDB
- modelagem NoSQL
- consultas avançadas
- utilização de DTOs
- relacionamentos entre documentos
- arquitetura em camadas
- boas práticas com Spring Boot

---

# Arquitetura do projeto

```text
src
 └── main
     └── java
         └── com.javaprojects.workshopmongo
             ├── config
             ├── domain
             ├── dto
             ├── repository
             ├── resources
             │    └── util
             ├── services
             └── services.exception
```

---

# Tecnologias e dependências

## Dependências principais

```xml
<dependencies>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-mongodb</artifactId>
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-webmvc</artifactId>
    </dependency>

</dependencies>
```

---

# Modelagem NoSQL

O projeto utiliza uma abordagem híbrida de modelagem no MongoDB:

| Estratégia | Utilização |
|---|---|
| Embedded Documents | Comentários |
| DBRef | Relacionamento usuário → posts |
| DTOs | Autor simplificado em posts e comentários |

---

# Entidades principais

## User

```java
@Document
public class User {

    @Id
    private String id;

    private String name;
    private String email;

    @DBRef(lazy = true)
    private List<Post> posts = new ArrayList<>();
}
```

### Conceitos aplicados

- `@Document` para mapear collections MongoDB
- `@DBRef` para relacionar documentos
- lazy loading com `lazy = true`
- relacionamento usuário → posts

---

## Post

```java
@Document
public class Post {

    @Id
    private String id;

    private Date date;
    private String title;
    private String body;

    private AuthorDTO author;

    private List<CommentDTO> comments = new ArrayList<>();
}
```

### Conceitos aplicados

- documentos embarcados
- lista de comentários embedded
- DTO simplificado do autor
- modelagem otimizada para leitura

---

# DTOs

O projeto utiliza DTOs para evitar exposição direta das entidades e reduzir acoplamento entre camadas.

---

## AuthorDTO

```java
public class AuthorDTO {

    private String id;
    private String name;

}
```

### Objetivo

Representar apenas os dados essenciais do autor dentro de posts e comentários.

---

## UserDTO

```java
public class UserDTO {

    private String id;
    private String name;
    private String email;

}
```

### Benefícios

- controle dos dados expostos
- desacoplamento
- payloads menores
- melhor organização da API

---

## CommentDTO

```java
public class CommentDTO {

    private String text;
    private Date date;
    private AuthorDTO author;

}
```

---

# Exemplo de documento MongoDB

```json
{
  "id": "681f8f9c9c2d2f0f3c123456",
  "date": "2025-05-18T20:10:00Z",
  "title": "Partiu viagem",
  "body": "Vou viajar para São Paulo amanhã",
  "author": {
    "id": "1",
    "name": "Maria Brown"
  },
  "comments": [
    {
      "text": "Boa viagem!",
      "date": "2025-05-18T21:00:00Z",
      "author": {
        "id": "2",
        "name": "Alex Green"
      }
    }
  ]
}
```

---

# API REST

## Endpoints de usuários

| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/users` | Lista todos os usuários |
| GET | `/users/{id}` | Busca usuário por ID |
| POST | `/users` | Cria usuário |
| PUT | `/users/{id}` | Atualiza usuário |
| DELETE | `/users/{id}` | Remove usuário |
| GET | `/users/{id}/posts` | Busca posts do usuário |

---

## Endpoints de posts

| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/posts/{id}` | Busca post por ID |
| GET | `/posts/titlesearch` | Busca posts por título |
| GET | `/posts/fullsearch` | Busca avançada |

---

# Consultas avançadas com MongoDB

O projeto implementa consultas customizadas utilizando:

- regex
- filtros compostos
- múltiplos critérios
- intervalos de datas
- busca em documentos aninhados

---

## Busca por título

```java
@Query("{ 'title': { $regex: ?0, $options: 'i'} }")
List<Post> searchTitle(String text);
```

### Recursos utilizados

- `$regex`
- busca case insensitive
- consultas textuais

---

## Query Method do Spring Data

```java
List<Post> findByTitleContainingIgnoreCase(String text);
```

---

## Busca avançada

```java
@Query("{ $and: [ 
    { date: {$gte: ?1} }, 
    { date: { $lte: ?2} }, 
    { $or: [ 
        { 'title': { $regex: ?0, $options: 'i' } }, 
        { 'body': { $regex: ?0, $options: 'i' } }, 
        { 'comments.text': { $regex: ?0, $options: 'i' } } 
    ] } 
] }")
List<Post> fullSearch(String text, Date minDate, Date maxDate);
```

### Recursos aplicados

- `$and`
- `$or`
- `$regex`
- `$gte`
- `$lte`
- busca em arrays embedded
- múltiplos filtros simultâneos

---

# Exemplos de requisição

## Buscar posts por título

```http
GET /posts/titlesearch?text=travel
```

---

## Busca avançada

```http
GET /posts/fullsearch?text=travel&minDate=2025-01-01&maxDate=2025-12-31
```

---

# Tratamento de parâmetros

A aplicação possui uma classe utilitária para:

- decodificar parâmetros URL
- converter datas
- evitar erros de parsing

```java
public static String decodeParam(String text)

public static Date convertDate(String textDate, Date defaultValue)
```

---

# Funcionalidades implementadas

## Usuários

- CRUD completo
- busca por ID
- atualização
- remoção
- listagem de posts

---

## Posts

- busca por ID
- busca textual
- busca avançada
- comentários
- autor simplificado via DTO

---

# Boas práticas aplicadas

- arquitetura em camadas
- separação de responsabilidades
- DTO Pattern
- MongoRepository
- ResponseEntity
- códigos HTTP corretos
- lazy loading
- serialização
- stream API
- consultas customizadas

---

# Códigos HTTP utilizados

| Código | Significado |
|---|---|
| 200 | OK |
| 201 | Created |
| 204 | No Content |

---

# Como executar o projeto

## Pré-requisitos

- Java 25
- Maven
- MongoDB

---

## Clonar repositório

```bash
git clone https://github.com/seuusuario/workshopmongo.git
```

---

## Entrar na pasta

```bash
cd workshopmongo
```

---

## Executar aplicação

```bash
mvn spring-boot:run
```

---

# Configuração MongoDB

```properties
spring.data.mongodb.uri=mongodb://localhost:27017/workshop_mongo
```

---

# Testes da API

A API pode ser testada utilizando:

- Postman
- Insomnia

---

# Aprendizados

Durante o desenvolvimento foram praticados conceitos importantes como:

- modelagem NoSQL
- embedded documents
- consultas avançadas no MongoDB
- integração Spring Boot + MongoDB
- APIs REST
- DTOs
- regex em consultas
- filtros compostos
- arquitetura backend

---

Projeto desenvolvido para estudos de Java, Spring Boot e MongoDB.
