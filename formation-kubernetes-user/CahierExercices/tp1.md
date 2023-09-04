
# TP 1 : Premiers pas

## 1.1 : Utilisation de Minikube et kubectl

### Démarrage de Minikube

Après vous être connecté à votre VMs Guacamole avec les identifiants donnés par votre formateur, veuillez démarrer Minikube :

```
minikube start --vm-driver none
```

**ATTENTION : Minikube a été lancé dans une VM distante (AWS), veuillez suivre les bonnes directives dans la suites des exercices**

### Gestion des addons

- Lister les addons activés/désactivés
- Vérifier que le addon `dashboard` est bien activé
- Activer le addon `heapster` s'il n'est pas activé

### Fichiers de configuration

Jetter un oeil aux fichiers :

- ~/.minikube/config/config.json
- ~/.minikube/profiles/minikube/config.json

<div class="pb"></div>

## TP 1.2 : Dashboard

### Récupérer l'ip de votre instance Minikube

- lancer la commande `minikube ip` si vous n'êtes pas en mode __Docker__ (`--vm-driver=none`)
- lancer la commande `curl ifconfig.me` si vous êtes dans une VM (AWS ou autre)

### Accéder au dashboard et aux métriques du cluster

- Si vous êtes en mode __Docker__, exposer le dashboard avec la commande `kubectl proxy --address='0.0.0.0' --disable-filter=true &`
- Lancer le dashboard avec la commande `minikube dashboard --url &`
- Ouvrir dans votre navigateur l'URL indiquée en adaptant éventuellement, si vous êtes en mode __Docker__, l'ip par celle de votre instance minikube et le port par `8001` (ou celui indiqué par la commande de proxy)
- Explorer les différents menus
- Optionnel : accéder aux métriques de `Heapster` par le biais de Grafana
  - Retrouver le port sur lequel est exposé le service `monitoring-grafana` dans le __namespace__ `kube-system`

<div class="pb"></div>

## TP 1.3 : kubectl

### Prise en main

- Afficher la liste de toutes les commandes disponibles
- Vérifier que la complétion est bien en place si vous l'aviez mise en place auparavant
- Afficher les versions du client (kubectl) et du cluster
- Afficher la liste des options communes à toutes les commandes

### Infos sur le cluster

- Afficher le nom du cluster actuellement utilisé (en utilisant `kubectl config`)
- Afficher les ressources utilisées sur les différents noeuds du cluster
- Afficher les ressources utilisées par les différents pods (tous `namespaces`confondus)



<div class="pb"></div>

## TP 1.4 : Groupes et Ressources de l'api

### Visualisation des groupes et ressources disponible

- Utiliser `kubectl api-versions` pour afficher les groupes supportés par le cluster

<div class="pb"></div>


## TP 1.5 : Docker Warmup

### Accéder à l'environnement Docker

- Se connecter dans la VM `minikube` avec `minikube ssh` si vous utilisez une instance Minikube sur votre poste de travail
- Ou bien se connecter dans la VM où Minikube tourne en mode `--vm-driver none` (cas d'une VM AWS par exemple)
- Vérifier que vous pouvez vous connecter au daemon docker
- Vérifier que votre version de __Docker__ est compatible avec __Kubernetes__

### Démarrage d'un container WhoAmI avec Docker

- Lancer un container :
  - en spécifiant son nom `whoami`
  - en mode daemon
  - à partir de l'image `containous/whoami:latest`
- Récupérer l'ip du container `whoami`
- Lancer la commande `curl (ip_de_whoami):80/api`
  - Pourquoi cela fonctionne-t-il ?
- Lancer la commande `curl localhost:80/api`
  - Que se passe-t-il ?
  - Pourquoi ?

### Démarrage d'un container Shell avec Docker

- Lancer un container :
  - en spécifiant son nom `shell`
  - en mode daemon
  - à partir de l'image `centos:7`
  - dont le process principal est la commande `sleep infinity`
- Se connecter dans le container `shell` et lancer la commande `curl (ip_de_whoami):80/api`
  - Pourquoi cela fonctionne-t-il ?
- (toujours depuis le container `shell`) Lancer la commande `curl localhost:80/api`
  - Que se passe-t-il ?
  - Pourquoi ?
- Sortir du container `shell`

### Démarrage d'un container Shell sidekick du container WhoAmI

- Lancer un container :
  - en spécifiant son nom `whoami-shell`
  - en mode daemon
  - à partir de l'image `centos:7`
  - qui utilise le même namespace que le container `whoami` (`--net=container:whoami --ipc=container:whoami --pid=container:whoami`)
  - dont le process principal est la commande `sleep infinity`
- Se connecter dans le container `whoami-shell` et lancer la commande `curl (ip_de_whoami):80/api`
  - Pourquoi cela fonctionne-t-il ?
- (toujours depuis le container `whoami-shell`) Lancer la commande `curl localhost:80/api`
  - Que se passe-t-il ?
  - Pourquoi ?
- Installer le package `iproute` (ou `net-tools` pour les nostalgiques) [`yum install iproute`]
- Lancer la commande `ip addr`, quelle est l'ip du container `whoami` (note : `ip addr` est la commande qui remplace la commande dépréciée `ifconfig`, voir [https://dougvitale.wordpress.com/2011/12/21/deprecated-linux-networking-commands-and-their-replacements/](https://dougvitale.wordpress.com/2011/12/21/deprecated-linux-networking-commands-and-their-replacements/))
- Lancer la commande `ss -lntp`, quels sont les ports en écoute ? (note: `ss -lntp` est la commande qui remplace la commande dépréciée `netstat -lntp` voir [https://dougvitale.wordpress.com/2011/12/21/deprecated-linux-networking-commands-and-their-replacements/](https://dougvitale.wordpress.com/2011/12/21/deprecated-linux-networking-commands-and-their-replacements/))
- Lancer la commande `ps auxwww`
  - Que constatez-vous ?
- Sortir du container `whoami-shell`

### Exposer le service WhoAmI en dehors de Minikube

- Stopper et détruire le container `whoami`
- Le relancer en exposant le port `80` du container sur le port `8080` de la VM hébergeant Minikube
- Vérifier avec un navigateur que le service est accessible (les urls `/` et `/api` doivent répondre)

### Nettoyage

- Supprimer les containers :
  - `whoami`
  - `shell`
  - `whoami-shell`
- Sortir de la VM `minikube`

<div class="pb"></div>


## TP 1.6 : kubectl run/expose

### Prise en main

- Afficher l'aide en ligne de `kubectl run`

### Première tentative pour démarrer un pod

- Utiliser la commande `kubectl run faulty-whoami --image=containous/whoami:nil --port=80 --generator=run-pod/v1` qui va :
  - démarrer un __pod__ (nous verrons plus en détail par la suite ce qu'est un pod)
  - dont le nom est `faulty-whoami`
  - à partir de l'image `containous/whoami:nil`
  - et expose le port `80`
- Surveiller le démarrage du pod à l'aide de la commande `watch kubectl get po` (__ctrl+c__ pour sortir de la boucle __watch__)
- Constater que le pod associé ne démarre pas correctement
- Afficher plus d'informations sur le pod pour accéder aux logs de sa création avec `kubectl describe` et déterminer la cause du disfonctionnement constaté

### Démarrer un pod (seconde tentative)

- Utiliser la commande `kubectl run whoami --image=containous/whoami:latest --port=80 --generator=run-pod/v1` qui va :
  - démarrer un pod appelé `whoami`
  - à partir de l'image `containous/whoami:latest`
  - et expose le port `80`
- Surveiller le démarrage du pod à l'aide de la commande `watch kubectl get po` (__ctrl+c__ pour sortir de la boucle __watch__)
- Que constatez-vous ?
- Afficher plus d'informations sur le pod pour accéder à plus d'informations par la commande `kubectl describe` et récupérer notamment son adresse IP dont vous aurez besoin par la suite
- Se connecter dans la VM `minikube` (avec `minikube ssh`) et lancer la commande `curl (ip-du-pod-whoami):80/api` pour vérifier que le composant démarré fonctionne

### Démarrer un pod Centos

- Utiliser la commande `kubectl run centos-shell --image=centos:7 --generator=run-pod/v1 --command -- sleep infinity` qui va :
  - démarrer un pod appelé `centos-shell`
  - à partir de l'image `centos:7`
  - dont le process principal est `sleep infinity`
- Surveiller le démarrage du pod
- Lister tous les pods tournants actuellement
- Lancer la commande `kubectl exec -ti centos-shell curl (ip-du-pod-whoami):80/api`
- Afficher l'aide en ligne pour la commande `kubectl exec`

### Accéder au pod whoami sans passer par un service

- Afficher l'aide en ligne pour `kubectl port-forward`
- Utiliser la commande `kubectl port-forward` pour forwarder le port 80 du pod `whoami` vers le port 8888 local
- Vérifier que l'url `http://localhost:8888/api` retourne le résultat attendu avec la commande curl `curl http://localhost:8888/api` ou avec un navigateur si vous utilisez minikube en local.

### Optionnel : Utilisation du Dashboard

- Accéder au dashboard __Kubernetes__ et parcourir les différents menus pour retrouver les mêmes informations que celles récupérées en ligne de commande

<div class="pb"></div>
