
# TP 2 : Pods, labels, annotations, namespaces

## TP 2.1 : Pods

### Prise en main de 'kubectl create'

- Afficher l'aide en ligne de `kubectl create` et parcourir les options possibles

### Utilisation d'un descripteur au format yaml

- Créer un modèle de descripteur pour un pod au format `yaml` (en passant par `kubectl run POD_NAME --image=IMAGE_NAME --port=PORT_VALUE --generator=run-pod/v1 --dry-run -o yaml` par exemple) :
  - nommer le pod `yaml-pod`
  - à partir d'une image `containous/whoami:latest`
  - exposer le port `80` du container
- Ajuster le descripteur pour ajouter un second container
  - appelé `shell-in-pod`
  - à partir de l'image `centos:7`
  - le process principal du container sera `sleep infinity` (ne pas hésiter à utiliser `kubectl explain` pour trouver la syntaxe permettant de le faire)
  - forcer Kubernetes à vérifier que l'image est à jour en spécifiant le `imagePullPolicy`
- Créer le pod avec `kubectl create -f <fichier>`

### Utilisation d'un descripteur au format json

- Créer un modèle de descripteur pour un pod au format `json` à partir de la configuration du pod précédent (tip : utiliser `kubectl get po POD_NAME --export -o json`)
  - nommer le pod `json-pod`
- Créer le pod avec `kubectl create`

### Tester le WhoAmI en passant par le container sidecar

- Afficher l'aide en ligne de `kubectl exec`
- Se connecter dans le container `shell-in-pod` du pod `yaml-pod` pour ouvrir une session bash à l'aide de `kubectl exec`
- Vérifier que le service `whoami` est bien accessible sur le port `80` de `localhost`
- Lister tous les process avec `ps auxwww` et constater (ou pas) que vous voyez les process des 2 containers du pod

### Modifier un pod

- Afficher l'aide en ligne de `kubectl apply` pour en parcourir les différentes options
- Afficher les informations détaillées pour le pod `yaml-pod` avec `kubectl describe` et avec `kubectl get pod POD_NAME -o yaml`
- Quelle est la valeur de l'annotation `kubectl.kubernetes.io/last-applied-configuration` ? [nous verrons à quoi peuvent servir les annotations dans la section suivante]
- Modifier le descripteur yaml pour ajouter un label `from-descriptor` avec comme valeur `yaml` (s'appuyer sur l'exemple ci-dessous, source : `workspaces/Lab2/pod--centos-with-label--2.1.yml`) [nous verrons à quoi servent les labels dans la section suivante]

```yaml
---
apiVersion: v1
kind: Pod
metadata:
  labels:
    my-label: cool
    env: rd
  name: centos-pod-with-labels
spec:
  containers:
  - image: centos:7
    imagePullPolicy: Always
    name: shelly
    command:
      - "bash"
      - "-c"
      - "sleep infinity"
  restartPolicy: Always
```

- Appliquer la modification sur le pod `yaml-pod` (noter le message de `Warning`)
- Quelle est désormais la valeur de l'annotation `kubectl.kubernetes.io/last-applied-configuration` ?
- Ajouter un nouveau label `standalone` avec comme valeur `"true"` (_Note_ : les guillemets autour du `true` sont ici importants car la valeur attendue côté `api server` Kubernetes est une _String_; si les guillemets ne sont pas présent, la valeur envoyée sera un booléen et la création de la ressource sera refusée. À noter qu'il est aussi possible d'utiliser des apostrophes plutôt que des guillemets)
- Appliquer la modification sur le pod `yaml-pod` (noter que le message de `Warning` n'apparaît plus)

### Créer un pod avec 'kubectl apply'

- Supprimer le pod `json-pod` (mais ne pas supprimer le descripteur json associé)
- Re-créer le pod avec `kubectl apply`
- Quelle est la valeur de l'annotation `kubectl.kubernetes.io/last-applied-configuration` ?
- Modifier le descripteur json pour ajouter un label `from-descriptor` avec comme valeur `json`
- Appliquer la modification sur le pod `json-pod` (noter que le message de `Warning` n'apparaît pas)

### Visualiser la definition d'un pod

- Lancer la commande `kubectl get po yaml-pod -o yaml`
- Observer les différentes sections de la description

<div class="pb"></div>

## TP 2.2 : Labels

### Prise en main

- Afficher l'aide en ligne de `kubectl label`
- Afficher les labels des pods existants
- Ajouter les labels `release=stable` et `env=int` aux pods `yaml-pod` et `json-pod`
- Modifier le label `release=stable` en `release=unstable` pour le pod `yaml-pod`
- Afficher la description complète du pod `yaml-pod` et vérifier que les labels attendus sont bien présents

### Afficher les labels des pods

- Afficher tous les labels de tous les pods
- Afficher les labels `run`, `release` et `env` dans des colonnes dédiées
- N'afficher que les pods qui ont le label `env` positionné
- N'afficher que les pods qui n'ont pas le label `release` positionné
- Afficher les pods pour lesquels le label `run` vaut `whoami` ou `json-pod`
- Afficher les pods pour lesquels le label `run` vaut `whoami` ou `json-pod` et pour lesquels `env` n'est pas spécifié

### Utilisation du nodeSelector

- Lister les labels actuels des noeuds du cluster
- Créer un descripteur de pod :
  - nom : `light-sleeper`
  - 1 container :
    - Utilisation de l'image `alpine:3.6`
    - Process principal : `ash -c 'sleep 3d'`
  - Labels :
    - `env = qlf`
    - `from-descriptor = yaml`
  - Forcer le pod à être schédulé sur un noeud qui porte le label `container-runtime=docker`
- Créer le pod
- Lister tous les pods dans un terminal en parallèle avec `watch kubectl get po`
- Quel est l'état du pod `light-sleeper` ? Pourquoi ?
- Récupérer le nom de votre noeud avec `kubectl get nodes` (qui sera `minikube` la plupart du temps sauf si vous avez démarré minikube avec l'option `--vm-driver=none`)
- Ajouter le label `container-runtime=docker` sur le noeud
- Vérifier que le pod `light-sleeper` est bien démarré

<div class="pb"></div>

## TP 2.3 : Annotations et Namespaces

### Annotations

- Consulter l'aide en ligne de la commande `kubectl annotate`
- Consulter les annotations existantes sur le pod `yaml-pod`
- Ajouter l'annotation `super.mycompany.com/ci-build-number=722` au pod `yaml-pod`
- Vérifiez que l'annotation est bien présente sur le pod

### Namespaces

- Afficher la liste de tous les namespaces
- Afficher tous les pods du namespace `kube-system`
- Afficher tous les pods de tous les namespaces

### Création d'un nouveau namespace

- Créer un nouveau namespace `my-sandbox`par le biais d'un fichier descripteur :
  - associer le label `sandbox=true` à ce namespace
- Créer une copie du pod `yaml-pod` dans le namespace `my-sandbox` sans modifier le descripteur du pod
- Créer une copie du pod `json-pod` dans le namespace `my-sandbox` sans spécifier le namespace cible dans la ligne de commande (et sans changer le namespace par défaut)
- Lister les pods du namespace `my-sandbox`
- Supprimer le namespace `my-sandbox`

<div class="pb"></div>

## TP 2.4 : logs et cycle de vie

### Prise en main

- Consulter l'aide en ligne de `kubectl logs`
- Consulter les logs du pod `whoami` créé lors du Lab 2.1
- Créer un pod à partir du descripteur suivant (source : `workspaces/Lab2/pod--whoami-and-clock--2.4.yml`)

```yaml
---
apiVersion: v1
kind: Pod
metadata:
  labels:
    env: int
  name: whoami-and-clock
spec:
  containers:
  - image: containous/whoami:latest
    imagePullPolicy: IfNotPresent
    name: whoami
    ports:
    - containerPort: 80
  - image: centos:7
    name: clock
    command:
      - "bin/bash"
      - "-c"
      - "while true; do date | tee /dev/stderr; sleep 1; done"
```

- Consulter les logs du container `clock` du pod `whoami-and-clock`
- Expérimenter les options `--follow`, `--tail` et `--since`

### Cycle de vie

- Dans la vm Minikube, identifier le container docker correspondant au container `clock` du pod `whoami-and-clock`
- Depuis l'extérieur de la VM, exécuter la commande `watch kubectl get po`
- Terminer le container identifié précédemment (avec `docker stop` ou `docker kill`)
- Vérifier que Kubernetes redémarre bien le container dans le pod `whoami-and-clock` et que le compteur de __restart__ a évolué

### Restart Policy

- Créer un pod à partir du descripteur suivant (source : `workspaces/Lab2/pod--restart-policy-rp-check--2.4.yml`) :

```yaml
---
apiVersion: v1
kind: Pod
metadata:
  labels:
    app: rpchk
    env: int
  name: rp-check
spec:
  restartPolicy: Always
  containers:
    - image: alpine:3.6
      name: rp-check
      command:
        - "ash"
        - "-c"
        - "sleep 15s"
```

- Surveiller la liste des pods avec un `watch kubectl get po`
- Que constatez-vous ?
- Supprimer le pod créé
- Modifier la valeur de `restartPolicy` à `OnFailure`
- Recréer le pod
- Surveiller la liste des pods avec un `watch kubectl get po`
- Que constatez-vous ?
- Afficher la liste des pods.
- Supprimer le pod `rp-check`
- Ajuster la commande dans le descripteur, remplacer `"sleep 15s"` par `"sleep 15s ; false"` (Note : la commande false retourne un status code qui vaut 1)
- Recréer le pod `rp-check`
- Surveiller la liste des pods avec un `watch kubectl get po`
- Que constatez-vous ?

<div class="pb"></div>

## TP 2.5 : Init Containers

### Experimentations

- Consulter les specs des __Init Containers__ avec `kubectl explain`
- Repartir du descripteur du pod `whoami-and-clock` pour créer un nouveau pod `whoami-and-clock-with-init` :
  - Ajouter un __Init Container__ :
    - appelé `timer`
    - qui utilise l'image `centos:7`
    - qui boucle en affichant la date toutes les 1 secondes pendant 15 secondes (avec la commande `for i in {1..15}; do date; sleep 1s; done` par exemple)
- Lancer le pod `whoami-and-clock-with-init`
- Vérifier avec un `watch kubectl get po whoami-and-clock-with-init` que le pod met plus de 15 secondes pour se lancer
- Accéder aux logs du container `timer` et vérifier qu'ils contiennent l'affichage de la date comme attendu

<div class="pb"></div>
