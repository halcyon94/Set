/**
 *	Set GUI Card class utilizing vector drawn traditional style designs
 *	@author Dolen Le
 *	@version 1.0
 */
package gui;
 
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;

public class Card extends JPanel {

	/**
	 *	Lists possible shape fill patterns.
	 */
	public enum Pattern {
		/**
		 *	Solid color fill
		 */
		SOLID,
		/**
		 *	Vertical stripes
		 */
		STRIPE,
		/**
		 *	White (empty) fill
		 */
		CLEAR
	}
	
	/**
	 *	Lists possible Card shapes.
	 */
	public enum Symbol {
		/**
		 *	Oblong diamond
		 */
		DIAMOND,
		/**
		 *	Linked Bezier curves in an S-shape
		 */
		TILDA,
		/**
		 *	Rectangle with round ends
		 */
		OVAL
	}
	
	private Pattern pat;
	private Symbol sym;
	private Color col;
	private int num;
	
	private static final double SHAPE_W_SCALE = 0.7;
	private static final double SHAPE_H_SCALE = 0.18;
	private static double MARGIN_SCALE = 0.06;
	private static final int MIN_MARGIN = 6;
	private static final float BORDER_WEIGHT = 5;
	private static final int STRIPE_WEIGHT = 4;
		
	/**
	 *	Creates a new Card
	 *	@param num Number of shapes to be shown on card
	 *	@param pat Fill pattern for the shape on the card. Must be one of Card.Pattern.
	 *	@param col Fill and border color for the shape on the card.
	 *	@param sym Shape to be shown on the card. Must be one of Card.Symbol.
	 */
	public Card(int num, Pattern pat, Color col, Symbol sym) {
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		setBackground(Color.WHITE);
		this.pat = pat;
		this.sym = sym;
		this.col = col;
		this.num = num;
	}
	
	/**
	 *	Gets minimum card size.
	 */
	public Dimension getMinimumSize() {
    	return new Dimension(35, 64);
	}
	
	public void paintComponent(Graphics gx) {
		super.paintComponent(gx);
		Graphics2D g = (Graphics2D) gx.create();
		int cardW = getWidth(); 
		int cardH = getHeight();
		double shapeW = cardW*SHAPE_W_SCALE;
		double shapeH = cardH*SHAPE_H_SCALE;
		double margin = cardH*MARGIN_SCALE > MIN_MARGIN ? cardH*MARGIN_SCALE : MIN_MARGIN; 
		double nFactor = 0.5*(num-1)*(shapeH+margin);
		
		if(sym == Symbol.OVAL) { //rounded rectangle
			for(int i = 1; i <= num; i++) {
				RoundRectangle2D s = new RoundRectangle2D.Double(	
					cardW/2-shapeW/2,
					cardH/2-shapeH/2-nFactor+(shapeH+margin)*(i-1),
					shapeW,
					shapeH,
					shapeH,
					shapeH); //dynamically size rectangle
				
				if(pat == Pattern.SOLID) {
					g.setPaint(col);
				} else if(pat == Pattern.CLEAR) {
					g.setPaint(Color.WHITE);
				} else if(pat == Pattern.STRIPE) {
					g.setPaint(getStripeFill(col));
				}
				g.fill(s);
				g.setPaint(col.darker());
				g.setStroke(new BasicStroke(BORDER_WEIGHT));
				g.draw(s);
			}
		} else if(sym == Symbol.DIAMOND) {
			for(int i = 1; i <= num; i++) {
				double translation = nFactor-(shapeH+margin)*(i-1);
				GeneralPath s = new GeneralPath(); //Custom diamond shape
				s.moveTo(cardW/2, (cardH-shapeH)/2-translation);
				s.lineTo((cardW-shapeW)/2, cardH/2-translation);
				s.lineTo(cardW/2, shapeH+(cardH-shapeH)/2-translation);
				s.lineTo(shapeW+(cardW-shapeW)/2, cardH/2-translation);
				s.closePath();
			
				if(pat == Pattern.SOLID) {
					g.setPaint(col);
				} else if(pat == Pattern.CLEAR) {
					g.setPaint(Color.WHITE);
				} else if(pat == Pattern.STRIPE) {
					g.setPaint(getStripeFill(col));
				}
				g.fill(s);
				g.setPaint(col.darker());
				g.setStroke(new BasicStroke(BORDER_WEIGHT));
				g.draw(s);
			}
		} else if(sym == Symbol.TILDA) {
			shapeW-=20;
			shapeH+=20;
			for(int i = 1; i <= num; i++) {
				double translation = nFactor-(shapeH+margin-20)*(i-1);
				GeneralPath s = new GeneralPath(); //Custom S-shape
				s.moveTo((cardW-shapeW)/2, (cardH-shapeH)/2-translation); //start left corner
				s.curveTo(s.getCurrentPoint().getX()-30, s.getCurrentPoint().getY()+80, cardW-(cardW-shapeW)/2+10, cardH-(cardH-shapeH)/2-40-translation, cardW-(cardW-shapeW)/2, cardH-(cardH-shapeH)/2-translation);
				s.curveTo(s.getCurrentPoint().getX()+30, s.getCurrentPoint().getY()-80, (cardW-shapeW)/2-10, (cardH-shapeH)/2+40-translation, (cardW-shapeW)/2, (cardH-shapeH)/2-translation);
				s.closePath();
			
				if(pat == Pattern.SOLID) {
					g.setPaint(col);
				} else if(pat == Pattern.CLEAR) {
					g.setPaint(Color.WHITE);
				} else if(pat == Pattern.STRIPE) {
					g.setPaint(getStripeFill(col));
				}
				g.fill(s);
				g.setPaint(col.darker());
				g.setStroke(new BasicStroke(BORDER_WEIGHT, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
				g.draw(s);
			}
		}
		g.dispose();
	}
	
	private TexturePaint getStripeFill(Color col) {
		BufferedImage bufferedImage = new BufferedImage(STRIPE_WEIGHT, STRIPE_WEIGHT, BufferedImage.TYPE_INT_RGB);
		Graphics2D temp = bufferedImage.createGraphics();
		temp.setColor(Color.WHITE);
		temp.fillRect(0, 0, STRIPE_WEIGHT, STRIPE_WEIGHT); //white bg
		temp.setColor(col);
		temp.drawLine(0, 0, 0, STRIPE_WEIGHT); //vertical lines
		Rectangle2D rect = new Rectangle2D.Double(0, 0, STRIPE_WEIGHT, STRIPE_WEIGHT); //pattern to be tiled
		return new TexturePaint(bufferedImage, rect);
	}
}