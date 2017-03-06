import java.io.File;
import java.io.IOException;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import javax.imageio.ImageIO;

public class Baum {
	//Ein Baum kennt seine Position und kann zur Lebenszeit nicht modifiziert werden
	private int posX;
	private int posY;
	private Image img1 = null;
	public Baum(int posX, int posY){
		this.posX=posX;
		this.posY=posY;
		try {
			img1=ImageIO.read(new File("baum.png"));
		} catch (IOException e) {
			System.out.println(e.toString());
			System.exit(0);
		}
	}
	public int getPosX(){
		return this.posX;
	}
	public int getPosY(){
		return this.posY;
	}
	public void zeichne1(Graphics g) {
		g.drawImage(img1, this.posX, this.posY, null);
	}
}
