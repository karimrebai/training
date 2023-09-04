# Annexe : Installation de minikube et kubectl

_Au cas où vous ne pouvez pas utiliser les VMs fournies par votre formateur, ou vous voulez refaire les exercices après la formation, il vous faudra installer sur votre poste *minikube* et *kubectl*. Vous pouvez suivre les instructions ci-dessous pour le faire._

Merci de bien prendre le temps de bien lire toutes les instructions d'installation de _Minikube_ et de ne pas vous précipiter. Toute tentative d'aller trop vite risque fortement de se solder par une instance _Minikube_ non fonctionnelle et vous fera perdre du temps pour la suite.

Les instructions diffèrent en fonction des combinaisons __OS__ (Windows 10, Windows 7, Linux, MacOS) et hyperviseur (HyperV, VirtualBox, KVM, Xhyve), veuillez être attentif à bien lire les instructions qui vous concernent.


## Pré-requis pour l'installation de Minikube en local

### Configuration Minimale

- Processeur core i5
- Mémoire vive 4Go
- Espace disque libre > 10 Go
- Options de virtualisation VT-x/AMD-v activées
- Connexion Internet lors du premier lancement
- Droits d'administration

### Configuration Idéale

- Processeur core i5 ou mieux
- Mémoire vive 8Go ou plus
- Espace disque libre > 20 Go
- Options de virtualisation VT-x/AMD-v activées
- Connexion Internet lors du premier lancement
- Droits d'administration

### Système d'exploitation et virtualisation

- Linux : VirtualBox, KVM
- macOS : Xhyve, VirtualBox ou VMWare Fusion
- Windows : Hyper-V ou VirtualBox

Voir la page [README.md](https://github.com/kubernetes/minikube/blob/master/README.md) du dépôt github de Minikube pour voir les détails des contraintes par rapport aux systèmes d'exploitation et des hyperviseurs utilisables.


## Pré-requis pour l'installation de Minikube dans une VM non locale

- Minikube supporte aussi l'option  `--vm-driver=none` qui fait tourner Kubernetes en s'appuyant uniquement sur Docker.
- Il n'y a donc pas besoin d'un hyperviseur mais uniquement de Docker
- Il est fortement déconseillé de le faire tourner directement sur un desktop linux
- Il est plutôt conseillé de le faire tourner dans une VM type AWS, GCE ou Azure


## Installation de Kubectl

- Parcourir la documentation d'installation de `kubectl` : [install kubectl](https://kubernetes.io/docs/tasks/tools/install-kubectl/)
- Installer kubectl (de préférence dans la dernière version disponible, voir <https://storage.googleapis.com/kubernetes-release/release/stable.txt>)
- Ajuster le `PATH` pour pouvoir lancer `kubectl`
- Optionnel, sous Linux, MacOS ou Windows Bash: mettre en place la completion bash (ou zsh) pour `kubectl`. (Cf. `kubectl completion --help`, ou complétion pour [fish](https://fishshell.com/) partielle disponible [ici](https://github.com/evanlucas/fish-kubectl-completions) ou [ici](https://github.com/tuvistavie/fish-kubectl)).
- Si vous installez kubectl via un package manager vérifiez que la version installée est proche de celle votre cluster (en général si il y a plus de deux versions d'écart entre k8s et kubectl vous pouvez rencontrer quelques incompatibilités)


## Installation de Minikube

- Installer le binaire `minikube` dans la dernière version disponible : [install minikube](https://kubernetes.io/docs/tasks/tools/install-minikube/#install-minikube)
- Ajuster le `PATH` pour pouvoir lancer `minikube`
- Vérifier votre version de `minikube` en lançant `minikube version`. La version retournée doit être __>=__ `v0.24.1`
- Fortement conseillé si vous aviez déjà installé `minikube` auparavant : vider le cache avec la commande `minikube cache delete`
- Optionnel : mettre en place la completion bash pour `minikube` (Cf. `minikube completion --help` ou complétion pour [fish](https://fishshell.com/) partielle disponible [ici](https://gist.github.com/looztra/5318ad606be14c06b3c733b736e0c728))
- Lancer `minikube start --help` et observer les options valides pour le paramètre `--vm-driver`


## Démarrage d'une instance Minikube

__!!! Vous devez choisir de partir sur uniquement une des 2 configurations proposées !!!__

### Minikube derrière un proxy

- Veuillez prendre en compte les instructions qui suivent lors de l'utilisation de la commande `minikube start` qui ne devra être lancée qu'une fois que vous aurez fait le choix de l'Hyperviseur à utiliser. La commande `minikube start` __NE DOIT PAS ETRE LANCEE__ avant le paragraphe suivant!
- L'utilisation d'un proxy impacte le démarrage de __Minikube__
  - il faut que _Minikube_ puisse télécharger l'image iso sur laquelle il s'appuie en passant par le proxy, il faut donc que les variables `HTTP_PROXY`, `HTTPS_PROXY` et `NO_PROXY` soient correctement positionnées dans l'invite de commande (shell, dos, powershell) qui sera utilisée pour lancer la commande `minikube` lors du `start` (dans l'étape suivante)
  - il faut transmettre les informations de configuration du proxy au _daemon_ docker qui tourne dans la VM `minikube` : lors du _start_, ajouter les options `--docker-env HTTP_PROXY=$HTTP_PROXY --docker-env HTTPS_PROXY=$HTTPS_PROXY` (ajuster la syntaxe en fonction de l'invite de commande utilisée si nécessaire)
  - une fois Minikube démarré avec les instructions des paragraphes suivants, il faut ajuster la variable `NO_PROXY` pour spécifier qu'il ne faut pas passer par le proxy pour atteindre la VM _Minikube_ :
    - exemple dans le cas d'un shell linux : `export no_proxy=$no_proxy,$(minikube ip)`

### Cas 1 : Version VM locale au poste de travail et Virtualbox, KVM, Xhyve ou HyperV

- Démarrer une instance Kubernetes avec Minikube et le driver qui convient à votre environnement à l'aide de la commande `minikube start --vm-driver=XXXX` en ajustant la valeur du `vm-driver` :
  - Si vous êtes sous `windows 10` et que vous utilisez `docker4windows` ou que vous utilisez déjà `hyperv` alors choissez le driver `hyperv` (__Attention__, si vous choisissez d'utiliser `virtualbox` alors que `hyperV` est installé/activé, vous devrez le désactiver). Assurez-vous de bien lire les instructions disponibles sur la page [Minikube + Driver HyperV](https://github.com/kubernetes/minikube/blob/master/docs/drivers.md#hyperv-driver) et faire les ajustements nécessaires concernant le `hyperv virtual switch` et la désactivation de __la gestion dynamique de la mémoire__ pour éviter les crash intempestifs de votre VM _HyperV_ pendant le reste de la formation.
  - Si vous êtes sous `macOs` et que vous utilisez `docker4mac` ou que vous utilisez déjà `xhyve` alors choissez le driver `xhyve` (si vous choisissez d'utiliser `virtualbox` alors que `xhyve` est installé/activé, vous devrez le désactiver)
  - Si vous êtes sous `windows` (quelque soit la version) ou `macOS` et que vous n'êtes pas dans les situations citées ci-dessus, choisissez `virtualbox` (et installez-le s'il ne l'est pas encore)
  - Si vous êtes sous `Linux`, vous avez le choix entre `virtualbox` et `kvm2` en fonction de vos préférences (Sur les machines de formation Zenika, si VirtualBox n'est pas installé, faites-le via la commande `sudo apt install virtualbox-nonfree` pour les version Linux Mint ou `sudo apt install virtualbox` pour les version Linux Ubuntu)
  - Nous déconseillons fortement d'utiliser la solution `--vm-driver=none` sur votre desktop linux, l'utilisation d'une VM `virtualbox` ou `kvm2` vous permettra en effet de mieux isoler `minikube` du reste de votre système
- Une fois la VM `minikube` installée, vous pouvez vérifier que la configuration pour votre instance Kubernetes a bien été positionnée en lançant `kubectl version`. Si vous observez une version retournée pour le client et le serveur alors c'est OK.
- Note :
  - Si vous êtes derrière un proxy, pensez à modifier la variable `NO_PROXY` comme précisé dans le paragraphe précédent
  - Vous pouvez spécifier la version de k8s que vous voulez utiliser avec l'option `--kubernetes-version=vX.Y.Z`, pour connaître les versions disponibles `minikube get-k8s-versions`. Selon la version choisit l'API peut être compatible (exemple : dans la version de k8s v1.8.0 le pod du dashboard fourni via minikube >= v0.25.O ne démarre pas car le descripteur utilise des mots clefs de l'API en version v1.9.0)

### Cas 2 : Version Docker sur VM Cloud ou Desktop

- Exécutez les commandes shell suivantes pour préparer le host :

```shell
export MINIKUBE_WANTUPDATENOTIFICATION=false
export MINIKUBE_WANTREPORTERRORPROMPT=false
export MINIKUBE_HOME=$HOME
export CHANGE_MINIKUBE_NONE_USER=true
mkdir $HOME/.kube || true
touch $HOME/.kube/config

export KUBECONFIG=$HOME/.kube/config
```

- Démarrer une instance Kubernetes avec Minikube `sudo -E minikube start --vm-driver=none`
- Vérifier que les containers docker de Minikube sont bien lancés avec `docker ps`
- Une fois l'instance `minikube` démarrée, vous pouvez vérifier que la configuration pour votre instance Kubernetes a bien été positionnée en lançant `kubectl version`. Si vous observez une version retournée pour le client et le serveur alors c'est OK.


## Recommandation : commande 'watch' et windows

Lors de plusieurs exercices vous serez invité à lancer une commande (`kubectl` ou autre) précédée de la commande unix `watch`. `watch` est une commande permettant d'exécuter un programme périodiquement en affichant le résultat à l'écran ce qui permet de voir l'évolution des résultats de cette commande au cours du temps. Il n'y a pas d'équivalent de `watch` sous windows, cependant plusieurs options sont possibles pour les utilisateurs windows :

- Utiliser une session locale [MobaXterm](https://mobaxterm.mobatek.net/), qui lance un shell [Cygwin](https://www.cygwin.com/) où la commande `watch` est disponible parce que préinstallé. Note: [MobaXterm](https://mobaxterm.mobatek.net/) fait aussi partie des logiciels recommandés pour lancer une connexion ssh, voir section suivante.
- Installer le paquet `watch` sous [Cygwin](https://www.cygwin.com/) si ce dernier (ou équivalent) est déjà disponible sur votre poste
- Passer par la commande _PowerShell_ `Watch` fournie par le module dont le code source est disponible dans le répertoire `watch-for-windows-ps` existant dans le zip fourni pour démarrer les Labs. Pour pouvoir utiliser ce module, déposer le fichier `Watch.ps1` dans le répertoire `$home\Documents\WindowsPowerShell\Modules\Watch`, `$home` représentant le répertoire de base de votre utilisateur courant (`C:\Users\<username>` par exemple). Vous devrez ensuite lancer toutes les commandes `kubectl` depuis une console _PowerShell_. Ce module `Watch` ne fonctionne cependant pas tout à fait comme la commande `watch` linux, vous devrez ajouter un paramètre supplémentaire pour spécifier la fréquence de rafraichissement (qui vaut 2 secondes par défaut avec `watch` sous linux) : `watch 2 kubectl get pods`
- Si aucune des options précédentes n'est possible, vous pourrez remplacer les commandes `watch kubectl` par `kubectl get <xx> -w` qui permet de rester en attente de modification dans la commande `kubectl` mais avec un affichage moins lisible qu'en passant par la commande `watch`


## Recommandation : sous windows, utilisation d'un terminal type MobaXterm/PuTTY pour les commandes ssh

- Si vous êtes sous windows, nous vous conseillons fortement de passer par un terminal type [MobaXterm](https://mobaxterm.mobatek.net/) ou [PuTTY](http://www.putty.org/) au lieu d'utiliser `minikube ssh` lorsque demandé dans la suite des Labs pour faciliter l'édition des lignes de commandes (ou vous allez vous heurter potentiellement à l'impossibilité de corriger les commandes shell que vous aller taper)
- Pour configurer les accès à la VM `minikube` en passant par _MobaXterm_ ou _PuTTY_ :
  - lancer `minikube ssh-key` pour visualiser le chemin vers la clé ssh à utiliser pour vous connecter à la VM
  - convertir la clé au format attendu par `PuTTY`/`MobaXterm`
  - lancer `minikube ip` pour connaitre l'ip de la VM `minikube`
  - créer une nouvelle connexion avec ces informations (username : `docker`)

<div class="pb"></div>
