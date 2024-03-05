package RobotProjectGUI;

/**
 * 
 * @author dijan
 * Class which represents a robot with whiskers which have collision detection.
 */
public class WhiskerRobot extends Robot{
	Line whisker1; // robot's whiskers
	Line whisker2;
	
	/**
	 * Constructor for whisker robot, used when loading from file
	 * @param x
	 * @param y
	 */
	WhiskerRobot(double x, double y, double angle, char colour){
		super(x, y, angle, colour); // radius 10, yellow ball
		// create line for each whisker, identical to how it is drawn
		whisker1 = new Line(getX()+getRadius(), getY()+getRadius(), getX()+getRadius()+15, getY()+getRadius()+getHeight()+15);
		whisker2 = new Line(getX()+getRadius(), getY()-getRadius(), getX()+getRadius()+15, getY()-getRadius()-15);	
	}
	
	/**
	 * Constructor for whisker robot, used when adding a robot from interface
	 * @param mc
	 */
	WhiskerRobot(MyCanvas mc, RobotArena myArena, char colour){
		super(mc, myArena, colour); // radius 10, yellow ball
		// create line for each whisker, identical to how it is drawn
		/*
		whisker1 = new Line(getX()+getRadius(), getY()+getRadius(), getX()+getRadius()+15, getY()+getRadius()+getHeight()+15);
		whisker2 = new Line(getX()+getRadius(), getY()-getRadius(), getX()+getRadius()+15, getY()-getRadius()-15);
		*/
        double whisker1StartX = getX() + getRadius() * Math.cos(Math.toRadians(getAngle()));
        double whisker1StartY = getY() + getRadius() * Math.sin(Math.toRadians(getAngle())) + getRadius();


        double whisker2StartX = getX() + getRadius() * Math.cos(Math.toRadians(getAngle()));
        double whisker2StartY = getY() + getRadius() * Math.sin(Math.toRadians(getAngle())) - getRadius();
		

		whisker1 = new Line(whisker1StartX, whisker1StartY, whisker1StartX + 15, whisker1StartY + getHeight() + 15);
		whisker2 = new Line(whisker2StartX, whisker2StartY, whisker2StartX + 15, whisker2StartY + getHeight()- 15);
		
		//whisker1.rotate(getAngle());
		//whisker2.rotate(getAngle());
	}

	/**
	 * Draws object similarly to standard robot, uses yellow instead.
	 */
	public void drawObject(MyCanvas mc){
		char robotColour = getIsParty() ? partyMode() : 'y';
		mc.showWhiskerRobot(getX(), getY(), getHeight(), getWidth(), getRadius(), getAngle(), robotColour);
		if (getIsParty()) {
			setSpeed(2); // adjust speed, needs to be fixed
		}
		//mc.testLine(whisker1);
		//mc.testLine(whisker2);
	}	
	
	/**
	 * Checks if the position it is going to is valid. First checks if its whiskers are hitting the wall, and then if the body is hitting
	 * the wall.
	 */
	@Override
    public boolean isValidPosition(double newX, double newY, MyCanvas mc) {
        double canvasWidth = mc.getXCanvasSize();
        double canvasHeight = mc.getYCanvasSize();
        Line[] sides = mc.getBorderLines(); // all bounds of canvas in terms of lines
        
        for (int i=0; i<4; i++) {
        	if (whisker1.findintersection(sides[i]) || whisker2.findintersection(sides[i])) {
        		return false; // if either whisker intersects with any of the walls, then it is not a valid position
        	}
        }
        return newX - getRobotWidth()  >= 0 && newX + getRobotWidth() <= canvasWidth && newY - getRobotHeight() >= 0 && newY + getRobotHeight() <= canvasHeight;
    }
	
	/**
	 * Logic for handling how the robot moves. If a collision occurs, robot needs to have a cooldown
	 * so that they can move after rotating.
	 */
	@Override
	public void updatePosition(MyCanvas mc, boolean isCollision) {
		collideCooldown--;
        double newX = (getX() + (Math.cos(Math.toRadians(getAngle())))*getSpeed());
        double newY = (getY() + (Math.sin(Math.toRadians(getAngle())))*getSpeed());
        
        double whisker1StartX = getX() + getRadius() * Math.cos(Math.toRadians(getAngle()));
        double whisker1StartY = getY() + getRadius() * Math.sin(Math.toRadians(getAngle())) + getRadius();


        double whisker2StartX = getX() + getRadius() * Math.cos(Math.toRadians(getAngle()));
        double whisker2StartY = getY() + getRadius() * Math.sin(Math.toRadians(getAngle())) - getRadius();
        
        // whichDirection is left, !whichDirection is right
        if (isValidPosition(newX, newY, mc) && !isCollision || collideCooldown > 0) {
        	setPosition(newX, newY);
        	whisker1 = new Line(whisker1StartX, whisker1StartY, whisker1StartX + 15, whisker1StartY + getHeight() + 15);
            whisker2 = new Line(whisker2StartX, whisker2StartY, whisker2StartX + 15, whisker2StartY - 15);
        } else if (!isValidPosition(newX, newY, mc) && whichDirection(newX, newY, mc)) {
            // If the new position is outside the canvas, change direction
        	setAngle(getAngle()-90);
        	//whisker1.rotate(getAngle());
        	//whisker2.rotate(getAngle());
        } else if (!isValidPosition(newX, newY, mc) && !whichDirection(newX, newY, mc)) {
        	setAngle(getAngle()+90);
        	//whisker1.rotate(getAngle());
        	//whisker2.rotate(getAngle());
        } else if (isCollision && collideCooldown <= 0) {
        	setAngle(getAngle()+90);
        	//whisker1.rotate(getAngle());
        	//whisker2.rotate(getAngle());
        	collideCooldown = COOLDOWN_PERIOD;
        }
	}
	
	@Override
	public String toString() {
	    double tempX = (Math.round(100*getX()))/100;
	    double tempY = (Math.round(100*getY()))/100;
	    String output = "Default Robot " + getID() + " is at position " + tempX + "," + tempY + " at angle " + getAngle() + "\n";
	    return output;
	}
}
