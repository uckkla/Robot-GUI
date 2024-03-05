package RobotProjectGUI;


/**
 * 
 * @author dijan
 * Class represents a party variant of an obstacle, which can infect other robots with a
 * "party mode".
 */
public class PartyObstacle extends Obstacle{
	private int colourIter; // iterator for identifying which colour to display
	private int cooldownTimer; // its own cooldown - time between colour changes
	
	/**
	 * Constructor for PartyObstacle, used when loading from file
	 * @param x
	 * @param y
	 */
	PartyObstacle(double x, double y){
		super(x,y);
		colourIter = 0;
	}
	
	/**
	 * Alternative constructor for PartyObstacle, used when adding through interface
	 * @param mc
	 * @param myArena
	 */
	PartyObstacle(MyCanvas mc, RobotArena myArena){
		super(mc, myArena); // radius 15, ball is red
		colourIter = 0;
	}
	
	/**
	 * Draws object onto canvas, colour alternating every 100 iterations.
	 */
	@Override
	public void drawObject(MyCanvas mc){
		cooldownTimer--;
		if (cooldownTimer <= 0) {
			setColour(COLOURS[colourIter++%COLOURS.length]);
			cooldownTimer = 100;
		}
		mc.showCircle(getX(), getY(), getRadius(), getColour());
	}
	
	/**
	 * Provides essential party obstacle information in string form.
	 */
	@Override
    public String toString() {
    	String output = "Party Obstacle " + getID() + " is at position " + getX() + "," + getY() + "\n";
    	return output;
    }
	
    /**
     * Provides necessary information about obstacle to be saved in a file
     */
    public String getSaveString() {
    	String fileInfo = "P " + getX() + " " + getY() + ";";
    	return fileInfo;
    }
}
