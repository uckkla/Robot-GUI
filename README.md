# Robot Simulation with JavaFX

## Overview
This project is a robot simulation which utilises the GUI JavaFX with Java, where a variety of robots roam around an arena.
The GUI Provided allows for users to view and control what robots are added.

## Features
- **Multiple Robot Types**: The simulation provides a variety of robot types, such as the "standard robot", "hungry robot" and "controllable robot." Each of these robots have their own characteristics, such as being manually controlled by the user, or eating other robots in its way.
- **User interaction**: Robots can be interacted through the GUI by dragging them across the arena. More robots can be added and selected robots can be removed through tbe buttons provided. The simulation can be paused, continued or restarted so all the robots are taken off the arena.
- **Save/Loading**: The arena can be saved onto a text file storing all robots inside of it. This text file can then be loaded which presents the arena again.
- **Obstacles in arena**: The arena can be filled with obstacles which can block the robot's path. An additional obstacle has been added which turn the robot into a party robot, increasing its speed and rapidly changing its colour for a short duration.
- **Robot information presented**: The GUI shows the positions of all robots on the arena and any keys being currently pressed on the keyboard. This is typically used for debugging rather than a visualisation.

## Installation Guide
1. Clone the repository: `git clone https://github.com/uckkla/Robot-GUI.git`.
2. Open the project using preferred IDE.
3. Configure build path in chosen IDE to include the JavaFX jar files provided in the `/lib` directory. This ensures the IDE has access to the JavaFX files to run the GUI.
4. Run the RobotGUI main jar.

## Known issues
- Robots will occassionally get stuck on the arena walls or with other robots. Can be unstuck by dragging the robot to a different part of the arena.
- Hungry robots can sometimes be unpredictable when colliding with other robots, as sometimes it changes direction while other times continues onwards.
- ArenaItem needs to be reimplemented, it utilises the Canvas class when it should not need to.

## How it's made
- **Creating GUI**: Using JavaFX, I created a window which has an about me section, buttons that were not in use yet and other empty toolbar options.
- **Class Diagram**: I created a basic class diagram which would showcase how the classes would interact with each other. This started with having the robot, an obstacle, GUI class/canvas and robot arena where the objects are placed in. This was later expanded to the ArenaItem class which is an abstract class that sets the template for every object in the arena, and other variants of the standard robot/obstacle.
- **Robot Class Development**: The robot class was made with standard attributes such as its x/y position, width and height. Integrating the robot into the GUI through the canvas class presented a challenge due to the absence of a standard method for rotating the robot. To circumvent this, I devised a workaround where I adjusted the canvas instead. This was done by rotating the canvas at the centre of the canvas and restoring only the canvas, which left the robot in the correct orientation. The robot was tested by outputting its "toString()" method onto the GUI, which would show any issues with its movement compared to the GUI equivalent.
- **Obstacle Class Development**: The obstacle class was implemented similarly to the robot class, however the canvas was easier to display as the obstacle is represented as a circle.
- **Abstract Class Implementation**: After realising that the robot and obstacles both had common attributes, I created an ArenaItem abstract class which contained the structure for a typical item. This served as a blueprint which defines the common attributes and methods needed for each object. This allowed for much clearer and easily maintainable code.
- **Arena Class implementation**: To be able to present multiple robots/obstacles in the arena GUI, the arena class was implemented which took in ArenaItem objects. This allowed for all objects in the arena to be easily managed for further features such as clearing the arena or checking for collisions.
- **Expansion to Other Robot/Obstacle Variants**: This was simpler to do as it contained a lot of the functions that the default robot/obstacle used. These variants would have small features such as the hungry robot consuming other objects in its path.
- **Saving/Loading Implementation**: This was done by extracting essential information from the arena such as its type, position, and other specific attributes. This data is simplified and placed in a text file, which when loaded creates each object manually and places it in the arena. To accomodate the random generation of x/y coordinates for placing the robot and obstacle objects in an arena, the constructor needed to be overloaded to ensure objects were position correctly..
