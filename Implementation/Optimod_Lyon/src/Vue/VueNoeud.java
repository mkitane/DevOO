package Vue;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;

import Modele.Noeud;
/**
 * Vue Noeud
 * @author Mehdi Kitane
 *
 */
public class VueNoeud {

	protected Noeud noeudAssocie;
	protected int x; 
	protected int y; 
	protected int rayon; 
	public boolean selected = false;
	
	public VueNoeud(int x, int y, int rayon, Noeud noeudAssocie)
	{
		this.x = x; 
		this.y = y; 
		this.rayon = rayon;
		this.noeudAssocie = noeudAssocie;
	}
	/**
	 * Methode pour dessiner une vue
	 * @param g
	 */
	public void dessiner(Graphics g)
	{
		
		if(selected)
		{
			Graphics2D g2d = (Graphics2D) g.create();
			g2d.setColor(Color.BLACK);
			Stroke dashed = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{2}, 0);
	        g2d.setStroke(dashed);
	        g2d.drawOval(x-rayon-3, y-rayon-3, 2*rayon+6, 2*rayon+6);
	        g2d.dispose();
			g.setColor(Color.BLACK);
		}else
		{
			g.setColor(Color.BLACK);
		}
		g.fillOval(x-rayon, y-rayon, 2*rayon, 2*rayon);
		
	
	}
	
	
	
	public int getRayon() {
		return rayon;
	}
	
	/**
	 * Permet de savoir si on a clique sur cette vue
	 * @param xclick
	 * @param yclick
	 * @return boolean
	 */
	public boolean clickDessus(int xclick, int yclick)
	{
		return xclick>x-rayon && xclick<x+rayon && yclick>y-rayon && yclick<y+rayon; 
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public Noeud getNoeudAssocie() {
		return noeudAssocie;
	}
	
	
	
}
