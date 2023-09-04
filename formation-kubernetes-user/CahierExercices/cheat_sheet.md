# Commandes usuelles pour interagir avec Docker/Kubernetes

## Docker

Créer un conteneur en mode interactif :
`docker container run -it ubuntu bash`

Créer un conteneur en mode interactif en modifiant l'entrypoint :
`docker container run --entrypoint bash -it ubuntu`

Lancer un conteneur en mode détaché :
`docker container run -d centos sleep infinity`

Afficher les conteneurs en cours d'exécution :
`docker container ls`

Ouvrir un shell dans un conteneur en mode interactif :
`docker container exec -it <CONTAINER_NAME> bash`

Construire une image Docker :
`docker image build . -t mon_image:mon_tag`

## Kubernetes

### Pods
Créer un pod en mode interactif
`kubectl run -ti --image=ubuntu --restart=Never --rm bash`

Lister les Pods en cours d'exécution du namespace `staging` :
`kubectl get pods -n staging`

Lister les Pods en cours d'exécution avec leur IP et noeud d'exécution :
`kubectl get pods -o wide`

Afficher le manifest `yaml` d'un Pod :
`kubectl get pod <POD_NAME> -o yaml`

Ouvrir un shell dans un Pod/conteneur en mode interactif :
`kubectl exec -it <POD_NAME> bash`

Si le Pod a plusieurs conteneurs, utiliser cette commande :
`kubectl exec -it <POD_NAME> -c <CONTAINER_NAME> bash`

Afficher les logs d'un Pod :
`kubectl logs <POD_NAME>`

Afficher et suivre les logs d'un Pod :
`kubectl logs <POD_NAME> -f`

Afficher les logs d'un conteneur spécifique d'un Pod :
`kubectl logs <POD_NAME> -c <CONTAINER_NAME>`

Afficher les informations complètes d'un Pod (dont les événements) :
`kubectl describe pod <POD_NAME>`

### Namespaces

Lister les Namespaces :
`kubectl get ns`

Crée un Namespace :
`kubectl create ns <NOM_NAMESPACE>`

### Service

Lister les services :
`kubectl get services`

Lister les endpoints :
`kubectl get endpoints`
