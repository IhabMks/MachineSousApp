package packMachineSous;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import java.awt.Font;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import javax.swing.border.EtchedBorder;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import java.awt.CardLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.Insets;

public class MachineSousApp {

	// Classe MachineSous
	private static MachineSous ms;

	// ==============================Panel&LabelGlobaux==============================
	private JFrame frmMachineSous;

	private JPanel interfaceJeu, interfaceMenu;

	private JLabel ligneLabel, miseLabel, gainLabel, soldeLabel, miseTotalLabel, gainPerteLabel;

	private JPanel rgbLeftPanel, machinePanel, rgbRightPanel;
	private JLabel leftCenterIcon, leftTopIcon, leftBottomIcon;
	private JLabel centerTopIcon, centerCenterIcon, centerBottomIcon;
	private JLabel rightCenterIcon, rightTopIcon, rightBottomIcon;

	private JButton btnStart, btnRetour, btnSoldePlus;
	private JButton btnLigneMoins, btnLignePlus;
	private JButton btnMiseMoins, btnMisePlus, btnMaxMise;
	// ==============================Panel&LabelGlobaux==============================

	// ==============================VariableManip==============================
	private boolean spinEnCours = false;
	private boolean stopLeftRotation = false;
	private boolean stopCenterRotation = false;

	private final String[] SymbolsComb = ms.getSymbolsComb();

	private final String[] couleurRotation = { "marron", "vertclair", "violet", "bgSecondaire", "vertsombre",
			"sarcelle" };
	private final HashMap<String, Color> couleur = new HashMap<String, Color>() {
		{
			put("noir", new Color(0, 0, 0));
			put("vertclair", new Color(144, 238, 144));
			put("violet", new Color(128, 0, 128));
			put("marron", new Color(150, 75, 0));
			put("vertsombre", new Color(21, 71, 52));
			put("sarcelle", new Color(0, 178, 169));
			put("bgPrincipal", new Color(255, 102, 102));
			put("bgSecondaire", new Color(255, 51, 51));
			put("vertclair1", new Color(102, 255, 51));
		}
	};
	private final HashMap<String, ImageIcon> icons = new HashMap<String, ImageIcon>() {
		{
			put("seven", new ImageIcon(MachineSousApp.class.getResource("/media/seven.png")));
			put("trefle", new ImageIcon(MachineSousApp.class.getResource("/media/trefle.png")));
			put("crown", new ImageIcon(MachineSousApp.class.getResource("/media/crown.png")));
			put("diamant", new ImageIcon(MachineSousApp.class.getResource("/media/diamant.png")));
			put("balle", new ImageIcon(MachineSousApp.class.getResource("/media/balle.png")));
			put("bar", new ImageIcon(MachineSousApp.class.getResource("/media/bar.png")));
		}
	};

	// private String[] resultatRotation;
	private String[][] resultatRotation = new String[3][3];
	private float gainsCourant = 0;


	// ==============================VariableManip==============================
	/**
	 * Launch the application.
	 * 
	 * @throws UnsupportedLookAndFeelException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws ClassNotFoundException
	 */
	private void demarrerPartie() {
		// Resultat
		resultatRotation[1] = ms.play();

		JLabel[] iconeLeftLabels = new JLabel[3];
		JLabel[] iconeCenterLabels = new JLabel[3];
		JLabel[] iconeRightLabels = new JLabel[3];
		for (int c = 0; c < machinePanel.getComponentCount(); c++) {
			JPanel tempPanel = (JPanel) machinePanel.getComponent(c);
			for (int l = 0; l < iconeLeftLabels.length; l++) {
				if (c == 0)
					iconeLeftLabels[l] = (JLabel) tempPanel.getComponent(l);
				if (c == 1)
					iconeCenterLabels[l] = (JLabel) tempPanel.getComponent(l);
				if (c == 2)
					iconeRightLabels[l] = (JLabel) tempPanel.getComponent(l);
			}
		}
		// variable commune entre threadCenterRotation et threadRightRotation
		int rotation = 20;
		int trancheUne = 80 * rotation / 100;
		int trancheDeux = trancheUne + (20 * rotation / 100);

		// ==============================ThreadLeftRotation==============================
		Thread threadLeftRotation = new Thread(() -> {
			int iconeIdx = SymbolsComb.length - 1;
			int leftrotation = 50;
			int leftTrancheUne = 50 * leftrotation / 100;
			int leftTrancheDeux = leftTrancheUne + (32 * leftrotation / 100);
			int trancheTrois = leftTrancheDeux + (8 * leftrotation / 100);
			boolean debutStopRotation = false;
			// Rotation allant du haut vers le bas.

			// l'addition de SymbolsComb.length a la rotation,c'est pour s'assurer qu'une
			// fois que stopRotation est true
			// et que si l'icone en question (X), est situee juste derrière l'icone
			// courante, on a assez de rotation
			// supplementaire à faire pour pouvoir arriver à cet X et s'arreter dessus.
			for (int r = 0; r < leftrotation + SymbolsComb.length; r++) {
				for (int i = 2; i >= 0; i--) {
					iconeLeftLabels[i].setIcon(icons.get(SymbolsComb[(iconeIdx + i) % SymbolsComb.length]));
					// iconeLabels[i].setIcon(icons.get(SymbolsComb[(iconeIdx - i +
					// SymbolsComb.length) % SymbolsComb.length]));
				}
				iconeIdx--;
				if (iconeIdx < 0)
					iconeIdx = SymbolsComb.length - 1;
				try {
					if (r < leftTrancheUne)
						Thread.sleep(75);
					else if (r < leftTrancheDeux)
						Thread.sleep(150);
					else if (r < trancheTrois)
						Thread.sleep(250);
				} catch (InterruptedException e) {
				}
				if (r > leftrotation)
					debutStopRotation = true;
				if (debutStopRotation && iconeLeftLabels[1].getIcon().equals(icons.get(resultatRotation[1][0]))) {
					stopLeftRotation = true;
					break;
				}
			}
		});
		threadLeftRotation.start();
		// ==============================ThreadLeftRotation==============================

		// ==============================ThreadCenterRotation==============================
		Thread threadCenterRotation = new Thread(() -> {
			int iconeIdx = 0;
			int r = 0;

			// Rotation allant du bas vers le haut.

			// meme chose pour l'addition de SymbolsComb.length a la rotation (voir
			// explication au thread précédent)
			while (true) {
				for (int i = 0; i < 3; i++) {
					iconeCenterLabels[i].setIcon(icons.get(SymbolsComb[(iconeIdx + i) % SymbolsComb.length]));
					// iconeLabels[i].setIcon(icons.get(SymbolsComb[(iconeIdx - i +
					// SymbolsComb.length) % SymbolsComb.length]));
				}
				iconeIdx++;
				if (iconeIdx == SymbolsComb.length)
					iconeIdx = 0;
				try {
					if (r < trancheUne)
						Thread.sleep(75);
					else if (r < trancheDeux)
						Thread.sleep(250);
				} catch (InterruptedException e) {
				}
				if (stopLeftRotation)
					r++;
				if (r > rotation && iconeCenterLabels[1].getIcon().equals(icons.get(resultatRotation[1][1]))) {
					stopCenterRotation = true;
					break;
				}
			}
		});
		threadCenterRotation.start();
		// ==============================ThreadCenterRotation==============================

		// ==============================ThreadRightRotation==============================
		Thread threadRightRotation = new Thread(() -> {
			int iconeIdx = SymbolsComb.length - 1;
			int r = 0;

			// Rotation allant du haut vers le bas.

			// meme chose pour l'addition de SymbolsComb.length a la rotation (voir
			// explication au thread précédent)
			while (true) {
				for (int i = 2; i >= 0; i--) {
					iconeRightLabels[i].setIcon(icons.get(SymbolsComb[(iconeIdx + i) % SymbolsComb.length]));
					// iconeLabels[i].setIcon(icons.get(SymbolsComb[(iconeIdx - i +
					// SymbolsComb.length) % SymbolsComb.length]));
				}
				iconeIdx--;
				if (iconeIdx < 0)
					iconeIdx = SymbolsComb.length - 1;
				try {
					if (r < trancheUne)
						Thread.sleep(75);
					else if (r < trancheDeux)
						Thread.sleep(250);
				} catch (InterruptedException e) {
				}
				if (stopCenterRotation)
					r++;
				if (r > rotation && iconeRightLabels[1].getIcon().equals(icons.get(resultatRotation[1][2]))) {
					spinEnCours = false;
					stopLeftRotation = false;
					stopCenterRotation = false;
					break;
				}
			}
		});
		threadRightRotation.start();
		// ==============================ThreadRightRotation==============================

		Thread threadCouleurRotation = new Thread(() -> {
			// Iterer sur tous les buttons du panel gauche et droite ==> animation couleur
			int rgbLen = rgbLeftPanel.getComponentCount();
			while (spinEnCours) {
				// rotation couleur vers l'avant
				for (int i = 0; i < rgbLen; i++) {
					rgbLeftPanel.getComponent(i).setBackground(couleur.get(couleurRotation[i]));
					rgbRightPanel.getComponent(i).setBackground(couleur.get(couleurRotation[i]));
					try {
						Thread.sleep(50);
					} catch (InterruptedException e1) {
					}
				}
				// rotation couleur en arrière
				for (int j = rgbLen - 1, i = 0; j >= 0; j--, i++) {
					rgbLeftPanel.getComponent(i).setBackground(couleur.get(couleurRotation[j]));
					rgbRightPanel.getComponent(i).setBackground(couleur.get(couleurRotation[j]));
					try {
						Thread.sleep(50);
					} catch (InterruptedException e1) {
					}
				}
			}
		});
		threadCouleurRotation.start();

		Thread traitementFinRotation = new Thread(() -> {

			while (true) {
				System.out.println("spinEnCours: " + spinEnCours);
				if (!spinEnCours) {
					String path;
					// Transposition du 2d array
					for (int i = 0; i < 3; i++) {
						JPanel tempPanel = (JPanel) machinePanel.getComponent(i);
						for (int j = 0; j < 3; j++) {
							// Version path de windows
							// Pas de linux sur cette version
							path = ((JLabel) tempPanel.getComponent(j)).getIcon().toString();
							path = path.substring(path.lastIndexOf("/") + 1, path.indexOf('.'));
							resultatRotation[j][i] = path.toString();
						}
					}
					if (Integer.parseInt(ligneLabel.getText()) == 1) {
						String checkWin = ms.checkWin(resultatRotation[1]);
						System.out.println("res: " + checkWin);
						if (checkWin != "0") {
							ms.calculGain(checkWin, ms.getMise()[ms.getNbMise() - 1]);
							gainsCourant += ms.getGain()[ms.getNbGains() - 1];
						}
					}
					// à revoir car c'est généraliser avec nbLigne = 2 et 3 alors que normalement
					// c'est 2 cas différents.
					else if (Integer.parseInt(ligneLabel.getText()) > 1) {
						String[] checkWin = ms.checkWin(resultatRotation);
						if (checkWin.length != 0) {
							for (int i = 0; i < checkWin.length; i++)
								if (checkWin[i] != "0") {
									ms.calculGain(checkWin[i], ms.getMise()[ms.getNbMise() - 1]);
									gainsCourant += ms.getGain()[ms.getNbGains() - 1];
								}
						}
					}
					if (gainsCourant > 0) {
						// Pas besoin d'ajouter les gains au solde, la méthode calculGain, le fait
						// automatiquement
						soldeLabel.setText(String.valueOf(ms.getSolde()));
						Thread threadGainPerte = new Thread(() -> {
							gainPerteLabel.setForeground(couleur.get("vertclair1"));
							gainPerteLabel.setText("+ " + String.valueOf(gainsCourant));
							try {
								Thread.sleep(4000);
							} catch (Exception e1) {
							}
							gainPerteLabel.setText(null);
						});
						threadGainPerte.start();
						soldeLabel.setText(String.valueOf(ms.getSolde()));
						gainLabel.setText(String.valueOf(ms.getNbGains()));
					} else {
						JOptionPane.showMessageDialog(null, "Bonne chance pour la prochaine tentative.", "Perdu!", 0);
					}
					setButton(new JButton[] { 
							btnStart, btnSoldePlus, btnRetour, 
							btnMaxMise, btnMisePlus, btnMiseMoins,
							btnLignePlus, btnLigneMoins }, true);
					
					gainsCourant = 0;
					break;
				}
				try {
					// Endormir le thread question de ne pas le laisser en cours durant toute la rotation
					Thread.sleep(1000);
				} catch (Exception e) {
				}
			}
		});
		traitementFinRotation.start();
	}

	private void setButton(JButton[] jbuttons, boolean action) {
		for (JButton jb : jbuttons)
			jb.setEnabled(action);
	}

	public static void main(String[] args) throws ClassNotFoundException, InstantiationException,
			IllegalAccessException, UnsupportedLookAndFeelException {
		UIManager.setLookAndFeel("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel");
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ms = new MachineSous();
					MachineSousApp window = new MachineSousApp();
					window.frmMachineSous.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MachineSousApp() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmMachineSous = new JFrame();
		frmMachineSous.getContentPane().setBackground(couleur.get("bgPrincipal"));
		frmMachineSous.setTitle("Machine à Sous");
		frmMachineSous.setBounds(100, 100, 700, 500);
		frmMachineSous.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmMachineSous.getContentPane().setLayout(new BorderLayout(0, 0));

		interfaceMenu = new JPanel();
		interfaceMenu.setBackground(couleur.get("bgPrincipal"));
		interfaceMenu.setLayout(new BorderLayout(0, 10));

		interfaceJeu = new JPanel();
		interfaceJeu.setLayout(new BorderLayout(0, 0));

		JPanel cards = new JPanel(new CardLayout());
		cards.add(interfaceMenu, "interfaceMenu");
		cards.add(interfaceJeu, "interfaceJeu");

		frmMachineSous.getContentPane().add(cards, BorderLayout.CENTER);

		JPanel panel_3 = new JPanel();
		panel_3.setBackground(couleur.get("bgPrincipal"));
		interfaceMenu.add(panel_3, BorderLayout.NORTH);

		JPanel panel_4 = new JPanel();
		panel_4.setBackground(couleur.get("bgPrincipal"));
		interfaceMenu.add(panel_4, BorderLayout.CENTER);
		panel_4.setLayout(new BorderLayout(0, 40));

		JLabel lblNewLabel_1 = new JLabel("MACHINE  À  SOUS");
		lblNewLabel_1.setForeground(new Color(255, 255, 255));
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setFont(new Font("Kristen ITC", Font.BOLD, 24));
		panel_4.add(lblNewLabel_1, BorderLayout.NORTH);

		JPanel panel_1_1 = new JPanel();
		panel_1_1.setBorder(
				new CompoundBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), new EmptyBorder(40, 0, 40, 0)));
		panel_1_1.setBackground(couleur.get("bgPrincipal"));
		panel_4.add(panel_1_1);
		panel_1_1.setLayout(new GridLayout(3, 3, 40, 20));

		JPanel panel_1 = new JPanel();
		panel_1.setBackground(couleur.get("bgPrincipal"));
		panel_1_1.add(panel_1);

		JButton btnJouer = new JButton("Jouer");
		btnJouer.setForeground(new Color(255, 255, 255));
		btnJouer.setFont(new Font("Kristen ITC", Font.BOLD, 20));
		btnJouer.setBackground(new Color(51, 153, 204));
		btnJouer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CardLayout cl = (CardLayout) (cards.getLayout());
				cl.next(cards);

				// Placé les 3 premières icones générer aléatoirement;
				// car ces derniers ne sont pas chargés au début(cardLayout index 2, en lance
				// l'app avec le index 2 en premier),
				// et donc, les contenus des panel sont null quand on lance l'app.
				for (int i = 0; i < 3; i++) {
					JPanel tempPanel = (JPanel) machinePanel.getComponent(i);
					for (int j = 0; j < 3; j++) {
						JLabel tempLabel = (JLabel) tempPanel.getComponent(j);
						tempLabel.setIcon(icons.get(SymbolsComb[j]));
					}
				}
			}
		});
		panel_1_1.add(btnJouer);

		JPanel panel_5 = new JPanel();
		panel_5.setBackground(couleur.get("bgPrincipal"));
		panel_1_1.add(panel_5);

		JPanel panel_6 = new JPanel();
		panel_6.setBackground(couleur.get("bgPrincipal"));
		panel_1_1.add(panel_6);

		JButton btnRegles = new JButton("Règles");
		btnRegles.setForeground(new Color(255, 255, 255));
		btnRegles.setFont(new Font("Kristen ITC", Font.BOLD, 20));
		btnRegles.setBackground(new Color(51, 153, 204));
		btnRegles.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Les règles de jeu machine à sous est simple : \n"
						+ "Vous devez insérer de l’argent dans la machine en sélectionnant le nombre de jetons à miser.\n"
						+ "Une fois que vous pressez le bouton de lancement, les rouleaux se mettent à tourner puis s’arrêtent.\n"
						+ "Si les symboles présents forment certaines lignes pré-définies dans les spécificités propres au jeu\n"
						+ "en question, vous générez des gains plus ou moins importants.\n\n"
						+ "Note: Sachez que si vous fixez le nombre de lignes à 1, seule la ligne du milieu sera prise en compte,\n"
						+ "et non pas une des lignes au hasard!", "Règle:", 3);
			}
		});
		panel_1_1.add(btnRegles);

		JPanel panel_7 = new JPanel();
		panel_7.setBackground(couleur.get("bgPrincipal"));
		panel_1_1.add(panel_7);

		JPanel panel_8 = new JPanel();
		panel_8.setBackground(couleur.get("bgPrincipal"));
		panel_1_1.add(panel_8);

		JButton btnQuitter = new JButton("Quitter");
		btnQuitter.setForeground(new Color(255, 255, 255));
		btnQuitter.setFont(new Font("Kristen ITC", Font.BOLD, 20));
		btnQuitter.setBackground(new Color(51, 153, 204));
		btnQuitter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		panel_1_1.add(btnQuitter);

		JPanel panel_9 = new JPanel();
		panel_9.setBackground(couleur.get("bgPrincipal"));
		panel_1_1.add(panel_9);

		JPanel panel_2_1 = new JPanel();
		panel_2_1.setBackground(couleur.get("bgPrincipal"));
		panel_4.add(panel_2_1, BorderLayout.SOUTH);

		JPanel Top = new JPanel();
		Top.setBackground(couleur.get("bgPrincipal"));
		Top.setBorder(
				new CompoundBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), new EmptyBorder(0, 10, 0, 0)));
		interfaceJeu.add(Top, BorderLayout.NORTH);
		Top.setLayout(new BorderLayout(20, 10));

		JPanel panel_2 = new JPanel();
		panel_2.setBackground(couleur.get("bgPrincipal"));
		Top.add(panel_2, BorderLayout.EAST);

		JButton btnCombo = new JButton("Combo");
		btnCombo.setForeground(new Color(255, 255, 255));
		btnCombo.setFont(new Font("Kristen ITC", Font.BOLD, 20));
		btnCombo.setBackground(new Color(51, 153, 255));
		btnCombo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JPanel comboPanel = new JPanel();
				comboPanel.setLayout(new BorderLayout(0, 10));

				JLabel comboImage = new JLabel();
				comboImage.setIcon(new ImageIcon(MachineSousApp.class.getResource("/media/combo.png")));

				JLabel comboLabel = new JLabel("Combo:");
				comboLabel.setForeground(couleur.get("bgPrincipal"));
				comboLabel.setHorizontalAlignment(SwingConstants.CENTER);
				comboLabel.setFont(new Font("Kristen ITC", Font.BOLD, 24));

				comboPanel.add(comboImage, BorderLayout.CENTER);
				comboPanel.add(comboLabel, BorderLayout.NORTH);
				JOptionPane.showMessageDialog(null, comboPanel, "Combinaison:", JOptionPane.PLAIN_MESSAGE, null);
			}
		});

		btnSoldePlus = new JButton("+");
		btnSoldePlus.setForeground(new Color(255, 255, 255));
		btnSoldePlus.setFont(new Font("Kristen ITC", Font.BOLD, 20));
		btnSoldePlus.setBackground(new Color(51, 153, 255));
		btnSoldePlus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					float soldeIncrVal = (float) (Math.round(Float.parseFloat(JOptionPane.showInputDialog(
							"Saisissez le montant à ajouter à votre solde dans une plage allant de 5 à 500 euros inclus:"))
							* 100.0) / 100.0);
					if (soldeIncrVal >= 5 && soldeIncrVal <= 500) {
						ms.addSolde(soldeIncrVal);
						soldeLabel.setText(String.valueOf(ms.getSolde()));
						Thread threadGainPerte = new Thread(() -> {
							gainPerteLabel.setForeground(couleur.get("vertclair1"));
							gainPerteLabel.setText("+ " + String.valueOf(soldeIncrVal));
							try {
								Thread.sleep(4000);
							} catch (Exception e1) {
							}
							gainPerteLabel.setText(null);
						});
						threadGainPerte.start();
					} else
						JOptionPane.showMessageDialog(null, "Le montant dois êtres entre 5 et 500 euros inclus!",
								"Valeur Invalide!", 0);

				} catch (NullPointerException pass) {
				} catch (NumberFormatException soldeIncrValErr) {
					JOptionPane.showMessageDialog(null, "Veuillez saisir un montant valide!", "Valeur Invalide!", 0);
				} catch (IllegalArgumentException maxSoldeAtteintErr) {
					JOptionPane.showMessageDialog(null, maxSoldeAtteintErr.getMessage(), "Solde Maximum Dépasser!", 0);
				}
			}
		});
		FlowLayout fl_panel_2 = new FlowLayout(FlowLayout.CENTER, 20, 10);
		panel_2.setLayout(fl_panel_2);
		panel_2.add(btnSoldePlus);
		panel_2.add(btnCombo);

		JPanel panel_17 = new JPanel();
		panel_17.setBackground(couleur.get("bgPrincipal"));
		Top.add(panel_17, BorderLayout.WEST);
		panel_17.setLayout(new BorderLayout(5, 0));

		JPanel panel = new JPanel();
		panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel.setBackground(couleur.get("bgSecondaire"));
		panel_17.add(panel);

		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(MachineSousApp.class.getResource("/media/euro40.png")));
		panel.add(lblNewLabel);

		soldeLabel = new JLabel(String.valueOf(ms.getSolde()));
		soldeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		soldeLabel.setForeground(Color.WHITE);
		soldeLabel.setFont(new Font("Kristen ITC", Font.BOLD, 22));
		panel.add(soldeLabel);

		btnRetour = new JButton("");
		btnRetour.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CardLayout cl = (CardLayout) (cards.getLayout());
				cl.next(cards);
			}
		});
		btnRetour.setIcon(new ImageIcon(MachineSousApp.class.getResource("/media/back.png")));
		btnRetour.setOpaque(false);
		btnRetour.setContentAreaFilled(false);
		btnRetour.setBorderPainted(false);
		btnRetour.setBorder(null);
		panel_17.add(btnRetour, BorderLayout.WEST);

		gainPerteLabel = new JLabel("");
		gainPerteLabel.setFont(new Font("Kristen ITC", Font.BOLD, 30));
		panel_17.add(gainPerteLabel, BorderLayout.EAST);

		JPanel Machine = new JPanel();
		interfaceJeu.add(Machine, BorderLayout.CENTER);
		Machine.setLayout(new BorderLayout(0, 0));

		rgbLeftPanel = new JPanel();
		rgbLeftPanel.setBackground(couleur.get("bgPrincipal"));
		Machine.add(rgbLeftPanel, BorderLayout.WEST);
		GridBagLayout gbl_rgbLeftPanel = new GridBagLayout();
		gbl_rgbLeftPanel.columnWidths = new int[] { 3 };
		gbl_rgbLeftPanel.rowHeights = new int[] { 9, 9, 9, 9, 9, 0 };
		gbl_rgbLeftPanel.columnWeights = new double[] { 0.0 };
		gbl_rgbLeftPanel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };
		rgbLeftPanel.setLayout(gbl_rgbLeftPanel);

		JButton btnL1 = new JButton(" ");
		btnL1.setFont(new Font("Kristen ITC", Font.BOLD, 20));
		btnL1.setBackground(couleur.get("noir"));
		GridBagConstraints gbc_btnL1 = new GridBagConstraints();
		gbc_btnL1.insets = new Insets(0, 15, 15, 10);
		gbc_btnL1.gridx = 0;
		gbc_btnL1.gridy = 0;
		rgbLeftPanel.add(btnL1, gbc_btnL1);

		JButton btnL2 = new JButton(" ");
		btnL2.setForeground(Color.WHITE);
		btnL2.setFont(new Font("Kristen ITC", Font.BOLD, 20));
		btnL2.setBackground(couleur.get("noir"));
		GridBagConstraints gbc_btnL2 = new GridBagConstraints();
		gbc_btnL2.insets = new Insets(0, 15, 15, 10);
		gbc_btnL2.gridx = 0;
		gbc_btnL2.gridy = 1;
		rgbLeftPanel.add(btnL2, gbc_btnL2);

		JButton btnL3 = new JButton(" ");
		btnL3.setForeground(Color.WHITE);
		btnL3.setFont(new Font("Kristen ITC", Font.BOLD, 20));
		btnL3.setBackground(couleur.get("noir"));
		GridBagConstraints gbc_btnL3 = new GridBagConstraints();
		gbc_btnL3.insets = new Insets(0, 15, 15, 10);
		gbc_btnL3.gridx = 0;
		gbc_btnL3.gridy = 2;
		rgbLeftPanel.add(btnL3, gbc_btnL3);

		JButton btnL4 = new JButton(" ");
		btnL4.setForeground(Color.WHITE);
		btnL4.setFont(new Font("Kristen ITC", Font.BOLD, 20));
		btnL4.setBackground(couleur.get("noir"));
		GridBagConstraints gbc_btnL4 = new GridBagConstraints();
		gbc_btnL4.insets = new Insets(0, 15, 15, 10);
		gbc_btnL4.gridx = 0;
		gbc_btnL4.gridy = 3;
		rgbLeftPanel.add(btnL4, gbc_btnL4);

		JButton btnL5 = new JButton(" ");
		btnL5.setForeground(Color.WHITE);
		btnL5.setFont(new Font("Kristen ITC", Font.BOLD, 20));
		btnL5.setBackground(couleur.get("noir"));
		GridBagConstraints gbc_btnL5 = new GridBagConstraints();
		gbc_btnL5.insets = new Insets(0, 15, 15, 10);
		gbc_btnL5.gridx = 0;
		gbc_btnL5.gridy = 4;
		rgbLeftPanel.add(btnL5, gbc_btnL5);

		JButton btnL6 = new JButton(" ");
		btnL6.setForeground(Color.WHITE);
		btnL6.setFont(new Font("Kristen ITC", Font.BOLD, 20));
		btnL6.setBackground(couleur.get("noir"));
		GridBagConstraints gbc_btnL6 = new GridBagConstraints();
		gbc_btnL6.insets = new Insets(0, 15, 15, 10);
		gbc_btnL6.gridx = 0;
		gbc_btnL6.gridy = 5;
		rgbLeftPanel.add(btnL6, gbc_btnL6);

		machinePanel = new JPanel();
		machinePanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		machinePanel.setBackground(couleur.get("bgPrincipal"));
		Machine.add(machinePanel, BorderLayout.CENTER);
		machinePanel.setLayout(new GridLayout(1, 3, 0, 0));

		JPanel machineLeftPanel = new JPanel();
		machinePanel.add(machineLeftPanel);
		machineLeftPanel.setBackground(couleur.get("bgPrincipal"));
		machineLeftPanel.setLayout(new GridLayout(3, 0, 0, 5));

		leftTopIcon = new JLabel("");
		leftTopIcon.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		leftTopIcon.setBackground(couleur.get("bgPrincipal"));
		leftTopIcon.setHorizontalAlignment(SwingConstants.CENTER);
		machineLeftPanel.add(leftTopIcon);

		leftCenterIcon = new JLabel("");
		leftCenterIcon.setHorizontalAlignment(SwingConstants.CENTER);
		leftCenterIcon.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		leftCenterIcon.setBackground(couleur.get("bgPrincipal"));
		machineLeftPanel.add(leftCenterIcon);

		leftBottomIcon = new JLabel("");
		leftBottomIcon.setHorizontalAlignment(SwingConstants.CENTER);
		leftBottomIcon.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		leftBottomIcon.setBackground(couleur.get("bgPrincipal"));
		machineLeftPanel.add(leftBottomIcon);

		JPanel machineCenterPanel = new JPanel();
		machinePanel.add(machineCenterPanel);
		machineCenterPanel.setBackground(couleur.get("bg"));
		machineCenterPanel.setLayout(new GridLayout(3, 0, 0, 5));

		centerTopIcon = new JLabel("");
		centerTopIcon.setHorizontalAlignment(SwingConstants.CENTER);
		centerTopIcon.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		centerTopIcon.setBackground(couleur.get("bgPrincipal"));
		machineCenterPanel.add(centerTopIcon);

		centerCenterIcon = new JLabel("");
		centerCenterIcon.setHorizontalAlignment(SwingConstants.CENTER);
		centerCenterIcon.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		centerCenterIcon.setBackground(couleur.get("bgPrincipal"));
		machineCenterPanel.add(centerCenterIcon);

		centerBottomIcon = new JLabel("");
		centerBottomIcon.setHorizontalAlignment(SwingConstants.CENTER);
		centerBottomIcon.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		centerBottomIcon.setBackground(couleur.get("bgPrincipal"));
		machineCenterPanel.add(centerBottomIcon);

		JPanel machineRightPanel = new JPanel();
		machinePanel.add(machineRightPanel);
		machineRightPanel.setBackground(couleur.get("bg"));
		machineRightPanel.setLayout(new GridLayout(3, 0, 0, 5));

		rightTopIcon = new JLabel("");
		rightTopIcon.setHorizontalAlignment(SwingConstants.CENTER);
		rightTopIcon.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		rightTopIcon.setBackground(couleur.get("bgPrincipal"));
		machineRightPanel.add(rightTopIcon);

		rightCenterIcon = new JLabel("");
		rightCenterIcon.setHorizontalAlignment(SwingConstants.CENTER);
		rightCenterIcon.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		rightCenterIcon.setBackground(couleur.get("bgPrincipal"));
		machineRightPanel.add(rightCenterIcon);

		rightBottomIcon = new JLabel("");
		rightBottomIcon.setHorizontalAlignment(SwingConstants.CENTER);
		rightBottomIcon.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		rightBottomIcon.setBackground(couleur.get("bgPrincipal"));
		machineRightPanel.add(rightBottomIcon);

		rgbRightPanel = new JPanel();
		rgbRightPanel.setBackground(couleur.get("bgPrincipal"));
		Machine.add(rgbRightPanel, BorderLayout.EAST);
		GridBagLayout gbl_rgbRightPanel = new GridBagLayout();
		gbl_rgbRightPanel.columnWidths = new int[] { 3 };
		gbl_rgbRightPanel.rowHeights = new int[] { 9, 9, 9, 9, 9, 0 };
		gbl_rgbRightPanel.columnWeights = new double[] { 0.0 };
		gbl_rgbRightPanel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };
		rgbRightPanel.setLayout(gbl_rgbRightPanel);

		JButton btnR1 = new JButton(" ");
		btnR1.setForeground(Color.WHITE);
		btnR1.setFont(new Font("Kristen ITC", Font.BOLD, 20));
		btnR1.setBackground(couleur.get("noir"));
		GridBagConstraints gbc_btnR1 = new GridBagConstraints();
		gbc_btnR1.insets = new Insets(0, 10, 15, 15);
		gbc_btnR1.gridx = 0;
		gbc_btnR1.gridy = 0;
		rgbRightPanel.add(btnR1, gbc_btnR1);

		JButton btnR2 = new JButton(" ");
		btnR2.setForeground(Color.WHITE);
		btnR2.setFont(new Font("Kristen ITC", Font.BOLD, 20));
		btnR2.setBackground(couleur.get("noir"));
		GridBagConstraints gbc_btnR2 = new GridBagConstraints();
		gbc_btnR2.insets = new Insets(0, 10, 15, 15);
		gbc_btnR2.gridx = 0;
		gbc_btnR2.gridy = 1;
		rgbRightPanel.add(btnR2, gbc_btnR2);

		JButton btnR3 = new JButton(" ");
		btnR3.setForeground(Color.WHITE);
		btnR3.setFont(new Font("Kristen ITC", Font.BOLD, 20));
		btnR3.setBackground(couleur.get("noir"));
		GridBagConstraints gbc_btnR3 = new GridBagConstraints();
		gbc_btnR3.insets = new Insets(0, 10, 15, 15);
		gbc_btnR3.gridx = 0;
		gbc_btnR3.gridy = 2;
		rgbRightPanel.add(btnR3, gbc_btnR3);

		JButton btnR4 = new JButton(" ");
		btnR4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnR4.setForeground(Color.WHITE);
		btnR4.setFont(new Font("Kristen ITC", Font.BOLD, 20));
		btnR4.setBackground(couleur.get("noir"));
		GridBagConstraints gbc_btnR4 = new GridBagConstraints();
		gbc_btnR4.insets = new Insets(0, 10, 15, 15);
		gbc_btnR4.gridx = 0;
		gbc_btnR4.gridy = 3;
		rgbRightPanel.add(btnR4, gbc_btnR4);

		JButton btnR5 = new JButton(" ");
		btnR5.setForeground(Color.WHITE);
		btnR5.setFont(new Font("Kristen ITC", Font.BOLD, 20));
		btnR5.setBackground(couleur.get("noir"));
		GridBagConstraints gbc_btnR5 = new GridBagConstraints();
		gbc_btnR5.insets = new Insets(0, 10, 15, 15);
		gbc_btnR5.gridx = 0;
		gbc_btnR5.gridy = 4;
		rgbRightPanel.add(btnR5, gbc_btnR5);

		JButton btnR6 = new JButton(" ");
		btnR6.setForeground(Color.WHITE);
		btnR6.setFont(new Font("Kristen ITC", Font.BOLD, 20));
		btnR6.setBackground(couleur.get("noir"));
		GridBagConstraints gbc_btnR6 = new GridBagConstraints();
		gbc_btnR6.insets = new Insets(0, 10, 15, 15);
		gbc_btnR6.gridx = 0;
		gbc_btnR6.gridy = 5;
		rgbRightPanel.add(btnR6, gbc_btnR6);

		JPanel panel_16 = new JPanel();
		panel_16.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_16.setBackground(couleur.get("bgPrincipal"));
		Machine.add(panel_16, BorderLayout.NORTH);

		JLabel lblNewLabel_6 = new JLabel("MACHINE À SOUS");
		lblNewLabel_6.setForeground(new Color(255, 255, 255));
		lblNewLabel_6.setFont(new Font("Kristen ITC", Font.BOLD, 20));
		panel_16.add(lblNewLabel_6);

		JPanel Bottom = new JPanel();
		Bottom.setBackground(couleur.get("bgPrincipal"));
		Bottom.setBorder(
				new CompoundBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), new EmptyBorder(0, 10, 0, 0)));
		interfaceJeu.add(Bottom, BorderLayout.SOUTH);
		Bottom.setLayout(new GridLayout(0, 5, 10, 0));

		JPanel panel_10 = new JPanel();
		panel_10.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_10.setBackground(couleur.get("bgSecondaire"));
		Bottom.add(panel_10);
		panel_10.setLayout(new BorderLayout(0, 0));

		btnLigneMoins = new JButton("");
		btnLigneMoins.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int nbLigne = Integer.parseInt(ligneLabel.getText());
				int miseVal = Integer.parseInt(miseLabel.getText());
				if (nbLigne > 1) {
					ligneLabel.setText(String.valueOf(nbLigne - 1));
					miseTotalLabel.setText(String.valueOf(miseVal * (nbLigne - 1)));
				}
			}
		});
		btnLigneMoins.setBorderPainted(false);
		btnLigneMoins.setOpaque(false);
		btnLigneMoins.setContentAreaFilled(false);
		btnLigneMoins.setBorder(null);
		btnLigneMoins.setIcon(new ImageIcon(MachineSousApp.class.getResource("/media/minus30.png")));
		panel_10.add(btnLigneMoins, BorderLayout.WEST);

		btnLignePlus = new JButton("");
		btnLignePlus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int miseVal = Integer.parseInt(miseLabel.getText());
				int nbLigne = Integer.parseInt(ligneLabel.getText());
				if (nbLigne < 3 && (miseVal * (nbLigne + 1)) <= ms.getSolde()) {
					ligneLabel.setText(String.valueOf(nbLigne + 1));
					miseTotalLabel.setText(String.valueOf(miseVal * (nbLigne + 1)));
				}
			}
		});
		btnLignePlus.setIcon(new ImageIcon(MachineSousApp.class.getResource("/media/plus30.png")));
		btnLignePlus.setOpaque(false);
		btnLignePlus.setContentAreaFilled(false);
		btnLignePlus.setBorderPainted(false);
		btnLignePlus.setBorder(null);
		panel_10.add(btnLignePlus, BorderLayout.EAST);

		JPanel panel_15 = new JPanel();
		panel_15.setBackground(couleur.get("bgSecondaire"));
		panel_10.add(panel_15, BorderLayout.CENTER);
		panel_15.setLayout(new BorderLayout(0, 0));

		JLabel lblNewLabel_3 = new JLabel("Lignes");
		lblNewLabel_3.setForeground(new Color(255, 255, 255));
		lblNewLabel_3.setFont(new Font("Kristen ITC", Font.BOLD, 10));
		lblNewLabel_3.setBackground(new Color(255, 51, 0));
		lblNewLabel_3.setHorizontalAlignment(SwingConstants.CENTER);

		panel_15.add(lblNewLabel_3, BorderLayout.NORTH);

		ligneLabel = new JLabel("1");
		ligneLabel.setForeground(new Color(255, 255, 255));
		ligneLabel.setFont(new Font("Kristen ITC", Font.BOLD, 12));
		ligneLabel.setBackground(couleur.get("bgSecondaire"));
		ligneLabel.setHorizontalAlignment(SwingConstants.CENTER);
		panel_15.add(ligneLabel, BorderLayout.SOUTH);

		JPanel panel_10_1 = new JPanel();
		panel_10_1.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_10_1.setBackground(couleur.get("bgSecondaire"));
		Bottom.add(panel_10_1);
		panel_10_1.setLayout(new BorderLayout(0, 0));

		btnMiseMoins = new JButton("");
		btnMiseMoins.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int miseVal = Integer.parseInt(miseLabel.getText());
				int nbLigne = Integer.parseInt(ligneLabel.getText());
				if (miseVal > 1) {
					miseLabel.setText(String.valueOf(miseVal - 1));
					miseTotalLabel.setText(String.valueOf((miseVal - 1) * nbLigne));
				}
			}
		});
		btnMiseMoins.setIcon(new ImageIcon(MachineSousApp.class.getResource("/media/minus30.png")));
		btnMiseMoins.setOpaque(false);
		btnMiseMoins.setContentAreaFilled(false);
		btnMiseMoins.setBorderPainted(false);
		btnMiseMoins.setBorder(null);
		panel_10_1.add(btnMiseMoins, BorderLayout.WEST);

		JPanel panel_15_1 = new JPanel();
		panel_15_1.setBackground(couleur.get("bgSecondaire"));
		panel_10_1.add(panel_15_1, BorderLayout.CENTER);
		panel_15_1.setLayout(new BorderLayout(0, 0));

		JLabel lblNewLabel_3_2 = new JLabel("Mise");
		lblNewLabel_3_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_3_2.setForeground(Color.WHITE);
		lblNewLabel_3_2.setFont(new Font("Kristen ITC", Font.BOLD, 10));
		lblNewLabel_3_2.setBackground(new Color(255, 51, 0));
		panel_15_1.add(lblNewLabel_3_2, BorderLayout.NORTH);

		miseLabel = new JLabel("1");
		miseLabel.setHorizontalAlignment(SwingConstants.CENTER);
		miseLabel.setForeground(Color.WHITE);
		miseLabel.setFont(new Font("Kristen ITC", Font.BOLD, 12));
		miseLabel.setBackground(couleur.get("bgSecondaire"));
		panel_15_1.add(miseLabel, BorderLayout.SOUTH);

		btnMisePlus = new JButton("");
		btnMisePlus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int miseVal = Integer.parseInt(miseLabel.getText());
				int nbLigne = Integer.parseInt(ligneLabel.getText());
				if (((miseVal + 1) * nbLigne) <= ms.getSolde()) {
					miseLabel.setText(String.valueOf(miseVal + 1));
					miseTotalLabel.setText(String.valueOf((miseVal + 1) * nbLigne));
				}
			}
		});
		btnMisePlus.setIcon(new ImageIcon(MachineSousApp.class.getResource("/media/plus30.png")));
		btnMisePlus.setOpaque(false);
		btnMisePlus.setContentAreaFilled(false);
		btnMisePlus.setBorderPainted(false);
		btnMisePlus.setBorder(null);
		panel_10_1.add(btnMisePlus, BorderLayout.EAST);

		JPanel panel_10_1_1 = new JPanel();
		panel_10_1_1.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_10_1_1.setBackground(couleur.get("bgSecondaire"));
		Bottom.add(panel_10_1_1);
		panel_10_1_1.setLayout(new BorderLayout(0, 0));

		JPanel panel_11 = new JPanel();
		panel_11.setBorder(new EmptyBorder(0, 5, 0, 5));
		panel_11.setBackground(couleur.get("bgSecondaire"));
		panel_10_1_1.add(panel_11, BorderLayout.NORTH);
		panel_11.setLayout(new BorderLayout(0, 0));

		JLabel lblNewLabel_3_2_1 = new JLabel("Mise total");
		lblNewLabel_3_2_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_3_2_1.setForeground(Color.WHITE);
		lblNewLabel_3_2_1.setFont(new Font("Kristen ITC", Font.BOLD, 10));
		lblNewLabel_3_2_1.setBackground(new Color(255, 51, 0));
		panel_11.add(lblNewLabel_3_2_1, BorderLayout.WEST);

		miseTotalLabel = new JLabel("1");
		miseTotalLabel.setHorizontalAlignment(SwingConstants.CENTER);
		miseTotalLabel.setForeground(Color.WHITE);
		miseTotalLabel.setFont(new Font("Kristen ITC", Font.BOLD, 12));
		miseTotalLabel.setBackground(couleur.get("bgSecondaire"));
		panel_11.add(miseTotalLabel, BorderLayout.EAST);

		JPanel panel_12 = new JPanel();
		panel_12.setBorder(new EmptyBorder(0, 5, 0, 5));
		panel_12.setBackground(couleur.get("bgSecondaire"));
		panel_10_1_1.add(panel_12, BorderLayout.SOUTH);
		panel_12.setLayout(new BorderLayout(0, 0));

		JLabel lblNewLabel_3_2_2 = new JLabel("Gain");
		lblNewLabel_3_2_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_3_2_2.setForeground(Color.WHITE);
		lblNewLabel_3_2_2.setFont(new Font("Kristen ITC", Font.BOLD, 10));
		lblNewLabel_3_2_2.setBackground(new Color(255, 51, 0));
		panel_12.add(lblNewLabel_3_2_2, BorderLayout.WEST);

		gainLabel = new JLabel("0");
		gainLabel.setHorizontalAlignment(SwingConstants.CENTER);
		gainLabel.setForeground(Color.WHITE);
		gainLabel.setFont(new Font("Kristen ITC", Font.BOLD, 12));
		gainLabel.setBackground(couleur.get("bgSecondaire"));
		panel_12.add(gainLabel, BorderLayout.EAST);

		btnMaxMise = new JButton("Max Mise");
		btnMaxMise.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int miseVal = Integer.parseInt(miseLabel.getText());
				int nbLigne = Integer.parseInt(ligneLabel.getText());
				miseVal = (int) ms.getSolde() / nbLigne;
				miseLabel.setText(String.valueOf(miseVal));
				miseTotalLabel.setText(String.valueOf(miseVal * nbLigne));
			}
		});
		btnMaxMise.setForeground(Color.WHITE);
		btnMaxMise.setFont(new Font("Kristen ITC", Font.BOLD, 20));
		btnMaxMise.setBackground(new Color(51, 153, 255));
		Bottom.add(btnMaxMise);

		btnStart = new JButton("START");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (ms.decrementSolde(Integer.parseInt(miseLabel.getText()), Integer.parseInt(ligneLabel.getText()))) {
					// la méthode decrementeSolde renvoie vrai si le solde a été réduit
					soldeLabel.setText(String.valueOf(ms.getSolde()));
					Thread threadGainPerte = new Thread(() -> {
						gainPerteLabel.setForeground(couleur.get("bgSecondaire"));
						gainPerteLabel.setText("- " + miseTotalLabel.getText());
						try {
							Thread.sleep(4000);
						} catch (Exception e1) {
						}
						gainPerteLabel.setText(null);
					});
					threadGainPerte.start();

					//Desactiver l'ensemble des bouttons avec la methode setButton avec le parametre action = false;
					setButton(new JButton[] { 
							btnStart, btnSoldePlus, btnRetour, 
							btnMaxMise, btnMisePlus, btnMiseMoins,
							btnLignePlus, btnLigneMoins }, false);
					
					ms.addMise(Integer.parseInt(miseTotalLabel.getText()));
					spinEnCours = true;
					demarrerPartie();
				} else
					JOptionPane.showMessageDialog(null, "Solde insuffisant pour effectuer cette mise.",
							"Solde Insuffisant!", 0, null);
			}
		});
		btnStart.setForeground(Color.WHITE);
		btnStart.setFont(new Font("Kristen ITC", Font.BOLD, 20));
		btnStart.setBackground(new Color(51, 153, 255));
		Bottom.add(btnStart);

	}

}
