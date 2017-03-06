import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// Beachte: AutoSpiel erbt von JFrame
class UmweltplakettenSpiel extends JFrame {
	private static final long serialVersionUID = 1L;
	private static final int FENSTER_BREITE = 1024;
	private static final int FENSTER_HOEHE = 786;
	private static final int FRAMERATE = 25;
	private static final int Versuche = 15;
	

	private static int score;
	private static int falsche_plakette;
	private Zeichenflaeche zeichenflaeche;
	private Timer timer = new Timer();
	private List<Auto> elemente = Collections.synchronizedList(new ArrayList<Auto>());
	private List<Baum> Baume = Collections.synchronizedList(new ArrayList<Baum>());

	private int level;
	//private List<Baum> elemente_b = Collections.synchronizedList(new ArrayList<Baum>());

	public UmweltplakettenSpiel() {
		// Zeichenfl��che und Oberfl��che initialisieren, bezieht sich auf die Funktionalit��t von JFrame
		super("Umweltplaketten-Spiel");
		setBounds(50, 50, FENSTER_BREITE, FENSTER_HOEHE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container container = this.getContentPane();
		
		// Zeichenfl��che anlegen und dem Fenster hinzuf��gen
		zeichenflaeche = new Zeichenflaeche(FENSTER_BREITE, FENSTER_HOEHE);
		
		container.add(zeichenflaeche);
		//this.zeichne();
		// Zufallsprinzip erzeugt werden sollten)
		
		
		  TimerTask timerTask = new TimerTask() 
		     { 
		        private int min=3; //Zwischen 3 und 8 Autos pro Niveau
				private int randU;
				private int max=8;

				public void run()  
		         { 	
					
					level+=1; //Niveau zunehmen
		        	Random r = new Random();
		     		randU = r.nextInt(max - min + 1) + min;
		     		for(int j=0;j<randU;j++){
		     			neuesAuto();
		     		} 
		     		System.out.println(level);
		         } 
		     }; 
		     
		     Timer timer = new Timer(); 
		     //Jede 7 Sekunde werden Autos zufällig erzeugt
		     timer.scheduleAtFixedRate(timerTask, 0, 9000);
		// An den Container wird ein MouseListener angehangen.
		// Dieser registriert Mauseingaben und gibt die Informationen weiter 
		     //um die Plakette verändern zu können
		container.addMouseListener(new MouseAdapter() {
			@Override
			
			public void mousePressed(MouseEvent e) {
				KlickBeiKoord(e.getX(), e.getY());
				
			}
		});
		
	
		// darstellen
		setVisible(true);
		
		// B��ume bewegen sich nicht und k��nnen daher au��erhalb vom Thread erstellt werden
		erzeugeBaeume(4);
		// Ein timer-gesteuerter regelm����iger Thread zur Aktualisierung der Zeichenfl��che
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				loescheAutosRechts();
				aktualisiereSzene();
				zeichenflaeche.repaint();
			}
		}, (1000 / FRAMERATE), (1000 / FRAMERATE));
		// Spiel neustarten
		JButton buttonNeuesAuto = new JButton("Play again");
		getContentPane().add(buttonNeuesAuto, BorderLayout.SOUTH);
		buttonNeuesAuto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Um Bäume zu löschen
				for (Iterator<Baum> iter = Baume.iterator(); iter.hasNext(); ) {
				    Baum b = iter.next();
				    
				     iter.remove();
				     Baume.remove(b);
				     zeichenflaeche.remove1(b);
				     
				}
				//Um Autos zu löschen
				for (Iterator<Auto> iter = elemente.iterator(); iter.hasNext(); ) {
				    Auto a = iter.next();
				    
				     iter.remove();	
				     elemente.remove(a);
				     zeichenflaeche.remove(a);
				}
				//Parameter neustarten
				score=0;
				falsche_plakette=0;
				level=0;
				//setVisible(false);
				//new UmweltplakettenSpiel();
				
			}
			
		});
	}
	/*public void delete_list(Object type1, Object list1){
		for (Iterator<type1> iter = list1.Iterator(); iter.hasNext(); ) {
		    type a = iter.next();
		    
		     iter.remove();		    
		}
	}*/
	// Hauptdialog anlegen
	public static void main(String arg[]) {
		new UmweltplakettenSpiel();
	}
	// Methode zum Erzeugen eines neuen Autos (mit Zufallswerten)
	private void neuesAuto() {
		String name = "IL-SSE " + String.format("%03d", elemente.size() + 1);
		int posY = (int) (Math.random() * (FENSTER_HOEHE - 200));
		Random rand = new Random();
		int  n = rand.nextInt(50) + this.level;
		int factor=n*this.level;
		if(factor>100){
			factor=100;
			factor-=10;
		}
		int geschwindigkeit = (int) (30 + Math.random() * factor); // 30..130 km/h
		Auto neuesAuto = new Auto(name, posY, geschwindigkeit);
		synchronized (elemente) {
			elemente.add(neuesAuto);
			zeichenflaeche.add(neuesAuto);
		}
	}
	
	// L��sche Autos, die rechts den Bildschirmrand verlassen haben
	private void loescheAutosRechts() {
		synchronized (elemente) {
			Iterator<Auto> it = elemente.iterator();
			while (it.hasNext()) {
				Auto auto = it.next();
				Boolean test;
				if (auto.getPosX() > FENSTER_BREITE) { //Beim Verlassen des Rechten Rands
					//Überprüfung, ob das Fahrzeug die richtige Umweltplakette hat
					test=auto.hatKorrektePlaketten(); 
		  
					 //Passt die Plakette zur Emissionshöhe wird (für die anschließende Bewertung
					   //und Punktevergabe) ein true zurückgeliefert, anderenfalls ein false
				   
					 if(test){
						   this.setScore(this.getScore()+1);
					 }
						  
					 else{
						 this.setFalsche_plakette(this.getFalsche_plakette()+1);
					   }	
					 
					 // nach Überprüfung, muss man das Auto zerstören
					it.remove();
					elemente.remove(auto);
					zeichenflaeche.remove(auto);
					System.out.println("Auto gel��scht " + auto.getName());
					 
				}
			}
		}
	}
	
	private void erzeugeBaeume(int anzahl) {
		// M��glicher Startpunkt: Definieren Sie eine Klasse Baum
		// Orientieren Sie sich an der Klasse Auto (Image "baum.png")
		// Erzeugen Sie hier 'anzahl' entsprechend viele Baum-Objekte
		// und f��gen Sie sie der Zeichenfl��che hinzu
		
		//Beim Spielstart wird die Szene mit bis zu vier Bäumen gefüllt 
		for(int i=0;i<anzahl;i++){
		int posY = (int) (Math.random() * (FENSTER_HOEHE - 200));
		int posX = (int) (Math.random() * (FENSTER_BREITE - 200));
		Baum b1 = new Baum(posX,posY);
		//synchronized (this) {
			//elemente_b.add(b1);
			zeichenflaeche.add1(b1); //Um die Szene mit Bäume zu füllen
			Baume.add(b1);
		//}
		}
	}
	
	// Aktualisiere die Position aller Autos
	private void aktualisiereSzene() {
		if(falsche_plakette<=Versuche){ //Keine Aktualisierung, falls Versuche schon benutzt wurden
		synchronized (elemente) {	
			
				for (Auto auto : elemente) {
					auto.updatePosition(FRAMERATE);
				}
			}
		
		
		}
	}
	
	// Reaktion auf Mausklick, Fläche des Objekts finden, wo geklickt wurde
	//position of the mouse pointer
	//Test axes within range
	private void KlickBeiKoord(int klickX, int klickY) {
		//System.out.println("Mausklick bei: (" + klickX + "," + klickY + ")");
		if (!elemente.isEmpty()){
		Boolean test;
		int Width=elemente.get(0).getWidth();
		int Height=elemente.get(0).getHeight();
		synchronized (elemente) {
			Iterator<Auto> it = elemente.iterator();
			while (it.hasNext()) {
				Auto a = it.next();
				int minx = a.getPosX();
				int miny = a.getPosY();
				int maxx = minx + Width;
				int maxy = miny + Height;
				int mousex = klickX;
				int mousey = klickY;
				if ((mousex >= minx && mousex <= maxx) && (mousey >= miny && mousey <= maxy)) {
				   a.wechselnPlaketten(); //Falls ein Auto geklickt wurde, dann ändert sich die Farbe der Plakette
				}				
			}
		}
		
	}	
}

	public static int getScore() {
		return score;
	}

	public static void setScore(int score) {
		UmweltplakettenSpiel.score = score;
	}
	public static int getFalsche_plakette() {
		return falsche_plakette;
	}

	public static void setFalsche_plakette(int falsche_plakette) {
		UmweltplakettenSpiel.falsche_plakette = falsche_plakette;
	}
	public static int getVersuche() {
		return Versuche;
	}

}

