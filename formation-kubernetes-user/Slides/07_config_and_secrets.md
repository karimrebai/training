# Configuration et Secrets

<!-- .slide: class="page-title" -->



## Agenda de ce chapitre 'ConfigMap et Secrets'

<!-- .slide: class="toc" -->

- [Variables d'environnements](#/env-vars)
- [ConfigMaps](#/config-map)
- [Secrets](#/secrets)



## Variables d'environnements

<!-- .slide: id="env-vars" -->

- Une des promesses de Docker est de pouvoir utiliser la même image sur tous les environnements
- Mais chaque environnement a ses caractéristiques
- Il est commun d'adapter le comportement d'un container en passant par des variables d'environnement
- Par exemple :
  - une url JDBC
  - le nom d'un service externe utilisé pour l'authentification
  - un profil Spring



## Déclaration d'une variable d'environnement

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
    spec:
      containers:
        - image: looztra/guestbook-storage:0.5.2-aio
          name: storage
          ports:
            - { name: main-port, containerPort: 8080 }
            - { name: admin-port, containerPort: 9090 }
          env:
            - name: MANAGEMENT_PORT
              value: "9090"
            - name: SPRING_PROFILES_ACTIVE
              value: "integration"

```


Notes :

- Les variables d'environnements sont obligatoirement des chaines de texte => échapper avec " pour les numériques
- Dans les premières versions de K8S, les clés devaient absolument être en majuscule, ce n'est plus le cas



## Variables composées

- Il est aussi possible de faire référence à d'autres variables d'environnement

```yaml
---
...
      env:
        - name: ENV_COLOR
          value: "blue"
        - name: COLORED_HOSTNAME
          value: "$(ENV_COLOR)_$(HOSTNAME)"
```



## ConfigMaps

<!-- .slide: id="config-map" -->

- <i class="fa fa-thumbs-down" aria-hidden="true"></i> L'inconvénient de passer par des variables d'environnement c'est que vous devrez construire des descripteurs pour chaque environnement
- Tout l'intérêt de la configuration c'est de pouvoir séparer les valeurs dépendantes de l'environnement du code source de l'application
- ... et vos descripteurs devraient faire partie du code source de l'application (Everything as a code)
- Kubernetes permet de séparer complètement la configuration d'une application par le biais de la ressource de type __ConfigMap__



## Exemple de ConfigMap

<br/>

```yaml
---
kind: ConfigMap
apiVersion: v1
metadata:
  name: ma-premiere-config-map
data:
  ma.property.1: hello
  ma.property.2: world
  mon-fichier-de-properties: |-
    property.1=value-1
    property.2=value-2
    property.3=value-3
```



## Création de Config Map

- Il est possible de créer une _ConfigMap_ par le biais d'un descripteur
- Il est possible de créer une _ConfigMap_ à partir de valeurs littérales
- Il est aussi possible d'importer un fichier ou un répertoire
- Dans le cas d'un répertoire, une clé sera créée pour chaque fichier



## Valeurs littérales

<br/>

```shell
kubectl create configmap special-config \
          --from-literal=special.how=very \
          --from-literal=special.type=charm

```
<br/>

```yaml
---
apiVersion: v1
kind: ConfigMap
data:
  special.how: very
  special.type: charm
```



## ConfigMap à partir d'un repertoire

  ```shell
  kubectl create configmap game-config --from-file=configs/
  ```



## Résultat de l'import de fichier

<br/>

```shell
kubectl get configmaps game-config -o yaml
```

```yaml
---
apiVersion: v1
kind: ConfigMap
data:
  game.properties: |
    enemies=aliens
    lives=3
    enemies.cheat=true
    enemies.cheat.level=noGoodRotten
    secret.code.passphrase=UUDDLRLRBABAS
    secret.code.allowed=true
    secret.code.lives=30
  ui.properties: |
    color.good=purple
    color.bad=yellow
    allow.textmode=true
    how.nice.to.look=fairlyNice
```



## Utilisation des ConfigMaps : Var d'env

```yaml
---
apiVersion: v1
kind: Pod
metadata:
  name: dapi-test-pod
spec:
  containers:
    - name: test-container
      image: gcr.io/google_containers/busybox
      command: [ "/bin/sh", "-c", "env" ]
      env:
        - name: SPECIAL_LEVEL_KEY
          valueFrom:
            configMapKeyRef:
              name: special-config
              key: special.how

```


Notes :

Ici cela remplit la variable d'env SPECIAL_LEVEL_KEY en interrogeant la `configMap` special-config en récupérant la valeur de la clef special.how



## Référencer toutes les clés d'une ConfigMap

```yaml
---
apiVersion: v1
kind: Pod
metadata:
  name: dapi-test-pod
spec:
  containers:
    - name: test-container
      image: gcr.io/google_containers/busybox
      command: [ "/bin/sh", "-c", "env" ]
      envFrom:
      - configMapRef:
          name: special-config

```



## Créer un volume à partir d'une ConfigMap

```yaml
---
apiVersion: v1
kind: Pod
metadata:
  name: dapi-test-pod
spec:
  containers:
    - name: test-container
      image: gcr.io/google_containers/busybox
      command: [ "/bin/sh", "-c", "ls /etc/config/" ]
      volumeMounts:
      - name: config-volume
        mountPath: /etc/config
  volumes:
    - name: config-volume
      configMap:
        name: game-config

```

- Autant de fichiers que de clés dans la ConfigMap seront créés
- Chaque fichier contiendra le contenu de chaque clé



## Contraintes d'utilisation des ConfigMaps

- Vous devez créer une ConfigMap __AVANT__ de la référencer dans un Pod
- Si vous référencez une ConfigMap qui n'existe pas dans une définition de Pod, le Pod ne démarrera pas
- De même si vous référencez une clé qui n'existe pas
- Une _ConfigMap_ n'est valide/utilisable que dans le _Namespace_ où elle a été créée



## TP 6.1 : ConfigMaps

<!-- .slide: class="page-tp6" -->



## Secrets

<!-- .slide: id="secrets" -->

- Les objets de type __Secret__ sont faits pour stocker des informations sensibles telles que des mots de passe, des tokens OAuth, des certificats ou des clés SSH
- Stocker ces informations dans un _Secret_ est plus sûr et flexible que de les stocker en dur dans un _Pod_ ou même dans une _ConfigMap_ (même si ce n'est pas encore idéal)
- En fait, les _Secrets_ sont des sortes de _ConfigMap_ customisées



## Stocker des Secrets

- Encodez les valeurs que vous voulez stocker en `Base64`

```shell
$ echo -n "admin" | base64
YWRtaW4=
$ echo -n "1f2d1e2e67df" | base64
MWYyZDFlMmU2N2Rm

```

```yaml
---
apiVersion: v1
kind: Secret
metadata:
  name: mysecret
type: Opaque
data:
  username: YWRtaW4=
  password: MWYyZDFlMmU2N2Rm

```


Notes :

Les secrets ne sont pas chiffrés mais il y a des alternatives (plus ou moins fiables)

- En alpha depuis la v1.7 [encryption-provider](https://kubernetes.io/docs/tasks/administer-cluster/encrypt-data/)
- Integration de [Vault](https://www.vaultproject.io/) (Hashicorp) pour avoir la gestion des secrets https://github.com/Boostport/kubernetes-vault
- https://vault.koudingspawn.de/how-does-vault-crd-work



## Visualiser un Secret

<br/>

```shell
$ kubectl get secret mysecret -o yaml
```

```yaml
---
apiVersion: v1
data:
  username: YWRtaW4=
  password: MWYyZDFlMmU2N2Rm
kind: Secret
type: Opaque

```



## Utiliser un Secret pour créer des fichiers

<br/>

```yaml
---
apiVersion: v1
kind: Pod
metadata:
  name: mypod
spec:
  containers:
  - name: mypod
    image: redis
    volumeMounts:
    - name: foo
      mountPath: "/etc/foo"
      readOnly: true
  volumes:
  - name: foo
    secret:
      secretName: mysecret

```

Chaque clé dans le secret devient un fichier



## Utiliser un Secret pour populer une variable d'env

<br/>

```yaml
---
apiVersion: v1
kind: Pod
metadata:
  name: secret-env-pod
spec:
  containers:
  - name: mycontainer
    image: redis
    env:
      - name: SECRET_USERNAME
        valueFrom:
          secretKeyRef:
            name: mysecret
            key: username
      - name: SECRET_PASSWORD
        valueFrom:
          secretKeyRef:
            name: mysecret
            key: password

```



## TP 6.2 : Secrets

<!-- .slide: class="page-tp6" -->



<!-- .slide: class="page-questions" -->
