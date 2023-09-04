
# TP 8 : Helm

## TP 8.1 : Helm

- Installer le client `helm` (documentation sur <https://docs.helm.sh/using_helm/#installing-helm>)

```bash
# don't curl | bash IRL
curl -s https://raw.githubusercontent.com/helm/helm/master/scripts/get | bash
```

- Configurer les droits (Role Based Access Control) pour Helm

```bash
kubectl create serviceaccount tiller -n kube-system
kubectl create clusterrolebinding tiller-is-admin \
      --clusterrole=cluster-admin --serviceaccount=kube-system:tiller
```

- Installer `Tiller`, la partie `serveur` de `Helm` :

```bash
helm init
```

- Surveiller la création des pods dans le namespace `kube-system`
- Vérifier que `Helm` est correctement installé :

```bash
helm version
```

- Créer un namespace `guestbook` :

```bash
kubectl create ns guestbook
```

- Visualiser le contenu du fichier `values.yml` fourni
- Se positionner dans le répertoire `Lab8/guestbook` et déployer une release avec

```shell
minikube_ip=$(curl ifconfig.me)
echo "minikube_ip : ${minikube_ip}"
helm install . \
    --set ingress.gateway.host=gb-api.${minikube_ip}.xip.io \
    --set ingress.frontend.host=gb.${minikube_ip}.xip.io \
    --namespace guestbook
```

- Observez la sortie produite par la commande d'installation
- Surveiller la création des pods dans le namespace `guestbook`
- Tester le guestbook dans votre navigateur à l'adresse <gb.$(ip de minikube).xip.io>
- Lister les releases avec `helm list`
- Mettre à jour le guestbook avec la commande

```shell
helm upgrade <release name> \
  --reuse-values \
  --set image.mainVersion=0.6.1 .
```

- Observer la mise à jour par la commande `watch kubectl get pods,rs,deploy,svc,ing -n guestbook`


<div class="pb"></div>
