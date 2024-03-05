package RobotProjectGUI;

import java.util.Random;

/**
 * 
 * @author dijan
 * Abstract class used to represent an item in the arena.
 */
public abstract class ArenaItem {
	private double x, y;
	private int radius;
	private char colour;
	private static int objectCount;
	private int objectID;
	private boolean isParty; // party mode
	protected static char[] COLOURS = {'r','o','y','g','b'};
	protected static int COLOUR_COOLDOWN_PERIOD = 100; // time between each colour change
	protected static int PARTY_LENGTH = 1000; // how long party mode lasts for
	protected int partyCooldownLength; // time left of party mode
	protected int colourCooldown; // time left between colour changes
	protected int currentColourIndex; // current colour - used for party mode
	
	private Random random;
	
	/**
	 * Constructor for an arena item, used for saving/loading
	 * @param _x
	 * @param _y
	 * @param _radius
	 */
	ArenaItem(double _x, double _y, int _radius, char _colour){
		x = _x;
		y = _y;
		radius = _radius;
		objectID = objectCount++;
		colour = _colour;
	}
	
	/**
	 * Constructor for an arena item, used when adding through interface
	 * @param _radius
	 * @param _colour
	 * @param mc Canvas needed to determine where to place ArenaItem
	 */
	ArenaItem(int _radius, char _colour, MyCanvas mc, RobotArena myArena){
		random = new Random();
		radius = _radius;
		do {
			x = random.nextInt(radius+5, mc.getXCanvasSize()-radius);
			y = random.nextInt(radius+5, mc.getYCanvasSize()-radius);
		} while(myArena.anyCollisions(this, mc));
		objectID = objectCount++;
		colour = _colour;
	}
	
	/**
	 * Default draw object on canvas
	 * @param mc canvas needed to draw object
	 */
	public void drawObject(MyCanvas mc){
		if (isParty) {
			mc.showCircle(x, y, radius, partyMode());
		} else {
			mc.showCircle(x, y, radius, colour);
		}
	}
	
	/** 
	 * Default collision between objects checker
	 * @param object2 second object needed to compare radii.
	 * @return If the objects collide
	 */
    public boolean isCollision(ArenaItem object2) {
        double distanceSquared = Math.pow(x - object2.getX(), 2) + Math.pow(y - object2.getY(), 2);
        double sumOfRadiiSquared = Math.pow(radius + object2.getRadius(), 2);
        return distanceSquared < sumOfRadiiSquared;
    }
    
    /**
     * Checks to see if the game object's new position is in the arena
     * @param newX x value that ArenaItems wants to move to
     * @param newY y value that ArenaItems wants to move to
     * @param mc canvas needed to check if new move is in the arena
     * @return If new move is in the arena
     */
    public boolean isValidPosition(double newX, double newY, MyCanvas mc) {
        double canvasWidth = mc.getXCanvasSize();
        double canvasHeight = mc.getYCanvasSize();

        return newX - radius >= 0 || newX <= canvasWidth || newY >= 0 | newY <= canvasHeight;
    }
    
    /**
     * Party mode, where objects will move faster and have a rainbow colour for a few seconds.
     * They can either receive this effect from the Party Obstacle, or from an infected robot.
     * @return What colour the item needs to be
     */
    public char partyMode() {
    	colourCooldown--; // time between colour changes
    	partyCooldownLength--; // party mode length
    	if (colourCooldown <= 0) { // handles how fast the colours change
    		currentColourIndex = (currentColourIndex + 1) % COLOURS.length;
    		colourCooldown = COLOUR_COOLDOWN_PERIOD;
    	} else if (partyCooldownLength <= 0) {
    		setParty(); // turns off party when finished
    	}
    	return COLOURS[currentColourIndex];
    }
    
    /**
     * Compares its size to other object. If the object is bigger, return true. If other object is bigger, return false.
     * @param item2 Other object that it will be compared to in terms of size.
     * @return True if the current item is bigger, false if the second item is bigger.
     */
    public boolean compareSize(ArenaItem item2) {
    	if (getRadius() > item2.getRadius()) {
    		return true;
    	}
    	else {
    		return false;
    	}
    }
    
    /**
     * Getter - Provides value for x coordinate
     * @return x coordinate.
     */
	public double getX() {
		return x;
	}
	
    /**
     * Getter - Provides value for y coordinate
     * @return y coordinate.
     */
	public double getY() {
		return y;
	}
	
    /**
     * Getter - Provides value for ID, only used for inherited classes.
     * @return item's ID.
     */
	protected int getID() {
		return objectID;
	}
	
    /**
     * Getter - Provides value for radius, only used for inherited classes.
     * @return item's radius.
     */
	protected int getRadius() {
		return radius;
	}
	
    /**
     * Getter - Provides value for current colour, only used for inherited classes.
     * @return item's current colour.
     */
	protected char getColour() {
		return colour;
	}
	
	/**
     * Getter - Provides value for if it's in party mode, only used for inherited classes.
     * @return If the item is in party mode or not.
     */
	protected boolean getIsParty() {
		return isParty;
	}
	
	/**
     * Setter - Sets the radius of the item.
     * @param _radius The radius that it will be set to.
     */
	public void setRadius(int _radius) {
		radius = _radius;
	}
	
	/**
     * Setter - Sets the colour of the item, only used for inherited classes.
     * @param _colour The colour that it will be set to.
     */
	protected void setColour(char _colour) {
		colour = _colour;
	}
	
	/**
	 * Setter - sets the x coordinate of the item. Kept private as x,y position will
	 * be set using the setPosition(x,y) method.
	 * @param _x The x coordinate that the item will be set to.
	 */
	private void setX(double _x) {
		x = _x;
	}
	
	/**
	 * Setter - sets the y coordinate of the item. Kept private as x,y position will
	 * be set using the setPosition(x,y) method.
	 * @param _y The y coordinate that the item will be set to.
	 */
	private void setY(double _y) {
		y = _y;
	}
	
	/**
	 * Setter - sets the position of the item. 
	 * @param _x The x coordinate that the item will be set to.
	 * @param _y The y coordinate that the item will be set to.
	 */
	public void setPosition(double _x, double _y) {
		setX(_x);
		setY(_y);
	}
	
	/**
	 * Turns the party mode on/off, and resets the cooldown.
	 */
	public void setParty() {
		isParty = !isParty;
		partyCooldownLength = PARTY_LENGTH;
	}
	
	/**
	 * Abstract class, set so that all objects that inherit will need to provide information
	 * that is placed in the right pane window.
	 * @return Item information in string form.
	 */
	public abstract String toString();
    
    /**
     * Abstract class, set so that all classes that inherit will need to provide essential
     * information for saving..
     * @return Essential item information in string form.
     */
    public abstract String getSaveString(); // abstract as all objects need to be saved
}
