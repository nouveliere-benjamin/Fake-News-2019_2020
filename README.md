
# Projet de détection de violence dans des tweets

SafeTweet est une application mobile pour Android qui permet à un utilisateur de Twitter de naviguer en toute sécurité sur ce-dit réseau social. L’application est un Twitter-like, c’est-à-dire, une application semblable à Twitter. Elle permet à son utilisateur de consulter sa timeline - Fil d’actualités - et d’informer (voire filtrer) le contenu qui peut s’avérer dangereux. Le domaine d’application des fake news étant très vaste, l’application a alors pour but de rassurer ou/et de prévenir les personnes victimes de harcèlement.

Pour faire cela nous avons fait :
 - Une application android : https://github.com/nouveliere-benjamin/Fake-News-2019_2020
 - Un notebook qui regroupe la création et l'entrainement du modèle LSTM ainsi que la Dataset utilise : https://github.com/nathan46/detection_tweet_violent_LSTM
 - Une Api python qui permet de faire le lien entre les deux : https://github.com/nathan46/api_flask_LSTM

SafeTweet est une application mobile pour Android qui permet à un utilisateur de Twitter de naviguer en toute sécurité sur ce-dit réseau social. L’application est un Twitter-like, c’est-à-dire, une application semblable à Twitter. Elle permet à son utilisateur de consulter sa timeline - Fil d’actualités - et d’informer (voire filtrer) le contenu qui peut s’avérer dangereux. Le domaine d’application des fake news étant très vaste, l’application a alors pour but de rassurer ou/et de prévenir les personnes victimes de harcèlement.

## Pour commencer
Cette application est l'interface finale de notre projet, c'est à partir de cette application que vous allez vous connecter à twitter et avoir toutes les fonctionnalitées annoncées ci-dessus.

### Cloner le dépôt
```bash
$ git clone https://github.com/nouveliere-benjamin/Fake-News-2019_2020.git
```
### Ouvrir avec Android Studio

 1. Démarrer **Android Studio**
 2. Ouvrir l'onglet **File**, puis **ouvrir**
 3. Choisir le dossier **Fake-News-2019_2020** (à l'endroit où vous avez fait le **git clone**)

 ### Changer l'adresse IP de l'API
 Dans le fichier :

	 app/src/main/java/com/safetweet/CustomTweet.java

Vous devez changer la variable **url**, en mettant l'**URL** de votre Api ou **localhost** si vous êtes en local.
Exemple :
```java
private EnumTweetEval evalTweet(String tweet){
	String url = "http://18.222.213.247"; // <--- Url de l'API /!\ A changer
	JSONObject jsonObject = new JSONObject();
	try {
		jsonObject.put("tweet", tweet);
	} catch (JSONException e) {
		e.printStackTrace();
	}
	String json = jsonObject.toString();
	String resp = "rien";

	try {
		resp = this.post(url, json);
	} catch (IOException e) {
		e.printStackTrace();
	}

	float nb = Float.parseFloat(resp.substring(9,15));
	if(nb > 0.95 ){                          // <--- Possibilite de changer les seuils
		return EnumTweetEval.DANGER;
	}else if(nb > 0.7){                      // <--- Possibilite de changer les seuils
		return EnumTweetEval.WARN;
	}else{
		return EnumTweetEval.SAFE;
	}
}
```
Vous pouvez aussi modifier les seuils des alertes en fonction du fonctionnement de votre modèle.
### Lancer l'application
Il ne vous reste plus qu'à lancer l'application en appuyant sur **RUN** (flèche verte en haut)

## Comment l'utiliser
Pour avoirs toutes les informations concernant l'utilisation de l'application, je vous invite a lire le manuel d'utilisation qui se trouve dans ce dépôt.

	manuel_utilisation.pdf
