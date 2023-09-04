# Premiers pas avec Kubernetes

<!-- .slide: class="page-title" -->



## Agenda de ce chapitre 'Premier pas'

<!-- .slide: class="toc" -->

- [Installation de Kubernetes en local avec mini kube](#/install-minikube)
- [Dashboard, CLI et API](#/dashboard-cli-and-api)
- [Démarrer un container](#/start-a-container)



## Minikube

<!-- .slide: id="install-minikube" -->

<figure style="margin-top: 20%;">
    <img src="ressources/minikube-logo.png" alt="Logo Minikube" width="40%"/>
</figure>



## Une instance Kubernetes locale

- Minikube est un outil qui vous permet de lancer facilement une instance Kubernetes locale
- Minikube lance un cluster Kubernetes composé d'un seul noeud
- ... dans une VM sur votre poste
- ... ce qui permet d'utiliser Kubernetes dès l'environnement __Poste du Développeur__ (ou du __testeur__...ou de __l'opérateur !__)

<br/>
[Site de Minikube](https://github.com/kubernetes/minikube)



## Compatibilité

- Système d'exploitation et virtualisation :
  - Linux : VirtualBox, KVM ou docker
  - macOS : HyperKit (Xhyve déprécié), VirtualBox ou VMWare Fusion
  - Windows : Hyper-V ou VirtualBox
- Options de virtualisation VT-x/AMD-v activées
- Connexion Internet lors du premier lancement



## Integration Continue et Minikube

- Minikube sous Linux peut être lancé avec l'option `--vm-driver=none` et s'appuyer sur un daemon `Docker` existant
- Il est donc possible d'utiliser Minikube dans les étapes d'intégration continue !



## Minikube addons

- Minikube propose par défaut un ensemble de `addons` qui peuvent être activés, désactivés et accédés
- La commande `minikube addons list` permet de lister les addons connus et leur état
- La commande `minikube addons enable ${ADDON_NAME}` permet d'activer un addon (Minikube doit tourner)



## Liste des addons par défaut

```text
└> minikube addons list
- ingress: disabled
- registry: disabled
- registry-creds: disabled
- addon-manager: enabled
- dashboard: enabled
- heapster: disabled
- default-storageclass: enabled
- kube-dns: enabled
```


Notes :

En version v0.26.1 de minikube avec k8s en v1.10 on a en plus :

- coredns: disabled
- efk: disabled
- freshpod: disabled
- metrics-server: disabled
- storage-provisioner: enabled



## TP 1.1 : Utilisation de Minikube

<!-- .slide: class="page-tp1" -->



## Dashboard, CLI et API

<!-- .slide: id="dashboard-cli-and-api" -->

<figure style="margin-top: 1%;">
    <img src="ressources/k8s-ui-dashboard.png" alt="Kubernetes Dashboard"/>
</figure>



## Le Dashboard Kubernetes

- Le __Dashboard__ est une interface web permettant d'interagir avec une instance Kubernetes
- Le __Dashboard__ permet de :
  - déployer des applications
  - rechercher des informations suite au comportement anormal d'une application déployée
  - visualiser l'ensemble des applications déployées
  - modifier la configuration des applications déployées et les mettre à jour
- Le __Dashboard__ permet aussi de connaître l'état des ressources d'une instance et d'accéder aux logs des composants du cluster ainsi que ceux des applications



## Accéder au Dashboard

- Sous Minikube, lancer la commande `minikube dashboard`



## kubectl proxy

* Dans tous les cas, il est possible de lancer la commande `kubectl proxy` qui permet d'accéder au __Dashboard__ depuis son poste local.

<br/>

```shell
└> kubectl proxy
Starting to serve on 127.0.0.1:8001
```

* On peut alors accéder au dashboard via l'url suivante:

<code>http://localhost:8001/api/v1/namespaces/kube-system/services/https:kubernetes-dashboard:/proxy/</code>

<br/>
__Remarque:__ sous minikube, il est exposé en http. L'accès au dashboard se fait donc sur [http](http://localhost:8001/api/v1/namespaces/kube-system/services/http:kubernetes-dashboard:/proxy)
<code>
    http://localhost:8001/api/v1/namespaces/kube-system/services/http:kubernetes-dashboard:/proxy
</code>



## Métriques et Dashboard

- [Heapster](https://github.com/kubernetes/heapster/) doit être installé dans le cluster pour que le __Dashboard__ puisse afficher les métriques et les graphiques associés
- [Heapster](https://github.com/kubernetes/heapster/) propose aussi son interface dédiée à la visualisation des métriques par le biais de [Grafana](https://grafana.com/)



## TP 1.2 : Dashboard

<!-- .slide: class="page-tp1" -->



## Dashboard vs CLI (kubectl)

- Toutes les informations disponibles et les actions réalisables par le biais du __Dashboard__ le sont aussi en passant par la ligne de commande `kubectl`



## Ligne de commande kubectl

- `kubectl` permet d'interagir en ligne de commande avec vos instances __Kubernetes__
- La documentation en ligne est disponible [ici](https://kubernetes.io/docs/user-guide/kubectl/) <i class="fa fa-external-link" aria-hidden="true"></i>



## kubectl : commandes simples (débutant)

| Commande | Description
| -------- | -------------------
| create        | Create a resource from a file or from stdin
| expose        | Take a replication controller, service, deployment or pod and expose it as a new Kubernetes Service
| run           | Run a particular image on the cluster
| set           | Set specific features on objects
| run-container | Run a particular image on the cluster. This command is deprecated, use "run" instead



## kubectl : commandes simples (intermédiaire)

| Commande | Description
| -------- | -------------------
| get      | Display one or many resources
| explain  | Documentation of resources
| edit     | Edit a resource on the server
| delete   | Delete resources by filenames, stdin, resources and names, or by resources and label selector



## kubectl : commande de déploiement

| Commande | Description
| -------- | -------------------
| rollout        | Manage the rollout of a resource
| rolling-update | Perform a rolling update of the given ReplicationController
| scale          | Set a new size for a Deployment, ReplicaSet, Replication Controller, or Job
| autoscale      | Auto-scale a Deployment, ReplicaSet, or ReplicationController



## kubectl : commande de gestion de cluster

| Commande | Description
| -------- | -------------------
| certificate  | Modify certificate resources
| cluster-info | Display cluster info
| top          | Display Resource (CPU/Memory/Storage) usage
| cordon       | Mark node as unschedulable
| uncordon     | Mark node as schedulable
| drain        | Drain node in preparation for maintenance
| taint        | Update the taints on one or more nodes


Notes :

__taint__ permet de gérer des affinités/anti affinités (ex : faire en sorte que certaines ressources ne tournent pas sur le même worker)



## kubectl : debug et diagnostique

| Commande | Description
| -------- | -------------------
| describe     | Show details of a specific resource or group of resources
| diff         | Diff configurations specified by filename or stdin and the current online configuration
| logs         | Print the logs for a container in a pod
| attach       | Attach to a running container
| exec         | Execute a command in a container
| port-forward | Forward one or more local ports to a pod
| proxy        | Run a proxy to the Kubernetes API server
| cp           | Copy files and directories to and from containers
| auth         | Inspect authorization


Notes :

__attach__ c'est le mal...

__diff__ : en beta depuis la 1.13, voir https://kubernetes.io/docs/reference/generated/kubectl/kubectl-commands#diff



## kubectl : commandes avancées

| Commande | Description
| -------- | -------------------
| apply   | Appliquer une configuration à une ressource par nom de fichier ou depuis stdin
| patch   | Update field(s) of a resource using strategic merge patch
| replace | Replace a resource by filename or stdin
| convert | Convert config files between different API versions



## kubectl : commande de paramétrage

| Commande | Description
| -------- | -------------------
| label      | Update the labels on a resource
| annotate   | Mettre à jour les annotations d'une ressource
| completion | Output shell completion code for the specified shell (bash or zsh)



## kubectl : autres commandes

| Commande | Description
| -------- | -------------------
| api-versions | Print the supported API versions on the server, in the form of "group/version"
| config       | Modifier des fichiers kubeconfig
| help         | Help about any command
| plugin       | Runs a command-line plugin
| version      | Print the client and server version information



## kubectl : aide en ligne de commande

- Lancer `kubectl` pour voir la liste de toutes les commandes disponibles
- Lancer `kubectl <command> --help` pour voir l'aide pour une commande en particulier
- Lancer `kubectl options` pour voir la liste des options globales (qui s'appliquent à toutes les commandes)



## API Kubernetes et Versioning d'API

- Afin de faciliter les évolutions telles que les ajouts/suppression de champs ou les restructurations, Kubernetes supporte plusieurs versions d'API
- Chaque version correspond à un chemin différent, ex: `/api/v1` ou `/apis/extensions/v1beta1`
- Différentes versions impliquent différents niveaux de stabilité

[API Overview Reference](https://kubernetes.io/docs/reference/using-api/api-overview/) <i class="fa fa-external-link" aria-hidden="true"></i>



## Fonctionnalités en version Alpha

- Le nom de version contient `alpha` (par exemple, `v1alpha1`)
- Activer une fonctionnalité `alpha` peut vous exposer à des bugs
- Ces fonctionnalités peuvent être désactivées par défaut
- Le support pour ces fonctionnalités peut s'arrêter sans avertissement
- L'API pourra changer en apportant des modifications non rétro-compatibles avec une version précédente
- Aucune garantie de support à long terme, a priori, à ne pas utiliser sur des instances de production sauf si vous êtes prêts à accepter les risques associés



## Fonctionnalités en version Beta (1/2)

- Le nom de version contient `beta` (par exemple, `v2beta3`)
- La fonctionnalité a été __bien__ testée, est considérée comme viable et est activée par défaut
- La fonctionnalité restera en place, des détails peuvent cependant changer d'ici le passage en niveau stable
- Des modifications peuvent être apportées dans le schéma ou la sémantique des objets en rapport avec la fonctionnalité dans une version beta ou stable suivante. Cependant, des instructions de migration seront fournies (mais peuvent impliquer un import/export ou une interruption du service utilisant la fonctionnalité)



## Fonctionnalités en version Beta (2/2)

- La communauté Kubernetes vous invite à tester ces fonctionnalités dès qu'elles sont en beta car il sera difficile de revenir en arrière quand elles seront passées en niveau stable
- A priori, à ne pas utiliser sur des instances de production sauf si vous êtes prêts à accepter les risques associés


Notes :

La non recommandation pour la prod est loin d'être respectée (ex Deployement en beta jusqu'en 1.8)... C'est une sorte de disclaimer ;)



## Fonctionnalités en version stable

- Le nom des versions stables est de la forme `vX` où `X` est un entier
- Les versions stables des fonctionnalités sont pérennes



## Groupes d'API

- Les `API groups` permettent de séparer la monolithique `API v1` en plusieurs sous-parties qui peuvent être activées/désactivées séparément ([Supporting multiple API Groups](https://github.com/kubernetes/community/blob/master/contributors/design-proposals/api-machinery/api-group.md) <i class="fa fa-external-link" aria-hidden="true"></i>)
- Plusieurs groupes existent :
  - le groupe `core` (aussi appelé `legacy`) est le groupe principal et n'est pas spécifié explicitement dans le chemin REST (`/api/v1`) ou le champ `apiVersion` dans les descripteurs (`apiVersion: v1`)
  - les groupes nommés sont visibles dans les chemins REST (`/apis/$GROUP_NAME/$VERSION`), et dans l'apiVersion (`$GROUP_NAME/$VERSION`), par exemple `apiVersion: batch/v1`



## Exemples de groupes

`batch`, `extensions`, `authorization`, `autoscaling`, `policy`

<br/>

- [Kubernetes v1.14 api reference](https://kubernetes.io/docs/reference/generated/kubernetes-api/v1.14/) <i class="fa fa-external-link" aria-hidden="true"></i>
- [Kubernetes v1.13 api reference](https://kubernetes.io/docs/reference/generated/kubernetes-api/v1.13/) <i class="fa fa-external-link" aria-hidden="true"></i>
- [Kubernetes v1.12 api reference](https://kubernetes.io/docs/reference/generated/kubernetes-api/v1.12/) <i class="fa fa-external-link" aria-hidden="true"></i>
- [Kubernetes v1.11 api reference](https://kubernetes.io/docs/reference/generated/kubernetes-api/v1.11/) <i class="fa fa-external-link" aria-hidden="true"></i>
- [Kubernetes v1.10 api reference](https://kubernetes.io/docs/reference/generated/kubernetes-api/v1.10/) <i class="fa fa-external-link" aria-hidden="true"></i>
- [Kubernetes v1.9 api reference](https://v1-9.docs.kubernetes.io/docs/reference/generated/kubernetes-api/v1.9/) <i class="fa fa-external-link" aria-hidden="true"></i>


Notes :

Plus de documentation en ligne pour les versions < 1.9. Pour la version de la 1.9 elle n'est plus maintenue `Documentation for Kubernetes v1.9 is no longer actively maintained. The version you are currently viewing is a static snapshot. For up-to-date documentation, see the latest version.`



## Activation des groupes et ressources

- Certains groupes et ressources (sous-parties dans les groupes) sont activés par défaut
- Il est possible de les activer/désactiver au niveau de l'`apiserver` (que l'on verra dans le chapitre [Architecture et composants](#/10))
- Exemples :
  - pour désactiver le groupe `batch/v1`, positionner le paramètre `--runtime-config=batch/v1=false` lors du lancement de l'`apiserver`
  - pour désactiver une ou plusieurs ressources :

```bash
--runtime-config=extensions/v1beta1/deployments=false,extensions/v1beta1/jobs=false
```



## Evolution de l'api et des versions associées

<i class="fa fa-info-circle" aria-hidden="true"></i> Si vous êtes intéressés par les stratégies de gestion des versions d'api :

- [Kubernetes Deprecation Policy](https://kubernetes.io/docs/reference/deprecation-policy/)




## TP 1.3 : Kubectl, TP 1.4 Groupes et ressources

<!-- .slide: class="page-tp1" -->



## TP 1.5 : Docker Warmup et Rappels

<!-- .slide: class="page-tp1" -->



## Démarrer son premier container dans k8s

<!-- .slide: id="start-a-container" -->

- Le meilleur moyen de déployer une application dans __Kubernetes__ de manière reproductible est de passer par un fichier descripteur au format `yaml` ou `json`
- Mais il est aussi possible d'utiliser la commande `run` qui permet de se passer d'un tel descripteur



## Exemples d'utilisation de 'kubectl run'

| Commande | Description
| ------------------------------- | -------------------
| `kubectl run nginx --image=nginx` | Démarre une instance unique à partir de l'image `nginx`
| `kubectl run hazelcast --image=hazelcast --port=5701` | Démarre une instance `hazelcast` et exposer le port `5701` du container
| `kubectl run hazelcast --image=hazelcast --env="DNS_DOMAIN=cluster" --env="POD_NAMESPACE=default"` | Démarre une instance `hazelcast` en spécifiant des variables d'environnement
| `kubectl run nginx --image=nginx --dry-run -o yaml` | Afficher les descripteurs sans réellement créer les objets


Notes :

L'option --dry-run qui permet de récuperer le descripteur est très pratique pour faire ses premiers modèles (et il n'installe rien)



## Ressources générées avec 'kubectl run'

- Par défaut, `kubectl run` créera une ressource de type __Deployment__ (que l'on verra dans le chapitre [Stratégies de déploiement](#/8)) en s'appuyant sur le __endpoint__ `extensions/v1beta1`
- Il est possible de créer d'autres types de ressources en utilisant le paramètre `--generator=<valeur>`



## Types de ressources utilisables avec 'kubectl run'

| Valeur pour `generator`   | Type de ressource
| ------------------------- | ------------------
| `deployment/v1beta1`      | Deployment utilisant le __endpoint__ `extensions/v1beta1` (Défaut)
| `deployment/apps.v1beta1` | Deployment utilisant le __endpoint__ `apps/v1beta1` (Recommandé)
| `run-pod/v1`              | Pod
| `run/v1`                  | Replication Controller
| `job/v1`                  | Job
| `batch/v1beta1`           | CronJob

<br/>
<br/>
Voir la [page de documentation associée](https://kubernetes.io/docs/user-guide/kubectl-conventions/#generators)



## Un exemple concret d'utilisation de 'kubectl run'

La commande : <br/>
`kubectl run whoami --image=containous/whoami:latest --port=80 --generator=run-pod/v1` :

- démarre un __Pod__ (que l'on verra en détail dans le chapitre [Pods](#/Pods))
- à partir de l'image Docker `containous/whoami:latest`
- et référence le port `80` du container créé
  - __"référence"__ dans le sens ou c'est une déclaration d'intention permettant d'utiliser par la suite ce port
  - Ne pas spécifier de port n'empêche pas le port d'être accessible


Notes :

- Not specifying a port here DOES NOT prevent that port from being exposed. Any port which is listening on the default "0.0.0.0" address inside a container will be accessible from the network



## 'kubectl get containers' ?

- Il n'existe pas de commande `kubectl` permettant de lister les containers lancés
- L'unité la plus petite est le __pod__ (que l'on verra en détail dans le chapitre [Pods](#/3))
- On peut donc lancer `kubectl get pods` (ou `kubectl get po` en utilisant le raccourci de la ressource __pods__)



## Lister les pods

<br/>
```shell
└> kubectl get po
NAME         READY     STATUS    RESTARTS   AGE
whoami       1/1       Running   0          2m
```

<br/>
À noter, par défaut, les informations remontées sont :

- Le nom du pod
- Le nombre de containers __prêts__ (un pod peut contenir plusieurs containers)
- Le status du pod (Running, Terminating, CrashLoopBackOff, ...)
- Le nombre de __restarts__ éventuels de l'un des containers du pod
- L'âge du pod : depuis combien de temps la demande de création du Pod a-t-elle été prise en compte



## Lister plus d'informations sur les pods ?

Il est possible d'utiliser l'option `-o` (ou `--output`) pour affiner le format de sortie

- `json` ou `yaml`
- `wide` ou `name`
- `custom-columns=...` ou `custom-columns-file=...` ([Documentation](http://kubernetes.io/docs/user-guide/kubectl-overview/#custom-columns))
- `go-template=...` ou `go-template-file=...` ([Documentation](http://golang.org/pkg/text/template/#pkg-overview))
- `jsonpath=...` ou `jsonpath-file=...` ([Documentation](http://kubernetes.io/docs/user-guide/jsonpath))



## Plus d'infos sur les pods (suite)

<br/>

```shell
└> kubectl get po -o wide
NAME         READY     STATUS    RESTARTS   AGE       IP           NODE
whoami       1/1       Running   0          1h        172.17.0.7   minikube
```

```shell
└> kubectl get po -o name
pods/whoami
```

```shell
└> kubectl get po \
      -o=custom-columns=NAME:.metadata.name,RSRC:.metadata.resourceVersion
NAME         RSRC
whoami       103751
```



## Accéder à tous les détails d'un pod

La commande `kubectl describe po <pod-name>` permet d'accéder à des informations détaillées sur un pod

```shell
└> kubectl describe po whoami
Name:           whoami
Namespace:      default
Node:           minikube/192.168.99.100
Start Time:     Sun, 08 Oct 2017 19:02:51 +0200
Labels:         run=whoami
Annotations:    kubernetes.io/created-by={"kind":"SerializedReference","apiVersion":"v1","reference":{"kind":"ReplicationController","namespace":"default","name":"whoami","uid":"850adebf-ac4a-11e7-b4c7-0800279f22af","a...
Status:         Running
IP:             172.17.0.7
.
.
Node-Selectors:  <none>
Tolerations:     <none>
Events:          <none>
```


Notes :

Le describe est particulièrement utile en cas de problèmes (cf champ Events)



## Types de ressources valides (1/2)

| Ressource | Ressource
| ------------- | -------------
| __all__ | horizontalpodautoscalers (aka 'hpa')
| certificatesigningrequests (aka 'csr') | __ingresses__ (aka '__ing__')
| clusterrolebindings | __jobs__
| clusterroles | limitranges (aka 'limits')
| clusters (valid only for federation apiservers) | __namespaces__ (aka '__ns__')
| componentstatuses (aka 'cs') | networkpolicies (aka 'netpol')
| __configmaps__ (aka '__cm__') | __nodes__ (aka '__no__')
| controllerrevisions | __persistentvolumeclaims__ (aka '__pvc__')


Notes :

- __all__ ne retourne pas tout et peut retourner les RS en double (car c'est en fait kubectl qui fait une interrogation parfois avec 2 api/version)
- L'idée n'est pas d'aller dans le détail de chacune des ressources mais de montrer qu'il y en a BEAUCOUP.
- Les ressources en gras sont celles qui seront abordées pendant la formation




## Types de ressources valides (2/2)

| Ressource | Ressource
| ------------- | -------------
| __replicasets__ (aka '__rs__') | customresourcedefinition (aka 'crd')
| __replicationcontrollers__ (aka '__rc__') | __daemonsets__ (aka '__ds__')
| resourcequotas (aka 'quota') | __deployments__ (aka '__deploy__')
| rolebindings | endpoints (aka 'ep')
| roles | events (aka 'ev')
| __secrets__ | poddisruptionbudgets (aka 'pdb')
| serviceaccounts (aka 'sa') | podpreset
| __services__ (aka '__svc__') | __pods__ (aka '__po__')
| __statefulsets__ | podsecuritypolicies (aka 'psp')
| storageclasses | podtemplates



## Exposer l'application démarrée (Contexte)

<!-- .slide: id="expose-application" -->

- Jusqu'à présent, l'utilisation de `kubectl run` a créé :
  - un pod
- L'application __whoami__ n'est pour l'instant accessible que depuis l'interieur du cluster __Kubernetes__, par le biais de l'adresse IP attribuée au pod

<br/>
<br/>

```shell
└> kubectl describe po whoami | grep IP
IP:             172.17.0.7
```



## Problématiques liées à l'accès à l'IP des pods

- Comment faire si on veut accéder à l'application __whoami__ de manière fiable ?
  - l'ip associée au Pod n'est pas stable dans le temps, si le pod était redémarré, l'adresse pourrait changer
  - dans le cas d'une mise à l'échelle, nous aurions plusieurs pods avec plusieurs IP, comment faire pour s'y connecter ?

<br/>
=> La solution : passer par un __service__ (les détails seront vus dans le chapitre [Services](#/5)) en utilisant la commande `kubectl expose`



## Accéder à un pod sans passer par un service

- Il est aussi possible d'accéder à un pod sans passer par un service de manière temporaire
- La commande `kubectl port-forward` permet de forwarder temporairement un port local (du host d'où est lancée la commande `kubectl`) vers le port d'un pod

<br/>

```shell
└> kubectl port-forward whoami 80
Forwarding from 127.0.0.1:80 -> 80
Handling connection for 80
Handling connection for 80
.
.
.
```



## TP 1.6 : kubectl run/port-forward

<!-- .slide: class="page-tp1" -->



<!-- .slide: class="page-questions" -->
