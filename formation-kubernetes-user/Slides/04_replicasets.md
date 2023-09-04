# Replicasets

<!-- .slide: class="page-title" -->



## Agenda de ce chapitre 'ReplicaSets'

<!-- .slide: class="toc" -->

- [HealthChecks](#/healthchecks)
- [ReplicationControllers vs ReplicaSets](#/rc-vs-rs)
- [DaemonSets](#/daemonsets)
- [Jobs](#/jobs)



## HealthChecks

<!-- .slide: id="healthchecks" -->

- Un des bénéfices de l'utilisation de Kubernetes (et des autres orchestrateurs) c'est de ne pas avoir à se soucier de où tournent les containers
- Répartir les containers sur les différents noeuds fait partie des responsabilités de l'orchestrateur
- Que se passe-t-il si un des containers pilotés par Kubernetes __meurt__ ?
- Que se passe-t-il si tous les containers d'un pod __meurent__ ?

<br/>
<i class="fa fa-hand-o-right" aria-hidden="true"></i> Comme experimenté dans le TP __2.4__, Kubernetes redémarrera les containers d'un pod si nécessaire (si la `restartPolicy` vaut __Always__ ou __OnFailure__)



## Liveness

- Mais comment détecter que l'application ne fonctionne pas quand le container associé ne __meurt__ pas ?
  - Une application Java avec un problème mémoire peut envoyer des `OutOfMemoryErrors`, mais la JVM n'est pas terminée pour autant !
- Comment faire pour indiquer à Kubernetes que l'application ne fonctionne plus correctement ?
- Vous pourriez __intercepter__ ce type d'erreurs et terminer l'application, mais ce serait fastidieux de gérer tous les cas
- ... et comment faire si votre application se trouve dans une __boucle infinie__ ou une situation de __deadlock__ ?

<br/>
<i class="fa fa-info-circle" aria-hidden="true"></i> Kubernetes propose la notion de __Liveness Probe__ ("sonde de vie") pour répondre à cette problématique



## Container Probes (1/2)

- Une __sonde__ (probe) est un diagnostique effectué par Kubernetes (par un composant appelé __kubelet__ que nous verrons plus tard) sur un container
- Il existe 3 types de sondes :
  - __httpGet__ : un appel __GET__ est effectué, un __status code__ >= 200 et < 400 est considéré comme un __succès__
  - __tcpSocket__ : si le port concerné est ouvert, la sonde est en __succès__
  - __exec__ : une commande est effectuée dans le container concerné, un code de retour à __0__ équivaut à un __succès__



## Container Probes (2/2)

- Les sondes sont utilisées pour vérifier qu'un container __fonctionne__ (__Liveness Probe__) ou pour vérifier qu'un container est __prêt__ à recevoir du trafic (__Readiness Probe__)
  - Nous aborderons les __Readiness Probe__ dans le chapitre suivant, [Services](#/5)
- Une seule sonde de chaque type (httpGet, tcpSocket, exec) peut être définie pour chaque catégorie (Liveness/Readiness) **par container**



## Configuration d'une sonde

```yaml
---
apiVersion: v1
kind: Pod
metadata:
  labels:
    test: liveness
  name: liveness-http
spec:
  containers:
    - name: liveness
      image: gcr.io/google_containers/liveness
      args:
        - /server
      livenessProbe:
        httpGet:
          path: /healthz
          port: 8080
        initialDelaySeconds: 15
        timeoutSeconds: 1
```



## Paramètres additionnels d'une sonde

- Parce que l'application dans le container peut ne pas être utilisable de suite, `initialDelaySeconds` permet de régler le délai avant de commencer à lancer les premiers diagnostiques
- `timeoutSeconds` permet de spécifier la durée max autorisée pour recevoir la réponse. Si la réponse n'est pas récupérée dans le temps imparti, la sonde est en échec (défaut: 1, minimum: 1)
- `periodSeconds` permet de spécifier la fréquence à laquelle le diagnostique sera effectué (défaut: 10, minimum: 1)
- `successThreshold` permet de spécifier le nombre minimum de succès pour que la sonde soit considérée en état __succès__ (défaut: 1, minimum: 1)
- `failureThreshold` permet de spécifier le nombre minimum d'échecs pour  que la sonde soit considérée en état __échec__ (défaut: 3, minimum: 1)


Notes :

Ne pas oublier de spécifier un `initialDelaySeconds`. S'il est trop court (si par exemple il y a très peu de CPU pour le pod) le pod peut être redémarré alors qu'il n'a pas fini de démarrer.



## Visualisation de l'état des sondes (1/2)

La colonne __RESTARTS__ dans la sortie de la commande `kubectl get po` indique le nombre de restarts du/des containers du pod (mais ces restarts ne sont pas forcément dus à une sonde en échec)

<br/>
<br/>

```bash
└> kubectl get pods
NAME       READY     STATUS    RESTARTS   AGE
rp-check   1/1       Running   2          1m
```



## Visualisation de l'état des sondes (2/2)

La commande `kubectl describe po` retourne des informations sur les potentielles sondes en échec (et les restarts associés)

<br/>
<br/>

```shell
Events:
  Type     Reason      Age                 From               Message
  ----     ------      ----                ----               -------
...
  Warning  Unhealthy   56s (x33 over 34m)  kubelet, minikube  Liveness probe failed: HTTP probe failed with statuscode: 500
  Normal   Killing     56s (x16 over 33m)  kubelet, minikube  Killing container with id docker://liveness:pod "liveness-http_default(b87d60ab-ad9d-11e7-b4c7-0800279f22af)" container "liveness" is unhealthy, it will be killed and re-created.
  Warning  BackOff     7s (x128 over 31m)  kubelet, minikube  Back-off restarting failed container
...
```


Notes :

- When a container is killed, a completely new container is created, it’s not just the same container being restarted again
- Dans le TP 3.1, bien faire un `docker kill` pour tuer le container, sinon la colonne RESTART ne sera pas modifiée
- ... si tu fais un `docker rm -f` le container ne sera pas redémarré mais recréé, du coup le "READY" passera (rapidement) à 1/2, mais le RESTART ne sera pas modifié



## TP 3.1 : Liveness probes

<!-- .slide: class="page-tp3" -->



## Anatomie d'une bonne Liveness Probe (1/2)

- Il est fortement recommandé de positionner des __Liveness Probes__ pour vos pods en production car si ce n'est pas le cas, __k8s__ n'a aucun moyen de savoir si vos applications fonctionnent comme attendu
- Une sonde (http) devrait correspondre à un chemin dédié dans votre application et correspondre à un diagnostique interne de fonctionnement



## Anatomie d'une bonne Liveness Probe (2/2)

- Une sonde ne doit pas être affectée par les dépendances de votre application :
  - ce sont vos dépendances qui sont en erreur, pas votre application !
  - à vous de bien gérer les dépendances en erreur dans votre application (utiliser des __circuit breaker__ par exemple)
- Gardez vos sondes __légères__ : elles doivent répondre rapidement et sans consommer trop de __cpu__ et de __mémoire__ pour ne pas perturber le fonctionnement de votre application


Notes :

- du coup, le endpoint `/health` d'un springboot n'est pas un bon candidat car par défaut, des tests associés aux dépendances sont inclus, ce qui n'est pas bon



## Introduction aux ReplicaSets (1/2)

<!-- .slide: id="rc-vs-rs" -->

Récapitulons :

- __k8s__ s'assure que nos containers fonctionnent et les redémarre si le process principal __meurt__ ou si la __liveness probe__ échoue
- Cette tâche incombe au composant __kubelet__ qui tourne sur chaque noeud (cf [Architecture et composants](#/10))
- Le __centre de contrôle__ de __Kubernetes__ qui tourne sur le(s) __master(s)__ ne joue aucun rôle dans cette partie (cf [Architecture et composants](#/10))



## Introduction aux ReplicaSets (2/2)

- Que se passe-t-il si le noeud qui héberge le pod venait à tomber ?
- Pour s'assurer que notre application/pod puisse être redémarré sur un autre noeud il faut passer par un mécanisme de plus haut niveau que le __pod__
- Ces mécanismes, appelés __Controllers__ sont les __Replicasets__ (et les __ReplicationControllers__), __Daemonsets__, __Jobs__ et __CronJobs__



## ReplicaSet ?

- Un __ReplicaSet__ est une ressource Kubernetes dont le rôle est de s'assurer qu'un pod est lancé et fonctionne correctement
- Si un pod disparait/meurt pour quelque raison que ce soit :
  - un noeud qui tombe
  - le pod a été retiré d'un noeud lors d'une maintenance
- ... le __ReplicaSet__ va détecter l'absence du pod et faire en sorte qu'un nouveau soit créé



## Replicas (1/2)

- un __ReplicaSet__ peut gérer plusieurs copies (appelées __replicas__) d'un même pod et s'assurer que le nombre de replicas en fonctionnement correspond à celui attendu :
  - s'il n'y pas assez de replicas par rapport à la cible, le __rs__ va créer ceux qui manquent
  - s'il y en a trop, il va en supprimer pour avoir le nombre demandé



## Replicas (2/2)

- un __ReplicaSet__ s'appuie sur un __selecteur__ de labels pour identifier les pods qu'il doit gérer
- un __ReplicaSet__ est similaire à un superviseur de process, mais au lieu de superviser des process sur un seul noeud, il peut superviser plusieurs pods sur plusieurs noeuds
- il est fortement recommandé d'utiliser un __ReplicaSet__ même si vous ne devez gérer qu'un pod


Notes :

À retenir :

- les labels sont très importants (et dans certains cas les labels des pods peuvent être rattachés à un autre replicaset)
- si plus de replicats actifs que ceux dans la conf k8s va arrêter arbitrairement les replicats en trop
- ne créez pas de pod directement (en dehors de la formation ;))
- la suppression d'un replicaset sans l'option `--cascade=false` va supprimer en cascade les pods et autres ressources définies dans ce replicat.



## Exemple de définition d'un ReplicaSet

```yaml
---
apiVersion: apps/v1 # Utiliser 'apps/v1beta2' ou 'apps/v1beta1' avant Kubernetes v1.9
kind: ReplicaSet
metadata:
  name: rs-nginx
spec:
  replicas: 3
  selector:
    matchLabels:
      app: nginx
  template:
    metadata:
      name: pod-nginx
      labels:
        app: nginx
    spec:
      containers:
        - name: container-nginx
          image: nginx
          ports:
            - containerPort: 80
```



## Contenu d'un descripteur de ReplicaSet

- les sections `apiVersion`, `kind` et `metadata` sont semblables à ce qu'on trouve pour toutes les ressources k8s
- la section `specs` contient :
  - le nombre de _replicas_ attendu
  - le _selecteur_ utilisé pour _identifier_ les pods associés
  - le _modèle_ à utiliser pour la création des pods



## Labels et ReplicaSet

- Il est possible de spécifier des labels au niveau du _ReplicaSet_ (`.metadata.labels`)
- La plupart du temps, ce seront les mêmes que ceux des pods associés (`.spec.template.metadata.labels`)
- Mais ils peuvent aussi être complètement différents et n'affecteront pas le comportement du _ReplicaSet_



## Pod Selector et ReplicaSet (1/2)

- Le champ `.spec.selector` est un selecteur de label
- Le _ReplicaSet_ va gérer tous les pods qui correspondent à ce selecteur
- ... sauf s'ils sont dans le giron d'un autre ReplicaSet (voir TP)
- <i class="fa fa-exclamation-triangle" aria-hidden="true"></i> Le _ReplicaSet_ ne fait pas la différence entre les pods qu'il a créés/supprimés et ceux qu'un autre process a créés/supprimés (s'il ne sont pas dans le giron d'un ReplicaSet [voir TP])
- Cela permet notamment potentiellement de remplacer le _RS_ de manière transparente sans affecter les pods créés



## Pod Selector et ReplicaSet (2/2)

- Le champ `.spec.selector` doit être obligatoirement spécifié, il doit correspondre aux labels du template de pod (`.spec.template.metadata.labels`)
- Si le champ `.spec.selector` est spécifié et ne correspond pas aux labels du template de pod (`.spec.template.metadata.labels`), la création sera refusée

<br/>

```bash
└> kubectl apply -f Exercices/workspaces/Lab3/rs--nginx-cannot-create--3.2.yml
The ReplicaSet "frontend" is invalid: spec.template.metadata.labels: Invalid value: map[string]string{"app":"frontend", "env":"int"}: `selector` does not match template `labels`
```



## Modifications des labels des pods ou du template

- Si vous modifiez les labels d'un pod piloté par un _RS_ il sortira du giron du _RS_
- Le _ReplicaSet_ ne controlle pas le contenu d'un pod, si vous modifiez le modèle des pods d'un _RS_, seuls les nouveaux pods créés seront affectés
  - Il existe cependant un moyen d'obliger les pods à se mettre à jour, nous verrons cela dans le chapitre [Stratégies de déploiement](#/8)



## Kubectl et les ReplicaSets

- `kubectl get replicasets` ou (plus court !) `kubectl get rs`
- `kubectl describe rs` pour accéder aux infos détaillées
- `kubectl edit rs` pour éditer la description d'un _RS_ (valable pour les autres ressources)
- `kubectl scale rs <rsName> --replicas=N` pour mettre un _RS_ à l'échelle de manière impérative
- `kubectl delete rs` pour supprimer un _RS_ : attention, cela va aussi supprimer les pods associés
- si vous souhaitez supprimer le _RS_ sans supprimer les pods associés, utiliser `kubectl delete rs --cascade=false`



## TP 3.2 : ReplicaSets

<!-- .slide: class="page-tp3" -->



## Replicaset plutôt que ReplicationController

- Les seules différences entre _ReplicaSet_ et _ReplicationController_ sont :
  - la manière d'écrire le _pod selector_ :
    - Le _ReplicationController_ ne supporte que les sélections de type `clé=valeur`
    - Le _ReplicaSet_ supporte aussi la selection avancée :
      - _env_ avec comme valeur `int` ou `rd`
      - clé _env_ définie, quelque soit la valeur
  - le champ `.spec.selector` n'est pas obligatoire pour le _ReplicationController_




## Exemple de ReplicationController

```yaml
---
apiVersion: v1 # Noter le v1 'tout court'
kind: ReplicationController
metadata:
  name: frontend
spec:
  replicas: 2
  template:
    metadata:
      name: frontend
      labels:
        app: frontend
        env: int
    spec:
      containers:
      - name: nginx-fe
        image: nginx:stable-alpine
        ports:
        - containerPort: 80
```



## Exemple de selector d'un ReplicaSet

<br/>

```yaml
---
apiVersion: apps/v1 # for versions before 1.9.0 use extensions/v1beta1
kind: ReplicaSet
metadata:
  name: frontend
  labels:
    app: guestbook
    tier: frontend
spec:
  replicas: 3
  selector:
    matchLabels:
      app: frontend
    matchExpressions:
      - {key: env, operator: In, values: [int,rd]}
  template:
  ...
```



## Autre syntaxe pour les selectors de ReplicaSet

<br/>

```yaml
---
...
  selector:
    matchExpressions:
      - {key: env, operator: In, values: [int,rd]}
  ...
```

est équivalent à

```yaml
---
...
  selector:
    matchExpressions:
      - key: env
        operator: In
        values:
          - int
          - rd
  ...
```



## Operators, matchLabels et matchExpressions

- Il y a 4 opérateurs disponibles :
  - `In` (`values` doit aussi être spécifié)
  - `NotIn` (`values` doit aussi être spécifié)
  - `Exists` (`values` ne doit pas être spécifié)
  - `DoesNotExist` (`values` ne doit pas être spécifié)
- Si `matchLabels` et `matchExpressions` sont spécifiés tous les deux, les labels doivent correspondre aux 2 contraintes



## TP 3.3 : ReplicaSets et matchExpressions

<!-- .slide: class="page-tp3" -->



## DaemonSets

<!-- .slide: id="daemonsets" -->

- Les _ReplicaSets_ (et les _ReplicationControllers_) sont utilisés pour faire tourner un nombre défini de pod n'importe où sur le cluster
- Un autre cas d'utilisation existe : faire tourner un et un seul exemplaire d'un pod sur chaque noeud (ou sur un sous-ensemble défini de noeuds)
- Pour répondre à cette problématique, utilisons un _DaemonSet_



## Cas d'usage des DaemonSets

- Faire tourner un daemon de stockage sur chaque noeud (glusterd, ceph, rook, ...)
- Faire tourner un agent de collecte des logs (fluentd, logstash, ...)
- Faire tourner un agent de collecte de métriques (Prometheus, collectd, datadog, ganglia, ...)



## Différence RS/RC vs DaemonSet

- Un _ReplicaSet_ (ou un _ReplicationController_) s'assure qu'un nombre désiré de replicas d'un pod tourne sur le cluster :
  - Si un noeud est ajouté au cluster, rien ne se passe
  - Si un noeud est retiré du cluster, les pods éventuellement schédulés sur ce noeud seront réaffectés sur d'autres noeuds
- Un _DaemonSet_ s'assure qu'un et un seul exemplaire de pod tourne sur chaque noeud (ou sous-ensemble de noeud)
  - Si un noeud est ajouté au cluster, un nouveau pod sera lancé sur le nouveau noeud
    - un _DaemonSet_ sera déployé même sur les noeuds marqués comme non-utilisables (par les administrateurs)
  - Si un noeud est retiré du cluster, aucun nouveau pod ne sera lancé



## DaemonSet sur un sous-ensemble de noeuds

- Un _DaemonSet_ déploie ses pods sur tous les noeuds du cluster ...
- ... sauf si l'on spécifie sur quels noeuds déployer par le biais d'un _nodeSelector_
  - cas d'usage : outillage spécifique en fonction du hardware de chaque noeud



## Descripteur de DaemonSet

<br/>

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



## TP 3.4 : DaemonSets

<!-- .slide: class="page-tp3" -->



## Des Jobs pour quoi faire ?

<!-- .slide: id="jobs" -->

- Nous n'avons pour l'instant vu que des pods qui doivent tourner en continu
- Mais on doit parfois lancer une tâche qui se termine quand le travail qu'elle représente est achevé
- Les _ReplicationControllers_, _ReplicaSets_ et _DaemonSets_ font tourner des tâches qui ne sont jamais considérées comme étant achevées
- Les process dans les pods associés aux _RC_, _RS_ et _DS_ sont redémarrés quand ils se terminent
- Dans le cas des tâches qui peuvent s'achever légitimement, il ne faut pas les redémarrer

<br/>
Les __Jobs__ Kubernetes correspondent à ce genre de tâches



## Jobs

- Un cas simple consiste à faire tourner un _Job_ pour lancer un Pod qui s'achèvera avec succès quand le process associé sera terminé avec un code de retour en __succès__
- Le Job pourra cependant aussi redémarrer le pod si celui-ci n'a pu terminer sa tâche associée correctement
- Un Job peut aussi lancer plusieurs pods en parallèle



## Descripteur de Jobs

<br/>

```yaml
---
apiVersion: batch/v1
kind: Job
metadata:
  name: batch-job
spec:
  template:
    metadata:
      labels:
        app: batch-job
    spec:
      restartPolicy: OnFailure
      containers:
      - name: main
        image: zenika/sample-batch
```

<i class="fa fa-hand-o-right" aria-hidden="true"></i> Noter le `restartPolicy: OnFailure`, c'est lui qui fait que le pod n'est pas redémarré quand le container se termine normalement, pas le fait que l'on soit dans un job



## Paramètres complémentaires des Jobs

- Pour spécifier qu'un _Job_ doit être exécuté plusieurs fois, utiliser le paramètre  `.spec.completions`
- Pour spécifier que plusieurs _pods_ d'un même _Job_ peuvent s'exécuter en parallèle, utiliser le paramètre `.spec.parallelism`
- Il est aussi possible de mettre à l'échelle un _Job_ qui tourne avec la commande `kubectl scale job`. <i class="fa fa-exclamation-triangle" aria-hidden="true"></i> Cette fonctionnalité a été dépréciée en version v1.10
- Pour spécifier qu'un _Job_ ne doit pas durer plus qu'un temps défini, utiliser `.spec.activeDeadlineSeconds`
- Pour spécifier qu'un _Job_ ne doit pas être redémarré plus qu'un certain nombre de fois, utiliser `.spec.backoffLimit`



## Fin des Jobs et nettoyage

- Quand un _Job_ est terminé, il n'est pas supprimé automatiquement, de même pour les _Pods_ associés
- Ceci afin de pouvoir accéder aux logs des pods concernés
- C'est donc à l'utilisateur de supprimer manuellement les _Jobs_
- Il est aussi possible de spécifier la taille maximum des historiques :  `.spec.successfulJobsHistoryLimit` et `.spec.failedJobsHistoryLimit `


Notes :

Par défaut `.spec.successfulJobsHistoryLimit = 3` et `.spec.failedJobsHistoryLimit = 1`



## Jobs et restartPolicy

- L'utilisation d'un _Job_ n'est appropriée que si _RestartPolicy_ vaut `OnFailure` ou `Never`
- Rappel : si aucune valeur n'est spécifiée, la valeur par défaut pour un pod est `Always`
- Il est donc obligatoire de spécifier la valeur de _RestartPolicy_ dans le cas d'un _Job_ !



## CronJobs

- Il est aussi possible de lancer des _Jobs_ à fréquence fixe ou à une date dans le futur
- C'est la ressource de type _CronJob_



## Descripteur de CronJobs

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



## Points d'attentions Jobs/CronJobs (1/2)

- Une ressource de type _Job_ sera créée à partir d'une ressource _CronJob_ approximativement à la date schédulée
- Puis le _Job_ crée un _Pod_
- Il est possible que le _Job_ et le _Pod_ soient créés en retard par rapport à la cible
- Si la date cible est un critère fort pour vous, utilisez `.spec.startingDeadlineSeconds` (si la deadline est dépassée, le _Job_ ne sera pas lancé)
- Dans des circonstances normales, le _CronJob_ ne va créer qu'un seul _Job_ pour une date d'exécution
- Mais il arrive que 2 _Jobs_ soient créés, ou aucun



## Points d'attentions Jobs/CronJobs (2/2)

- Il faut rendre vos jobs _idempotents_ :
  - pour prendre en compte le cas où un noeud tombe pendant que le job a commencé, car celui-ci sera alors relancé sur un autre noeud
  - pour prendre en compte les cronjobs qui se lancent 2 fois (c'est un cas qui peut arriver)
  - pour prendre en compte les cronjobs qui ne se lancent pas du tout, le job doit potentiellement pouvoir "rattraper" la non exécution du job précédent



## TP 3.5 : Jobs et CronJobs

<!-- .slide: class="page-tp3" -->



<!-- .slide: class="page-questions" -->
