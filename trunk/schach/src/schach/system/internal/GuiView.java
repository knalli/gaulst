package schach.system.internal;

import java.awt.BorderLayout;
import java.awt.Color;
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

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

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

	private JDialog aboutDialog = null;

	private JPanel aboutContentPane = null;

	private JLabel aboutVersionLabel = null;

	private JLabel feld1 = null;

	private JPanel jContentPane2;

	private JPanel jContentPane3;

	private JTextField jInputField;

	private JButton jSendButton;

	private JPanel jContentPane1;

	private JLabel jLabelAktuellerSpieler;

	private JPanel jContentPane4;

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
			GridLayout gridLayout1 = new GridLayout(2, 1);
			gridLayout1.setHgap(7);
			gridLayout1.setVgap(7);
			GridLayout gridLayout = new GridLayout(1, 2);
			gridLayout.setRows(2);
			gridLayout.setVgap(5);
			gridLayout.setHgap(5);
			jLabel = new JLabel();
			jLabel.setText("Status: Die Partie ist gestartet.");
			jContentPane = new JPanel();
			jContentPane1 = new JPanel();
			jContentPane2 = new JPanel();
			jContentPane3 = new JPanel();
			jContentPane4 = new JPanel();
			
			jContentPane2.setLayout(new GridLayout(10,10));
			jContentPane4.setLayout(new GridLayout(3,1));
			
			jLabelAktuellerSpieler = new JLabel("Partie wird gestartet..");
			jContentPane1.add(jLabelAktuellerSpieler);
			jContentPane.setLayout(gridLayout1);
			
			char[] abcdefgh = {' ','A','B','C','D','E','F','G','H',' '};
			boolean blank = false;
			for(char l : abcdefgh){
				JPanel panel = new JPanel();
				feld1 = new JLabel();
				feld1.setText(Character.toString(l));
				if((l == ' ' && !blank) || l == 'B' || l == 'D' || l == 'F' || l == 'H'){
					panel.setBackground(Color.DARK_GRAY);
					feld1.setForeground(Color.LIGHT_GRAY);
					if(l == ' ')
						blank = true;
				} 
				else {
					panel.setBackground(Color.LIGHT_GRAY);
					feld1.setForeground(Color.DARK_GRAY);

				}
				panel.add(feld1,null);
				jContentPane2.add(panel, null);
			}
			for(int r=8; r>=1; r--){
				JPanel panel = new JPanel();
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
				jContentPane2.add(panel, null);
				
				for(int l=1; l<=8; l++){
					panel = new JPanel();
					feld1 = new JLabel();
					besetzteFelder2.put(Brett.getInstance().gebeFeld(Reihe.values()[r-1],Linie.values()[l-1]), feld1);
					feld1.setText(""+abcdefgh[l]+r);
					if(r%2==1 && l%2==0 || r%2==0 && l%2==1){
						panel.setBackground(Color.BLACK);
						feld1.setForeground(Color.WHITE);
					} 
					else {
						panel.setBackground(Color.WHITE);
						feld1.setForeground(Color.BLACK);
					}
					panel.add(feld1,null);
					jContentPane2.add(panel, null);
					
					panel.addMouseListener(newMouseAdapter(r,abcdefgh[l]));
				}
				
				panel = new JPanel();
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
				jContentPane2.add(panel, null);
			}
			blank = false;
			for(char l : abcdefgh){
				JPanel panel = new JPanel();
				feld1 = new JLabel();
				feld1.setText(Character.toString(l));
				if((l == ' ' && blank) || l == 'A' || l == 'C' || l == 'E' || l == 'G'){
					panel.setBackground(Color.DARK_GRAY);
					feld1.setForeground(Color.LIGHT_GRAY);
				} 
				else {
					panel.setBackground(Color.LIGHT_GRAY);
					feld1.setForeground(Color.DARK_GRAY);
					if(l == ' ')
						blank = true;
				}
				panel.add(feld1,null);
				jContentPane2.add(panel, null);
			}
			jContentPane.add(jContentPane2);
			jSendButton = new JButton();
			jInputField = new JTextField();
			jSendButton.setText("Absenden");
			jSendButton.addActionListener(parseCommand);
			jInputField.setText("");
			jInputField.addActionListener(parseCommand);
			jContentPane3.setLayout(gridLayout);
			jContentPane3.add(jInputField);
			jContentPane3.add(jContentPane4);
			jContentPane4.add(jLabelAktuellerSpieler);
			jContentPane4.add(jSendButton);
			jContentPane4.add(jLabel, null);
			jContentPane.add(jContentPane3);
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
						jLabel.setText("Koordinaten waren falsch.");
					}
					else {
						jLabel.setText(controller.getMessage());
					}
					klickfeld = "";
				}
				
			}
		};
	}

	private int debugcount = 0;

	/**
	 * This method initializes aboutDialog	
	 * 	
	 * @return javax.swing.JDialog
	 */
	private JDialog getAboutDialog() {
		if (aboutDialog == null) {
			aboutDialog = new JDialog(getJFrame(), true);
			aboutDialog.setTitle("About");
			aboutDialog.setContentPane(getAboutContentPane());
		}
		return aboutDialog;
	}

	/**
	 * This method initializes aboutContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getAboutContentPane() {
		if (aboutContentPane == null) {
			aboutContentPane = new JPanel();
			aboutContentPane.setLayout(new BorderLayout());
			aboutContentPane.add(getAboutVersionLabel(), BorderLayout.CENTER);
		}
		return aboutContentPane;
	}

	/**
	 * This method initializes aboutVersionLabel	
	 * 	
	 * @return javax.swing.JLabel	
	 */
	private JLabel getAboutVersionLabel() {
		if (aboutVersionLabel == null) {
			aboutVersionLabel = new JLabel();
			aboutVersionLabel.setText("Version 1.0");
			aboutVersionLabel.setHorizontalAlignment(SwingConstants.CENTER);
		}
		return aboutVersionLabel;
	}

	private Map<IFeld,IFigur> besetzteFelder = new HashMap<IFeld,IFigur>();
	private Map<IFeld,JLabel> besetzteFelder2 = new HashMap<IFeld,JLabel>();
	private static IAlleFiguren allefiguren = null;
	private static List<Figurart> listeFigurarten = Arrays.asList(Figurart.values());
	private static List<Farbe> listeFarben = Arrays.asList(Farbe.values());

	private JLabel jLabel = null;
	
	public void update() {
		besetzteFelder.clear();
		IPartie partie = Partie.getInstance();
		IPartiezustand partiezustand = Partiezustand.getInstance();
		for(IFigur figur : allefiguren.gebeFiguren(listeFigurarten,listeFarben)){
			besetzteFelder.put(figur.gebePosition(),figur);
//			Logger.debug("View: "+figur+" steht auf "+figur.gebePosition());
		}
		
		if(!partiezustand.inPartie()){
			jLabelAktuellerSpieler.setText("Partie läuft nicht.");
		}
		else {
			jLabelAktuellerSpieler.setText("Aktueller Spieler: "+(partie.aktuellerSpieler().toString()));
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
				jLabel.setText("Koordinaten waren falsch.");
			}
			else {
				jLabel.setText(controller.getMessage());
			}
			
			jInputField.setText("");
			jInputField.setFocusable(true);
		}
	};
}
