
# TP 3 : ReplicaSets

## TP 3.1 : Liveness probes

### Nettoyage

- Supprimer tous les pods et services du namespace __default__ à l'aide de la commande `kubectl delete all --all`
- Optionnel : observer les différents états des ressources avec la commande `watch kubectl get pod,rs --show-labels`

### Liveness 1

- Créer un descripteur avec le contenu suivant (source : `workspaces/Lab3/pod--liveness-probe-starter--3.1.yml`) :

```yaml
---
apiVersion: v1
kind: Pod
metadata:
  labels:
    test: liveness
  annotations:
    what-do-you-like: crash-with-error-500
  name: liveness-http
spec:
  containers:
  - name: i-am-alive
    image: gcr.io/google_containers/liveness
    args:
    - /server
```

- Ajouter une sonde de type _liveness_ pour le container `i-am-alive` :
  - de type httpGet
  - sur le port `8080` du container
  - qui teste le chemin `/healthz`
  - délai initial : 10s
  - timeout : 1s
  - le nombre minimum d'échecs pour  que la sonde soit considérée en état __échec__ : 5
- Créer le pod
- Observer les états successifs du pod
- Afficher les événements associés au pod

### Liveness 2

- Créer un pod à partir du descripteur suivant (source : `workspaces/Lab3/pod--liveness-probe-kubia--3.2.yml`) :

```yaml
---
apiVersion: v1
kind: Pod
metadata:
  name: kubia-liveness
  annotations:
    i-will-crash: after-5-calls-to-slash
spec:
  containers:
    - image: luksa/kubia-unhealthy
      name: kubia
      livenessProbe:
        httpGet:
          path: /
          port: 8080
        periodSeconds: 5
```

- Observer les états successifs du pod (attendre une trentaine de secondes une fois que le pod est créé)
- Afficher les événements associés au pod

<div class="pb"></div>

## TP 3.2 : ReplicaSets

### Premier RS

- Créer un _ReplicaSet_ appelé `nginx-simple` à partir du descripteur suivant (source : `workspaces/Lab3/rs--nginx-simple--3.2.yml`) :

```yaml
---
apiVersion: apps/v1 # Utiliser 'apps/v1' à partir de Kubernetes v1.9
kind: ReplicaSet
metadata:
  name: nginx
spec:
  replicas: 3
  selector:
    matchLabels:
      app: nginx
      env: int
  template:
    metadata:
      name: nginx
      labels:
        app: nginx
        env: int
    spec:
      containers:
        - name: nginx
          image: 'nginx:alpine'
          ports:
            - containerPort: 80
```

- Surveiller l'avancement de la création du _RS_ et des pods associés avec la commande `watch kubectl get pod,rs --show-labels`
- Supprimer un des pods créés et vérifier que le _RS_ fait son travail
- Créer un pod à partir du fichier `workspaces/Lab3/pod--looks-like-a-nginx--3.3.yml`
- Modifier le label `app` du dernier pod créé en remplaçant `imposteur` par `nginx` et observer


### RS et pod selector

- Créer un _RS_ à partir du descripteur suivant (source : `workspaces/Lab3/rs--nginx-cannot-create--3.2.yml`) :

```yaml
---
apiVersion: apps/v1 # Utiliser 'apps/v1' à partir de Kubernetes v1.9, sinon apps/v1beta2
kind: ReplicaSet
metadata:
  name: frontend
spec:
  replicas: 2
  selector:
    matchLabels:
      app: frontend
      env: qlf
  template:
    metadata:
      name: frontend
      labels:
        app: frontend
        env: int
    spec:
      containers:
      - name: nginx-fe
        image: nginx:alpine
        ports:
        - containerPort: 80

```

- Que se passe-t-il ?
- Corriger le _selecteur_ dans le descripteur et créer le _RS_
- Vérifiez que le ReplicaSet a été créé correctement

### Modifications de RS

- Modifier le template du _RS_ `frontend` en changeant l'image _nginx_ utilisée pour `nginx:stable-alpine`
- Appliquer la nouvelle configuration du _RS_
- Vérifier qu'il n'y a pas de recréation des pods
- Modifier avec `kubectl label` les labels sur les pods `frontend-xxxxx` en surchargeant la valeur pour la clé _env_ à `rd`
- Que se passe-t-il ? Pourquoi ?
- Vérifier que l'image docker utilisée par les nouveaux pods créés est bien `nginx:stable-alpine`

### Mise à l'échelle d'un RS

- Modifier le nombre de replicas du _RS_ `frontend` en le passant à _3_ en utilisant `kubectl scale`
- Modifier le nombre de replicas du _RS_ `frontend` en le passant à _2_ en utilisant `kubectl edit`
- Modifier le nombre de replicas du _RS_ `frontend` en le passant à _4_ en utilisant `kubectl apply`
- Utiliser `kubectl describe pod frontend-xxxx` sur l'un des pods associés au ReplicaSet frontend et noter la valeur du champ `Controlled By`
- Utiliser `kubectl get pod frontend-xxxx -o yaml` sur l'un des pods associés au ReplicaSet frontend et noter la valeur du champ `.metadata.ownerReferences`

### Suppression d'un RS

- Supprimer le _RS_ `nginx` en supprimant aussi ses pods
- Supprimer le _RS_ `frontend` _*sans supprimer*_ ses pods
- Utiliser `kubectl describe pod frontend-xxxx` sur l'un des pods associés au ReplicaSet frontend et noter que le champ `Controlled By` n'est plus présent
- Utiliser `kubectl get pod frontend-xxxx -o yaml` sur l'un des pods associés au ReplicaSet frontend et noter que le champ `.metadata.ownerReferences` n'est plus présent


<div class="pb"></div>

## TP 3.3 : ReplicaSets et matchExpressions

### Migration de pods d'un RS à un autre

- Créer un _ReplicaSet_ (appelé `frontend-rebirth`) pour piloter les pods auparavant gérés par le _RS_ `frontend` (`app=frontend,env=int`)
- Vérifier l'état du _ReplicaSet_ avec `kubectl get rs frontend-rebirth`
- Utiliser `kubectl describe pod frontend-xxxx` sur l'un des pods associés au ReplicaSet frontend et noter la valeur du champ `Controlled By`
- Utiliser `kubectl get pod frontend-xxxx -o yaml` sur l'un des pods associés au ReplicaSet frontend et noter la valeur du champ `.metadata.ownerReferences`

### Étendre le ReplicaSet

- Lister les pods et rs en cours `watch kubectl get po,rs --show-labels` (vous devriez avoir 4 pods app=frontend)
- Supprimer le _ReplicaSet_ sans supprimer les _Pods_ associés avec la commande `kubectl delete rs frontend-rebirth --cascade=false`
- Etendre la portée du _RS_ frontend-rebirth pour qu'il corresponde à :
  - app=frontend
  - env parmi (int, rd)
- Que se passe-t-il pour les pods app=frontend
- Afficher les informations détaillées du _RS_ frontend-rebirth

<div class="pb"></div>

## TP 3.4 : DaemonSets

### Premier DaemonSet

- Créer un _DaemonSet_ à partir du descripteur ci-dessous (source : `workspaces/Lab3/ds-ssd-tp-3.4.yml`) :

```yaml
---
apiVersion: apps/v1
kind: DaemonSet
metadata:
  name: ssd-monitor
spec:
  selector:
    matchLabels:
      app: ssd-monitor
  template:
    metadata:
    labels:
      app: ssd-monitor
    spec:
      nodeSelector:
        disk: ssd
      containers:
      - name: main
        image: luksa/ssd-monitor
```

- Lister les _DaemonSets_ avec `kubectl get ds`
- Que remarquez-vous ?
- Lister les pods pour voir si des pods associés au _DaemonSet_ `ssd-monitor` ont été créés

### Mise en place du label sur les noeuds

- Ajouter le label `disk=ssd` sur le noeud minikube
- Lister les _DaemonSets_ avec `kubectl get ds`
- Lister les pods pour voir si des pods associés au _DaemonSet_ `ssd-monitor` ont été créés
- Accéder aux logs du pod créé par le _DaemonSet_ `ssd-monitor`
- Supprimer le pod créé par la _DaemonSet_ et vérifiez qu'il est bien recréé
- Remplacez le label `disk=ssd` sur le noeud minikube par `disk=hdd`
- Que constatez vous ?

<div class="pb"></div>

## TP 3.5 : Jobs et CronJobs

### Premier Job

- Créer un _Job_ à partir du descripteur suivant (source : `workspaces/Lab3/job--compute-pi--3.5.yml`) :

```yaml
---
apiVersion: batch/v1
kind: Job
metadata:
  name: pi
spec:
  template:
    metadata:
      name: pi
    spec:
      containers:
      - name: pi
        image: perl
        command: ["perl",  "-Mbignum=bpi", "-wle", "print bpi(2000)"]
      restartPolicy: Never
```

- Accéder aux logs du job une fois celui-ci terminé (pour voir les pods terminés, utiliser l'option `--show-all`)
- Afficher les informations détaillées sur le _Job_

### Un peu de parallèlisme

- Faites les modifications pour que le _Job_ soit lancé 5 fois, avec 2 occurrences en parallèle

### CronJob (Optionnel)

- Créer le _CronJob_ à partir du descripteur suivant (source : `workspaces/Lab3/cron--hello-from-k8s-cluster--3.5.yml`) :

```yaml
---
apiVersion: batch/v1beta1
kind: CronJob
metadata:
  name: hello
spec:
  schedule: "*/1 * * * *"
  jobTemplate:
    spec:
      template:
        spec:
          containers:
          - name: hello
            image: busybox
            args:
            - /bin/sh
            - -c
            - date; echo Hello from the Kubernetes cluster
          restartPolicy: OnFailure
```

- Après avoir créé le _CronJob_, récupérer son statut avec `kubectl get cronjob`
- Surveiller le premier _Job_ associé : `kubectl get jobs --watch`
- Récupérer le statut du _CronJob_ une fois que le premier _Job_ est passé
- Penser à supprimer le _CronJob_ pour arrêter l'exécution des Jobs pour le reste de la formation

<div class="pb"></div>
