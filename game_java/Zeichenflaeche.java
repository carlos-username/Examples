import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

public class Zeichenflaeche extends JPanel {
	private static final long serialVersionUID = 1L;

	private List<Auto> elemente = new ArrayList<Auto>(); //Arraylist für Autos die in der Szene gezeigt werden werden
	private List<Baum> elemente_b = new ArrayList<Baum>();//Arraylist für Bäume die in der Szene gezeigt werden werden
	
	private Graphics bufferImageGraphics;
	private Image bufferImage;
	private int breite;
	private int hoehe;

	public Zeichenflaeche(int breite, int hoehe) { 
		this.breite = breite;
		this.hoehe = hoehe;
	}

	public void add(Auto element) { 
		this.elemente.add(element);
	}
	public void add1(Baum b1) { // Um die Szene mit Bäumen füllen zu können
		this.elemente_b.add(b1);
	}
	public void remove1(Baum b1) {
		this.elemente_b.remove(b1);
	}

	public void remove(Auto element) {
		this.elemente.remove(element);
	}
	public void Daten(Graphics g) { //Um Punkte und Zahl der ausgewählten falschen Plaketten
		g.setColor(Color.ORANGE); 
		g.setFont(new Font("Dialog", Font.ROMAN_BASELINE, 30));
		g.drawString("Score-> "+ UmweltplakettenSpiel.getScore(), 400,50); //Punkte, getScore ist static
		g.drawString("Falsche Plakette-> "+ UmweltplakettenSpiel.getFalsche_plakette(), 650,50);
		//g.drawString("Niveau-> "+ UmweltplakettenSpiel.getLevel(), 100,50);
	}
	public void gameOver(Graphics g){ //Um den Text Game Over zu erzeugen
		g.setColor(Color.BLACK);
		g.setFont(new Font("Dialog", Font.ROMAN_BASELINE, 100));	
		g.drawString("GAME OVER" ,100,400);
	}

	@Override
	public void paintComponent(Graphics g) {
		if (bufferImage == null) {
			bufferImage = this.createImage(this.breite, this.hoehe);
			bufferImageGraphics = bufferImage.getGraphics();
		}
		bufferImageGraphics.clearRect(0, 0, breite, hoehe);
		for (Auto element : elemente) {  //der Szene Autos hinzufügen
			element.zeichne(bufferImageGraphics);
		}
		for(Baum elem_b: elemente_b){
			elem_b.zeichne1(bufferImageGraphics);//der Szene Bäume hinzufügen
		}
		Daten(bufferImageGraphics); //
		if(UmweltplakettenSpiel.getFalsche_plakette()>=UmweltplakettenSpiel.getVersuche()){ 
		//Falls der Nutzer schon bestimmte falsche Plaketten ausgewählt hat, zeigt GAME OVER
		gameOver(bufferImageGraphics);
		}
		g.drawImage(bufferImage, 0, 0, this);
	}
	
	public void zeichne(Graphics g) {
		paint(g);
	}


}