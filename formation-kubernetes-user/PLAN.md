Domaine: [DevOps]

# Kubernetes

## Orchestrer ses containers avec Kubernetes

Durée: 3j

Prix: ?€

## Présentation

Déployer ses applications sous la forme de containers/conteneurs est de plus en plus courant, notamment dans les architectures techniques prônant l’organisation d’une application comme un ensemble de services collaborant entre eux.
Afin de mieux répondre aux problématiques de répartition de charge et de tolérance à la panne des applications déployées sous forme de containers, l’utilisation d’un outil d’orchestration est fortement conseillée afin d’éviter de réinventer la roue et de pouvoir profiter de l'augmentation de productivité et de réactivité liées à l'utilisation des containers.
Kubernetes est l’un des produits les plus cités et utilisés dans ce domaine, sa réputation et sa maturité s’appuient sur l’expérience et le savoir faire de Google en matière de containers. Cette formation a pour but de découvrir comment l’utiliser tout en comprenant quelles sont les contraintes associées à son utilisation, notamment sur la façon de développer les applications qui y seront déployées.

## Objectifs

* déployer des applications simples (stateless et stateful)
* déployer une application composée de plusieurs services
* exposer une application vers l'extérieur de l’infrastructure Kubernetes
* apprendre à gérer les données manipulées par l’application dans l’infrastructure Kubernetes
* mettre à jour une application déjà déployée dans Kubernetes
* parcourir les bonnes pratiques associées au développement d’une application déployée dans Kubernetes

## Pédagogie

50% théorie, 50% pratique

## Public

* Développeurs
* Tech Lead
* Architectes techniques et solutions
* Opérateurs de plateforme technique

## Pré-requis

Connaissances des technologies de conteneurisation, notamment Docker

## Programme

1. Contexte
   1. Rappels sur les containers
   1. Containers sans orchestration
   1. Fonctionnalités d'orchestration
   1. Orchestrateurs du marché
   1. Kubernetes
   1. Distributions Kubernetes
   1. Versions et API
1. Premiers pas avec Kubernetes
   1. Installation de Kubernetes en local avec mini kube
   1. Dashboard, CLI et API
   1. Démarrer et exposer un container
1. Les pods
   1. Modèle/Concept du pod
   1. Descripteurs yaml et json
   1. Organisation des pods avec les labels, les sélecteurs et les namespaces
   1. Cycle de vie des pods
   1. Init Containers
1. ReplicaSets
   1. HealthChecks
   1. ReplicaSets
   1. DaemonSets
   1. Jobs
1. Services
   1. Exposer en interne du cluster
   1. Exposer un service vers l'extérieur de l'instance Kubernetes (NodePort, LoadBalancer, Ingress)
   1. Le concept du __readiness__
1. Volumes
   1. Partage simple de données entre 2 containers d'un même pod
   1. Accéder au fs d'un nœud du cluster
   1. Persistent Volumes et Persistent Volumes Claims
1. Configuration et secrets
   1. Variables d'environnements
   1. ConfigMaps
   1. Secrets
1. Stratégies de déploiement
   1. Mise à disposition d'une nouvelle version d'un pod
   1. Rolling Update impératif
   1. Rolling Update déclaratif et objet 'Deployment'
1. Stateful sets
1. Architecture et composants
1. Fonctionnalités nécessaires en entreprise
   1. Gestion des ressources
   1. Auto-scaling horizontal
   1. Applications Compatibles Kubernetes
   1. Helm pour simplifier vos déploiements
