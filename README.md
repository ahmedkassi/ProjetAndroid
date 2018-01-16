# ProjetAndroid
Pour la partie Android : 
On a créé une application en suivant le tutorial du cours. 
Sauf qu’on a fait des modifications :
•	La récupération des donnés se fait auprès du Cloud Heroku du projet Web.
•	L’application Android est un client MQTT(Publisher) qui envoi des requêtes MQTT au broker pour publier sur des topics (Les ampoules Philips Hue) ceci nous permet d’éteindre/allumer les philipsHue à partir de notre téléphone Android. Pour ce faire on a utilisé les bibliothèques Paho (pour les requêtes MQTT )et Volley(pour les requêtes HTTP) de Android.

On a créé un client MQTT (Publisher) Arduino constitué de deux parties 
Partie luminosité : 
À partir d’un capteur de luminosité on capte tout changement de la luminosité afin de publier la valeur de cette dernière dans un topic (building1/room1/light).
Partie Contrôle de Brightness :
À partir d’un potentiomètre on peut changer la luminosité des hues. Pour ce faire on a utilisé le montage suivant : 
