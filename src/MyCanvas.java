package RobotProjectGUI;

import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Rotate;

/**
 * @author shsmchlr
 *  Class to handle a canvas, used by different GUIs
 */
public class MyCanvas {
	int xCanvasSize = 512;				// constants for relevant sizes, default values set
	int yCanvasSize = 512;
	Line side1, side2, side3, side4; // border of arena
    GraphicsContext gc; 

    /**
     * constructor sets up relevant Graphics context and size of canvas
     * @param g
     * @param cs
     */
    public MyCanvas(GraphicsContext g, int xcs, int ycs) {
    	gc = g;
    	xCanvasSize = xcs;
    	yCanvasSize = ycs;
    	side1 = new Line(0,0,xcs,0); // top left to top right
    	side2 = new Line(0,0,0,ycs); // top left to bottom left
    	side3 = new Line(0,ycs,xcs,ycs); // bottom left to bottom right
    	side4 = new Line(xcs,0,xcs,ycs); // top right to bottom right
    }
    
    public int getXCanvasSize() {
    	return xCanvasSize;
    }
    public int getYCanvasSize() {
    	return yCanvasSize;
    }
    /**
     * clear the canvas
     */
    public void clearCanvas() {
		gc.clearRect(0,  0,  xCanvasSize,  yCanvasSize);		// clear canvas
    }
	/**
     * drawImage ... draws object defined by given image at position and size
     * @param i		image
     * @param x		xposition	in range 0..1
     * @param y
     * @param sz	size
     */
	public void drawImage (Image i, double x, double y, double sz) {
			// to draw centred at x,y, give top left position and x,y size
			// sizes/position in range 0.. canvassize 
		gc.drawImage(i, x - sz/2, y - sz/2, sz, sz);
	}

	
	/** 
	 * function to convert char c to actual colour used
	 * @param c
	 * @return Color
	 */
	Color colFromChar (char c){
		Color ans = Color.BLACK;
		switch (c) {
		case 'y' :	ans = Color.YELLOW;
					break;
		case 'w' :	ans = Color.WHITE;
					break;
		case 'r' :	ans = Color.RED;
					break;
		case 'g' :	ans = Color.GREEN;
					break;
		case 'b' :	ans = Color.BLUE;
					break;
		case 'o' :	ans = Color.ORANGE;
					break;
		case 'x' :  ans = Color.BLACK;
					break;
		}
		return ans;
	}
	
	/**
	 * set the fill colour to color c
	 * @param c
	 */
	public void setFillColour (Color c) {
		gc.setFill(c);
	}
	/**
	 * show the ball at position x,y , radius r in colour defined by col
	 * @param x
	 * @param y
	 * @param rad
	 * @param col
	 */
	public void showCircle(double x, double y, double rad, char col) {
	 	setFillColour(colFromChar(col));			// set the fill colour
	 	showCircle(x, y, rad);						// show the circle
	}

	/**
	 * show the ball in the current colour at x,y size rad
	 * @param x
	 * @param y
	 * @param rad
	 */
	public void showCircle(double x, double y, double rad) {
		gc.fillArc(x-rad, y-rad, rad*2, rad*2, 0, 360, ArcType.ROUND);	// fill circle
	}
	
	/**
	 * Show the wheel on canvas, colour defined with col.
	 * @param x Wheel's x position from one of the corners.
	 * @param y Wheel's x position from one of the corners.
	 * @param width Wheel's width.
	 * @param height Wheel's height.
	 * @param col Wheel's colour.
	 */
	public void showWheel(double x, double y, double width, double height, char col) {
	 	setFillColour(colFromChar(col));			// set the fill colour
	 	showWheel(x, y, width, height);						// show the circle
	}
	
	/**
	 * Show the wheel with the current colour at the position specified.
	 * @param x Wheel's x position from one of the corners.
	 * @param y Wheel's x position from one of the corners.
	 * @param width Wheel's width.
	 * @param height Wheel's height.
	 */
	public void showWheel(double x, double y, double width, double height) {
		gc.fillRect(x, y, width, height);
	}
	
	/**
	 * Presents robot onto canvas, done so as a body and two wheels. Starts with setting robot
	 * as the origin for rotation, adds robot while it is rotated and restores the canvas' changes.
	 * @param x Robot's x position.
	 * @param y Robot's y position.
	 * @param h Robot's wheel height.
	 * @param w Robot's wheel width.
	 * @param rad Robot's radius.
	 * @param angle Robot's angle.
	 * @param col Robot's colour.
	 */
	public void showRobot(double x, double y, double height, double width, double rad, double angle, char col) {
		// set wheel positions compared to the robot body
		double wheelX1 = x-rad;
		double wheelY1 = (y+rad-height);
		double wheelX2 = x-rad;
		double wheelY2 = (y-rad);
		gc.save();
		gc.translate(x, y); // translate canvas to robot's centre so it doesnt go off screen
		gc.rotate(angle); // rotates whole canvas
		gc.translate(-x, -y); // translate canvas back to original point
		showCircle(x, y, rad, col);
		showWheel(wheelX1, wheelY1, width, height, 'x');
		showWheel(wheelX2, wheelY2, width, height, 'x');
		gc.restore();
	}
	
	/**
	 * Presents whisker robot onto canvas, similar to robot but with whiskers.
	 * @param x Robot's x position.
	 * @param y Robot's y position.
	 * @param h Robot's wheel height.
	 * @param w Robot's wheel width.
	 * @param rad Robot's radius.
	 * @param angle Robot's angle.
	 * @param col Robot's colour.
	 */
	public void showWhiskerRobot(double x, double y, double height, double width, double rad, double angle, char col) {
		double wheelX1 = x-rad;
		double wheelY1 = (y+rad-height);
		double wheelX2 = x-rad;
		double wheelY2 = (y-rad);
		gc.save();
		gc.translate(x, y); // translate canvas to robot's centre so it doesnt go off screen
		gc.rotate(angle); // rotates whole canvas
		gc.translate(-x, -y); // translate canvas back to original point
		showCircle(x, y, rad, col); // body
		showWheel(wheelX1, wheelY1, width, height, 'x'); // wheels
		showWheel(wheelX2, wheelY2, width, height, 'x');
		gc.strokeLine(x+rad,y+rad,x+rad+15,y+rad+height+15); // drawing whiskers
		gc.strokeLine(x+rad,y-rad,x+rad+15,y-rad-15);
		gc.restore();
	}
	
	/**
	 * Show Text .. by writing string s at position x,y
	 * @param x
	 * @param y
	 * @param s
	 */
	public void showText (double x, double y, String s) {
		gc.setTextAlign(TextAlignment.CENTER);							// set horizontal alignment
		gc.setTextBaseline(VPos.CENTER);								// vertical
		gc.setFill(Color.WHITE);										// colour in white
		gc.fillText(s, x, y);											// print score as text
	}
	
	/**
	 * Shows where the actual line is, used for debugging
	 * @param tLine
	 */
	/*
	public void testLine(Line tLine) {
		gc.strokeLine(tLine.getLinePos()[0],tLine.getLinePos()[1],tLine.getLinePos()[2],tLine.getLinePos()[3]);
	}
	*/

	/**
	 * Show Int .. by writing int i at position x,y
	 * @param x
	 * @param y
	 * @param i
	 */
	public void showInt (double x, double y, int i) {
		showText (x, y, Integer.toString(i));
	}	
	
	public Line[] getBorderLines() {
		Line[] border = {side1, side2, side3, side4};
		return border;
	}
}

