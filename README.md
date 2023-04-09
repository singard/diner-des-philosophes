# Diner-des-philosophes
Une proposition de résolution du fameux problème du diner des philosophes
# lancement

Pour lancer le programme vous devez d'abord cloner le projet,
```
git clone https://github.com/singard/diner-des-philosophes.git
```

l'importer et le modifier sur eclipse de préférence.

/!\ Pour que le projet soit compatible vous devez installer lombok sur votre IDE /!\

Pour générer le jar il faudra utiliser maven

``` 
mvn clean install
```

et le lancer en ligne de commande. 
Généralement sur windows on utilise

```
java -jar le_nom_du_projet.jar
```

# Enoncer 

Un nombre n (par exememple n = 5) de philosophes sont assis autour d’une table ronde, sur laquelle
sont installées n assiettes de spaghettis et n fourchettes. Chaque philosophe dispose d’une fourchette
de chaque côté de son assiette.

Les philosophes commencent tous à penser.

Lorsque l’un d’eux se décide à faire une pause pour manger, il cherche à prendre les deux fourchettes
qui se trouvent de chaque côté de son assiette. Si un de ses voisins mange, il devra attendre... Si une
des deux fourchettes est occupée par un voisin, il devra attendre.

Le programme prend fin dès que NBCYCLES cycles penser-manger ont été réalisés par chaque
philosophe.

L'affichage devra se faire sur une seule ligne représentant l'état de chaque Philosophe (P P P P P). On
doit afficher seulement quand il y a Changement d'Etat d'1 Philosophe (P P P P P , P P P P A, P P P P M, P
P P A M, etc...).

![image](https://user-images.githubusercontent.com/77006808/230772666-f47f310d-8e83-4c30-a478-1ae5ad702d59.png)

source : http://www.fredericgrolleau.com/2021/12/le-probleme-dit-du-diner-des-philosophes.html

# Résolution

Pour résoudre ce problème il y a principalement deux choses à prendre en compte.

premièrement chaque philosophe peu importe qui ils sont doit être indépendants. Du coup, il faut la création d'un thread par philosophe qui évoluera indépendamment des autres à des durées de changement d'état différence des autres.

En second temps, les couverts sont à établir  comme une ressource limitée partagée entre tous les philosophes. De ce fait on peut réaliser une liste de sémaphores représentatifs des couverts présents sur la table.

pour choisir quels couverts prenaient prénom ce schéma suivant, qui représente la table sous forme de liste, avec à son bord N philosophe.


![Diagramme sans nom drawio(1)](https://user-images.githubusercontent.com/77006808/230774650-45bf9bd3-67ea-4884-a5c2-beedb9843fe2.png)


n étant le nombre défini de philosophe présent à la table.

le O représente une assiette mais sera ignoré par la suite car inutile dans l'exercice car il y a une assiette pas philosophe. Mais le X représente les couverts et il y en a un seul entre chaque assiette.

Pour savoir quel couvert prendre c'est plutôt simple.

Par exemple, le philosophe 0 devra prendre dans sa main droite le couvert 0 et dans sa main gauche le couvert 1.
Pour le philosophe N il devra prendre dans sa main droite le couvert N et dans sa main gauche le couvert N+1.

Cependant il reste un problème, comment faire pour que le philosophe en bout de liste récupère un couvert N+1 vu qu'il n'existe pas ? En faisant en sorte de faire reboucler la liste grace au modulo, qui permettra de récupérer le tout premier couvert car il ne faut pas oublier que la table est ronde donc la liste doit boucler sur elle-même.

Exemple: 

Il y a 5 philosophes à la table. pour le philosophe 4 (la liste commence avec le philosophe 0), il prend dans sa main droite le couvert 4 et dans sa main gauche il prendre le couvert 4+1 % 5 = 0.

Pour un cas général cela donne pour le couvert gauche : numéro_du_philosophe + 1 % nombre_total_de_philosophe.

