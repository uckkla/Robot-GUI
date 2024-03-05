package RobotProjectGUI;

/**
 * 
 * @author dijan
 * Class used to represent a bullet shot by the ControllableRobot object.
 */
public class Bullet extends Robot{
	private boolean isDestroyed;
	
	/**
	 * Constructor for bullet, only one needed as position depends on where the controllable robot is.
	 * @param x Bullet's x position.
	 * @param y Bullet's y position.
	 * @param robotAngle Need robot's angle so it knows which trajectory to follow.
	 * @param colour Bullet's colour
	 */
	Bullet(double x, double y, double robotAngle, char colour){
		super(x, y, robotAngle, colour);
		setRadius(2);
		isDestroyed = false;
	}
	
	/**
	 * Draws bullet on screen represented as a small circle.
	 * @param mc Canvas needed to draw the bullet onto the canvas.
	 */
	@Override
	public void drawObject(MyCanvas mc){
		mc.showCircle(getX(), getY(), getRadius(), 'x'); // draws black circle
	}
	
	/**
	 * Logic for handling how the bullet moves. Will keep moving the same direction until it's destroyed.
	 * @param mc Needed for when checking if the new position is within the arena's bounds.
	 * @param isCollision Needed for checking if it is colliding with another item.
	 */
	@Override
	public void updatePosition(MyCanvas mc, boolean isCollision) {
		double bulletSpeed = 5;
		// Calculate the bullet's position in front of the robot
		double bulletX = getX() + bulletSpeed * Math.cos(Math.toRadians(getAngle()));
		double bulletY = getY() + bulletSpeed * Math.sin(Math.toRadians(getAngle()));

		// Adjust the bullet's position based on the robot's radius
		bulletX += getRadius() * Math.cos(Math.toRadians(getAngle()));
		bulletY += getRadius() * Math.sin(Math.toRadians(getAngle()));
        
        // whichDirection is left, !whichDirection is right
        if (isValidPosition(bulletX, bulletY, mc) && !isCollision) {
        	setPosition(bulletX, bulletY);
        }
        else {
        	isDestroyed = true;
        }
	}
	
	/**
	 * Getter to see if bullet is destroyed due to hitting a wall.
	 * @return If the bullet has been destroyed.
	 */
	public boolean getIsDestroyed() {
		return isDestroyed;
	}
	
    /**
     * Provides essential bullet information in string form.
     * @return Bullet's ID, x position, y position and angle.
     */
    @Override
    public String toString() {
    	double tempX = (Math.round(100*getX()))/100;
    	double tempY = (Math.round(100*getY()))/100;
    	String output = "Bullet " + getID() + " is at position " + tempX + "," + tempY + " at angle " + getAngle() + "\n" ;
    	return output;
    }
}
