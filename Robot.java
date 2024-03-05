package RobotProjectGUI;

import java.util.Random;

/**
 * 
 * @author dijan
 * Class that represents a typical robot that is able to move around the arena.
 */
public class Robot extends ArenaItem{
	private Random random;
	private double angle;
	private double speed;
    private double width;
    private double height;
    protected double robotWidth; // used for checking boundaries
    protected double robotHeight;
    protected static int COOLDOWN_PERIOD = 3; // collide cooldown reset length
    protected int collideCooldown; // cooldown after colliding with other game object

	
	/**
	 * Constructor for default robot, used when loading from file
	 * @param x Robot's x position.
	 * @param y Robot's y position.
	 * @param _angle Robot's angle.
	 * @param colour Robot's colour.
	 */
	Robot(double x, double y, double _angle, char colour){
		super(x, y, 10, colour); // radius 10, ball is blue
		angle = _angle;
		speed = 1; // default speed
		width = 20;
		height = 3;
		robotWidth = getRadius() + height+5; // may need to adjust this, robots keep getting stuck
		robotHeight = getRadius()+5;
	}
	
	/**
	 * Constructor for default robot, used when adding a robot from interface
	 * @param mc Canvas needed to determine where to place the Robot.
	 * @param myArena Arena needed to check if the placement collides with any other items in the arena.
	 * @param colour Robot's colour.
	 */
	Robot(MyCanvas mc, RobotArena myArena, char colour){
		super(10, colour, mc, myArena); // radius 10, ball is blue
		random = new Random();
		angle = random.nextInt(360);
		speed = 1; // default speed
		width = 20;
		height = 3;
		robotWidth = getRadius() + height+5; // may need to adjust this, robots keep getting stuck
		robotHeight = getRadius()+5;
	}
	
	/**
	 * Draws default robot on screen by making a circle with two rectangles above and below it.
	 * @param mc Canvas needed to draw the robot onto the canvas.
	 */
	// need to fix drawRobot, as the lines seem to be inconsistent with each other unless adjusted for.
	@Override
	public void drawObject(MyCanvas mc){
		char robotColour = getIsParty() ? partyMode() : 'b';
		mc.showRobot(getX(), getY(), height, width, getRadius(), angle, robotColour);
		if (getIsParty()) {
			setSpeed(2); // adjust speed, needs to be fixed
		}
	}
	
	/**
	 * Logic for handling how the robot moves. If a collision occurs, robot needs to have a cooldown
	 * so that they can move after rotating.
	 * @param mc Needed for when checking if the new position is within the arena's bounds.
	 * @param isCollision Needed for checking if it is colliding with another item.
	 */
	public void updatePosition(MyCanvas mc, boolean isCollision) {
		collideCooldown--;
        double newX = (getX() + (Math.cos(Math.toRadians(angle)))*speed);
        double newY = (getY() + (Math.sin(Math.toRadians(angle)))*speed);
        
        // whichDirection is left, !whichDirection is right
        if (isValidPosition(newX, newY, mc) && !isCollision || collideCooldown > 0) {
        	setPosition(newX, newY);
        } else if (!isValidPosition(newX, newY, mc) && whichDirection(newX, newY, mc)) {
            // If the new position is outside the canvas, change direction
            angle -= 90;
        } else if (!isValidPosition(newX, newY, mc) && !whichDirection(newX, newY, mc)) {
        	angle += 90;
        } else if (isCollision && collideCooldown <= 0) {
        	angle += 90;
        	collideCooldown = COOLDOWN_PERIOD;
        }
	}
	
    /**
     * Party mode, where objects will move faster and have a rainbow colour for a few seconds
     * @return Next colour.
     */
	@Override
    public char partyMode() {
		// should've been an ArenaItem method, but had to fix speed...
    	colourCooldown--; // time between colour changes
    	partyCooldownLength--; // party mode length
    	if (colourCooldown <= 0) { // handles how fast the colours change
    		currentColourIndex = (currentColourIndex + 1) % COLOURS.length;
    		colourCooldown = COLOUR_COOLDOWN_PERIOD;
    	} else if (partyCooldownLength <= 0) {
    		setParty(); // turns off party when finished
    		setSpeed(1);
    	}
    	return COLOURS[currentColourIndex];
    }
	
	/**
	 * Similar to ArenaItem's method, except takes the robot's width/height into
	 * consideration.
	 * @param newX The x position it wants to move to.
	 * @param newY The y position it wants to move to.
	 * @param mc Canvas needed to check if it is in the arena's bounds.
	 * @return If it is within the arena's bounds.
	 */
	@Override
    public boolean isValidPosition(double newX, double newY, MyCanvas mc) {
        double canvasWidth = mc.getXCanvasSize();
        double canvasHeight = mc.getYCanvasSize();

        return newX - robotWidth  >= 0 && newX + robotWidth <= canvasWidth && newY - robotHeight >= 0 && newY + robotHeight <= canvasHeight;
    }
	
	/**
	 * Allows for the robot to change size taking into consideration its wheel/body proportions.
	 * @param newRadius The new size the robot is turning to, needed to check proportions.
	 * @param oldRadius The old size the robot is turning to, needed to check proportions.
	 */
    public void updateSize(double newRadius, double oldRadius) {
        // increase wheels with the same ratio as radius
        height = height * (newRadius / oldRadius);
        width = width * (newRadius / oldRadius);

        // Update other size-related properties if needed
        robotWidth = newRadius + height + 5;
        robotHeight = newRadius + 5;
    }
    
	/**
	 * Chooses direction on which the robot should go.
	 * @param newX The x position it wants to move to.
	 * @param newY The y position it wants to move to.
	 * @param mc Canvas needed to check which wall the robot is colliding with.
	 * @return Which direction to go.
	 */
	protected boolean whichDirection(double newX, double newY, MyCanvas mc) {
    	double gradient = (newY-getY())/(newX-getX()); // (y2-y1)/(x2-x1)
        double canvasWidth = mc.getXCanvasSize();
        double canvasHeight = mc.getYCanvasSize();
        
        // goes left if either case is true, right if it is not
    	if (newX - robotWidth <= 0 || newX + robotWidth >= canvasWidth) {
    		return gradient < 0;
    	} else if (newY - robotHeight <= 0 || newY + robotHeight >= canvasHeight) {
    		return gradient >= 0;
    	}
    	return true; // default direction, should not occur

	}
    
    /**
     * Getter - Provides the robot's angle.
     * @return Robot's angle.
     */
    public double getAngle() {
    	return angle;
    }
    
    /**
     * Getter - Provides the robot's speed.
     * @return Robot's speed.
     */
    protected double getSpeed() {
    	return speed;
    }
    
    /**
     * Getter - Provides the robot's wheel height.
     * @return Robot's wheel height.
     */
    protected double getHeight() {
    	return height;
    }
    
    /**
     * Getter - Provides the robot's wheel width.
     * @return Robot's wheel width.
     */
    protected double getWidth() {
    	return width;
    }
    
    /**
     * Gets the robot's width, calculated through the radius, height and an offset.
     * @return Robot's width.
     */
    protected double getRobotWidth() { // might not be needed, as width can be calculated through radius/height
    	return robotWidth;
    }
    
    /**
     * Gets the robot's height, calculated through the radius and an offset.
     * @return Robot's height.
     */
    protected double getRobotHeight() { // might not be needed, as width can be calculated through radius
    	return robotHeight;
    }
    
    /**
     * Sets the robot's width.
     * @param _robotWidth The robot width that it will be set to.
     */
    protected void setRobotWidth(double _robotWidth) { // might not be needed, as width can be calculated through radius/height
    	robotWidth = _robotWidth;
    }
    
    /**
     * Sets the robot's height.
     * @param _robotHeight The robot height that it will be set to.
     */
    protected void setRobotHeight(double _robotHeight) { // might not be needed, as width can be calculated through radius/height
    	robotHeight = _robotHeight;
    }
    
    /**
     * Sets the robot's angle.
     * @param _angle The robot angle that it will be set to.
     */
    public void setAngle(double _angle) {
    	angle = _angle;
    }
    
    /**
     * Sets the robot's speed.
     * @param _speed The robot speed that it will be set to.
     */
    public void setSpeed(double _speed) {
    	speed = _speed;
    }
    
    /**
     * Used for specific child classes such as hungry robot, making it go slower.
     * @param increment the amount the speed will go up/down by.
     */
    protected void incrementSpeed(double increment) {
    	speed += increment;
    }
    
    /**
     * Set the robot wheel's height, typically used for hungry robot.
     * @param _height height of wheel
     */
    protected void setHeight(double _height) {
    	height = _height;
    }
    
    /**
     * Set the robot wheel's width, typically used for hungry robot.
     * @param _width height of wheel
     */
    protected void setWidth(double _width) {
    	width = _width;
    }
    
    /**
     * Provides robot information in string form to be placed in the GUI.
     * @return robot ID, x position, y position and angle.
     */
    @Override
    public String toString() {
    	double tempX = (Math.round(100*getX()))/100;
    	double tempY = (Math.round(100*getY()))/100;
    	String output = "Default Robot " + getID() + " is at position " + tempX + "," + tempY + " at angle " + getAngle() + "\n" ;
    	return output;
    }
    
    /**
     * Provides necessary information about robot to be saved in a file.
     * @return robot type, x position, y position and angle.
     */
    public String getSaveString() {
    	String fileInfo = "R " + getX() + " " + getY() + " " + getAngle() + ";";
    	return fileInfo;
    }
}
