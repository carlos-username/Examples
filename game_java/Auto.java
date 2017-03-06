import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

public class Auto {
	private String name;
	//private Color farbe;
	private int posX;
	private int posY;
	private int geschwindigkeit;
	private Image img,img1,img2 = null;
	private int[] Emission={4,3,2}; //Die Emission wird durch Abgaswolken hinter dem Fahrzeug dargestellt und
	//hat drei Arten (schwach, mittel, stark)
	private int[] Plak={2,3,4}; //Die Umweltplakette existiert in drei Varianten (grün, gelb, rot)
	//private int randNum=2 + (int)(Math.random() * ((2 - 0) + 1));
	private List<Image> Plaketten = new ArrayList<Image>();
	private List<Image> Emission_a = new ArrayList<Image>();

	private Random randomGenerator,random2;
	private int index,index1;
	//private Random rand1 = new Random();
	//private int randNum = Plak[rand1.nextInt(Plak.length)];
	public Auto(String name, int posY, int geschw) {
		this.name = name;
		this.posX = -200;
		this.posY = posY;
		this.geschwindigkeit = geschw;
		//this.farbe = new Color((int) (Math.random() * 255.0), (int) (Math.random() * 255.0), (int) (Math.random() * 255.0));
		for(int i=0;i<Emission.length;i++){//Um das Array mit Bildern der Emissionen zu füllen
			try{
			Emission_a.add(ImageIO.read(new File("abgas"+Emission[i]+".png")));
			} catch (IOException e) {
				System.out.println(e.toString());
				System.exit(0);
			}	
		}
		for(int i=0;i<Plak.length;i++){ //Um das Array mit Bildern der Plaketten zu füllen
			try{
			Plaketten.add(ImageIO.read(new File("plakette"+Plak[i]+".png")));
			} catch (IOException e) {
				System.out.println(e.toString());
				System.exit(0);
			}	
		}
		try {
			img = ImageIO.read(new File("auto.png")); //Bild des Autos
			this.randomGenerator = new Random();
			this.index = randomGenerator.nextInt(Plaketten.size());
			Image it1 = Plaketten.get(this.index);//zufällige Plakette zum ersten mal
			img1 = it1;
			this.random2 = new Random();
			this.index1 = random2.nextInt(Emission_a.size());
			
			Image it2 = Emission_a.get(this.index1); //zufällige Emission zum ersten mal
			img2 = it2;
			
		} catch (IOException e) {
			System.out.println(e.toString());
			System.exit(0);
		}
	
		System.out.println("Auto erzeugt " + this.name);
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getPosX() {
		return this.posX;
	}
	
	public int getPosY() {
		return this.posY;
	}
	
	public int getHeight(){
	return this.img.getHeight(null);	
	}
	
	public int getWidth(){
	return this.img.getWidth(null);
	}//zufällige Emission zum ersten mal
	
	public int getGeschwindigkeit() {
		return this.geschwindigkeit;
	}
	
	public int getEmission(){
		return this.index1;
	}
	
	public void updatePosition(int framerate) {
		// Umrechnung von Geschwindigkeit in Bildschirm-Bewegung: 130km/h --> 400px/s
		int posXAenderung = (int) (1.0 / framerate * (400.0 * this.geschwindigkeit / 130.0));
		this.posX = this.posX + posXAenderung;
		//System.out.println("Auto " + this.name + " bei (" + this.posX + "," + this.posY + " Vel: "+ this.geschwindigkeit+")");
	}
	public void wechselnPlaketten(){ 
		this.index+=1;
		this.img1=Plaketten.get((this.index)%Plaketten.size());
	}
	
	public Boolean hatKorrektePlaketten(){ //überprüfung der Plakette
		
		if(this.index%Plaketten.size()==this.index1){ //Subindex ist gleich, dann passt die Plakette
			//{4,3,2} mit {2,3,4} 4->2,3->3,2->4
			return true;
		}
		
			return false;
		
		//System.out.println(this.index%Plaketten.size()+"  "+this.index1);
		
		
	}
	public void zeichne(Graphics g) {
		g.drawImage(img, this.posX, this.posY, null);
		g.drawImage(img1, this.posX+57, this.posY+42,60,60, null);
		if(this.index1>=3){
		g.drawImage(img2, this.posX-250, this.posY+70, null);
		}
		else{
		g.drawImage(img2, this.posX-50, this.posY+70, null);

		}
		
	}
}
