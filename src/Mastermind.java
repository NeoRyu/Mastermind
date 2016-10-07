/*	JEU MASTERMIND (MODE CONSOLE JAVA)
 *
 *	Auteur 		: COUPEZ Frédéric
 *  Date 		: 31 AOÛT 2016 
 *	Compatible 	: Console IDE JAVA (Eclipse)
 *
 * Utilité 		: Permettre de se divertir et imaginer une mecanique de resolution
 * Règles 		: https://fr.wikipedia.org/wiki/Mastermind
 */

// LIBRAIRIES
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Mastermind {

	// VARIABLES D'ENVIRONNEMENT
	public static int[] couleur;					// contient le scouleur (numerique)
	public static int[] tSoluce;					// solution 		
	public static int[] tProp;						// tProp saisie		
	
	public static int NbrColor = 2;					// Nombre de couleur
	public static int colorGood;					// Couleurs bien placées
	public static int colorBad;						// Couleurs mal positionné	
	public static int essais;						// Nombre d'essais	
	public static int NbrPions = 4;					// Nombre de pions	
	public static int Index;						// Curseur navigation
	
	public static boolean continuer = true;			// sortie du program ou reboot
	public static boolean debugHack = true;			// affichage de la solution ?
	public static boolean basicRules = true;		// Regle basique ou avancée ?
	public static boolean colorMultiple = false;	// couleur unique ou multiple ?		
	public static boolean MCPChoice = true;			// MENU PRINCIPAL (1 PLAY / 0 DECODE ?)
	public static boolean Aleatoire = true;			// decode : premiere sequence aleatoire ?
		
	public static ArrayList<Integer> colorRestant = new ArrayList<>(); 	// Liste comprenant les couleurs n'ayant pas été éliminée
	public static ArrayList<ArrayList<Integer>> Historique = new ArrayList<ArrayList<Integer>>(); // Liste multidimentionnelle comprennant toute les sequences (individus) proposées et leur resultat
		
	// INSTANCIATION	
	public static Scanner saisie = new Scanner(System.in);
	
	
	public static void main(String[] args) {							//OK
		System.out.println("     MASTERMIND\n");
		Mastermind mind = new Mastermind();		
		do {
			System.out.println("Lancer une partie ?");
			continuer = ConvertStringToBool(saisie.nextLine());	
			if (continuer) {
				System.out.println("Vous préférez jouer vous-même ?");
				MCPChoice = ConvertStringToBool(saisie.nextLine());				
				if (MCPChoice){	
					System.out.println("\nInitialisation du jeu...");	
					mind.config();			
					mind.play();			
				} else {
					System.out.println("\nInitialisation du decryptage...");
					mind.config();
					mind.launcher();
				}
			} else {
				saisie.close();
			}
		} while (continuer == true);		
	}
	
	public void config() {												//OK + TODO
		System.out.println("____________________________________________________");
		System.out.print("CONFIGURATION.BAT");	
		System.out.println("\n1 > Souhaitez vous jouer avec les règles de base ?");
		basicRules = ConvertStringToBool(saisie.nextLine());
		
		NbrColor = 8;
		if (basicRules == false) {
			System.out.println("\n2 > Combien de couleur composeront le jeu ? (max 10)");
			do {
				NbrColor = Integer.parseInt(saisie.nextLine());	
				if (NbrColor > 10)
					System.out.println("Le maximum est de 10... Resaisissez :");
				if (NbrColor < 2)
					System.out.println("Le minimum est de 2... Resaisissez :");
			} while (NbrColor > 10 || NbrColor < 2);
		}
		
		NbrPions = 4;
		if (basicRules == false) {
			System.out.println("\n3 > Combien de pions composeront la solution ?");
			NbrPions = Integer.parseInt(saisie.nextLine());		
		}
		
		colorMultiple = false;
		if (basicRules == false)
			if (NbrColor > NbrPions) {
				System.out.println("\n4 > Une même couleur peut-elle être présente\n    plusieurs fois dans la solution ?");
				colorMultiple = ConvertStringToBool(saisie.nextLine());		
			}
		
		couleur = new int[NbrColor];
		for (int i = 0 ; i < NbrColor ; i++)	{ 
			couleur[i] = i; 
		}
		
		Aleatoire = true;
		/*		//TODO : Exception java.lang.NullPointerException...
		if (!MCPChoice) {
			System.out.println("\nTRON > Generer un nombre aléatoire pour le premier essai ?");
			Aleatoire = ConvertStringToBool(saisie.nextLine());
			if (Aleatoire == false) {
				saisie();	
				
				String s;
				do {
					System.out.println("\nQuelle sera la première séquence ?");
					s = saisie.nextLine();
					if (s.length() < NbrPions)
						System.out.println("La sequence est composée de "+NbrPions+" pions...");
				} while (s.length() != NbrPions);
				
				for (int i = 0 ; i < NbrPions ; i++)
				{
					tProp[i] = Integer.parseInt(s.substring(i,i+1));
				}
				
			}
		}
		*/
		
	
		Master();
		System.out.println("\n____________________________________________________");
	}
		
	public static void Master() {										//OK
		// Initialisation des réglages du jeu		
		essais = 10;
		colorGood = 0;
		colorBad = 0;		
		tSoluce = new int[NbrPions];		
		tProp = new int[NbrPions];		
		
		// Generation d'une solution selon la config du jeu
		if(debugHack) {
			System.out.println("____________________________________________________");
			System.out.println("INTRUSION SYSTEM DETECTEE !!!");
		}
		if (colorMultiple == true) {	
			if(debugHack) System.out.print("\n> SOLUTION COMPOSÉE DE COULEURS MULTIPLE\n(pouvant être présente plusieurs fois dans la solution)");
			for (int i = 0 ; i < NbrPions ; i++) 				// genere x chiffres [1,2,3,4]
				tSoluce[i] = (int) Math.floor((Math.random() * NbrColor));
		} else {
			if(debugHack) System.out.print("\n> SOLUTION COMPOSÉE DE COULEURS UNIQUE\n(n'apparaissant qu'une fois maximum dans la solution)");
			boolean exit = true;
			for (int i = 0 ; i < NbrPions ; i++) 				// genere x chiffres [1,2,3,4]
			{
				do { 
					exit = true;					
					//genere une couleur unique aleatoirement
					tSoluce[i] = (int) Math.floor((Math.random() * NbrColor));					
					if (i > 0) 									// pas besoin de check au premier tour				
						for (int j = 0 ; j < i ; j++) 			// check de l'index 0 a l'index i
							if (tSoluce[i] == tSoluce[j]) 			// si a l'index j on trouve une couleur identique						
								exit = false;					// on empeche de sortir de la boucle			
				} while(!exit);
			}
		}		
		
		// REVELATEUR DE SOLUTION
		if(debugHack) {
			System.out.print("\n\n>> SOLUTION : ");
			for (int i = 0 ; i < NbrPions ; i++)
				System.out.print(tSoluce[i]);
		}
	}
	
	public void play() {												//OK
		System.out.println("Gort klaatu barada nikto\n\nMCP > \"ARRIVEREZ VOUS A CASSER MON CODE ?\""
				+ "\nTRON > Info > \""+NbrPions+" chiffres, compris entre 0 et "+(couleur.length-1)+"\"\n");
		
		// Tant qu'on a pas la solution exacte, et qu'il reste des essais
		while ( (! victory()) && (essais > 0) )	
		{
			
			saisie();			// On choisi les couleurs
			
			check();			// On verifie si identique / different

			if (! victory()) 	// Si l'on a aps resolu, on retire un essai
				essais--;
		}
		
		// Une fois tout nos essais terminés, ou que l'on a gagné, on affiche :
		if (victory()) 
			System.out.println("MCP > \"IMPOSSSIBLE, VouS AvEz TrouuvVé LA sSOlutionnn...\" ");
		else
		{
			System.out.print("MCP > \"Dommage, la sequence exacte était :");
			for (int i = 0 ; i < NbrPions ; i++)
				System.out.print(tSoluce[i]);
			System.out.println("\"\nMCP > \"Vous êtes une donnée corrompue, suppression en cours...\"");
		}
	}	
	
	private void launcher() {											//OK
		// Création d'une liste contenant toute les couleurs du jeu
		for (int i = 0 ; i < NbrColor ; i++)
			colorRestant.add(i);
		
		// SEQUENCE DE JEU : MCP versus TRON
		System.out.println("Gort klaatu barada nikto\n\nMCP > \"ARRIVEREZ VOUS A CASSER MON CODE ?\""
				+ "\nTRON > Info > \""+NbrPions+" chiffres, compris entre 0 et "+(couleur.length-1)+"\"\n");
		
		// DEBUT DU DECODAGE
		while ( (! victory()) && (essais > 0) )	
		{
			System.out.println("\nMCP : Entrez une séquence (essais restant :"+essais+")");				
			generateCode();			// Tentative de resolution automatique du code				
			check();				// On verifie si identique / different				
			//histoAdd();				// On historise la sequence saisie				
			//decode();				// On verifie les genes forts et faibles
			
			if (! victory()) 	// Si TRON n'a pas encore resolu, un essai est retiré
				essais--;
		}
		
		// Une fois tout les essais terminés (TRON t'as merdé...), ou que TRON a gagné, on affiche :
		if (victory()) 
			System.out.println("MCP > \"IMPOSSSIBLE, VouS AvEz TrouuvVé TriIicHÉéé...\" ");
		else
		{
			System.out.print("MCP > \"Dommage, la séquence exacte était :");
			for (int i = 0 ; i < NbrPions ; i++)
				System.out.print(tSoluce[i]);
			System.out.println("\"\nMCP > \"Détection d'une anomalie SYSTEM, suppression en cours...\"");
		}
	}
	
	public void saisie() {												//OK

			String s;
		do {
			System.out.println("\nEntrez une sequence (essais restant :"+essais+")");
			s = saisie.nextLine();
			if (s.length() < NbrPions)
				System.out.println("/!\\ Mauvaise saisie, vérifiez que votre \n    chiffre comporte bien "+NbrPions+" caracteres...\n");
		} while (s.length() != NbrPions);
		
		for (int i = 0 ; i < NbrPions ; i++)
		{
		   tProp[i] = Integer.parseInt(s.substring(i,i+1));
		}	

	}	

	public void check() {												//OK

		colorGood = 0;		
		colorBad = 0;		
		
		// TODO
		int compteur = 0;
		
		// VERIFICATION DES COULEURS BIEN PLACEES
		for (int i = 0 ; i < NbrPions ; i++)
		{		
			if (tSoluce[i] == tProp[i])
				colorGood++;	
			else {
				// Sinon on crée un tableau avec les index mal positionnés / incorrects
				compteur++;
			}
		}	
		
		int[] tempProp = new int[compteur]; //java.lang.NullPointerException
		int[] tempSolu = new int[compteur]; //java.lang.NullPointerException
		int k=0;
		for (int i = 0 ; i < NbrPions ; i++)
		{			
				if (tSoluce[i] != tProp[i]) {
					// Sinon on crée un tableau avec les index mal positionnés / incorrects
					tempSolu[k] = tSoluce[i];
					tempProp[k] = tProp[i];					
					k++;
				}
		}

		if ((tempSolu != null) || (tempProp != null)) {	
			// VERIFICATION DES BONNES COULEURS MAL POSITIONNÉES			
			for (int i = 0 ; i < tempProp.length ; i++)
			{	// On verifie chaque pion encore present			
				for (int j = 0 ; j < tempSolu.length ; j++)
				{	// Si sa couleur correspond a l'une des positions
					if ((tempProp[i] == tempSolu[j]) && (i != j)) {
						colorBad++;
						j = tempSolu.length;	// On quitte la boucle pour passer au prochain pion
					}
				}			
			}
		}
		
		System.out.print("\nIl y a "+colorGood+" pions bien placés...\n");
		if (colorGood < 4)
			System.out.println("Il y a "+colorBad+" pions mal positionné...\n");
		
	}	
	
	private void generateCode() {										//TODO

		if (!Historique.isEmpty()) {			
			// Si la liste n'est pas vide, on va comparer genetiquement
			ArrayList<Integer> findSequence =  histoCheck();
			
			// On injecte la combinaison calculée génétiquement
			for (int i = 0 ; i < NbrPions ; i++) {
				tProp[i] = (int) findSequence.get(i);
			}
			
		} else {
			aleatCode();
		}
		
		System.out.print("TRON > ");
		for (int i = 0 ; i < NbrPions ; i++)
			System.out.print(tProp[i]);
	}	
	
	private void aleatCode() {											//TODO
		if (Aleatoire) {
			// Si la liste est vide (premier essai), on génère un nombre aleatoire :
			if (colorMultiple == true) {
				// COULEUR MULTIPLE
				for (int i = 0 ; i < NbrPions ; i++) {
					tProp[i] = (int) Math.floor((Math.random() * NbrColor));
				}
			} else {
				// COULEUR UNIQUE
				boolean exit = true;
				for (int i = 0 ; i < NbrPions ; i++)
					do { 
						exit = true;
						tProp[i] = (int) Math.floor((Math.random() * NbrColor));	
						if (i > 0) 									
							for (int j = 0 ; j < i ; j++) 			
								if (tProp[i] == tProp[j]) 
									exit = false;					
					} while(!exit);		
			}
		} else {
			Aleatoire = true; // On modifie une fois la premiere sequence passée pour les prochaines !
		}
	}
	
	private ArrayList<Integer> histoCheck() {							//TODO
		// > comparer au prealable l'historique pour trouver des couleurs bien positionnée
		// > on gardera les couleurs bien positionnée pour la prochaine sequence
		
		// if second tour () else :
		
		System.out.println("je suis ici");
		
		ArrayList<Integer> seqCalculee = new ArrayList<>(); 		
		ArrayList<Integer> tempHisto = new ArrayList<>();
		ArrayList<Integer> tempComp = new ArrayList<>();
		
		for (Index = 0 ; Index < Historique.size() ; Index++) {				
			// On recherche parmis tout les individus le premier ayant de bons genes (solutions)
			if (Integer.parseInt(Historique.get(NbrPions+1).toString()) > 0) {
				tempHisto = Historique.get(Index);
				Index = Historique.size();				
				
				// On le comparer alors avec les autres individus pour trouver le meilleur candidat
				for (int i = 0 ; i < Historique.size() ; i++) {	
					tempComp = Historique.get(i);					
					// Si l'individu i présente de meilleurs genes et possibilités, on le selectionne
					if (( Integer.parseInt(tempHisto.get(NbrPions+1).toString()) <= Integer.parseInt(tempComp.get(NbrPions+1).toString()) )  && Integer.parseInt(tempHisto.get(NbrPions+2).toString()) <= Integer.parseInt(Historique.get(NbrPions+2).toString())) 
						tempHisto = tempComp;
				} // On possède maintenant le meilleur individu (parmis les dernières generations)
			} 
		}
		
		// On va maintenant chercher pour cet individu supérieur les redondances de genes... 
		for (int g = 0 ; g < Integer.parseInt(Historique.get(NbrPions).toString()) ; g++) {
			// ...avec les autres individus
			for (int i = 0 ; i < Historique.size() ; i++) {
				// Si a l'emplacement i on trouve une redondance && que la chaine n'est pas identique en tout point
				if (( Integer.parseInt(tempHisto.get(i).toString()) == Integer.parseInt(Historique.get(i).toString()) ) && (!tempHisto.equals(Historique))) {
					// On a trouvé un gène fort, qui pourrait etre transmis
					// il faut alors stocké la couleur et incrémenté un compteur
					// TODO > creer une autre arraylist[i,i,i] // position/couleur/fitness
					// fitness = ((colorGood * NbrPions) + (colorbad))
					
				}
			}
		}
		
		// On va maintenant trier les couleurs par position et niveau de redondance
		// if arraylist[i] (position,couleur) equals arraylist[!i]
		// count thiscolor++;
		
		tempHisto = compare(tempComp);		
		
		//	colorRestant[i] qui n'a jamais été utilisé dans l'historique pour cette position.
		// tempComp
		
		for (int i = 0 ; i < NbrPions ; i++) {
			seqCalculee.add(tempComp.get(i));	//position			
		}
		
		
		return seqCalculee;		
	}		
	
	private ArrayList<Integer> compare (ArrayList<Integer> tempComp) {	//TODO
		// > puis parmis colorRestant[i], positionné une couleur a un emplacement 
		// > qui n'a jamais été utilisé dans l'historique pour cette couleur.
		
		return tempComp;
	}	
		
	private void decode() {			
		//TODO
		// DECODAGE (ALGORITHME GENETIQUE)
		
		// Si il n'y a aucune bonne réponse ni positionnement
		// on supprime toute les couleurs utilisées ce tour.
		if (colorGood == 0 && colorBad == 0)
			for (int i = 0 ; i < NbrPions ; i++)
				remove(tProp[i]);	
		
	}	
	private void remove(int i) {										//OK
		// SUPPRESION DES GENES DEFECTUEUX (ALGORITHME GENETIQUE)
		Index = colorRestant.indexOf(tProp[i]);
		colorRestant.remove(Index);
	}
	
	public boolean victory() {											//OK
		// Si le nombre de bonnes couleurs est identique au nombre de pions, on retourne true
		return (colorGood == NbrPions);		
	}
	
	private void histoAdd() {											//OK
			// HISTORISATION (MEMOIRE GENETIQUE)
			// Creation de la nouvelle liste contenant la sequence et le resultat
			ArrayList<Integer> temp = new ArrayList<>();
			for (int i = 0 ; i < NbrPions ; i++)
				temp.add(tProp[i]);
			temp.add(colorGood);
			temp.add(colorBad);		
			// Historisation de cette liste
			Historique.add(temp);
		}
		
	public static boolean ConvertStringToBool(String s) {				//OK
		switch (s.toUpperCase()) {
			// Possible d'ajouter d'autres termes (en majuscule)...
			case "1" :
			case "TRUE":
			case "OUI" :
			case "YES" :			
				return true;
			default:
				return false;
		}
	}	// Convertir un String en Boolean (evite les exceptions en cas de mauvaise saisie)
	
	private String convertColor(int i) {								//OK
		String colorint;
		switch (i) {
			case 0 :
				colorint = "Rouge";
				break;
			case 1 :
				colorint = "Jaune";
				break;
			case 2 :
				colorint = "Vert";
				break;
			case 3 :
				colorint = "Bleu";
				break;
			case 4 :
				colorint = "Orange";
				break;
			case 5 :
				colorint = "Fuchsia";
				break;
			case 6 :
				colorint = "Violet";
				break;
			case 7 :
				colorint = "Rose";
				break;
			case 8 :
				colorint = "Blanc";
				break;
			case 9 :
			default :
				colorint = "Noir";
				break;
		}
		/*
		for (int j = 0 ; j < couleur.length ; j++)
			System.out.print(j+" > "+convertColor(j)+"\n");
		*/
		return colorint;		
	}
}
