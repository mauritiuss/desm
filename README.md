# DESM — Distributed Energy Supply Management

A distributed energy management simulation developed in Java as an individual university project for the **Distributed and Pervasive Systems** course at the University of Milan.

The project demonstrates how independent processes can coordinate in a peer-to-peer network using distributed algorithms, remote procedure calls and asynchronous messaging.

## Overview

DESM simulates an energy supply network in which a renewable energy provider requests additional energy from a distributed network of thermal power plants.

Each power plant competes to fulfill incoming energy requests. The selection process is decentralized and based on the lowest offered price, with a deterministic tie-breaking rule.

The system also simulates environmental monitoring by collecting and aggregating CO₂ emission measurements.

## Key Features

- **Distributed coordination:** Peer-to-peer communication between independent power plant processes using gRPC.
- **Leader election:** Implementation of the Chang–Roberts ring election algorithm, adapted to the project's bidding requirements.
- **Asynchronous messaging:** MQTT-based publication of energy requests and environmental measurements.
- **Dynamic network membership:** Registration and controlled removal of power plants.
- **Concurrent processing:** Synchronization mechanisms for shared data and producer-consumer workflows.
- **REST API:** Administration server for plant registration and environmental statistics.
- **Sensor data aggregation:** Processing of simulated CO₂ measurements using sliding windows.

## Architecture

The application consists of four main components:

**Renewable Energy Provider**
Publishes energy requests through an MQTT broker.

**Thermal Power Plants**
Independent Java processes that participate in distributed elections, handle energy requests and publish simulated emission measurements.

**Administration Server**
Provides REST endpoints for plant registration and statistics while collecting environmental measurements through MQTT.

**Administration Client**
Command-line application for interacting with the administration server.

### Communication Protocols

| Protocol | Purpose |
|---|---|
| gRPC | Peer-to-peer coordination and distributed elections |
| MQTT | Energy request publication and sensor data transmission |
| REST | Plant registration and administrative queries |

## Technology Stack

- Java
- Gradle
- gRPC
- MQTT / Eclipse Mosquitto
- REST APIs
- Multithreading and synchronization

## Distributed Algorithm

The project implements a ring-based election mechanism inspired by the Chang–Roberts algorithm.

When an energy request is received, available power plants participate in a decentralized election. The plant offering the lowest price is selected, with the highest plant ID used as a tie-breaker.

## Project Scope

This is an educational simulation designed to explore distributed systems concepts, including concurrency, message passing, decentralized coordination and dynamic network membership.

The implementation includes controlled node removal. Recovery from unexpected node failures was outside the required project scope.

## Author

Davide Mauri  
MSc in Computer Science — University of Milan

[GitHub Profile](https://github.com/mauritiuss)
