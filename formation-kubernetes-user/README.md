# Formation Kubernetes

## Pour la formation en elle-même

### Utilisation de VSCode et d'extensions

- <https://code.visualstudio.com/>
- [Extension Yaml RedHat](https://marketplace.visualstudio.com/items?itemName=redhat.vscode-yaml)
- [Extension Kubernetes Support](https://marketplace.visualstudio.com/items?itemName=ipedrazas.kubernetes-snippets)
- [Extension Kubernetes par Microsoft](https://marketplace.visualstudio.com/items?itemName=ms-kubernetes-tools.vscode-kubernetes-tools)
- [Extension pour Helm](https://marketplace.visualstudio.com/items?itemName=technosophos.vscode-helm)

Attention : l'extension [YAML par Adam voss](https://marketplace.visualstudio.com/items?itemName=adamvoss.yaml) produit du code YAML non interpretable par Kubernetes (les macros %YAML ne sont pas supportées pour l'instant par Kubernetes, même si elles font bien partie de la norme YAML). Il est donc fortement recommandé de positionner `"yaml.format.enable": false` dans votre `settings.json` global ou dans celui du workspace.

### Utilitaires

- [kubeval](https://github.com/garethr/kubeval) permet de valider la syntaxe de vos descripteurs Kubernetes
- [yamkix](https://github.com/looztra/yamkix) permet de formatter vos descripteurs Kubernetes de manière compatible avec Kubernetes

La config VSCode présente dans le dépôt dans `.vscode` propose 2 tâches permettant de valider/formatter vos descripteurs, il vous suffit de les associer à des raccourcis claviers.

### Documents à fournir aux stagiaires

#### Au démarrage de la formation

- Exercices/workspaces
- REFERENCES.md
- PDF/Slides.pdf
- PDF/CachierExercices.pdf

#### A la fin de la formation

- Exercices/workspaces
- Exercices/corrections
- REFERENCES.md
- PDF/Slides.pdf
- PDF/CachierExercices.pdf

### Répartition (approximative) sur les 3 jours

- Jour 1 :
  - 01_context
  - 02_first_steps
  - 03_pods début (jusqu'à namespaces)
- Jour 2 :
  - 03_pods fin (à partir de cycle de vie et logs)
  - 04_replicasets
  - 05_services
  - 06_volumes début (jusqu'à vol de type HostPath)
- Jour 3 :
  - 06_volumes (à partir de PV/PVC/SC)
  - 07_config_and_secrets
  - 08_deployment_strategies
  - 09_statefulsets
  - 10_architecture
  - 11_enterprise_ready
  - 12_compatible_app

/!\ : le premier TP avec l'installation de Minikube est très consommateur surtout si les configs sont hétérogènes

## Pour le support

### Modèle

- Détails du modèle dans le [wiki](https://github.com/Zenika/Formation--Modele/wiki)
- Documentation du [framework](https://github.com/Zenika/zenika-formation-framework)
- Utilisation du framework en v3

### Supports PDF et Live

Les supports peuvent être obtenus à ces adresses :

Version courante : <https://master-dot-formation-kubernetes-user-dot-zen-formations.appspot.com/> [![CircleCI](https://circleci.com/gh/Zenika/formation-kubernetes-user/tree/master.svg?style=svg&circle-token=d977b3830bcfcb671ca6777466cd319ab77af15c)](https://circleci.com/gh/Zenika/formation-kubernetes-user/tree/master)

### Utilisation avec Docker

Pour ceux qui veulent se passer de l'installation de `node` et `npm`, il est possible d'utiliser [Docker](https://www.docker.com).

Lancer une des commandes suivantes :

```shell
./run.sh dev   # pour afficher les slides
./run.sh prod  # pour afficher la page de garde
./run.sh pdf   # pour générer les `.pdf` des slides
./run.sh pdf-labs   # pour générer les `.pdf` des labs
./run.sh clean # pour terminer le conteneur Docker
```

Il est également possible de combiner les commandes :

```shell
./run.sh clean pdf prod
```

Par défaut, le _fournisseur_ de démon docker est sélectionné de manière automatique (docker-machine, boot2docker ou natif). Pour forcer un fournisseur en particulier (si vous êtes sous linux mais que vous avez aussi docker-machine par exemple), vous pouvez positionner la variable d'environnement `Z_DOCKER_PROVIDER` qui peut prendre les valeurs `machine|boot2docker|native`.

Exemple sous bash : `Z_DOCKER_PROVIDER=native ./run.sh dev`

Exemple sous fish : `env Z_DOCKER_PROVIDER=boot2docker ./run.sh dev`
