# Car Dealer Service

Car Dealer Service is a rest API that offers adding cars to car dealers stores, querying with certain parameters and car
recommendations by given period, travel distance and fuel consumption.

## Installation and Running

```bash
docker-compose up
```

## Running Tests

```bash
./mvnw clean verify
  ```

## Tech Stack

**Server:** Java 17, Spring Boot 3.0.1, Elasticsearch, Docker, Swagger, Github Actions.

## Api Documentation

[SwaggerUI](http://localhost:8080/swagger-ui/index.html)

## AQ

#### Question 1 Describe the architecture you will use and include a motivation of your choices

Backend architecture is simply a rest api architecture. As a development approach, hexagonal architecture has been preferred as opposed
to n-tier architectures.

In this way, the business code is separated from the infrastructure so which leads to build an easy-to-maintain, easily extendable
and
technology-independent code.

#### What is the Hexagonal Architecture

![Hexagonal Architecture](https://user-images.githubusercontent.com/17534654/210247951-dae73b29-7b86-47a6-8175-c6c2de6381c2.jpeg)

The hexagonal architecture was invented by Alistair Cockburn in an attempt to avoid known structural pitfalls in object-oriented
software design, such as undesired dependencies between layers and contamination of user interface code with business logic, and
published in 2005.

A timeless goal of software engineering has been to separate code that changes frequently from code that is stable.

~ James Coplien / Lean Architecture

The Hexagonal Architecture is recommended for those who want to write clean, maintainable, well-defined boundary context,
well-tested
domain, and decoupling business logic from technical code

Elasticsearch has been preferred as the database - search engine . Elasticsearch is a powerful search engine. Many recommendation
engine and search apis (product search, wikipedia)
uses the Elasticsearch. It provides more efficient realtime querying and indexing. Elasticsearch has been chosen because it serves
the purposes of the
car dealer service(recommendation,search).

#### Question 2 You have a team of 3 developers. How would you tackle working together on the stories?

Assuming that 2 of the 3 developers are backend and 1 frontend developer, it can be started by writing consumer driven contract
tests between the client and the server.

Client developers and server developers can start and progress independently of each other, if an agreed contract is created on
API end points requests and responses.

For 2 backend developers, instead of separating tasks, pair programming would be an excellent work to build a higher quality and
faster development.
Therefore, developers do not have to wait each other. By using test driven development approach, back end developers can focus on
implementation of user stories more efficiently.

#### Question 3 Can you describe 1 thing that can go wrong with your code once in production?

Default value of Elasticsearch's maximum page size is 10_000. This means that a maximum of 10_000 records can be fetch in one
query. If
users want to paginate for more than 10_000 records this will not be possible.

For this problem, there are a few solutions like **"after search"** or **"scroll api**" applications in Elasticsearch which should
be integrated.

https://www.elastic.co/guide/en/elasticsearch/reference/current/paginate-search-results.html
