
# TP 4 : Services, Ingress et LoadBalancers

## TP 4.1 : Premier service

### Préparation

- Créer le _ReplicaSet_ `whoami` à partir du descripteur suivant (source : `workspaces/Lab4/rs--whoami--4.1.yml`) :

```yaml
---
apiVersion: apps/v1
kind: ReplicaSet
metadata:
  name: whoami
spec:
  selector:
    matchLabels:
      app: whoami
      env: int
  replicas: 2
  template:
    metadata:
      labels:
        app: whoami
        env: int
    spec:
      containers:
      - image: containous/whoami:latest
        name: whoami
        ports:
          - name: main-port
            containerPort: 80
```

### Création du premier service

- Créer le descripteur permettant de créer un _Service_ appelé `whoami-int` qui écoute sur le port _8080_ et va rediriger le trafic vers tous les pods qui correspondent à `app=whoami,env=int` sur le port _80_ (le service devra aussi porter les labels `app=whoami,env=int`)
- Regarder le statut du service créé
- Afficher les informations détaillées du service
- Quelle est l'ip du service dans le cluster ?

### Créer un pod "rebond"

- Créer un pod appelé `gateway` à partir du descripteur suivant (source : `workspaces/Lab4/pod--gateway.yml`) :

```yaml
---
apiVersion: v1
kind: Pod
metadata:
  labels:
    app: gw
  name: gateway
spec:
  containers:
    - image: centos:7
      name: shell-in-pod
      command:
        - "bash"
        - "-c"
        - "sleep infinity"

```

- Se connecter dans le pod pour exécuter les commandes
  - `ping -c 1 -W 1 clusterIp-du-svc-whoami-int`
  - `curl clusterIp-du-svc-whoami-int:80/api`
- Corriger la commande curl pour qu'elle retourne un résultat
- Optionnel : inspecter les variables d'environnement dans le pod `gateway`

### Ca marche encore quand on kill les pods ?

- Depuis le pod `gateway`, exécuter plusieurs fois la commande `curl clusterIp-du-svc-whoami-int:8080/api` et vérifier que la clé `hostname` ne retourne pas toujours la même valeur
- Supprimer les 2 pods associés au _ReplicaSet_ `whoami`
- Depuis le pod `gateway`, exécuter plusieurs fois la commande `curl clusterIp-du-svc-whoami-int:8080/api` et vérifier que la clé `hostname` retourne des valeurs différentes des précédentes

### Et si on augmente le nombre de replicas ?

- Passer le _ReplicaSet_ `whoami` à 5 replicas
- Depuis le pod `gateway`, exécuter plusieurs fois la commande `curl clusterIp-du-svc-whoami-int:8080/api` et vérifier que la clé `hostname` retourne des valeurs supplémentaires par rapport au cas précédent

<div class="pb"></div>

## TP 4.2 : Service avec plusieurs ports

### RS Guestbook Storage

- Créer le _ReplicaSet_ `guestbook-storage` à partir du descripteur suivant (source : `workspaces/Lab4/rs--guestbook-storage--4.2.yml`) :

```yaml
---
apiVersion: extensions/v1beta1
kind: ReplicaSet
metadata:
  name: guestbook-storage
spec:
  replicas: 1
  template:
    metadata:
      labels:
        app: gbs
        env: int
    spec:
      containers:
      - image: looztra/guestbook-storage:0.5.2-aio
        name: storage
        ports:
        - name: main-port
          containerPort: 8080
        - name: admin-port
          containerPort: 9090
        env:
        - name: MANAGEMENT_PORT
          value: "9090"
```

### Service Guestbook Storage

- Créer le service `guestbook-storage-int` :
  - qui redirige vers les pods avec `app=gbs,env=int`
  - qui expose le port `main-port` sur le port 80 et le port `admin-port` sur le port 8000
- Tester depuis le pod `gateway` :
  - `curl ip-du-service/api/v1/hello-world` doit retourner une string json hello-world
  - `curl ip-du-service:8000/admin/info` doit retourner une string json avec des informations git

<div class="pb"></div>

## TP 4.3 : Service Discovery

### Variables d'environnement

- Se connecter dans le pod `gateway` et lister les variables d'environnement
- Aucune variable d'environnement n'est présente pour le service `guestbook-storage-int`, pourquoi ?
- Détruire le pod `gateway` et le reconstruire
- Lister les variables d'environnement
- Effectuer les mêmes requêtes curl que dans l'exercice précédent en utilisant des variables d'environnement plutôt qu'une IP

### Utilisation du DNS

- Effectuer les mêmes requêtes curl que dans l'exercice précédent en utilisant les noms DNS complet (_fqdn_) et court

<div class="pb"></div>

## TP 4.4 : Service Externe

### Endpoints

- Affichez la structure yaml du endpoints `whoami-int` associé au service du même nom
- Augmentez le nombre de replicas du _ReplicaSet_ `whoami`
- Affichez de nouveau la structure yaml du endpoints `whoami-int`
- Supprimez le sélecteur sur le service `whoami-int`
- Diminuez le nombre de replicas du _ReplicaSet_ `whoami`
- Constater que la liste des IPs du endpoints associé n'a pas été mise à jour


<div class="pb"></div>

## TP 4.5 : NodePort et LoadBalancer

### NodePort

- Modifier le service `whoami-int` en un service de type _NodePort_ sans spécifier la valeur du nodePort
- Vérifier avec `kubectl get svc whoami-int`
- Tester le service depuis l'extérieur de Minikube
- Afficher les informations détaillées du service `whoami-int`

### LoadBalancer

- Supprimer le service `whoami-int`
- Recréez le avec le type _LoadBalancer_ (supprimer la valeur du `.spec.ports.nodePort` si vous l'aviez précisé précédemment)
- Vérifier avec `kubectl get svc whoami-int` (l'IP externe ne sera jamais allouée sur Minikube)
- Tester le service depuis l'extérieur de Minikube
- Afficher les informations détaillées du service `whoami-int`


<div class="pb"></div>

## TP 4.6 : Ingress

### Ingress Controller

- Si le cluster k8s a RBAC activé etendez les droits du `ServiceAccount` traefik-ingress-controller (voir https://docs.traefik.io/user-guide/kubernetes/)

```sh
kubectl apply -f \
  https://raw.githubusercontent.com/containous/traefik/v1.7/examples/k8s/traefik-rbac.yaml
```

- Créez un fichier contenant la définition suivante (source : `workspaces/Lab4/ingress-controller--traefik--4.6.yml`) :

```yaml
---
apiVersion: v1
kind: ServiceAccount
metadata:
  name: traefik-ingress-controller
  namespace: kube-system
---
kind: Deployment
apiVersion: extensions/v1beta1
metadata:
  name: traefik-ingress-controller
  namespace: kube-system
  labels:
    k8s-app: traefik-ingress-lb
spec:
  replicas: 1
  selector:
    matchLabels:
      k8s-app: traefik-ingress-lb
  template:
    metadata:
      labels:
        k8s-app: traefik-ingress-lb
        name: traefik-ingress-lb
    spec:
      serviceAccountName: traefik-ingress-controller
      terminationGracePeriodSeconds: 60
      containers:
      - image: traefik:maroilles
        name: traefik-ingress-lb
        resources:
          limits:
            cpu: 200m
            memory: 30Mi
          requests:
            cpu: 100m
            memory: 20Mi
        ports:
        - containerPort: 80
          hostPort: 80
        - containerPort: 8080
        args:
        - --web
        - --kubernetes
```

- Appliquez-le et vérifier des pods sont bien créés (attention au namespace !)

### Ingress traefik-ui

- Créez un descripteur avec le contenu suivant (source : `workspaces/Lab4/ingress--trafik-ui--4.6.yml`) :

```yaml
---
apiVersion: v1
kind: Service
metadata:
  name: traefik-web-ui
  namespace: kube-system
spec:
  selector:
    k8s-app: traefik-ingress-lb
  ports:
  - port: 80
    targetPort: 8080
---
apiVersion: extensions/v1beta1
kind: Ingress
metadata:
  name: traefik-web-ui
  namespace: kube-system
  annotations:
    kubernetes.io/ingress.class: traefik
spec:
  rules:
  - host: traefik-ui.FIXME.xip.io
    http:
      paths:
      - backend:
          serviceName: traefik-web-ui
          servicePort: 80
```

- Appliquez le et vérifiez que l'_Ingress_ a bien été créé (attention au namespace !)
- Testez dans un navigateur l'url `http://traefik-ui.FIXME.xip.io/dashboard/#/`

Remarque : l'ip est donnée en exemple (`curl ifconfig.me`)

### Ingress whoami

- En vous appuyant sur le modèle précédent, exposez le service `whoami-int` par un _Ingress_ (dans le namespace `default`) qui devra répondre sur l'url `whoami.FIXME.xip.io`
- Vérifiez que l'_Ingress_ est correctement configuré avec `kubectl get ingress`
- Vérifiez dans l'interface d'administration que la configuration est bonne (vous devriez voir un ou plusieurs pods associés au Host:whoami.FIXME.xip.io)
- Testez l'url `http://whoami.FIXME.xip.io/`, vérifiez en rafraîchissant la page que vous arrivez bien alternativement sur les différents pods du service (voir `Hostname`)

<div class="pb"></div>

## TP 4.7 : Sonde Readiness

### Simuler une erreur sur les pods du RS whoami

- Depuis le pod `gateway`, vérifier que le code de retour HTTP en GET sur l'URL http://whoami-int:8080/health est 200 : `curl --head whoami-int:8080/health`
- Lancer la commande suivante pour changer ce code de retour HTTP à 500 pour tous les pods derrière le service `whoami-int` :

```shell
kubectl get endpoints whoami-int --output jsonpath='{.subsets[0].addresses[*].ip}' \
  | kubectl exec --stdin gateway -- sh -c \
  'for pod in $(cat /dev/stdin); do curl -s --data 500 ${pod}:80/health; done'
```
- Depuis le pod `gateway`, vérifier que le code de retour HTTP en GET est bien maintenant 500

### Ajout d'une sonde Readiness sur les pods du RS whoami

- Éditez le descripteur du ReplicaSet `whoami`
- Ajoutez la sonde Readiness suivante (source : `workspaces/Lab4/readiness-definition--4.7.yml`) :

```yaml
readinessProbe:
  httpGet:
    path: /health
    port: 80
```

- Appliquez les modifications au ReplicaSet
- Optionnel : les pods apparaissent toujours _READY_ pourquoi ?
- Supprimez les _Pods_ `whoami` existants
- Vérifiez que les nouveaux _Pods_ sont en status _READY_ (avec `kubectl get po`)
- Depuis le pod `gateway`, exécutez la commande `curl -s --data 500 whoami-int:8080/health`
- Constater qu'un des pods perd l'état _READY_ au bout d'une dizaine de secondes
- Optionnel : comment cela se traduit-il dans l'interface d'admin _Traefik_ ? Pourquoi ?
- Faire le nécessaire pour que tous les pods `whoami` perdent l'état _READY_
- Vérifier avec `kubectl` (Optionnel : et dans l'interface d'admin de Traefik ainsi qu'en testant `http://whoami.192.168.99.100.xip.io/`)
- Depuis le pod `gateway`, exécutez la commande `curl -s --data 200 <ip_d_un_pod_whoami>:80/health`
- Constater que le pod concerné passe à l'état _READY_ au bout d'une dizaine de secondes
- Optionnel : Constater que lorsque l'on consulte `http://whoami.192.168.99.100.xip.io/` c'est toujours ce même pod qui est retourné
- Faire le nécessaire pour que tous les pods `whoami` reviennent à l'état _READY_

<div class="pb"></div>

## TP 4.8 : Service Headless

### Restaurer la config précédente du ReplicatSet whoami

- Restaurer la configuration précédent du RS `whoami` sans la sonde _Readiness_
- Supprimer les _Pods_ pour s'assurer qu'ils soient créés sans la sonde

### Dnsutils

- Créer un pod `dnsutils` à partir du descripteur suivant (source : `workspaces/Lab4/pod--dnsutils--4.8.yml`) :

```yaml
---
apiVersion: v1
kind: Pod
metadata:
  labels:
    app: du
  name: dnsutils
spec:
  containers:
  - image: tutum/dnsutils
    name: dnsutils
    command:
      - "bash"
      - "-c"
      - "sleep infinity"
```

### WhoAmI Headless

- Créer un nouveau service `whoami-int-headless` qui redirige le trafic vers tous les pods qui correspondent à `app=whoami,env=int` sur le port _80_, mais de type _Headless_
- Lancer la commande `kubectl exec dnsutils nslookup whoami-int-headless` et vérifier que le résultat est bien celui attendu

<div class="pb"></div>
