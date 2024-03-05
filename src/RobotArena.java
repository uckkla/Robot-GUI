package RobotProjectGUI;

import java.util.ArrayList;

/**
 * 
 * @author dijan
 * Class that handles all ArenaItem objects in an array list.
 */
public class RobotArena {
	private ArrayList<ArenaItem> robotArena; // represents arena that all objects will go in
	/**
	 * Default constructor, creates an array list which will contain ArenaItem type objects.
	 */
	RobotArena(){
		robotArena = new ArrayList<>();
	}
	
	/**
	 * Checks the object against all other objects in the arena to make sure there are no collisions.
	 * If the object is touching a party infected robot/party obstacle, it also infects it.
	 * If one of the objects are a bullet, they are both destroyed.
	 * @param item1 item in the arena
	 * @param mc Canvas needed when checking for a hungry robot, as change in size would require to see if it is in arena boundaries.
	 * @return If a collision has occurred.
	 */
	public boolean anyCollisions(ArenaItem item1, MyCanvas mc) {
        // Iterate through all pairs of arena items
        for (int i = 0; i < robotArena.size(); i++) {
            ArenaItem item2 = robotArena.get(i);
            if (item1 != item2 && item1.isCollision(item2)) { // ensure it isnt comparing itself and collision is made
            	if ((item2 instanceof PartyObstacle || item2.getIsParty()) && !item1.getIsParty()) { // checks to see if it's infected/hitting a party obstacle
            		item1.setParty();
            	}
            	checkHungryRobot(item1, item2, mc); // checks to see if they're hungry robots
            	checkIfBullet(item1, item2); // checks if either one is a bullet.
                return true;
            }
        }
        return false;
	}
	
	/**
	 * checks to see what robot is at those particular coordinates
	 * @param x Coordinate of the location being checked.
	 * @param y Coordinate of the location being checked.
	 * @return Object on that position if it exists, otherwise null.
	 */
	public ArenaItem getArenaItemAt(double x, double y) {
	    for (int i=0; i<robotArena.size(); i++) {
	    	double upperboundX = robotArena.get(i).getX()+robotArena.get(i).getRadius();
	    	double lowerboundX = robotArena.get(i).getX()-robotArena.get(i).getRadius();
	    	double upperboundY = robotArena.get(i).getY()+robotArena.get(i).getRadius();
	    	double lowerboundY = robotArena.get(i).getY()-robotArena.get(i).getRadius();
	        if (upperboundX >= x && lowerboundX <= x && upperboundY >= y && lowerboundY <= y) {
	            return robotArena.get(i);
	        }
	    }
	    return null;
	}
	
	/**
	 * Constantly updating positions of each object, taking into consideration collisions.
	 * @param mc Canvas needed when updating its position, as it needs to check if it is in the arena's boundaries.
	 */
	public void updateSystem(MyCanvas mc) {
		ArrayList<Bullet> destroyedBullets = new ArrayList<>(); // bullets that need to be destroyed after iteration
		for (int i=0; i<robotArena.size(); i++) {
			if (robotArena.get(i) instanceof Bullet) { // checks first to see if it's a bullet
				if (((Bullet)robotArena.get(i)).getIsDestroyed()) { // if so, check if it's been destroyed (hit a wall)
					destroyedBullets.add((Bullet)robotArena.get(i)); // adds to destroyed arraylist
				}
			}
			if (robotArena.get(i) instanceof Robot) {
				((Robot)robotArena.get(i)).updatePosition(mc, anyCollisions(robotArena.get(i),mc));
			}
		}
		destroyAllBullets(destroyedBullets); // destroys all the bullets
	}
	
	/**
	 * Adds new item to arena list
	 * @param object The object that will be added to the arena list.
	 */
	
	public void addObject(ArenaItem object) {
		robotArena.add(object);
	}
	
	/**
	 * Removes selected item from arena list
	 * @param object The object that will be removed to the arena list.
	 */
	public void destroyObject(ArenaItem object) {
		robotArena.remove(object);
	}
	
	/**
	 * Draws each object onto canvas
	 * @param mc Canvas needed so each robot can be drawn on the canvas.
	 */
	public void drawSystem(MyCanvas mc) {
		for (int i=0; i<robotArena.size(); i++) {
			robotArena.get(i).drawObject(mc);
		}
	}
	
	/**
	 * Checks all controllable robots that exist, and moves them to the direction the user inputted.
	 * @param mc Canvas needed as the movement methods change the robot's position in the canvas.
	 * @param direction Directions the user has inputted.
	 */
	public void moveControllableRobots(MyCanvas mc, char direction) {
		for (int i=0; i<robotArena.size(); i++) {
			if (robotArena.get(i) instanceof ControllableRobot) {
				switch (direction) {
				case 'Q': // top left
					((ControllableRobot)robotArena.get(i)).moveTopLeft(mc);
					break;
				case 'E': // top right
					((ControllableRobot)robotArena.get(i)).moveTopRight(mc);
					break;
				case 'Z': // bottom left
					((ControllableRobot)robotArena.get(i)).moveBottomLeft(mc);
					break;
				case 'C': // bottom right
					((ControllableRobot)robotArena.get(i)).moveBottomRight(mc);
					break;
				case 'W': // up
					((ControllableRobot)robotArena.get(i)).moveUp(mc);
					break;
				case 'A': // left
					((ControllableRobot)robotArena.get(i)).moveLeft(mc);
					break;
				case 'S': // down
					((ControllableRobot)robotArena.get(i)).moveDown(mc);
					break;
				case 'D': // right
					((ControllableRobot)robotArena.get(i)).moveRight(mc);
					break;
				}
					
			}
		}
	}
	
	/**
	 * Looks for all Controllable Robots and shoots a bullet out of them
	 * @return All the bullets that need to be shot out.
	 */
	public ArrayList<Bullet> shootAllBullets() {
		double offset = 7; // offset of bullet compared to the front of robot
		ArrayList<Bullet> bullets = new ArrayList<>();
		for (int i=0; i<robotArena.size(); i++) {
			if (robotArena.get(i) instanceof ControllableRobot) {
				//double bulletX = getX() + bulletDistance * Math.cos(Math.toRadians(getAngle()));
				//double bulletY = getY() + bulletDistance * Math.sin(Math.toRadians(getAngle()));
		        double bulletX = robotArena.get(i).getX() + (robotArena.get(i).getRadius() + offset) * Math.cos(Math.toRadians(((Robot)robotArena.get(i)).getAngle()));
		        double bulletY = robotArena.get(i).getY() + (robotArena.get(i).getRadius() + offset) * Math.sin(Math.toRadians(((Robot)robotArena.get(i)).getAngle()));
				Bullet bullet = new Bullet(bulletX, bulletY, ((Robot)robotArena.get(i)).getAngle(), 'x');
				robotArena.add(bullet);
			}
		}
		return bullets;
	}
	
	/**
	 * Destroys all bullets that were found to be destroyed from the arena/canvas.
	 * @param bullets
	 */
	private void destroyAllBullets(ArrayList<Bullet> bullets) {
		for (int i=0; i<bullets.size(); i++) {
			destroyObject(bullets.get(i));
		}
	}
	
	/**
	 * First robot is known to be hungry, so it checks to see if the other is hungry. If so, compare sizes and
	 * the biggest eats the smallest.
	 * @param item1 
	 * @param item2
	 */
	private void checkHungryRobot(ArenaItem item1, ArenaItem item2, MyCanvas mc) {
		if (item1 instanceof HungryRobot && item2 instanceof HungryRobot) { // both are hungry robots
			if (item1.compareSize(item2)) { // 1st robot is bigger than the other
				eatRobot(item1, item2, mc); // 
			}
			else { // 2nd robot is bigger than first
				eatRobot(item2, item1, mc); 
			}
		}
		else if (item1 instanceof HungryRobot) { // only 1st robot is hungry
			eatRobot(item1, item2, mc);
		}
		else if (item2 instanceof HungryRobot) { // only 2nd robot is hungry
			eatRobot(item2, item1, mc);
		}
	}
	
	/**
	 * item1 eats item2, increasing its size.
	 * @param item1
	 * @param item2
	 */
	private void eatRobot(ArenaItem item1, ArenaItem item2, MyCanvas mc) {
		destroyObject(item2);
		((HungryRobot)item1).increaseSize(mc);
	}
	
	/**
	 * Checks if one of the items are a bullet. If so, both objects are destroyed.
	 * @param item1
	 * @param item2
	 */
	private void checkIfBullet(ArenaItem item1, ArenaItem item2) {
		if (item1 instanceof Bullet || item2 instanceof Bullet) {
			destroyObject(item1);
			destroyObject(item2);
		}
	}
	

	
	/**
	 * Removes all items from arena
	 */
	public void clearArena() {
		robotArena.clear();
	}
	
	/**
	 * Output each object's information such as ID, x and y.
	 */
	public String toString() {
		String output = "";
		for (int i=0; i<robotArena.size(); i++) {
			output += robotArena.get(i).toString();
		}
		return output;
	}
	/**
	 * Outputs necessary information needed of each object so it can be saved
	 * @return All of the ArenaItems' essential information in order.
	 */
	public String fileString() {
		String fileInfo = "";
		for (int i=0; i<robotArena.size(); i++) {
			fileInfo += robotArena.get(i).getSaveString();
		}
		return fileInfo;
	}
}
