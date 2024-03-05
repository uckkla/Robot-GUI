package RobotProjectGUI;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * 
 * @author dijan
 * Class that handles creating the window and sectioning it for the menu, user interface,
 * arena and information sections.
 */
public class RobotGUI extends Application{
	private int canvasSize = 512;				// constants for relevant sizes
    private MyCanvas mc; 
    private RobotArena myArena;
    private TextFile tf; // handles saving & loading
    private VBox rtPane;
    private boolean animationOn = false;
    private ArenaItem selectedArenaItem = null;
    private double offsetX, offsetY; // offset for dragging object
    private Set<KeyCode> pressedKeys = new HashSet<>(); // used for checking which keys are currently used. Used HashSet to avoid duplicates.
    
    /**
     * Updating old canvas with new object positions and information.
     */
	public void displaySystem() {
		mc.clearCanvas();
		myArena.drawSystem(mc);
		drawStatus();
	}
	/**
	 * Constantly updating the status of each object's position on the right.
	 */
	private void drawStatus() {
		rtPane.getChildren().clear(); // clears current status
		String statusInfo = myArena.toString() + "\n"; // string for all status info
		String keyInputInfo = "Keys pressed: " + pressedKeys.toString();
		Label label = new Label(statusInfo + keyInputInfo); // creates new status
		rtPane.getChildren().add(label); // adds it to pane
	}
	
	/** 
	 * Creates a standard robot and draws onto canvas.
	 */
	private void createRobot() {
		Robot robot = new Robot(mc, myArena, 'r');
		myArena.addObject(robot); // adding robot to the arena
		robot.drawObject(mc); // if paused, robot should still be presented on screen
	}
    
	/**
	 * Creates a standard obstacle and draws onto canvas.
	 */
	private void createObstacle() {
		Obstacle obstacle = new Obstacle(mc, myArena);
		myArena.addObject(obstacle);
		obstacle.drawObject(mc);
	}
	
	/**
	 * Creates party obstacle and draws onto canvas.
	 */
	private void createPartyObstacle() {
		PartyObstacle party = new PartyObstacle(mc, myArena);
		myArena.addObject(party);
		party.drawObject(mc);
	}
	
	/**
	 * Creates hungry robot and draws onto canvas.
	 */
	private void createHungryRobot() {
		HungryRobot hungryRobot = new HungryRobot(mc, myArena, 'g');
		myArena.addObject(hungryRobot);
		hungryRobot.drawObject(mc);
	}
	
	/**
	 * Creates controllable robot and draws onto canvas.
	 */
	private void createControllableRobot() {
		ControllableRobot controllableRobot = new ControllableRobot(mc, myArena, 'r');
		myArena.addObject(controllableRobot);
		controllableRobot.drawObject(mc);
	}
	
	/**
	 * Creates whisker robot and draws onto canvas, not finished.
	 */
	private void createWhiskerRobot() {
		WhiskerRobot whiskerRobot = new WhiskerRobot(mc, myArena, 'y');
		myArena.addObject(whiskerRobot);
		whiskerRobot.drawObject(mc);
	}
	
	/**
	 * Creates bullet, places it in the arena and draws it on canvas.
	 * @param x
	 * @param y
	 * @param angle
	 */
	private void shootBullets() {
		ArrayList<Bullet> bullets = myArena.shootAllBullets();
		for (int i=0; i<bullets.size(); i++) {
			bullets.get(i).drawObject(mc);
			//System.out.println(bullets.get(i).getX() + " " + bullets.get(i).getY() + " " + bullets.get(i).getAngle());
		}
	}
	
	/**
	 * Destroys selected object
	 */
	private void destroyObject(ArenaItem item) {
		myArena.destroyObject(item);
		displaySystem();
	}
	
	/**
	 * clears the arena, canvas and status information
	 */
	private void clearCanvas() {
		myArena.clearArena();
		mc.clearCanvas();
		rtPane.getChildren().clear();
	}
	
	/**
	 * saves arena, putting it in text file
	 */
	private void saveArena() {
		if (tf.createFile()) {
			tf.writeAllFile(myArena.fileString());
		}
	}
	
	/**
	 * loads arena, each robot separated by a semicolon. checks object type, and then loads
	 * appropriate information.
	 */
	
	private void loadArena() {
		if (tf.openFile()) {
			String fs = tf.readAllFile(); 
			fs = fs.substring(0,fs.length()-1); // removing /n at end of string
			String[] spltfs = fs.split(";"); // separates each object
			clearCanvas(); // clears whatever is currently on the canvas
			for (int i=0; i<spltfs.length; i++) {
				String itemType = spltfs[i].split(" ")[0];
				switch (itemType) {
				case "C":
					double ControllablerobotX = Double.parseDouble(spltfs[i].split(" ")[1]);
					double ControllablerobotY = Double.parseDouble(spltfs[i].split(" ")[2]);
					double ControllablerobotAngle = Double.parseDouble(spltfs[i].split(" ")[3]);
					Robot controllableRobot = new ControllableRobot(ControllablerobotX,ControllablerobotY,ControllablerobotAngle, 'r');
					myArena.addObject(controllableRobot);
					break;
				case "R":
					double robotX = Double.parseDouble(spltfs[i].split(" ")[1]);
					double robotY = Double.parseDouble(spltfs[i].split(" ")[2]);
					double robotAngle = Double.parseDouble(spltfs[i].split(" ")[3]);
					Robot robot = new Robot(robotX,robotY,robotAngle, 'b');
					myArena.addObject(robot);
					break;
				case "O":
					double obstacleX = Double.parseDouble(spltfs[i].split(" ")[1]);
					double obstacleY = Double.parseDouble(spltfs[i].split(" ")[2]);
					Obstacle obstacle = new Obstacle(obstacleX,obstacleY);
					myArena.addObject(obstacle);
					break;
				case "P":
					double PartyX = Double.parseDouble(spltfs[i].split(" ")[1]);
					double PartyY = Double.parseDouble(spltfs[i].split(" ")[2]);
					PartyObstacle party = new PartyObstacle(PartyX,PartyY);
					myArena.addObject(party);
					break;
				case "H":
					double hungryRobotX = Double.parseDouble(spltfs[i].split(" ")[1]);
					double hungryRobotY = Double.parseDouble(spltfs[i].split(" ")[2]);
					double hungryRobotAngle = Double.parseDouble(spltfs[i].split(" ")[3]);
					int hungryRobotSize = Integer.parseInt(spltfs[i].split(" ")[4]);
					HungryRobot hungryRobot = new HungryRobot(hungryRobotX,hungryRobotY,hungryRobotAngle, 'g');
					hungryRobot.increaseSize(hungryRobotSize);
					myArena.addObject(hungryRobot);
					break;			
				}
				displaySystem();
			}
		}
	}
	
	/**
	 * generates an alert, used for menu messages
	 * @param TStr
	 * @param CStr
	 */
	private void showMessage(String TStr, String CStr) {
	    Alert alert = new Alert(AlertType.INFORMATION);
	    alert.setTitle(TStr);
	    alert.setHeaderText(null);
	    alert.setContentText(CStr);

	    alert.showAndWait();
	}
	
	/**
	 * About section
	 */
    private void showAbout() {
    	String message = "Dijan's Robot Simulation. Student Number: 31007786";
    	showMessage("About", message);
    }
    
    /**
     * Info section, stores information on each robot.
     */
    private void showInfo() {
    	String message = "Details: \nDefault Robot: moves straight until it hits another object or a wall, in which it turns direction.\n"
    			+ "\nHungry Robot: Moving robot that eats any object that it touches, growing larger and slower. \n"
    			+ "\nControllable Robot: Similar to the default robot, but is moved using the WASD inputs by the user. If two key inputs are"
    			+ "pressed, then it will move diagonally. It also has the ability to shoot a bullet by pressing R. Any object hit by the"
    			+ "bullet will be destroyed with the bullet.\n"
    			+ "\nDefault Obstacle: Stationary object that blocks the robot's path. If a robot collides with it, they will change direction.\n"
    			+ "\nParty Obstacle: Similar to the default obstacle, but its colour is alternating constantly. If a robot collides with this "
    			+ "object, they will be infected with the rainbow, changing colour constantly and going twice the speed. If this infected robot touches another robot, that "
    			+ "robot will also be infected.\n";
    	showMessage("Info", message);
    }
    
    /**
     * Main menu section, handles features such as saving, loading, and about section.
     * @return
     */
    MenuBar setMenu() {
    	MenuBar menuBar = new MenuBar(); // creating menu
    	
    	Menu mFile = new Menu("File"); // file section, handles saving and loading
    	MenuItem mSave = new MenuItem("Save");
    	MenuItem mLoad = new MenuItem("Load");
    	MenuItem mExit = new MenuItem("Exit");
    	
    	Menu mHelp = new Menu("Help"); // help section
    	MenuItem mAbout = new MenuItem("About"); // info on creator 
    	MenuItem mInfo = new MenuItem("Info"); //  info on robots
    	
    	mSave.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
            	saveArena();				// show the about message
            }	
 		});
    	
    	mLoad.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
            	loadArena();				// show the about message
            }	
 		});
    	
    	mExit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
            	System.exit(0);				// show the about message
            }	
 		});
    	
    	mAbout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
            	showAbout();				// show the about message
            }	
 		});
    	
    	mInfo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
            	showInfo();				// show the about message
            }	
 		});
    	
    	mFile.getItems().addAll(mSave, mLoad, mExit); // merging submenus to file section
    	mHelp.getItems().addAll(mAbout, mInfo); // merging submenus to help section
    	menuBar.getMenus().addAll(mFile, mHelp); // merging sections to menu
    	
    	return menuBar;
    }
    
    /**
     * Handles user interface on the bottom
     * @return
     */
    private HBox setInterface() {
    	Button startAnim = new Button("Start");
    	startAnim.setOnAction(new EventHandler<ActionEvent>() {
    		@Override
    		public void handle(ActionEvent actionEvent) {
    			animationOn = true;
    		}
    	});
    	
    	Button pauseAnim = new Button("Pause");
    	pauseAnim.setOnAction(new EventHandler<ActionEvent>() {
    		@Override
    		public void handle(ActionEvent actionEvent) {
    			animationOn = false;
    		}
    	});
    	
    	Button clear = new Button("Clear");
    	clear.setOnAction(new EventHandler<ActionEvent>() {
    		@Override
    		public void handle(ActionEvent actionEvent) {
    			clearCanvas();
    		}
    	});
    	
    	Button remove = new Button("Remove");
    	remove.setOnAction(new EventHandler<ActionEvent>() {
    		@Override
    		public void handle(ActionEvent actionEvent) {
    			if (selectedArenaItem != null) {
        			destroyObject(selectedArenaItem);
    			}
    		}
    	});
    	
    	Label add = new Label("Add: ");
    	
    	Button addRobot = new Button("Robot");
    	addRobot.setOnAction(new EventHandler<ActionEvent>() {
    		@Override
    		public void handle(ActionEvent actionEvent) {
    			createRobot();
    		}
    	});
    	
    	Button addHungryRobot = new Button("Hungry Robot");
    	addHungryRobot.setOnAction(new EventHandler<ActionEvent>() {
    		@Override
    		public void handle(ActionEvent actionEvent) {
    			createHungryRobot();
    		}
    	});
    	
    	Button addControllableRobot = new Button("Controllable Robot");
    	addControllableRobot.setOnAction(new EventHandler<ActionEvent>() {
    		@Override
    		public void handle(ActionEvent actionEvent) {
    			createControllableRobot();
    		}
    	});
    	
    	Button addWhiskerRobot = new Button("Whisker Robot");
    	addWhiskerRobot.setOnAction(new EventHandler<ActionEvent>() {
    		@Override
    		public void handle(ActionEvent actionEvent) {
    			createWhiskerRobot();
    		}
    	});
    	
    	Button addObstacle = new Button("Obstacle");
    	addObstacle.setOnAction(new EventHandler<ActionEvent>() {
    		@Override
    		public void handle(ActionEvent actonEvent) {
    			createObstacle();
    		}
    	});
    	
    	Button addPartyObstacle = new Button("Party Obstacle");
    	addPartyObstacle.setOnAction(new EventHandler<ActionEvent>() {
    		@Override
    		public void handle(ActionEvent actonEvent) {
    			createPartyObstacle();
    		}
    	});
    	return new HBox(startAnim, pauseAnim , clear, remove, add, addRobot, addHungryRobot,
    			addControllableRobot, addObstacle, addPartyObstacle);
    	// addWhiskerRobot,
    }
    
    /**
     * Allows user to drag the robots across the arena, but not off the screen.
     * @param canvas
     */
    private void setMouseEvents (Canvas canvas) {
	       canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, 
	    	       new EventHandler<MouseEvent>() {
	    	           @Override
	    	           public void handle(MouseEvent e) {
	    	        	   double mouseX = e.getX();
	    	        	   double mouseY = e.getY();
	    	        	   selectedArenaItem = myArena.getArenaItemAt(mouseX, mouseY);
	    	        	   
	    	        	   // if it exists, need to add an offset so object doesn't snap to mouse
	    	               if (selectedArenaItem != null) {
	    	                   offsetX = mouseX - selectedArenaItem.getX();
	    	                   offsetY = mouseY - selectedArenaItem.getY();
	    	               }
	    	           }
	    	       });
	       canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, 
	    	       new EventHandler<MouseEvent>() {
	    	           @Override
	    	           public void handle(MouseEvent e) {
	    	               if (selectedArenaItem != null) {
	    	                   // Update the position of the selected item based on mouse drag
	    	                   double newX = e.getX() - offsetX;
	    	                   double newY = e.getY() - offsetY;
		    	        	   if (selectedArenaItem.isValidPosition(newX, newY, mc)){
		    	        		   selectedArenaItem.setPosition(newX, newY);
		    	                   displaySystem();
		    	        	   }
	    	               }
	    	           }
	       		});
    	}
    
    /**
     * Used for handling ControllableRobot inputs. These inputs include movement and shooting bullets.
     */
    private void setKeyEvents(Scene scene) {
        scene.setOnKeyPressed(e -> {
            pressedKeys.add(e.getCode());
            updateControllableRobots();
            if (e.getCode() == KeyCode.R) {
            	shootBullets();
            }
        });

        scene.setOnKeyReleased(e -> {
            pressedKeys.remove(e.getCode());
            updateControllableRobots();
        });
    }
    
    /**
     *  Determines direction the controllable robot should go dependent on which keys are held.
     * @return
     */
    private char determineMovementDirection() {
        boolean isWPressed = pressedKeys.contains(KeyCode.W);
        boolean isAPressed = pressedKeys.contains(KeyCode.A);
        boolean isSPressed = pressedKeys.contains(KeyCode.S);
        boolean isDPressed = pressedKeys.contains(KeyCode.D);

        if (isWPressed && isAPressed) {
            return 'Q'; // Diagonal movement (Up-Left)
        } else if (isWPressed && isDPressed) {
            return 'E'; // Diagonal movement (Up-Right)
        } else if (isSPressed && isAPressed) {
            return 'Z'; // Diagonal movement (Down-Left)
        } else if (isSPressed && isDPressed) {
            return 'C'; // Diagonal movement (Down-Right)
        } else if (isWPressed) {
            return 'W'; // Up
        } else if (isAPressed) {
            return 'A'; // Left
        } else if (isSPressed) {
            return 'S'; // Down
        } else if (isDPressed) {
            return 'D'; // Right
        } else {
            return ' '; // No movement
        }
    }
    
    /**
     * Sets the direction that it should go and passes it to the arena.
     */
    private void updateControllableRobots() {
        // Determine the movement direction based on the pressed keys
        char direction = determineMovementDirection();

        // Update ControllableRobots
        myArena.moveControllableRobots(mc, direction);
    }
    

	       
    /**
     * Main GUI section, creates the window and handles the UI/animations.
     */
	@Override
	public void start(Stage stagePrimary) throws Exception {
		stagePrimary.setTitle("Dijan's Robot Simulation");

	    BorderPane bp = new BorderPane();			// create border pane

	    bp.setTop(setMenu());						// create menu, add to top

	    Group root = new Group();					// create group
	    Canvas canvas = new Canvas( canvasSize, canvasSize );
	    											// and canvas to draw in
	    root.getChildren().add( canvas );			// and add canvas to group
	    mc = new MyCanvas(canvas.getGraphicsContext2D(), canvasSize, canvasSize);
					// create MyCanvas passing context on canvas onto which images put
	    myArena = new RobotArena();
	    tf = new TextFile("Text files", "txt");
	    
	    bp.setCenter(root);							// put group in centre pane

	    rtPane = new VBox();						// set vBox for listing data
	    bp.setRight(rtPane);						// put in right pane

	    Scene scene = new Scene(bp, canvasSize*1.6, canvasSize*1.2);
		// create scene so bigger than canvas, 
	    
	    setMouseEvents(canvas); // handles dragging objects using the mouse
	    setKeyEvents(scene); // handles key inputs for controllable robots

	    final long startNanoTime = System.nanoTime();		// for animation, note start time

	    new AnimationTimer()			// create timer
	    	{
	    		public void handle(long currentNanoTime) {
	    				// define handler for what do at this time
	    			if (animationOn) {
	    				double t = (currentNanoTime - startNanoTime) / 1000000000.0; // calculate time
	    				myArena.updateSystem(mc);	// update arena
	    				displaySystem();	// clear canvas and draw system
	    			}	
	    		}
	    	}.start();

	    bp.setBottom(setInterface()); // add interface to bottom

		stagePrimary.setScene(scene);
		stagePrimary.show();
	}
	
	/**
	 * Launches GUI
	 * @param args
	 */
	public static void main(String[] args) {
		Application.launch(args);			// launch the GUI
	}
}
