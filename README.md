# Take Home Assignment 

This assignment tests Back-end and Front-end development.

## Introduction

Thank you for taking time to do this home assignment! 

The assignment asks you to do some simple things. The obvious implementation 
should be easy for you. What we're interested in seeing is what you
know from experience to be not so obvious.

Imagine this has to run in production.

What, besides the obvious, do you believe is necessary? Please code it that way :-)

There are 2 parts to the assignment:

1) Some [code](#Coding) to be written.
2) Some [questions](#Questions) about your code.

## Tech choices

Please implement the stories below, including a few tests for the back-end, in one of the following languages: 

- Back-end: gRPC, using Kotlin, Go. 
- Front-end: gRPC, React. 

If you use any dependencies, please add the associate containers to the project.

This repo will be connected to Heroku so that your app can be deployed and inspected functionally. Please let us know what implementation languages you're going to use so we can set it up for you.

You are free to use the CIS Automotive API (https://api.autodealerdata.com/docs).

## UI

You are free to design your own UI. 

Or you can take some clues from a UI we've uploaded to Zeplin. The UI is in the public domain
and we've uploaded it to Zeplin for your convenience. Let us know if you want to use it.

The design: https://www.xdguru.com/cars-classified-website-xd-template/

Zeplin: https://zpl.io/2j6Q1lx



## Intermediate Review

We offer you the option to ask one of us to give you intermediate feedback on your code before you hand your assignment in.

Please use the "Review" button in Github to ask us.


## Assessment

We "grade" based on the following criteria:

* Did you implement according to the requirements?
* Readability is important to us.
* We look at your git commit log. It would be good if it shows us your process.
* Understanding your tactical decisions should be made clear.

## Coding Part

### As a car dealer, I want to browse the cars I have in stock and give purchase recommendations tailored to the needs of my customers.

More specifically,

### As a car dealer, I want to search for cars by year and make.

Example:

- Given the year 2018, I should get the Citroen C3 and Honda Fit.
- Given the brand Citroën, I should get the Citroen C3 2018.
- Leaving the search string blank should return a list of all cars.

### As a car dealer, I want to be able to add new cars to my store.

Example:

* I will enter the car's model, make, version, year of release, price, fuel consumption, and annual maintenance cost. The car will show up in the results returned by story #1.

### As a car dealer, I want to recommend to my clients the car with the lowest total annual cost over a period of four (4) years, given the price of fuel (€/L) and the expected distance to travel each month (km/month).

Relevant car parameters are price of the car (€), fuel consumption (km/l), and annual maintenance cost.

Example:

- Given that I expect to travel 250 km each month for the next 4 years, and the expected
price of fuel is 0.66 €/L, what is the ranking of cars according to their total annual cost?

### Questions
---

Based on these stories, please do as follows:

* Describe the architecture you will use and include a motivation of your choices. (max 500 characters)

* You have a team of 3 developers. How would you tackle working together on the stories?

* Can you describe 1 thing that can go wrong with your code once in production?

## Architecture

### Server
The server consists of just a single service, because it's features are closely related and are not complex.

Data is stored in a cloud hosted MongoDB cluster and retrieved using the `CarRepo` repository class. This repository (with its interface) adds a separation of concern so that replacing the database engine to something else, or adding a memory-cache doesn't affect the entire code base.

The stored data uses it's own model, to keep the logic loosely coupled with the proto definition.

In this case I've added a separate package `recommendations` that contains the logic to calculate the annual cost price per car. This makes sure the logic is contained, reusable and testable.

### Client
The client is a simple React based web-application, set up using the `create-react-app` tool to ensure a consistent setup with potential other React web application projects.

I've added `react-router-dom` to enable navigation between screens and used `grpc-web` to enable communication with the gRPC server.

The `components` folder consists of a collection of re-usable components that may or may not have their own logic. These components are the building blocks of the app.

The `services` folder contains the abstraction layer between the app and the gRPC server, and adds the wiring to enable proper `await` promises for the app.

### Envoy
The Envoy proxy is needed to make a web-application work with gRPC on localhost (or for older browsers), because the web-client connects using HTTP/1, while the Kotlin server only accepts HTTP/2.

## Dividing work between 3 developers
The division of work depends on the state of the project, when starting a new project (like in this assignment), it may be good to dedicate setup work like front-end components (the scss part) to one individual, while others set up the React app, gRPC protos and the server.

When initial setup and global front-end components are done, it might be a good idea to work on one story with the three developers at the same time. 
In that way, all developers are aware of the quirks and requirements of the stories and are able to apply fixes or expansions when needed in the future.

I would imagine that when starting a story, one developer sets up the protos and stubs (on the server), so implementation of the actual logic and front-end can be divided.

This way of working will require setting up main story branches where small pull requests can be rapidly reviewed and merged to. Once the main story is implemented, that story branch can be merged into a master branch.

## Potential problems in production
The server currently retrieves all cars from the Mongo database when providing the list of annual costs. 

In a real production situation, we may want to move the annual cost calculation to a (suitable) database query, offloading the calculations and sorting so that the webserver isn't hogging too much resources.

For mongo, this would probably use aggregation The calculation would look something like this:
```
{
    newField:
    {$add:
            [
                {
                    $add:
                        [
                            {
                                $divide:
                                    [
                                        {$multiply: [INSERT_MONTHLY_DISTANCE, 12]},
                                        "$fuelConsumption"
                                    ]
                            },
                            "$maintenanceCostInCents"
                        ]
                },
                {$multiply: ["$priceInCents", INSERT_DEPRECIATION_PERCENTAGE]}
            ]
    }
}
```

This probably requires a good evaluation on what database engine is best for this use case.

## Running the application
The application consists of three parts: The server (Kotlin), the client (React) and the Envoy proxy (to make grpc work in the browser).

To start it all up, do the following:

### Server

#### Run locally
- Run `./gradlew CarCatalogueServer`

#### Server (compile jar and run stand-along)
- Run `./gradlew installDist` to compile the code
- Run `./server/build/install/server/bin/car-catalogue-server` to start the server

### Envoy
You need to have Docker installed for this
- Run `docker-compose build` to create the docker container
- Run `docker-compose up` to start the docker container

### Client
Execute these commands in the client folder
- Run `brew install protobuf` to install protobuf for compiling proto files for web
- Run `npm install` to install dependencies
- Run `npm run generate-proto` to generate the gRPC JavaScript/TypeScript files
- Run `npm start` to start the react web application