package schach.system.internal;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import schach.brett.Farbe;
import schach.brett.Figurart;
import schach.brett.IAlleFiguren;
import schach.brett.IBrett;
import schach.brett.IFeld;
import schach.brett.IFigur;
import schach.brett.Linie;
import schach.brett.Reihe;
import schach.brett.internal.AlleFiguren;
import schach.brett.internal.Brett;
import schach.partie.IPartie;
import schach.partie.IPartiezustand;
import schach.partie.internal.Partie;
import schach.partie.internal.Partiezustand;
import schach.system.IController;
import schach.system.IView;
import schach.system.Logger;

public class GuiView implements IView {
	private static GuiView instance = null;  //  @jve:decl-index=0:
	public static GuiView getInstance() {
		if(instance == null)
			instance = new GuiView();
		
		return instance;
	}
	
	private GuiView() {
		Logger.debug("GuiView Konstruktor");
		allefiguren = AlleFiguren.getInstance();
		getJFrame().setVisible(true);
		getJFrame().setSize(500, 700);
	}

	private JFrame jFrame = null;

	private JPanel jContentPane = null;

	private JPanel aboutContentPane = null;

	private JLabel aboutVersionLabel = null;

	private JLabel feld1 = null;

	private JPanel jcpSchachbrett;

	private JPanel jcpController;

	private JTextField jInputField;

	private JButton jSendButton;


	private JLabel jLabelAktuellerSpieler;


	/**
	 * This method initializes jFrame
	 * 
	 * @return javax.swing.JFrame
	 */
	private JFrame getJFrame() {
		if (jFrame == null) {
			jFrame = new JFrame();
			jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			jFrame.setSize(300, 200);
			jFrame.setContentPane(getJContentPane());
			jFrame.setTitle("Die Schachpartie - Denn Schach ist einfach Pferd.");
		}
		return jFrame;
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			GridLayout gridLayout = new GridLayout();
			gridLayout.setRows(2);
			gridLayout.setColumns(1);
			jlBild = new JLabel();
			jlBild.setIcon(new ImageIcon("images/gaul.png",""));
			jlBild.setPreferredSize(new Dimension(81, 150));
			Aufforderung = new JLabel();
			Aufforderung.setText("<html>Bitte geben Sie Ihren gewünschten Zugweg in <br>" +
										"Kurznotation ein oder klicken mit der Maus die <br>" +
										"entsprechenden Felder an.");
			Aufforderung.setPreferredSize(new Dimension(300, 48));
			GridLayout gridLayout1 = new GridLayout(2, 1);
			gridLayout1.setHgap(7);
			gridLayout1.setVgap(7);
			jContentPane = new JPanel();
			jContentPane.setPreferredSize(new Dimension(1051, 447));
			jcpSchachbrett = new JPanel();
			jcpController = new JPanel();
			
			jcpSchachbrett.setLayout(new GridLayout(10,10));
			
			jLabelAktuellerSpieler = new JLabel("Partie wird gestartet..");
			jLabelAktuellerSpieler.setPreferredSize(new Dimension(300, 16));
			jContentPane.setLayout(gridLayout1);
			
			char[] abcdefgh = {' ','A','B','C','D','E','F','G','H',' '};
			boolean blank = false;
			for(char l : abcdefgh){
				JPanel panel = new JPanel();
				feld1 = new JLabel();
				feld1.setText(Character.toString(l));
				if((l == ' ' && !blank) || l == 'B' || l == 'D' || l == 'F' || l == 'H'){
					panel.setBackground(Color.LIGHT_GRAY);
					feld1.setForeground(Color.DARK_GRAY);
					if(l == ' ')
						blank = true;
				} 
				else {
					panel.setBackground(Color.DARK_GRAY);
					feld1.setForeground(Color.LIGHT_GRAY);
				}
				panel.add(feld1,null);
				jcpSchachbrett.add(panel, null);
			}
			for(int r=8; r>=1; r--){
				JPanel panel = new JPanel();
				feld1 = new JLabel();
				feld1.setText(Integer.toString(r));
				if(r % 2 == 0){
					panel.setBackground(Color.DARK_GRAY);
					feld1.setForeground(Color.LIGHT_GRAY);
				} 
				else {
					panel.setBackground(Color.LIGHT_GRAY);
					feld1.setForeground(Color.DARK_GRAY);
				}
				panel.add(feld1,null);
				jcpSchachbrett.add(panel, null);
				
				for(int l=1; l<=8; l++){
					panel = new JPanel();
					feld1 = new JLabel();
					besetzteFelder2.put(Brett.getInstance().gebeFeld(Reihe.values()[r-1],Linie.values()[l-1]), feld1);
					feld1.setText(""+abcdefgh[l]+r);
					if(r%2==1 && l%2==0 || r%2==0 && l%2==1){
						panel.setBackground(Color.WHITE);
						feld1.setForeground(Color.BLACK);
					} 
					else {
						panel.setBackground(Color.BLACK);
						feld1.setForeground(Color.WHITE);
					}
					panel.add(feld1,null);
					jcpSchachbrett.add(panel, null);
					
					panel.addMouseListener(newMouseAdapter(r,abcdefgh[l]));
				}
				
				panel = new JPanel();
				feld1 = new JLabel();
				feld1.setText(Integer.toString(r));
				if(r % 2 == 1){
					panel.setBackground(Color.DARK_GRAY);
					feld1.setForeground(Color.LIGHT_GRAY);
				} 
				else {
					panel.setBackground(Color.LIGHT_GRAY);
					feld1.setForeground(Color.DARK_GRAY);
				}
				panel.add(feld1,null);
				jcpSchachbrett.add(panel, null);
			}
			blank = false;
			for(char l : abcdefgh){
				JPanel panel = new JPanel();
				feld1 = new JLabel();
				feld1.setText(Character.toString(l));
				if((l == ' ' && blank) || l == 'A' || l == 'C' || l == 'E' || l == 'G'){
					panel.setBackground(Color.LIGHT_GRAY);
					feld1.setForeground(Color.DARK_GRAY);
				} 
				else {
					panel.setBackground(Color.DARK_GRAY);
					feld1.setForeground(Color.LIGHT_GRAY);
					if(l == ' ')
						blank = true;
				}
				panel.add(feld1,null);
				jcpSchachbrett.add(panel, null);
			}
			jContentPane.add(jcpSchachbrett);
			jSendButton = new JButton();
			jInputField = new JTextField();
			jSendButton.setText("Absenden");
			jSendButton.addActionListener(parseCommand);
			jInputField.setText("");
			jInputField.setPreferredSize(new Dimension(140, 22));
			jcpController.setLayout(gridLayout);
			jcpController.add(getJpBildtrenner(), null);
			jcpController.add(getJepSystemantwort(), null);
			jInputField.addActionListener(parseCommand);
			jContentPane.add(jcpController);
		}
		return jContentPane;
	}
	
	protected String klickfeld = "";  //  @jve:decl-index=0:
	private MouseListener newMouseAdapter(final int r, final char l) {
		return new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				System.out.println("klick auf "+l+r);
				if(klickfeld.length() == 0){
					klickfeld = ""+l+r;
				}
				else {
					klickfeld = klickfeld + l+r;
					IController controller = Controller.getInstance();
					
					if(!controller.parseInputString(klickfeld)) {
						jepSystemantwort.setText("Fehler: Koordinaten waren falsch.");
					}
					else {
						jepSystemantwort.setText(controller.getMessage());
					}
					klickfeld = "";
				}
				
			}
		};
	}


	private Map<IFeld,IFigur> besetzteFelder = new HashMap<IFeld,IFigur>();
	private Map<IFeld,JLabel> besetzteFelder2 = new HashMap<IFeld,JLabel>();
	private static IAlleFiguren allefiguren = null;
	private static List<Figurart> listeFigurarten = Arrays.asList(Figurart.values());
	private static List<Farbe> listeFarben = Arrays.asList(Farbe.values());

	public void update() {
		besetzteFelder.clear();
		IPartie partie = Partie.getInstance();
		IPartiezustand partiezustand = Partiezustand.getInstance();
		for(IFigur figur : allefiguren.gebeFiguren(listeFigurarten,listeFarben)){
			besetzteFelder.put(figur.gebePosition(),figur);
//			Logger.debug("View: "+figur+" steht auf "+figur.gebePosition());
		}
		
		if(!partiezustand.inPartie()){
			jLabelAktuellerSpieler.setText("<html><b>Partie läuft nicht.");
		}
		else {
			jLabelAktuellerSpieler.setText("<html><b>Aktueller Spieler: "+(partie.aktuellerSpieler().toString()));
		}
		
		
		// zeichne Brett
		IFeld feld;
		IFigur figur;
		IBrett brett = Brett.getInstance();
		for(Reihe r : Reihe.values()){
			for(Linie l : Linie.values()){
				besetzteFelder2.get(brett.gebeFeld(r,l)).setText("");
			}
		}
		
		Reihe[] reihen = {Reihe.R8,Reihe.R7,Reihe.R6,Reihe.R5,
				Reihe.R4,Reihe.R3,Reihe.R2,Reihe.R1};
		for(Reihe r : reihen){
			for(Linie l : Linie.values()){
				feld = brett.gebeFeld(r, l);
				StringBuilder sb = new StringBuilder();
				if(besetzteFelder.containsKey(feld)){
					figur = besetzteFelder.get(feld);
					
					switch(figur.gebeFarbe()){
					case SCHWARZ:
						sb.append('S');
						break;
					case WEISS:
					default:
						sb.append('W');
					}
					
					switch(figur.gebeArt()){
					case BAUER:
						sb.append('B');
						break;
					case DAME:
						sb.append('D');
						break;
					case KOENIG:
						sb.append('K');
						break;
					case LAEUFER:
						sb.append('L');
						break;
					case SPRINGER:
						sb.append('S');
						break;
					case TURM:
						sb.append('T');
						break;
					}
					
					besetzteFelder2.get(brett.gebeFeld(r,l)).setText(sb.toString());
				}
			}
		}
	}

	public void update(Observable o, Object arg) {
		update();
	}

	private ActionListener parseCommand = new java.awt.event.ActionListener() {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			IController controller = Controller.getInstance();  //  @jve:decl-index=0:
			
			if(!controller.parseInputString(jInputField.getText())) {
				jepSystemantwort.setText("Fehler: Koordinaten waren falsch.");
			}
			else {
				jepSystemantwort.setText(controller.getMessage());
			}
			
			jInputField.setText("");
			jInputField.setFocusable(true);
		}
	};

	private JLabel Aufforderung = null;

	private JLabel jlBild = null;

	private JPanel jpControllereinheiten = null;

	private JPanel jpBildtrenner = null;

	private JPanel jpEingabe = null;

	private JEditorPane jepSystemantwort = null;
	/**
	 * This method initializes jpControllereinheiten	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJpControllereinheiten() {
		if (jpControllereinheiten == null) {
			GridLayout gridLayout2 = new GridLayout();
			gridLayout2.setRows(3);
			gridLayout2.setHgap(2);
			gridLayout2.setVgap(2);
			gridLayout2.setColumns(1);
			jpControllereinheiten = new JPanel();
			jpControllereinheiten.setLayout(gridLayout2);
			jpControllereinheiten.setSize(new Dimension(300, 58));
			jpControllereinheiten.add(jLabelAktuellerSpieler, null);
			jpControllereinheiten.add(Aufforderung, null);
			jpControllereinheiten.add(getJpEingabe(), null);
		}
		return jpControllereinheiten;
	}

	/**
	 * This method initializes jpBildtrenner	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJpBildtrenner() {
		if (jpBildtrenner == null) {
			jpBildtrenner = new JPanel();
			jpBildtrenner.add(getJpControllereinheiten(), null);
			jpBildtrenner.add(jlBild, null);
		}
		return jpBildtrenner;
	}

	/**
	 * This method initializes jpEingabe	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJpEingabe() {
		if (jpEingabe == null) {
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints.weightx = 1.0;
			jpEingabe = new JPanel();
			jpEingabe.setLayout(new GridBagLayout());
			jpEingabe.add(jInputField, gridBagConstraints);
			jpEingabe.add(jSendButton, new GridBagConstraints());
		}
		return jpEingabe;
	}

	/**
	 * This method initializes jepSystemantwort	
	 * 	
	 * @return javax.swing.JEditorPane	
	 */
	private JEditorPane getJepSystemantwort() {
		if (jepSystemantwort == null) {
			jepSystemantwort = new JEditorPane();
		}
		return jepSystemantwort;
	}
}
