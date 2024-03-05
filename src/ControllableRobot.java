package RobotProjectGUI;

/**
 * 
 * @author dijan
 * Class used to represent a robot that can be controlled by the player.
 */
public class ControllableRobot extends Robot{
	/**
	 * Constructor used when loading from file.
	 * @param x
	 * @param y
	 * @param angle
	 * @param colour
	 */
	ControllableRobot (double x, double y, double angle, char colour){
		super(x, y, angle, colour); // radius 10, red ball
	}
	
	/**
	 * Constructor used when loading through the user interface.
	 * @param mc
	 * @param myArena
	 * @param colour
	 */
    ControllableRobot(MyCanvas mc, RobotArena myArena, char colour) {
        super(mc, myArena, colour);
    }
    
	/**
	 * Draws object similarly to standard robot, uses red instead.
	 */
    @Override
	public void drawObject(MyCanvas mc){
		char robotColour = getIsParty() ? partyMode() : 'r';
		mc.showRobot(getX(), getY(), getHeight(), getWidth(), getRadius(), getAngle(), robotColour);
		if (getIsParty()) {
			setSpeed(2); // adjust speed, needs to be fixed
		}
	}	
	
    /**
     * Not needed, user will be providing the movement.
     */
	@Override
	public void updatePosition(MyCanvas mc, boolean isCollision) {
		// empty as it waits for user to input which direction to go
	}

	/**
	 * Moves the controllable robot up.
	 * @param mc Canvas needed to check if the new position is within arena boundaries.
	 */
	public void moveUp(MyCanvas mc) {
		if (isValidPosition(getX(),getY()-10,mc)) {
	        setPosition(getX(),getY()-10);
		}
        setAngle(270);
    }

	/**
	 * Moves the controllable robot left.
	 * @param mc Canvas needed to check if the new position is within arena boundaries.
	 */
	public void moveLeft(MyCanvas mc) {
		if (isValidPosition(getX()-10,getY(),mc)) {
	        setPosition(getX()-10,getY());
		}
        setAngle(180);
    }

	/**
	 * Moves the controllable robot down.
	 * @param mc Canvas needed to check if the new position is within arena boundaries.
	 */
	public void moveDown(MyCanvas mc) {
		if (isValidPosition(getX(),getY()+10,mc)) {
	        setPosition(getX(),getY()+10);
		}
        setAngle(90);
    }

	/**
	 * Moves the controllable robot right.
	 * @param mc Canvas needed to check if the new position is within arena boundaries.
	 */
	public void moveRight(MyCanvas mc) {
		if (isValidPosition(getX()+10,getY(),mc)) {
	        setPosition(getX()+10,getY());
		}
        setAngle(0);
    }
	
	/**
	 * Moves the controllable robot top left.
	 * @param mc Canvas needed to check if the new position is within arena boundaries.
	 */
	public void moveTopLeft(MyCanvas mc) {
		if (isValidPosition(getX()-10,getY()-10,mc)) {
	        setPosition(getX()-10,getY()-10);
		}
        setAngle(225);
    }
	
	/**
	 * Moves the controllable robot top right.
	 * @param mc Canvas needed to check if the new position is within arena boundaries.
	 */
	public void moveTopRight(MyCanvas mc) {
		if (isValidPosition(getX()+10,getY()-10,mc)) {
	        setPosition(getX()+10,getY()-10);
		}
        setAngle(315);
    }
	
	/**
	 * Moves the controllable robot bottom left.
	 * @param mc Canvas needed to check if the new position is within arena boundaries.
	 */
	public void moveBottomLeft(MyCanvas mc) {
		if (isValidPosition(getX()-10,getY()+10,mc)) {
	        setPosition(getX()-10,getY()+10);
		}
        setAngle(135);
    }
	
	/**
	 * Moves the controllable robot bottom right.
	 * @param mc Canvas needed to check if the new position is within arena boundaries.
	 */
	public void moveBottomRight(MyCanvas mc) {
		if (isValidPosition(getX()+10,getY()+10,mc)) {
	        setPosition(getX()+10,getY()+10);
		}
        setAngle(45);
    }
    
    /**
     * Provides essential controllable robot information in string form.
     * @return Controllable robot's ID, x position, y position and angle.
     */
    @Override
    public String toString() {
    	double tempX = (Math.round(100*getX()))/100;
    	double tempY = (Math.round(100*getY()))/100;
    	String output = "Controllable Robot " + getID() + " is at position " + tempX + "," + tempY + " at angle " + getAngle() + "\n";
    	return output;
    }
    
    /**
     * Provides necessary information about robot to be saved in a file
     * @return robot type, x position, y position and angle.
     */
    public String getSaveString() {
    	String fileInfo = "C " + getX() + " " + getY() + " " + getAngle() + ";";
    	return fileInfo;
    }
}
