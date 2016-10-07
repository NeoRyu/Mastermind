import java.util.ArrayList;

public class Resolve_Masterming {
	
	// Curseur permettant de naviguer dans les listes
	public static int Index;		
	// Liste multidimentionnelle comprennant toute les sequences (individus) proposées et leur resultat
	public static ArrayList<ArrayList<Integer>> Historique = new ArrayList<ArrayList<Integer>>(); 
	// Liste comprenant les couleurs n'ayant pas été éliminée
	public static ArrayList<Integer> colorRestant = new ArrayList<>(); 	
	public static boolean Aleatoire = true;
	

	private static Mastermind mind = new Mastermind();
	public void launcher() {			//OK
		// CONFIGURATION	
		mind.config();	
		
		/*//TODO > une erreur quelque part ici :
		Aleatoire = true;
		if (mind.basicRules == false) {
			System.out.println("\nTRON > Generer un nombre aléatoire pour le premier essai ?");
			Aleatoire = mind.ConvertStringToBool(mind.saisie.nextLine());
			if (Aleatoire == false) {
				String s;
				do {
					System.out.println("\nQuelle sera la première séquence ?");
					 s = mind.saisie.nextLine();
					if (s.length() < mind.NbrPions)
						System.out.println("La sequence est composée de "+mind.NbrPions+" pions...\n");
				} while (s.length() != mind.NbrPions);
				for (int i = 0 ; i < mind.NbrPions ; i++)
				{
				   mind.tProp[i] = Integer.parseInt(s.substring(i,i+1));
				}
			}
		}
		*/
		
		// Création d'une liste contenant toute les couleurs du jeu
		for (int i = 0 ; i < mind.NbrColor ; i++)
			colorRestant.add(i);
		
		// HACK : simplification du jeu
		if (mind.debugHack) {
			mind.basicRules = true;
			mind.NbrColor = 4;
			mind.NbrPions = 4;
			mind.colorMultiple = false;
		}		
		
		// SEQUENCE DE JEU
		play();
	}
	
	private void play() {				//OK
		// MCP versus TRON
		System.out.println("Gort klaatu barada nikto\n\nMCP > \"ARRIVEREZ VOUS A CASSER MON CODE ?\""
				+ "\nTRON > Info > \""+mind.NbrPions+" chiffres, compris entre 0 et "+(mind.couleur.length-1)+"\"\n");
		
		// DEBUT DU DECODAGE
		while ( (! mind.victory()) && (mind.essais > 0) )	
		{
			System.out.println("\nMCP : Entrez une séquence (essais restant :"+mind.essais+")");
			
			generateCode();			// Tentative de resolution automatique du code
			
			mind.check();			// On verifie si identique / different
			
			histoAdd();				// On historise la sequence saisie
			
			decode();				// On verifie les genes forts et faibles
			
			if (! mind.victory()) 	// Si TRON n'a pas encore resolu, un essai est retiré
				mind.essais--;
		}
		
		// Une fois tout les essais terminés (TRON t'as merdé...), ou que TRON a gagné, on affiche :
		if (mind.victory()) 
			System.out.println("MCP > \"IMPOSSSIBLE, VouS AvEz TrouuvVé TriIicHÉéé...\" ");
		else
		{
			System.out.print("MCP > \"Dommage, la séquence exacte était :");
			for (int i = 0 ; i < mind.NbrPions ; i++)
				System.out.print(mind.tSoluce[i]);
			System.out.println("\"\nMCP > \"Détection d'une anomalie SYSTEM, suppression en cours...\"");
		}
	}
	
	private void generateCode() {		

		/*
		if (!Historique.isEmpty()) {
			// Si la liste n'est pas vide, on va comparer genetiquement
			ArrayList<Integer> findSequence = new ArrayList<>(); 	
			findSequence = histoCheck();
			
			// On injecte la combinaison calculée génétiquement
			for (int i = 0 ; i < mind.NbrPions ; i++) {
				mind.tProp[i] = (int) findSequence.get(i);
			}
		} else {	
		*/		
		System.out.print(mind.NbrColor);
			if (Aleatoire) {
				// Si la liste est vide (premier essai), on génère un nombre aleatoire :
				if (mind.colorMultiple == true) {
					// COULEUR MULTIPLE
					for (int i = 0 ; i < mind.NbrPions ; i++) {
						mind.tProp[i] = (int) Math.floor((Math.random() * mind.NbrColor));
					}
				} else {
					// COULEUR UNIQUE
					boolean exit = true;
					for (int i = 0 ; i < mind.NbrPions ; i++)
						do { 
							exit = true;
							mind.tProp[i] = (int) Math.floor((Math.random() * mind.NbrColor));	
							if (i > 0) 									
								for (int j = 0 ; j < i ; j++) 			
									if (mind.tProp[i] == mind.tProp[j]) 
										exit = false;					
						} while(!exit);		
				}
			} else {
				Aleatoire = true; // On modifie une fois la premiere sequence passée pour les prochaines !
			}
		//}
		for (int i = 0 ; i < mind.NbrPions ; i++)
			System.out.print(mind.tProp[i]);
	}	
	
	private ArrayList<Integer> histoCheck() {		
		//TODO
		// > comparer au prealable l'historique pour trouver des couleurs bien positionnée
		// > on gardera les couleurs bien positionnée pour la prochaine sequence
		
		// if second tour () else :
		
		ArrayList<Integer> seqCalculee = new ArrayList<>(); 		
		ArrayList<Integer> tempHisto = new ArrayList<>();
		ArrayList<Integer> tempComp = new ArrayList<>();
		
		for (Index = 0 ; Index < Historique.size() ; Index++) {				
			// On recherche parmis tout les individus le premier ayant de bons genes (solutions)
			if (Integer.parseInt(Historique.get(mind.NbrPions+1).toString()) > 0) {
				tempHisto = Historique.get(Index);
				Index = Historique.size();				
				
				// On le comparer alors avec les autres individus pour trouver le meilleur candidat
				for (int i = 0 ; i < Historique.size() ; i++) {	
					tempComp = Historique.get(i);					
					// Si l'individu i présente de meilleurs genes et possibilités, on le selectionne
					if (( Integer.parseInt(tempHisto.get(mind.NbrPions+1).toString()) <= Integer.parseInt(tempComp.get(mind.NbrPions+1).toString()) )  && Integer.parseInt(tempHisto.get(mind.NbrPions+2).toString()) <= Integer.parseInt(Historique.get(mind.NbrPions+2).toString())) 
						tempHisto = tempComp;
				} // On possède maintenant le meilleur individu (parmis les dernières generations)
			} 
		}
		
		// On va maintenant chercher pour cet individu supérieur les redondances de genes... 
		for (int g = 0 ; g < Integer.parseInt(Historique.get(mind.NbrPions).toString()) ; g++) {
			// ...avec les autres individus
			for (int i = 0 ; i < Historique.size() ; i++) {
				// Si a l'emplacement i on trouve une redondance && que la chaine n'est pas identique en tout point
				if (( Integer.parseInt(tempHisto.get(i).toString()) == Integer.parseInt(Historique.get(i).toString()) ) && (!tempHisto.equals(Historique))) {
					// On a trouvé un gène fort, qui pourrait etre transmis
					// il faut alors stocké la couleur et incrémenté un compteur
					// TODO > creer une autre arraylist[i,i,i] // position/couleur/fitness
					// fitness = ((mind.colorGood * NbrPions) + (mind.colorbad))
					
				}
			}
		}
		
		// On va maintenant trier les couleurs par position et niveau de redondance
		// if arraylist[i] (position,couleur) equals arraylist[!i]
		// count thiscolor++;
		
		

		
		tempHisto = compare(tempComp);		
		
		//	colorRestant[i] qui n'a jamais été utilisé dans l'historique pour cette position.
		// tempComp
		
		for (int i = 0 ; i < mind.NbrPions ; i++) {
			seqCalculee.add(tempComp.get(i));	//position			
		}
		
		
		return seqCalculee;		
	}		
	
	private ArrayList<Integer> compare (ArrayList<Integer> tempComp) {		
		//TODO
		// > puis parmis colorRestant[i], positionné une couleur a un emplacement 
		// > qui n'a jamais été utilisé dans l'historique pour cette couleur.
		
		
		return tempComp;
	}	
		
	private void decode() {			
		//TODO
		// DECODAGE (ALGORITHME GENETIQUE)
		
		// Si il n'y a aucune bonne réponse ni positionnement
		// on supprime toute les couleurs utilisées ce tour.
		if (mind.colorGood == 0 && mind.colorBad == 0)
			for (int i = 0 ; i < mind.NbrPions ; i++)
				remove(mind.tProp[i]);	
		
	}	
	private void remove(int i){		//OK
		// SUPPRESION DES GENES DEFECTUEUX (ALGORITHME GENETIQUE)
		Index = colorRestant.indexOf(mind.tProp[i]);
		colorRestant.remove(Index);
	}
	
	private void histoAdd() {		//OK
		// HISTORISATION (MEMOIRE GENETIQUE)
		// Creation de la nouvelle liste contenant la sequence et le resultat
		ArrayList<Integer> temp = new ArrayList<>();
		for (int i = 0 ; i < mind.NbrPions ; i++)
			temp.add(mind.tProp[i]);
		temp.add(mind.colorGood);
		temp.add(mind.colorBad);		
		// Historisation de cette liste
		Historique.add(temp);
	}
}
