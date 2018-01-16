# ProjetAndroid
Pour la partie Android : 
On a créé une application en suivant le tutorial du cours. 
Sauf qu’on a fait des modifications :
•	La récupération des donnés se fait auprès du Cloud Heroku du projet Web.
•	L’application Android est un client MQTT(Publisher) qui envoi des requêtes MQTT au broker pour publier sur des topics (Les ampoules Philips Hue) ceci nous permet d’éteindre/allumer les philipsHue à partir de notre téléphone Android. Pour ce faire on a utilisé les bibliothèques Paho (pour les requêtes MQTT )et Volley(pour les requêtes HTTP) de Android.

