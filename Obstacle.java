package RobotProjectGUI;

/**
 * 
 * @author dijan
 * Class that represents an obstacle, any robot that hits it will turn to a different direction.
 */
public class Obstacle extends ArenaItem{
	/**
	 * Constructor used when loading from file.
	 * @param x
	 * @param y
	 */
	Obstacle(double x, double y){
		super(x,y,15,'r');
	}
	
	/**
	 * Constructor used when loading from user interface.
	 * @param mc
	 * @param myArena
	 */
	Obstacle(MyCanvas mc, RobotArena myArena){
		super(15,'r', mc, myArena); // radius 15, ball is red
	}
	
	/**
	 * Draws obstacle onto the canvas.
	 */
	@Override
	public void drawObject(MyCanvas mc){
		mc.showCircle(getX(), getY(), getRadius(), getColour());
	}
	
	/**
	 * Provides essential obstacle information in string form.
	 */
	@Override
    public String toString() {
    	String output = "Obstacle " + getID() + " is at position " + getX() + "," + getY() + "\n";
    	return output;
    }
	
    /**
     * Provides necessary information about obstacle to be saved in a file
     */
    public String getSaveString() {
    	String fileInfo = "O " + getX() + " " + getY() + ";";
    	return fileInfo;
    }
}
