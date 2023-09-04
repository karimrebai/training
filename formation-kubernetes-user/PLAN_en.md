Domain : [DevOps]

# Kubernetes

## Orchestrate containers with Kubernetes

Duration: 3d

Price: ?€

## Context

Deploying applications as containers is becoming a shared defacto method of managing component deployments, especially when companies are more and more tempted to split their applications into smaller components that need to communicate with each other.

Container orchestrators are the best way to deal with problematics like fault tolerance, high availability,
load-balancing and service discovery.

Despite stiff competition, the industry seems to be settling on Kubernetes as the default container orchestration engine. Its reputation and maturity are built upon a decade and a half of experience that Google has with running production workloads at scale combined with best-of-breed ideas and practices from the community.

The goal of this training is to help you discover how to use Kubernetes and how it can help you deploy your containerized workloads. In the same time, we will see what are the constraints that your components will have to fulfill so to you can use Kubernetes efficiently.

This training does not deal with the installation of a production ready Kubernetes instance.

## Goals

* deploy simple components (stateless and stateful)
* deploy collaborating components
* expose a component inside the Kubernetes instance and outside of the Kubernetes infrastructure
* store and share configuration, secrets and persistent data
* update component with or without zero downtime
* share best practices regarding the development and the deployment of containerized workloads

## Teaching method

50% theory, 50% practice

## Target audience

* Developers
* Tech Lead
* Technical and Solutions Architects
* Technical platform Operators

## Prerequisites

Knowledge of container technologies (mainly Docker)

## Program

1. Context
   1. Reminders about containers
   1. Containers without orchestration
   1. What is orchestration?
   1. Orchestrators
   1. Kubernetes
   1. Kubernetes distributions
   1. Versions and API
1. Getting started with Kubernetes
   1. Kubernetes on local workstation
   1. Dashboard and CLI
   1. Start and expose a container
1. Pods
   1. The concept
   1. Yaml and Json descriptors
   1. Organize pods and kubernetes objects with labels, selectors, namespaces and annotations
   1. Pods lifecycle
   1. Init Containers
1. ReplicaSets
   1. HealthChecks
   1. ReplicaSets
   1. DaemonSets
   1. Jobs
1. Services
   1. Expose components internally
   1. Expose components to the outside (NodePort, LoadBalancers, Ingress)
   1. Readiness probes
1. Volumes
   1. Share data between containers of a pod
   1. Share data with the host/node
   1. Persistent Volumes and Persistent Volumes Claims
1. Configuration and secrets
   1. Environment variables
   1. ConfigMaps
   1. Secrets
1. Deployment strategies
   1. Update pod content
   1. Imperative Rolling Update
   1. Declarative Rolling Update and the object 'Deployment'
1. Stateful sets
1. Architecture and components of a Kubernetes instance
1. Enterprise ready features
   1. Resource (cpu/memory) management
   1. Horizontal Auto-scaling
   1. Compatible Apps
   1. Helm as an example of Kubernetes package management
