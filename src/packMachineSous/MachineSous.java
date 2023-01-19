package packMachineSous;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import javax.print.DocFlavor.INPUT_STREAM;

public class MachineSous {
	private Random random;
	
	private float solde;
	private int[] mise;
	private int nbMise;
	
	//Streak à appliquer après
	private boolean enStreak;
	private int streak;
	
	private float[] gains;
	private int nbGains;

	private String[] symbolsComb;
	private int nbSymboles;
	private final static HashMap<String, Float> SYMBOLESMULTIPLICATEUR = new HashMap<String, Float>() {
		{
			put("seven", 12.5f);
			put("trefle", 10f);
			put("crown", 8f);
			put("diamant", 6.5f);
			put("balle", 4f);
			put("bar", 2f);
		}
	};
	private final static Map<String, Integer> NBSYMBOLES = new HashMap<String, Integer>() {
		{
			put("seven", 2);
			put("bar", 3);
			put("trefle", 3);
			put("crown", 5);
			put("diamant", 6);
			put("balle", 8);
		}
	};
	//================Constructeur====================
	public MachineSous() {
		this.solde = 50.00f;// 50 euros en solde de bienvenue
		this.mise = new int[0];
		this.nbMise = 0;
		this.nbGains = 0;
		this.enStreak = false;
		this.streak = 0;
		this.gains = new float[0];
		random = new Random();
		
		for (int nb : NBSYMBOLES.values())
			nbSymboles += nb;
		symbolsComb = new String[nbSymboles];
		
		String k;
		int v;
		int index = 0;
		for (Map.Entry<String, Integer> entry : NBSYMBOLES.entrySet()) {
			k = entry.getKey();
			v = entry.getValue();
			for (int i = 1; i <=v ;i++) {
				symbolsComb[index] = k;
				index+=1;
			}
		}
		melange();
	}
	//================Constructeur====================
	
	
	//================Solde====================
	public float getSolde() {
		return solde;
	}
	
	public void addSolde(float solde) {
		if (this.solde + solde > 5000)
			throw new IllegalArgumentException("Le montant ajouté au solde dépasse le maximum permis! (5000)");
		this.solde += solde;
	}
	
	private boolean soldeSuffisant(int mise, int nbLigne) {
		return this.solde >= mise * nbLigne;
	}
	
	public boolean decrementSolde(int mise, int nbLigne) {
		if (!soldeSuffisant(mise, nbLigne))
			return false;
		this.solde -= mise * nbLigne;
		return true;
		
	}
	//================Solde====================
	
	//================Symbolecombinaison====================
	public String[] getSymbolsComb() {
		return symbolsComb;
	}

	public void setSymbolsComb(String[] symbolsComb) {
		this.symbolsComb = symbolsComb;
	}
	
	//================Symbolecombinaison====================
	//================Mise====================
	public int[] getMise() {
		//Référencement seulement
		return mise;
	}
	
	public int getNbMise() {
		return nbMise;
	}
	
	public void addMise(int mise) {
		int[] temp = new int[getNbMise() + 1];
		for (int i = 0; i < getNbMise(); i++)
			temp[i] = this.mise[i];
		temp[getNbMise()] = mise;

		this.mise = temp;
		this.nbMise +=1;
	}
	private String miseList() {
		String tabMise = "[";
		for (int i=0; i< getNbMise();i++) {
			tabMise += this.mise[i];
			if (i < getNbMise() -1 )
				tabMise+= ", ";
		}
		tabMise += "]";
		
		return tabMise;
	}
	
	//================Mise====================
	
	//================Gains====================
	public float[] getGain() {
		//Référencement seulement
		return gains;
	}
	
	public int getNbGains() {
		return nbGains;
	}
	
	private void addGain(float gain) {
		float[] temp = new float[getNbGains() + 1];
		for (int i = 0; i < getNbGains(); i++)
			temp[i] = this.gains[i];
		temp[getNbGains()] = gain;

		this.gains = temp;
		this.nbGains += 1;
	}

	public float calculGain(String symbol, int mise) {
		float multiplicateur = getSymbolmultiplicateur().get(symbol);
		addGain(mise * multiplicateur);
		this.solde += gains[this.nbGains-1];
		return this.solde;
	}
	
	private String gainsList() {
		String tabGains = "[";
		for (int i=0; i< getNbGains();i++) {
			tabGains += this.gains[i];
			if (i < getNbGains() -1 )
				tabGains+= ", ";
		}
		tabGains += "]";
		
		return tabGains;
	}
	//================Gains====================

	public void melange() {
		String symTemp;
		int indexTemp;
		for(int i = this.symbolsComb.length - 1; i > 0; i--) {
			indexTemp = random.nextInt(i + 1);
			symTemp = symbolsComb[indexTemp];
			symbolsComb[indexTemp] = symbolsComb[i];
			symbolsComb[i] = symTemp;
		}
	}
	public String[] play() {
		String[] res = new String[3];
		for (int i = 0 ;i < 3; i++)
			res[i] = symbolsComb[random.nextInt(nbSymboles)];
		return res;
	}
	
	public String checkWin(String[] arr) {
		String symb1 = arr[0];
		if(symb1.equals(arr[1]) && symb1.equals(arr[2]))
			return symb1;
			
		return "0";
	}
	public String[] checkWin(String[][] arr) {
		String[] symbGagnant = new String[3];
		int nbGagnant=0;
		String checkMatch;
		for (int i=0;i< arr.length;i++) {
			checkMatch = checkWin(arr[i]);
			if ( checkMatch != "0") {
				symbGagnant[nbGagnant] = checkMatch;
				nbGagnant +=1;
			}
		}
		if(nbGagnant == 0)
			return new String[0];
		else if(nbGagnant == 1)
			return new String[] {symbGagnant[0]};
		else if(nbGagnant == 2)
			return new String[] {symbGagnant[0], symbGagnant[1]};
		
		return symbGagnant;
	}

	private static HashMap<String, Float> getSymbolmultiplicateur() {
		return SYMBOLESMULTIPLICATEUR;
	}

	public static void main(String[] args) {
		MachineSous m = new MachineSous();
		/*System.out.println(MachineSous.SYMBOLESMULTIPLICATEUR.values());
		System.out.println("Solde: " + m.solde);
		m.addSolde(500);
		System.out.println("Solde: " + m.solde);
		m.addMise(4);
		m.decrementSolde(4,2);
		System.out.println("Solde: " + m.solde);
		m.addMise(6);
		m.decrementSolde(6,3);
		System.out.println("Solde: " + m.solde);
		System.out.println("Mises Liste:\n" + m.miseList());
		m.calculGain('A', 6);
		m.calculGain('D', 4);
		m.calculGain('C', 15);
		System.out.println("Gains Liste:\n" + m.gainsList());
		System.out.println("Solde: " + m.solde);*/
		
		
//		====================DeroulementConsole======================
		for(int i = 0; i < m.getSymbolsComb().length;i++)
			System.out.println(m.getSymbolsComb()[i]);
		Scanner sc = new Scanner(System.in);
		int nbMise = 1;
		int mise;
		int nbLigne;
		String rejouer;
		while (true) {
			System.out.println("========================================\nSolde courant: " + m.getSolde());
			System.out.print("Entrer la valeur a miser: ");
			while (true) {
				mise = sc.nextInt();
				if (mise <= m.getSolde())
					break;
				else
					System.out.print("Votre mise doit etre une valeur inferieur a votre solde: ");
			}
			System.out.print("Entrer le nombre de lignes dont lesquelles vous voulez miser [1-3]: ");
			while(true) {
				nbLigne = sc.nextInt();
				if (nbLigne < 4 && nbLigne > 0)
					if(nbLigne * mise <= m.getSolde())
						break;
					else
						System.out.print("Avec votre mise et le nombre de lignes entrer, "
								+ "veuillez a ne pas dépasser votre solde.");
				else
					System.out.print("Le nombre de lignes doit etre soit 1,2 ou 3: ");
			}
			String[][] resultat = new String[3][3];
			for (int i =0;i< resultat.length;i++)
				resultat[i] = m.play();
			m.decrementSolde(mise, nbLigne);
			System.out.println("Solde apres " + nbMise + " mise(s): " + m.getSolde());
			for(String[] cTab : resultat) {
				for (String c : cTab)
					System.out.print(c + " ");
				System.out.println();
			}
			System.out.println();
			String[] checkWin = m.checkWin(resultat);
			//System.out.println(checkWin);
			for (int i=0;i < checkWin.length;i++)
				if (checkWin[i] != "0")
					m.calculGain(checkWin[i], mise);
			System.out.println("Solde apres gain: " + m.getSolde());
			System.out.println("Vous voulez rejouer [*/N] ?");
			rejouer = sc.next();
			if (rejouer.charAt(0) == 'N' || m.getSolde() == 0)
				break;
			nbMise+=1;
		}
	}
}
