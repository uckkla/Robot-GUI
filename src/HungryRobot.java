package RobotProjectGUI;

/**
 * 
 * @author dijan
 * Class used to represent a robot that can eat other ArenaItem type objects.
 */
public class HungryRobot extends Robot{
/**
 * Constructor for default robot, used when loading from file
 * @param x
 * @param y
 * @param angle
 * @param colour
 */
	HungryRobot(double x, double y, double angle, char colour){
		super(x, y, angle, colour); // radius 10, green ball
		//setRobotWidth(getRadius() + getHeight() + 5); // may need to adjust this, robots keep getting stuck
		//setRobotHeight(getRadius() + 5);
	}
	
	/**
	 * Constructor for default robot, used when adding a robot from interface
	 * @param mc
	 * @param myArena
	 * @param colour
	 */
	HungryRobot(MyCanvas mc, RobotArena myArena, char colour){
		super(mc, myArena, colour); // radius 10, ball is green
		//setRobotWidth(getRadius() + getHeight() + 5); // may need to adjust this, robots keep getting stuck
		//setRobotHeight(getRadius() + 5);
	}
	
	/**
	 * Draws default robot on screen by making a circle with two rectangles above and below it.
	 */
	// need to fix drawRobot, as the lines seem to be inconsistent with each other unless adjusted for.
	@Override
	public void drawObject(MyCanvas mc){
		char robotColour = getIsParty() ? partyMode() : 'g';
		mc.showRobot(getX(), getY(), getHeight(), getWidth(), getRadius(), getAngle(), robotColour);
		if (getIsParty()) {
			setSpeed(2); // adjust speed, needs to be fixed
		}
	}	
	
	/**
	 * Increases the size of the robot by a set amount.
	 * @param mc Canvas needed to check if the size change stays within the arena's boundaries.
	 */
    public void increaseSize(MyCanvas mc) {
    	int oldRadius = getRadius();
    	setRadius(getRadius()+2);
    	updateSize(getRadius(), oldRadius);
    	
    	if (!isValidPosition(getX(), getY(), mc)) {
    		// makes newX and newY smaller than the canvas, but larger than the radius
    		double newX = Math.max(getRadius(), Math.min(mc.getXCanvasSize() - getRadius(), getX()));
            double newY = Math.max(getRadius(), Math.min(mc.getYCanvasSize() - getRadius(), getY()));
            
            setPosition(newX, newY);
    	}
    	
    	if (getSpeed() > 0.025) {
    		incrementSpeed(-0.025);
    	}
    	else if (getSpeed() < 0.025) {
    		setSpeed(0.025); // slowest the hungry robot can go
    	}
    }
    
    /**
     * Increase size - used when loading from file
     * @param radius Hungry robot's radius.
     */
    public void increaseSize(int radius) {
    	int oldRadius = getRadius();
    	setRadius(radius);
    	updateSize(getRadius(), oldRadius);
    	
    	// setting speed dependent on how many times its radius has increased
    	setSpeed(1-(((radius-getRadius())/2)*0.025));
    }
    
    /**
     * Provides essential hungry robot information in string form.
     * @return Hungry robot's ID, x position, y position and angle.
     */
    @Override
    public String toString() {
    	double tempX = (Math.round(100*getX()))/100;
    	double tempY = (Math.round(100*getY()))/100;
    	String output = "Hungry Robot " + getID() + " is at position " + tempX + "," + tempY + " at angle " + getAngle() + "\n";
    	return output;
    }
    
    /**
     * Provides necessary information about robot to be saved in a file
     * @return Robot type, x position, y position, angle and radius.
     */
    public String getSaveString() {
    	String fileInfo = "H " + getX() + " " + getY() + " " + getAngle() +  " " + getRadius() +";";
    	return fileInfo;
    }
}
