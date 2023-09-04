
# TP 5 : Volumes

## TP 5.1 : Volume partagé

- Créer le pod à partir du descripteur suivant (source : `workspaces/Lab5/pod--shared-emtpyDir-vol--5.1.yml`) :

```yaml
---
apiVersion: v1
kind: Pod
metadata:
  name: shared-vol-pod
spec:
  containers:
  - image: nodevops/nginx-log2fs:1.12-alpine
    name: log2fs
    volumeMounts:
    - mountPath: /var/log/nginx
      name: var-log-nginx
  - image: centos:7
    name: shell
    command:
      - "bash"
      - "-c"
      - "sleep infinity"
    volumeMounts:
    - mountPath: /data
      name: var-log-nginx
  volumes:
  - name: var-log-nginx
    emptyDir: {}
```

- Se connecter dans le container `shell`
- Vérifier que le répertoire `/data` contient les fichiers de logs nginx
- Depuis le container `shell`, lancer plusieurs fois la commande `curl localhost`
- Vérifier que le fichier `access_log` a été modifié

<div class="pb"></div>

## TP 5.2 : Volume hostPath

### Initialisation

- Adapter le descripteur suivant (source : `workspaces/Lab5/pod--shared-hostPath-bootstrap--5.2.yml`) :

```yaml
---
apiVersion: v1
kind: Pod
metadata:
  name: shared-hostpath-vol-pod
spec:
  containers:
  - image: nodevops/nginx-log2fs:1.12-alpine
    name: log2hostfs
  - image: centos:7
    name: shell
    command:
      - "bash"
      - "-c"
      - "sleep infinity"
```

- Faire en sorte que le container `log2hostfs` monte un volume appelé `var-log-nginx` sur le répertoire  `/var/log/nginx`
- Faire en sorte que le volume `var-log-nginx` soit un volume de type `hostPath` qui monte le répertoire `/mnt/sda1/data/var-log-nginx` du noeud

### Checks

- Se connecter dans le container `shell`
- Lancer plusieurs fois la commande `curl localhost`
- Vérifier que le fichier `access_log` dans le répertoire `/mnt/sda1/data/var-log-nginx` du noeud minikube a été modifié

<div class="pb"></div>

## TP 5.3 : PVC et PV

### Pré-provision d'un PV

- Créer un PersistentVolume à partir du descripteur suivant (source : `workspaces/Lab5/pv--my-hostpath-pv--5.3.yml`) :

```yaml
---
apiVersion: v1
kind: PersistentVolume
metadata:
  name: my-hostpath-pv
spec:
  capacity:
    storage: 1Gi
  accessModes:
    - ReadWriteOnce
    - ReadOnlyMany
  persistentVolumeReclaimPolicy: Retain
  hostPath:
    path: /data/pv/pv003
```

- Vérifier que le _PersistentVolume_ a été créé et qu'il n'est pour l'instant pas associé à une _PersistentVolumeClaim_

### Création d'une PersistentVolumeClaim

- Créer une _PersistentVolumeClaim_ à partir du descripteur suivant (source : `workspaces/Lab5/pvc--firstclaim--5.3.yml`) :

```yaml
---
kind: PersistentVolumeClaim
apiVersion: v1
metadata:
  name: my-first-claim
spec:
  accessModes:
    - ReadWriteOnce
  resources:
    requests:
      storage: 500Mi
  storageClassName: ""
```

- Vérifier que la _PersistentVolumeClaim_ a été créée et qu'elle est associée au _PersistentVolume_ créé précédemment
- Vérifier l'état du _PersistentVolume_ créé précédemment
- Quelle est la capacité associée au PVC créé ? Pourquoi ?

### Utilisation de la PVC

- Adapter le descripteur suivant (source : `workspaces/Lab5/pod--use-pvc-bootstrap--5.3.yml`) :

```yaml
---
apiVersion: v1
kind: Pod
metadata:
  name: pod-pvc
spec:
  containers:
  - image: nodevops/nginx-log2fs:1.12-alpine
    name: log2hostfs
  - image: centos:7
    name: shell
    command:
      - "bash"
      - "-c"
      - "sleep infinity"
```

- Faire en sorte que le container `log2hostfs` monte un volume appelé `var-log-nginx` sur le répertoire  `/var/log/nginx`
- Faire en sorte que le volume `var-log-nginx` soit un volume provisionné à partir d'une _PersistentVolumeClaim_

### Checks

- Se connecter dans le container `shell`
- Lancer plusieurs fois la commande `curl localhost`
- Où le fichier access_log a-t-il été créé sur le noeud minikube ?

### Création d'un second PVC

- Créer un second PVC à partir du descripteur suivant (source : `workspaces/Lab5/pvc--second-claim--5.3.yml`) :

```yaml
---
kind: PersistentVolumeClaim
apiVersion: v1
metadata:
  name: my-second-claim
spec:
  accessModes:
    - ReadWriteOnce
  resources:
    requests:
      storage: 500Mi
  storageClassName: ""
```

- Quel est l'état du PVC ?

### Optionnel : Correction de la situation

- Faire ce qu'il faut pour que le second PVC puisse être associé à un PV :
  - soit en créant manuellement un second PV
  - ou en recréant le second PVC en modifiant le `storageClassName` (soit supprimer complètement le champ, c'est la StorageClass par défaut qui sera utilisée, soit utiliser le nom de la StorageClass existante `kubectl get storageclass`)

<div class="pb"></div>
