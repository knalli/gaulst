package schach.system.internal;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Event;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
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
import schach.partie.internal.Partie;
import schach.spieler.ISpieler;
import schach.system.IView;
import schach.system.Logger;
import schach.system.NegativeConditionException;

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

	private JMenuBar jJMenuBar = null;

	private JMenu fileMenu = null;

	private JMenu editMenu = null;

	private JMenu helpMenu = null;

	private JMenuItem exitMenuItem = null;

	private JMenuItem aboutMenuItem = null;

	private JMenuItem cutMenuItem = null;

	private JMenuItem copyMenuItem = null;

	private JMenuItem pasteMenuItem = null;

	private JMenuItem saveMenuItem = null;

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
			jFrame.setJMenuBar(getJJMenuBar());
			jFrame.setSize(300, 200);
			jFrame.setContentPane(getJContentPane());
			jFrame.setTitle("Application");
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
			jContentPane = new JPanel();
			jContentPane1 = new JPanel();
			jContentPane2 = new JPanel();
			jContentPane3 = new JPanel();
			jContentPane4 = new JPanel();
			
			jContentPane.setLayout(new GridLayout(2,1));
			jContentPane2.setLayout(new GridLayout(8,8));
			jContentPane3.setLayout(new GridLayout(1,2));
			jContentPane4.setLayout(new GridLayout(2,1));
			
			jLabelAktuellerSpieler = new JLabel("Partie wird gestartet..");
			jContentPane1.add(jLabelAktuellerSpieler);
			
			for(int r=1; r<=8; r++){
				for(int l=1; l<=8; l++){
					JPanel panel = new JPanel();
					feld1 = new JLabel();
					besetzteFelder2.put(Brett.getInstance().gebeFeld(Reihe.values()[r-1],Linie.values()[l-1]), feld1);
					feld1.setText("L"+l+"R"+r);
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
				}
			}
			jContentPane.add(jContentPane2);
			jSendButton = new JButton();
			jInputField = new JTextField();
			jSendButton.setText("Absenden");
			jSendButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
//					if(jInputField.getText().equals("b2b4")){
						IFeld feld = Brett.getInstance().gebeFeld(Reihe.R4, Linie.A);
						IFigur figur = Brett.getInstance().gebeFigurVonFeld(Reihe.R2, Linie.A);
						Logger.debug(figur.toString());
						try {
							figur.zieht(feld);
						} catch (NegativeConditionException e1) {
							Logger.info(e1.getMessage());
						}
						
						IFeld feld2 = Brett.getInstance().gebeFeld(Reihe.R3, Linie.A);
						IFigur figur2 = Brett.getInstance().gebeFigurVonFeld(Reihe.R1, Linie.A);
						Logger.debug(figur.toString());
						try {
							figur2.zieht(feld2);
						} catch (NegativeConditionException e1) {
							Logger.info(e1.getMessage());
						}
//					}
					jInputField.setText("");
				}
			});
			jInputField.setText("");
			jContentPane3.add(jInputField);
			jContentPane3.add(jContentPane4);
			jContentPane4.add(jLabelAktuellerSpieler);
			jContentPane4.add(jSendButton);
			jContentPane.add(jContentPane3);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jJMenuBar	
	 * 	
	 * @return javax.swing.JMenuBar	
	 */
	private JMenuBar getJJMenuBar() {
		if (jJMenuBar == null) {
			jJMenuBar = new JMenuBar();
			jJMenuBar.add(getFileMenu());
			jJMenuBar.add(getEditMenu());
			jJMenuBar.add(getHelpMenu());
		}
		return jJMenuBar;
	}

	/**
	 * This method initializes jMenu	
	 * 	
	 * @return javax.swing.JMenu	
	 */
	private JMenu getFileMenu() {
		if (fileMenu == null) {
			fileMenu = new JMenu();
			fileMenu.setText("File");
			fileMenu.add(getSaveMenuItem());
			fileMenu.add(getExitMenuItem());
		}
		return fileMenu;
	}

	/**
	 * This method initializes jMenu	
	 * 	
	 * @return javax.swing.JMenu	
	 */
	private JMenu getEditMenu() {
		if (editMenu == null) {
			editMenu = new JMenu();
			editMenu.setText("Edit");
			editMenu.add(getCutMenuItem());
			editMenu.add(getCopyMenuItem());
			editMenu.add(getPasteMenuItem());
		}
		return editMenu;
	}

	/**
	 * This method initializes jMenu	
	 * 	
	 * @return javax.swing.JMenu	
	 */
	private JMenu getHelpMenu() {
		if (helpMenu == null) {
			helpMenu = new JMenu();
			helpMenu.setText("Help");
			helpMenu.add(getAboutMenuItem());
		}
		return helpMenu;
	}

	/**
	 * This method initializes jMenuItem	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getExitMenuItem() {
		if (exitMenuItem == null) {
			exitMenuItem = new JMenuItem();
			exitMenuItem.setText("Exit");
			exitMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					System.exit(0);
				}
			});
		}
		return exitMenuItem;
	}

	/**
	 * This method initializes jMenuItem	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getAboutMenuItem() {
		if (aboutMenuItem == null) {
			aboutMenuItem = new JMenuItem();
			aboutMenuItem.setText("About");
			aboutMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JDialog aboutDialog = getAboutDialog();
					aboutDialog.pack();
					Point loc = getJFrame().getLocation();
					loc.translate(20, 20);
					aboutDialog.setLocation(loc);
					aboutDialog.setVisible(true);
				}
			});
		}
		return aboutMenuItem;
	}

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

	/**
	 * This method initializes jMenuItem	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getCutMenuItem() {
		if (cutMenuItem == null) {
			cutMenuItem = new JMenuItem();
			cutMenuItem.setText("Cut");
			cutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
					Event.CTRL_MASK, true));
		}
		return cutMenuItem;
	}

	/**
	 * This method initializes jMenuItem	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getCopyMenuItem() {
		if (copyMenuItem == null) {
			copyMenuItem = new JMenuItem();
			copyMenuItem.setText("Copy");
			copyMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,
					Event.CTRL_MASK, true));
		}
		return copyMenuItem;
	}

	/**
	 * This method initializes jMenuItem	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getPasteMenuItem() {
		if (pasteMenuItem == null) {
			pasteMenuItem = new JMenuItem();
			pasteMenuItem.setText("Paste");
			pasteMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V,
					Event.CTRL_MASK, true));
		}
		return pasteMenuItem;
	}

	/**
	 * This method initializes jMenuItem	
	 * 	
	 * @return javax.swing.JMenuItem	
	 */
	private JMenuItem getSaveMenuItem() {
		if (saveMenuItem == null) {
			saveMenuItem = new JMenuItem();
			saveMenuItem.setText("Save");
			saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
					Event.CTRL_MASK, true));
		}
		return saveMenuItem;
	}

	private Map<IFeld,IFigur> besetzteFelder = new HashMap<IFeld,IFigur>();
	private Map<IFeld,JLabel> besetzteFelder2 = new HashMap<IFeld,JLabel>();
	private static IAlleFiguren allefiguren = null;
	private static List<Figurart> listeFigurarten = Arrays.asList(Figurart.values());
	private static List<Farbe> listeFarben = Arrays.asList(Farbe.values());
	
	public void update() {
		besetzteFelder.clear();
		for(IFigur figur : allefiguren.gebeFiguren(listeFigurarten,listeFarben)){
			besetzteFelder.put(figur.gebePosition(),figur);
//			Logger.debug("View: "+figur+" steht auf "+figur.gebePosition());
		}
		
		ISpieler aktSpieler = Partie.getInstance().aktuellerSpieler();
		if(aktSpieler == null){
			jLabelAktuellerSpieler.setText("Partie läuft nicht.");
		}
		else {
			jLabelAktuellerSpieler.setText("Aktueller Spieler: "+aktSpieler.toString());
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

}
