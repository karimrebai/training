# Volumes

<!-- .slide: class="page-title" -->



## Agenda de ce chapitre 'Volumes'

<!-- .slide: class="toc" -->

- [Présentation des Volumes](#/intro-to-volumes)
- [Partage simple de données entre 2 containers d'un même pod](#/shared-volume)
- [Accéder au fs d'un noeud du cluster](#/host-volume)
- [Persistent Volumes et Persistent Volumes Claims](#/pv-and-pvc)



## Présentation des Volumes

<!-- .slide: id="intro-to-volumes" -->

- Les fichiers créés dans un container sont éphémères
- si un _Pod_ est redémarré, les containers créés pour la nouvelle instance du _Pod_ n'auront pas accès aux fichiers du container de l'instance précédente
- Un container d'un _Pod_ doit parfois partager des fichiers avec les autres containers du même _Pod_ (dans le cas d'un _Pod_ multi-containers)
- Les __Volumes__ Kubernetes permettent de répondre à ces problématiques



## Types de volumes (1/2)

- emptyDir : un répertoire initialement vide
- hostPath : un répertoire __monté__ depuis le filesystem du noeud du cluster
- gitRepo : un volume initialisé à partir du clone de dépôt Git
- nfs : un partage NFS exposé comme un volume
- _gcePersistentDisk_ (Google Compute Engine Persistent Disk), _awsElasticBlockStore_ (Amazon Web Services Elastic Block Store Volume), _azureDisk_ (Microsoft Azure Disk Volume) : des volumes exposés par les fournisseurs d'infrastructure Cloud



## Types de volumes (2/2)

- cinder, cephfs, iscsi, flocker, glusterfs, quobyte, rbd, flexVolume, vsphereVolume, photonPersistentDisk, scaleIO : des volumes partagés dits _réseau_ (_network storage_)
- _ConfigMaps_, _Secrets_, [downwardAPI](https://kubernetes.io/docs/tasks/inject-data-application/downward-api-volume-expose-pod-information/) : des volumes spécialisés pour exposer certaines ressources Kubernetes
- persistentVolumeClaim : une manière d'allouer dynamiquement des volumes


Notes :

Le `ConfigMaps` utilisé pour de la configuration

`persistentVolumeClaim` : une sorte d'abstraction des espaces de stockage (indépendant de l'implémentation)

`downwardAPI` : cela pemet d'accèder et monter en fichier de informations dynamiques sur le pod (mem, cpu, ... mais aussi sur le descripteur du pod comme par exemple les labels)



## Volume de type emptyDir

- Un volume de type `emptyDir` fournit un volume ... vide au départ
- L'application qui tourne dans le _Pod_ qui monte ce volume peut alors y écrire des fichiers
- Attention, le cycle de vie du volume `emptyDir` est lié à celui du _Pod_
- Si le _Pod_ est détruit, le volume aussi
- Ce type de volume est parfaitement adapté dans le cas de containers d'un même _Pod_ qui doivent collaborer sur des fichiers communs



## Partage simple de données entre 2 containers d'un même pod

<!-- .slide: id="shared-volume" -->

```yaml
---
apiVersion: v1
kind: Pod
metadata:
  name: shared-vol
spec:
  containers:
    - image: nodevops/nginx-log2fs:1.12-alpine
      name: log2fs
      volumeMounts:
        - mountPath: /var/log/nginx
          name: var-log-nginx
    - image: centos:7
      name: shell
      command: ["bash", "-c", "sleep infinity"]
      volumeMounts:
        - mountPath: /data
          name: var-log-nginx
  volumes:
    - name: var-log-nginx
      emptyDir: {}

```



## Accéder au fs d'un noeud du cluster

<!-- .slide: id="host-volume" -->

- La plupart des _Pods_ devraient être agnostiques par rapport au noeud sur lequel ils tournent
- Mais certains _Pods_ d'administration doivent pouvoir potentiellement accéder au filesystem du noeud sur lequel ils tournent :
  - par exemple, un _Pod_ créé par un _DaemonSet_ d'archivage des logs
- les volumes de type `hostPath` permettent d'accéder au filesystem des noeuds



## Précautions d'utilisation des volumes "hostPath"

- Les données stockées dans les volumes `hostPath` sont persistantes (contrairement aux `emptyDir`)
- Mais il ne faut pas trop compter sur ce point car rien ne vous garantit que vos _Pods_ seront relancés sur le même noeud



## Exemple de volume "hostPath"

<br/>

```yaml
---
apiVersion: v1
kind: Pod
metadata:
  name: hostpath-vol
spec:
  containers:
  - image: nodevops/nginx-log2fs:1.12-alpine
    name: log2fs
    volumeMounts:
    - mountPath: /var/log/nginx
      name: var-log-nginx
  volumes:
  - name: var-log-nginx
    hostPath:
      path: /data/var-log-nginx
```



## TP 5.1 : Volume partagé, TP 5.2 : Volume hostPath

<!-- .slide: class="page-tp5" -->



## Persistent Volumes et Persistent Volumes Claims

<!-- .slide: id="pv-and-pvc" -->

- Gérer les problématiques d'espace disque demande une approche dédiée
- Le principe des _PersistentVolumes_ permet aux administrateurs de cluster de fournir une couche d'abstraction de consommation et de fourniture de _Volumes_
- Cette couche d'abstraction s'appuie sur les _PersistentVolumes_ et les _PersistentVolumeClaims_
- Un _PersistentVolume_ est un espace de stockage qui a été réservé par les admins du cluster
- Une _PersistentVolumeClaim_ (littéralement _Demande de PersistentVolume_) est une requête d'espace de stockage faite par les utilisateurs du cluster



## PersistentVolumeClaim vs Pods

- Les _PVC_ sont semblables aux _Pods_
- Les _Pods_ consomment des ressources (CPU & Mémoire) des noeuds du cluster
- Les _PVC_ consomment des ressources de stockage du cluster
- Les _PVC_ sont faits pour fournir des _PersistentVolumes_ répondant à des critères :
  - notamment de taille et de type d'accès (ReadOnly/ReadWrite/...)
- ... tout en n'ayant pas à exposer aux utilisateurs du cluster la façon dont les volumes sont fournis
- Les _StorageClass_ sont le moyen donné aux admins d'exposer les différents types de volumes disponibles



## Cycle de vie des Volumes et des Demandes (Claims)

- Les _PersistentVolumes_ peuvent être provisionnés de manière _Statique_ ou _Dynamique_
- Les _PV Statiques_ sont des _PV_ pré-provisionnés par les admins
- Les _PV Dynamiques_ sont des _PV_ provisionnés à la volée par le cluster en s'appuyant sur une _StorageClass_
- Une _PersistentVolumeClaim_ doit spécifier le type de _StorageClass_ souhaité
- Une _StorageClass_ vide (`""`) équivaut à demander un _PV_ statique



## L'étape d'association ("Binding")

- Un utilisateur crée une _PersistentVolumeClaim_ en précisant la taille de l'espace de stockage et le mode d'accès souhaités
- Le cluster examine les _PersistentVolumes_ disponibles et correspondant _a minima_ à la demande
- L'utilisateur aura toujours _au moins_ ce qu'il a demandé, mais peut avoir plus, et l'association entre la _PVC_ et le _PV_ est effectuée
- Si aucun _PV_ ne correspond à la demande, elle reste en attente indéfiniment (ou jusqu'à sa suppression)


Notes :

Si les PV sont définis statistiquement le PVC alloué aura la taille du PV attribut, il n'y a pas de redimensionnement du volume.



## L'étape d'utilisation ("Using")

- Le _Pod_ utilise les demandes comme des volumes
- Le cluster inspecte la _PVC_ pour retrouver le _PV_ associé et monte le volume dans le _Pod_
- Une fois que l'utilisateur a créé une demande (_PVC_) et que cette demande est associée (à un _PV_), le _PV_ associé appartient à l'utilisateur tant qu'il en a besoin



## L'étape de libération ("Reclaiming")

- Quand l'utilisateur n'a plus besoin du volume, il peut supprimer la _PVC_ associée
- **Attention**, supprimer un **Pod** ne supprime pas les **PersistenceVolumeClaims** associées!
- La _ReclaimPolicy_ associée au _PersistentVolume_ détermine ce qui arrive au _PersistentVolume_ une fois qu'il a été libéré :
- Les _PV_ libérés peuvent être :
  - retenus
  - recyclés
  - supprimés



## Retained/Recycled/Deleted

- retenu (_Retained_) signifie que le _PV_ n'est plus utilisé mais ne peut pas être associé à une autre _PersistentVolumeClaim_ tant qu'un admin ne le libère pas définitivement
- recyclé (_Recycled_) : le _PersistentVolume_ est nettoyé, c.a.d les données qui y sont stockées sont supprimées. Une fois que c'est fait il peut être de nouveau associé à une _PVC_
- supprimé (_Deleted_) : le _PersistentVolume_ est supprimé (ex AzureDisk, GC Disk, AWS EBS...)


Notes :

Attention: La `Recycle reclaim policy` est dépréciée, à la place il est recommandé d'utiliser un provisionning dynamique.



## Access Modes

- _ReadWriteOnce_ (RWO) : le volume peut être monté en read-write par un seul noeud
- _ReadOnlyMany_ (ROX) : le volume peut être monté en read-only par plusieurs noeuds
- _ReadWriteMany_ (RWX) : le volume peut être monté en read-write par plusieurs noeuds

<br/>

<i class="fa fa-exclamation-triangle" aria-hidden="true"></i>
 Un volume ne peut être monté que suivant un seul mode à la fois, même s'il supporte plusieurs modes


Notes :

La gestion des accès concurrents est gérée par le provider. Ces volumes sont souvent moins performants qu'un disque physique sur la machine.



## Exemple de PersistentVolume

<br/>

```yaml
---
apiVersion: v1
kind: PersistentVolume
metadata:
  name: pv0003
spec:
  capacity:
    storage: 5Gi
  accessModes:
    - ReadWriteOnce
  persistentVolumeReclaimPolicy: Recycle
  storageClassName: slow
  mountOptions:
    - hard
    - nfsvers=4.1
  nfs:
    path: /tmp
    server: 172.17.0.2
```



## Exemple de PersistentVolumeClaim

<br/>

```yaml
---
kind: PersistentVolumeClaim
apiVersion: v1
metadata:
  name: myclaim
spec:
  accessModes:
    - ReadWriteOnce
  resources:
    requests:
      storage: 8Gi
  storageClassName: slow
  selector:
    matchLabels:
      release: "stable"
    matchExpressions:
      - {key: environment, operator: In, values: [dev]}
```

Notes :

selector : A label query over volumes to consider for binding.



## Utilisation de la PVC

<br/>

```yaml
---
kind: Pod
apiVersion: v1
metadata:
  name: mypod
spec:
  containers:
    - name: myfrontend
      image: dockerfile/nginx
      volumeMounts:
      - mountPath: "/var/www/html"
        name: mypd
  volumes:
    - name: mypd
      persistentVolumeClaim:
        claimName: myclaim

```



## Exemple de StorageClass

<br/>

```yaml
---
kind: StorageClass
apiVersion: storage.k8s.io/v1
metadata:
  name: standard
provisioner: kubernetes.io/aws-ebs
parameters:
  type: gp2
reclaimPolicy: Retain
mountOptions:
  - debug
```



## TP 5.3 : PVC et PV

<!-- .slide: class="page-tp5" -->



<!-- .slide: class="page-questions" -->
