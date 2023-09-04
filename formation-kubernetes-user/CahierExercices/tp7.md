
# TP 7 : Deployments

## TP 7.1 : Deployments

### Version1

- Créer le descripteur de _Deployment_ avec le contenu suivant (source : `workspaces/Lab7/deploy--kubia-v1--7.1.yml`) :

```yaml
---
apiVersion: apps/v1
kind: Deployment
metadata:
  name: kubia
spec:
  replicas: 3
  minReadySeconds: 5
  revisionHistoryLimit: 5
  selector:
    matchLabels:
      app: kubia
  strategy:
    type: RollingUpdate
    rollingUpdate:
      maxSurge: 1
      maxUnavailable: 0
  template:
    metadata:
      labels:
        app: kubia
    spec:
      containers:
      - name: nodejs
        image: luksa/kubia:v1
        ports:
          - name: main
            containerPort: 8080
        livenessProbe:
          httpGet:
            path: /.healthcheck
            port: 8080
          periodSeconds: 5
          initialDelaySeconds: 2
        readinessProbe:
          httpGet:
            path: /.readicheck
            port: 8080
          periodSeconds: 5
          initialDelaySeconds: 5

```

- Lancer la commande `kubectl create -f workspaces/Lab7/deploy--kubia-v1--7.1.yml --record` pour initier le deployment
- Vérifier l'état du deployment, du replicaset et des pods associés

### Service

- Créer un service associé (port `80`, targetPort `8080`, nom `kubia-svc`) à ce déploiement
- Recréer si nécessaire le _Pod_ `gateway` utilisé dans le TP4
- Connectez vous dans le pod gateway et lancer la commande `while true; do curl --silent http://kubia-svc; sleep 1; echo; done`
- (laisser cette commande tourner)

### Version 2

- Mettre à jour le descripteur de déploiement pour utiliser l'image avec le tag _v2_
- Mettre à jour le déploiement
- Lancer la commande `kubectl rollout status deployment kubia` de temps en temps pour voir les infos remontées
- Vérifier l'état du deployment, du replicaset et des pods associés
- Vérifier dans le pod gateway que l'application est mise à jour

<div class="pb"></div>
