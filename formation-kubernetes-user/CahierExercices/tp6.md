
# TP 6 : ConfigMaps et Secrets

## TP 6.1 : ConfigMaps

### Valeurs littérales

- Créer la _ConfigMap_ `special-config` qui doit contenir les clés/valeurs suivantes
  - special.k=cereales
  - special.ite=kubernetes
- Vérifier le contenu de la ConfigMap

### Import de fichier

- Créer la _ConfigMap_ `game-config` à partir du contenu du répertoire `workspaces/Lab6/configs`
- Vérifier le contenu de la ConfigMap

### Utilisation en tant que variable d'env

- Créer un pod à partir du descripteur suivant (source : `workspaces/Lab6/pod--cm-one-venv--6.1.yml`) :

```yaml
---
apiVersion: v1
kind: Pod
metadata:
  name: pod-with-cm-one-venv
spec:
  containers:
    - name: witness
      image: centos:7
      command: [ "/bin/sh", "-c", "env ; sleep infinity" ]
      env:
        - name: SPECIAL_LEVEL_KEY
          valueFrom:
            configMapKeyRef:
              name: special-config
              key: special.k

```

- Afficher les logs du pod et vérifier que la variable d'environnement SPECIAL_LEVEL_KEY est bien définie

### Importer toutes les clés d'une ConfigMap

- Créer la _ConfigMap_ `special-config-for-venv` qui doit contenir les clés/valeurs suivantes
  - SPECIAL_K=cereales
  - SPECIAL_ITE=kubernetes
- Créer un pod à partir du descripteur suivant (source : `workspaces/Lab6/pod--cm-all-vars--6.1.yml`) :

```yaml
---
apiVersion: v1
kind: Pod
metadata:
  name: pod-with-cm-all-vars
spec:
  containers:
    - name: neo
      image: centos:7
      command: [ "/bin/sh", "-c", "env ; sleep infinity" ]
      envFrom:
      - configMapRef:
          name: special-config-for-venv

```

- Afficher les logs du pod et vérifier que toutes les variables d'environnement SPECIAL_* sont bien définies

### Monter un volume à partir d'une ConfigMap

- Créer un pod à partir du descripteur suivant (source : `workspaces/Lab6/pod--cm-as-vol--6.1.yml`) :

```yaml
---
apiVersion: v1
kind: Pod
metadata:
  name: cm-as-vol
spec:
  containers:
    - name: droopy
      image: centos:7
      command: [ "/bin/sh", "-c", "ls /etc/config/ ; sleep infinity" ]
      volumeMounts:
      - name: config-volume
        mountPath: /etc/config
  volumes:
    - name: config-volume
      configMap:
        name: game-config

```

- Se connecter dans le pod et vérifier le contenu des fichiers dans /etc/config


<div class="pb"></div>

## TP 6.2 : Secrets

### Créer un secret

- Créer un secret appelé `secret-identities` avec les clés valeurs suivantes :
  - spiderman=Peter Parker
  - superman=Clark Kent

### Utiliser le secret / variable d'env

- Créer le pod à partir du descripteur suivant (source : `workspaces/Lab6/pod--civil-war-expose-secret-identity--6.2.yml`) :

```yaml
---
apiVersion: v1
kind: Pod
metadata:
  name: civil-war
spec:
  containers:
    - name: revelation
      image: centos:7
      command: [ "/bin/sh", "-c", "env ; sleep infinity" ]
      env:
        - name: SPIDERMAN_IS
          valueFrom:
            secretKeyRef:
              name: secret-identities
              key: spiderman

```

- Vérifier dans le Pod que la variable d'environnement est valorisée comme attendu

### Utiliser le secret / fichiers

- Créer le pod à partir du descripteur suivant (source : `workspaces/Lab6/pod--dc-vs-marvel--6.2.yml`) :

```yaml
---
apiVersion: v1
kind: Pod
metadata:
  name: dc-vs-marvel
spec:
  containers:
    - name: revelations
      image: centos:7
      command: [ "/bin/sh", "-c", "env ; sleep infinity" ]
      volumeMounts:
      - name: for-your-eyes-only
        mountPath: "/etc/revelations"
        readOnly: true
  volumes:
    - name: for-your-eyes-only
      secret:
        secretName: secret-identities

```

- Vérifier dans le Pod que les fichiers contiennent les informations attendues

<div class="pb"></div>
