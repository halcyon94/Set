package gui;
 
import java.lang.Math;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;

/**
 *	Set GUI Card class utilizing vector drawn traditional style designs
 *	@author Dolen Le
 *	@version 1.0
 */
public class Card extends JPanel implements MouseListener {

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
		CLEAR,
		/**
		 *	Circle pattern
		 */
		CIRCLE
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
		OVAL,
		/**
		 *	Rectangle with square ends
		 */
		RECTANGLE
	}
	
	/**
	 *	Lists default colors for Card shapes
	 */
	public enum SetColor {
		RED(240,30,30), GREEN(30,190,30), PURPLE(150,85,230), BLUE(135, 175, 235);
		private final int red, green, blue;
		SetColor(int r, int g, int b) {
			red = r;
			green = g;
			blue = b;
		}
		public Color toColor() {
			return new Color(red, green, blue);
		}
	}
	
	private Symbol sym;
	private Pattern pat;
	private SetColor sCol;
	private int num;
	
	private Color col; //shape color
	private Paint pnt; //shape fill
	private Color bgColor = Color.WHITE;
	private BasicStroke cardBorder = new BasicStroke();
	private Color borderColor = Color.BLACK;
	private boolean selected = false;
	
	//shape size to card size proportions
	private static final double SHAPE_W_SCALE = 0.7;
	private static final double SHAPE_H_SCALE = 0.18;
	private static final double MARGIN_SCALE = 0.06;
	private static final double CORNER_SCALE = 0.06;
	private static final double INNER_W_SCALE = 0.2;
	private static final double INNER_H_SCALE = 0.4;
	private static final double OUTER_W_SCALE = 0.08;
	private static final double OUTER_H_SCALE = 0.2;
	
	private static final int MIN_MARGIN = 6;
	private static final float BORDER_WEIGHT = 5;
	private static final int STRIPE_WEIGHT = 4;
	
	/**
	 *	Creates a new Card with custom defined color
	 *	@param num Number of shapes to be shown on card
	 *	@param pat Fill pattern for the shape on the card. Must be one of Card.Pattern.
	 *	@param col Color object representing the fill and border color of the shape.
	 *	@param sym Shape to be shown on the card. Must be one of Card.Symbol.
	 */
	public Card(int num, Pattern pat, Color col, Symbol sym) {
		//setBorder(BorderFactory.createLineBorder(Color.BLACK));
		//setBackground(Color.WHITE);
		this.col = col;
		this.pat = pat;
		switch(pat) {
			case SOLID:
				this.pnt = this.col;
				break;
			case STRIPE:
				this.pnt = getStripeFill(this.col);
				break;
			case CLEAR:
				this.pnt = bgColor;
				break;
			case CIRCLE:
				this.pnt = getCircleFill(this.col);
				break;
		}
		this.sym = sym;
		this.num = num;
		addMouseListener(this);
	}

	/**
	 *	Creates a new Card using a preset color
	 *	@param num Number of shapes to be shown on card
	 *	@param pat Fill pattern for the shape on the card. Must be one of Card.Pattern.
	 *	@param col Fill and border color for the shape on the card. Must be one of Card.SetColor.
	 *	@param sym Shape to be shown on the card. Must be one of Card.Symbol.
	 */
	public Card(int num, Pattern pat, SetColor col, Symbol sym) {
		this(num, pat, col.toColor(), sym);
		this.sCol = col;
	}
	
	/**
	 *	Creates a new Card from a unique ID using presets. The total number of cards is equal to setSize^4.
	 *	The ID is indexed from 0 to the total number of cards. The setSize determines the available
	 *	colors, shapes, patterns and numbers for the card.
	 *	@param id ID of the card. Must be less than setSize^4.
	 *	@param setSize Number of cards per set, in range 2-4.
	 */
	public Card(int id, int setSize) {
		//Sort by color, then pattern, then shape, then number
		Pattern[] patA = Pattern.values();
		Symbol[] symA = Symbol.values();
		SetColor[] colA = SetColor.values();
		int total = (int) Math.pow(setSize, 4);
		int cardsPerColor = total/setSize;
		int cardsPerPattern = cardsPerColor/setSize;
		int cardsPerShape = cardsPerPattern/setSize;
				
		this.sCol = colA[(id/cardsPerColor)%setSize];
		this.col = sCol.toColor();
		this.pat = patA[(id/cardsPerPattern)%setSize];
		switch(pat) {
			case SOLID:
				this.pnt = this.col;
				break;
			case STRIPE:
				this.pnt = getStripeFill(this.col);
				break;
			case CLEAR:
				this.pnt = bgColor;
				break;
			case CIRCLE:
				this.pnt = getCircleFill(this.col);
				break;
		}
		this.sym = symA[(id/cardsPerShape)%setSize];
		this.num = id%setSize+1;
		addMouseListener(this);
	}
	
	/**
	 *	Compute the ID of the Card. The number of cards in the deck is setSize^4.
	 *	@param setSize The number of choices per attribute and cards per set.
	 *	@return The card's unique ID, or -1 if the ID exceeds the card total.
	 */
	public int getID(int setSize) {
		int id =	sCol.ordinal()*((int) Math.pow(setSize, 3))
					+pat.ordinal()*((int) Math.pow(setSize, 2))
					+sym.ordinal()*setSize
					+num-1;
		if(id < Math.pow(setSize, 4))
			return id;
		else
			return -1;
	}
	
	/**
	 *	Marks or unmarks the Card as selected.
	 *	@param highlight Border highlight color for selected card
	 */
	public void toggleSelection(Color highlight) {
		if(!selected) {
			cardBorder = new BasicStroke(5.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
			borderColor = highlight;
		} else {
			cardBorder = new BasicStroke();
			borderColor = Color.BLACK;
		}
		selected = !selected;
		repaint();
	}
	
	/**
	 *	Gets minimum card size.
	 */
	@Override
	public Dimension getMinimumSize() {
    	return new Dimension(35, 64);
	}
	
	/**
	 *	Method used by the layout manager to draw the Card graphics
	 */
	@Override
	public void paintComponent(Graphics gx) {
		super.paintComponent(gx);
		Graphics2D g = (Graphics2D) gx.create();
		int cardW = getWidth()-1; 
		int cardH = getHeight()-1;
		double shapeW = cardW*SHAPE_W_SCALE;
		double shapeH = cardH*SHAPE_H_SCALE;
		double margin = cardH*MARGIN_SCALE > MIN_MARGIN ? cardH*MARGIN_SCALE : MIN_MARGIN; 
		double nFactor = 0.5*(num-1)*(shapeH+margin);
		RoundRectangle2D.Double cardShape = new RoundRectangle2D.Double(0,0,cardW,cardH,cardW*CORNER_SCALE,cardW*CORNER_SCALE);
		g.setPaint(bgColor);
		g.fill(cardShape);
		g.setPaint(borderColor);
		g.setStroke(cardBorder);
		g.draw(cardShape);
		//System.out.println("Card Dimensions x="+cardW+" y="+cardH);
		
		for(int i = 1; i <= num; i++) {
			Shape s = null;
			if(sym == Symbol.OVAL) { //rounded rectangle
				s = new RoundRectangle2D.Double(	
					cardW/2-shapeW/2,
					cardH/2-shapeH/2-nFactor+(shapeH+margin)*(i-1),
					shapeW,
					shapeH,
					shapeH,
					shapeH); //dynamically size rectangle
			} else if(sym == Symbol.DIAMOND) {
				double translation = nFactor-(shapeH+margin)*(i-1);
				GeneralPath p = new GeneralPath(); //Custom diamond shape
				p.moveTo(cardW/2, (cardH-shapeH)/2-translation);
				p.lineTo((cardW-shapeW)/2, cardH/2-translation);
				p.lineTo(cardW/2, shapeH+(cardH-shapeH)/2-translation);
				p.lineTo(shapeW+(cardW-shapeW)/2, cardH/2-translation);
				p.closePath();
				s = p;
			} else if(sym == Symbol.TILDA) {
				double translation = (0.5*(num-1)-(i-1))*(shapeH+margin);
				GeneralPath p = new GeneralPath(); //Custom S-shape
				p.moveTo((cardW-shapeW)/2, (cardH-shapeH)/2-translation); //start left corner
				p.curveTo(p.getCurrentPoint().getX()-cardW*INNER_W_SCALE, p.getCurrentPoint().getY()+cardH*INNER_H_SCALE, cardW-(cardW-shapeW)/2+cardW*OUTER_W_SCALE, cardH-(cardH-shapeH)/2-cardH*OUTER_H_SCALE-translation, cardW-(cardW-shapeW)/2, cardH-(cardH-shapeH)/2-translation);
				p.curveTo(p.getCurrentPoint().getX()+cardW*INNER_W_SCALE, p.getCurrentPoint().getY()-cardH*INNER_H_SCALE, (cardW-shapeW)/2-cardW*OUTER_W_SCALE, (cardH-shapeH)/2+cardH*OUTER_H_SCALE-translation, (cardW-shapeW)/2, (cardH-shapeH)/2-translation);
				p.closePath();
				s = p;
			} else if(sym == Symbol.RECTANGLE) {
				s = new Rectangle2D.Double(	
					cardW/2-shapeW/2,
					cardH/2-shapeH/2-nFactor+(shapeH+margin)*(i-1),
					shapeW,
					shapeH); //dynamically size rectangle
			}
			g.setPaint(pnt);
			g.fill(s);
			g.setPaint(col.darker());
			g.setStroke(new BasicStroke(BORDER_WEIGHT, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
			g.draw(s);
		}
		g.dispose();
	}
	
	public void mouseClicked(MouseEvent e) {} //handle elsewhere
    
    public void mousePressed(MouseEvent e) {
    	bgColor = Color.DARK_GRAY;
        repaint();
    } //dont care

    public void mouseReleased(MouseEvent e) {
    	bgColor = Color.LIGHT_GRAY;
        repaint();
    } //dont care

    public void mouseEntered(MouseEvent e) { //focus
         bgColor = Color.LIGHT_GRAY;
         repaint();
    }

    public void mouseExited(MouseEvent e) { //lose focus
         bgColor = Color.WHITE;
         repaint();
    }
	
	//Generates the striped fill pattern
	private TexturePaint getStripeFill(Color col) {
		BufferedImage bufferedImage = new BufferedImage(STRIPE_WEIGHT, STRIPE_WEIGHT, BufferedImage.TYPE_INT_RGB);
		Graphics2D temp = bufferedImage.createGraphics();
		temp.setColor(Color.WHITE);
		temp.fillRect(0, 0, STRIPE_WEIGHT, STRIPE_WEIGHT); //white bg
		temp.setColor(col);
		temp.drawLine(0, 0, 0, STRIPE_WEIGHT); //vertical lines
		return new TexturePaint(bufferedImage, new Rectangle2D.Double(0, 0, STRIPE_WEIGHT, STRIPE_WEIGHT));
	}
	
	//Generates the circle fill pattern
	private TexturePaint getCircleFill(Color col) {
		int radius=18;
		BufferedImage image = new BufferedImage(radius*2, radius*2, BufferedImage.TYPE_INT_RGB);
		Graphics2D temp = image.createGraphics();
		temp.setColor(Color.WHITE);
		temp.fillRect(0, 0, radius*2, radius*2);
		for(int i = 0; i<= radius; i+=3) {
			temp.setColor(col);
			temp.drawOval(radius-i, radius-i, i*2, i*2); 
		}
		Rectangle2D rect = new Rectangle2D.Double(0, 0, (int) (radius*1.41), (int) (radius*1.41)); //pattern to be tiled
		return new TexturePaint(image.getSubimage((int) (radius*0.4142), (int) (radius*0.4142), (int) (radius*1.41), (int) (radius*1.41)), rect);
	}
	
}