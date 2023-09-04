# Fonctionnalités d'entreprise

<!-- .slide: class="page-title" -->



## Agenda

- [gestion des ressources](#/resources)
- [auto-scaling](#/auto-scaling)



## Gestion des ressources

<!-- .slide: id="resources" -->

- Quand vous créez un pod vous pouvez positionner :
  - La quantité de CPU et de mémoire dont chacun de ses containers a besoin (_Requests_)
  - Une limite max sur ces 2 mêmes compteurs qu'il lui sera impossible de dépasser (_Limits_)
- Les ressources consommées par un _Pod_ sont les sommes des ressources consommées par ses containers



## Exemple

```yaml
---
apiVersion: v1
kind: Pod
metadata:
  name: frontend
spec:
  containers:
  - name: db
    image: mysql
    resources:
      requests:
        memory: "64Mi"
        cpu: "250m"
      limits:
        memory: "128Mi"
        cpu: "500m"
  - name: wp
    image: wordpress
    resources:
      requests:
        memory: "64Mi"
        cpu: "250m"
      limits:
        memory: "128Mi"
        cpu: "500m"

```


Notes :

- Mem : Mi base 1024 (2^10) => 1 Mi = 2^20 (1 048 576)
- CPU : voir plus loin (Unité de cpu (1/2))



## Schedule et Requests

- Quand vous créez un _Pod_, le `scheduler` sélectionne un noeud pour l'héberger
- Chaque noeud a une capacité max (CPU et Mémoire)
- Le `scheduler` s'assure que la somme des _Requests_ des Pods sur un noeud ne dépasse pas la capacité du noeud
- Note : ce n'est donc pas la consommation réelle en CPU/Mémoire du noeud au moment du scheduling qui fait foi mais bien la somme des _Requests_ des pods du noeud


Notes :

- On peut positionner de quotas sur les namespaces
- Et l'on peut ajouter des `limitRange` qui s'appliqueront par défaut aux containers



## Gestion des Limits pour la mémoire

- Un container peut consommer plus de mémoire que la `request` effectuée si le noeud qui l'héberge a de la mémoire disponible
- Un container ne peut pas consommer plus que la `limit` paramétrée
- Si un container alloue plus de mémoire que la `limit` le container va être marqué comme un candidat pour être terminé. S'il continue à consommer plus de mémoire que la valeur du `limit` alors il sera _terminé_.
  - S'il est _redémarrable_, le _kubelet_ essaiera de le redémarrer (c'est un cas d'erreur comme les autres)

<br/>

Voir l'exemple `workspace/others/pod--try-to-allocate-too-much-memory.yml`



## Gestion des Limits pour le cpu

- Un container peut consommer plus de cpu que la `request` effectuée si le noeud qui l'héberge a des ressources cpu disponibles
- Un container ne peut pas consommer plus que la `limit` positionnée



## Gestion des Limits pour le cpu : exemple

```yaml
---
apiVersion: v1
kind: Pod
metadata:
  name: cpu-demo
spec:
  containers:
  - name: cpu-demo-ctr
    image: vish/stress
    resources:
      limits:
        cpu: "1"
      requests:
        cpu: "0.5"
    args:
    - -cpus
    - "2"
```



## Unité de cpu (1/2)

- La ressource CPU est mesurée en `cpu units`
- 1 unité de cpu vaut :
  - 1 AWS vCPU
  - 1 GCP Core
  - 1 Azure vCore
  - 1 Hyperthread sur un processeur Intel d'un serveur _bare-metal_ avec Hyperthreading



## Unité de cpu (2/2)

- Un Container qui demande 0.5 cpu est garanti d'avoir la moitié de cpu d'un Container qui demande 1 cpu
- Il est possible d'utiliser le suffixe `m` qui signifie `milli`
  - 100m cpu, 100 millicpu, et 0.1 cpu représentent la même valeur
- La valeur d'une unité CPU est une valeur absolue : 0.1 correspond à la même quantité de CPU sur un `single-core`, `dual-core`, ou une machine `48-core`


Notes :

Pour la JVM 9 (et back port sur 8u131) des options permettent d'éviter que le JVM prenne plus que ce qui lui est autorisé :
`-XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap`

Voir https://blogs.oracle.com/java-platform-group/java-se-support-for-docker-cpu-and-memory-limits



## Consommation par noeud (1/3)

```shell
$ kubectl describe nodes e2e-test-minion-group-4lw4
Name:            e2e-test-minion-group-4lw4
Capacity:
 cpu:                               2
 memory:                            7679792Ki
 pods:                              110
Allocatable:
 cpu:                               1800m
 memory:                            7474992Ki
 pods:                              110

...
```



## Consommation par noeud (2/3)

```

...

Non-terminated Pods:        (5 in total)
  Namespace    Name                                  CPU Requests  CPU Limits  Memory Requests  Memory Limits
  ---------    ----                                  ------------  ----------  ---------------  -------------
  kube-system  fluentd-gcp-v1.38-28bv1               100m (5%)     0 (0%)      200Mi (2%)       200Mi (2%)
  kube-system  kube-dns-3297075139-61lj3             260m (13%)    0 (0%)      100Mi (1%)       170Mi (2%)
  kube-system  kube-proxy-e2e-test-...               100m (5%)     0 (0%)      0 (0%)           0 (0%)
  kube-system  monitoring-influxdb-grafana-v4-z1m12  200m (10%)    200m (10%)  600Mi (8%)       600Mi (8%)
  kube-system  node-problem-detector-v0.1-fj7m3      20m (1%)      200m (10%)  20Mi (0%)        100Mi (1%)
...

```



## Consommation par noeud (3/3)

```shell

...
Allocated resources:
  (Total limits may be over 100 percent, i.e., overcommitted.)
  CPU Requests    CPU Limits    Memory Requests    Memory Limits
  ------------    ----------    ---------------    -------------
  680m (34%)      400m (20%)    920Mi (12%)        1070Mi (14%)
```



## Limit Ranges (1/2)

- Il est possible de configurer par `namespace` :
  - des valeurs par défaut pour les `requests` et les `limits`
  - des valeurs max pour les `requests` et les `limits`



## Limit Ranges (2/2)

```yaml
---
apiVersion: v1
kind: LimitRange
metadata:
  name: mem-limit-range
spec:
  limits:
  - default:
      memory: 512Mi
    defaultRequest:
      memory: 256Mi
    max:
      memory: 1Gi
    min:
      memory: 500Mi
    type: Container
```



## Auto-Scaling

<!-- .slide: id="auto-scaling" -->

- L'auto-scaling horizontal de pod est la mise à l'échelle automatique du nombre de replicas d'un controller
- Il est effectué par un controller appelé _autoscaler_ qui est piloté par une ressource de type _HorizontalPodAutoscaler_
- Le controller vérifie régulièrement (`f=30s` par défaut) les métriques des pods concernés et calcul le nombre de replicas par rapport aux critères d'auto-scaling paramétrés pour la ressource cible (Deployment, ReplicaSet ou ReplicationController)
- Les métriques sont soit :
  - les ressources cpu/mémoire
  - des métriques personnalisées


Notes :

- Les métriques par défaut sont fournies par les addons *Heapster* (déprécié) ou *metrics-server* qui font partie de la distribution officielle k8s.
- Les métriques custom sont fournies par des addons hors distribution officielle, il en existe plusieurs, ex : Prometheus, Datadog, Azure Monitor, ...



## Configuration

```shell
└> kubectl autoscale deployment autosc --cpu-percent=30 --min=1 --max=5
deployment "autosc" autoscaled
```

```shell
└> kubectl get hpa
NAME      REFERENCE          TARGETS           MINPODS   MAXPODS   REPLICAS   AGE
autosc     Deployment/autosc   <unknown> / 30%   1         5         0          28s
```



## Descripteur autoscaling/v1

En version `autoscaling/v1`, support uniquement du cpu :

```yaml
---
apiVersion: autoscaling/v1
kind: HorizontalPodAutoscaler
metadata:
  name: autosc
spec:
  maxReplicas: 5
  minReplicas: 1
  scaleTargetRef:
    apiVersion: extensions/v1beta1
    kind: Deployment
    name: autosc
  targetCPUUtilizationPercentage: 30
```



## Descripteur

En version `autoscaling/v2beta1` (k8s >= *1.6*), support cpu+mémoire et des métriques personnalisées :

```yaml
apiVersion: autoscaling/v2beta1
kind: HorizontalPodAutoscaler
metadata:
  name: php-apache
  namespace: default
spec:
  scaleTargetRef: { apiVersion: apps/v1beta1, kind: Deployment, name: php-apache, minReplicas: 1, maxReplicas: 10 }
  metrics:
    - type: Resource
      resource: {name: cpu, targetAverageUtilization: 50}
    - type: Pods
      pods: { metricName: packets-per-second, targetAverageValue: 1k }
    - type: Object
      object:
        metricName: requests-per-second
        target: { apiVersion: extensions/v1beta1, kind: Ingress, name: main-route }
        targetValue: 10k
```


Notes :

On ne passe pas par un selector mais par une association directe à la ressource, ici le Deployement de nom php-apache (explain `scaleTargetRef`)



<!-- .slide: class="page-questions" -->
