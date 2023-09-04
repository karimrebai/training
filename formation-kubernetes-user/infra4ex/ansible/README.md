# Préparation de VMs pour lancer Minikube

## Compte AWS

- Vous devez avoir un accès au compte aws `zenika-training`
- Un user **sans API KEY** qui est admin
- et un user **api-only** avec le rôle `docker-trainer` pour pouvoir créer les VMs

A demander à `dsi@zenika.com` ou aux autres formateurs kubernetes [si c'est ta première fois, ton _shadow_ pourra te donner les infos]

## Prérequis

- ansible
- modules boto et boto3, suivant votre version de python :

  ```bash
  pip install boto
  pip install boto3
  ```

  ou

  ```bash
  pip3 install boto
  pip3 install boto3
  ```

- en fonction de la version de boto/boto3 installée, vous devrez peut-être aussi positionner la variable d'environnement `BOTO_USE_ENDPOINT_HEURISTICS=True` pour pouvoir déployer sur `eu-west-3`
- puttygen (oui oui, c'est dispo sous Linux, `putty-tools` sous Ubuntu, `putty` sous Fedora, et sous macOS `brew install putty`)
- variables `AWS_ACCESS_KEY_ID` et `AWS_SECRET_ACCESS_KEY` postionnées (et correspondant à un user AWS "api-only"):

  ```bash
  # Adjust the 'export' command to your shell
  export AWS_ACCESS_KEY_ID='AK123'
  export AWS_SECRET_ACCESS_KEY='abc123'
  ```

## Contexte

Quand la configuration hardware des postes de formation (chez un client ou pas) n'est pas suffisante pour faire tourner Minikube, il est possible de passer par des VMs AWS

Chaque stagiaire a droit à sa VM avec :

- minikube installé et conf sudo configurée (alias `minikube` vers `sudo -E minikube`)
- kubectl installé, completion bash activée
- vscode (aucun plugin installé par contre)

Il est possible de se connecter dans la VM :

- en ssh, grace aux clés openssh et putty créées
- par le biais d'un browser, en remote desktop en passant par [guacamole](https://guacamole.apache.org/). Il est fortement recommandé de lire la [FAQ](https://guacamole.apache.org/faq/) pour bien comprendre comment utiliser notamment le copier/coller entre la VM et le poste qui héberge le browser

## Lancer la création des VMs

Ajuster :

- le nom de la session, sinon vous aurez des `fixme` dans les noms des objets AWS créés avec `session_name`
- le nombre d'instances créées avec `nb_instances` (vous pouvez relancer le playbook en ajustant le nombre d'instances si nécessaire, les VMs supplémentaires seront ajoutées)

  ```shell
  ansible-playbook create_vms.yml -e session_name=wip-session -e nb_instances=1
  ```

Le playbook créée :

- Un _security group_ dédié
- Une clé ssh partagée pour tous, au format openssh et putty (ppk, si puttygen est dispo sur le host qui lance le playbook ansible [rpm putty sous fedora par ex])
- Autant de VMs que demandé
- Un inventaire à utiliser pour la partie setup et delete des vms

Installation par défaut dans `eu-west-3`

## Tester (c'est douter)

Pour vérifier que les VMs sont up :

```shell
ansible -i target/hosts.${session_name} -m ping nodes
```

## Lancer le provisionning des VMs

Pour lancer l'installation de guacamole et de ses dépendances :

```shell
ansible-playbook -i target/hosts.${session_name} setup_vms.yml
```

- voir `common-vars.yml` pour la config par défaut
- installation de docker, docker-compose, minikube, kubectl, vscode, firefox, chrome, jq, glances
- génération et upload d'un zip du repertoire Exercices/workspace
- configuration système faite pour guacamole
- démarrage de guacamole par un docker-compose

Une fois les VMs provisionnées, vous pouvez vous connecter en ssh avec la clé générée, ou par un browser sur l'IP publique des VMs et le port 9000, http://${ip_pub}:9000/guacamole.
Dans guacamole, le user est `zenika` et le mot de passe est le nom de la session utilisé (`-e session_name=xxx`) pour générer les vms. A noter, le mot de passe `z_remote_user_password` défini dans l'inventaire n'est pas utilisé pour la connexion à guacamole.

## Supprimer les VMs

```shell
ansible-playbook delete_vms.yml -i target/hosts.${session_name}
```

Le playbook supprime :

- les instances EC2
- le _security group_
- la clé côté AWS et en local

L'inventaire n'est pas supprimé

## Next Steps

- rendre paramétrable le port d'écoute de guacamole-ui qui vaut `8080` pour l'instant
- config TLS pour l'ui guacamole
- mot de passe du user `centos` moins évident (utilisé uniquement par guacd pour créer une session remote desktop)
- plugins kubernetes et autres installés dans vscode
- plugins yaml et Kubernetes installés pour vim
