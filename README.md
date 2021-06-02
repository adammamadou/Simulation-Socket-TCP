# Simulation-Socket

## Information
- interaction entre le client et le serveur
- le client initie la connexion
- le timout est à 30 s
- le client demande au serveur un nombre variable de paquet en precisant sa capacité de reception Buffer
- le serveur compare sa capacité de reception et envoie le paquet à sa demande s'il peut envoyer une fois ou plusieur fois avec un ack
- le client envoie le paquet fin puis le serveur attend 30 second et ferme la connexion


## Execution du projet
### Assurez vous d'avoir le JDK et JRE installé
- Pour Compiler soit faites Javac *.java ou bien javac Serveur.java && Javac Client.java
- pour executez tapez la commande suivante java Serveur puis java Client
- en premier il faut executez le Serveur car si vous executez le client il va genéré des erreurs parce qu'il ne trouve pas le serveur pour se connecter.
