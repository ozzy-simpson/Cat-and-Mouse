import java.util.Random;

public class Mouse extends Creature {
    private int numRounds;

    public Mouse(int x, int y, City cty, Random rnd) {
        super(x, y, cty, rnd); // call the constructor of the superclass
        this.numRounds = 0; // initialize the number of rounds
        this.lab = LAB_BLUE; // set the color to blue
    }

    public void step(){
        // Should it turn?
        this.randomTurn();

        // Move, based on direction
        if (this.getDir() == NORTH) {
            this.point.y -= this.stepLen;
        } else if (this.getDir() == EAST) {
            this.point.x += this.stepLen;
        } else if (this.getDir() == SOUTH) {
            this.point.y += this.stepLen;
        } else if (this.getDir() == WEST) {
            this.point.x -= this.stepLen;
        }

        // Wrap around the edges
        if (this.getX() < 0) {
            this.point.x += City.WIDTH;
        } else if (this.getX() > City.WIDTH) {
            this.point.x = this.point.x - City.WIDTH;
        }
        if (this.getY() < 0) {
            this.point.y += City.HEIGHT;
        } else if (this.getY() > City.HEIGHT) {
            this.point.y = this.point.y - City.WIDTH;
        }
    } 

    public void takeAction(){
        // After 20 rounds, mouse creates new mouse
        if (numRounds == 20) {
            Mouse newMouse = new Mouse(this.getX(), this.getY(), this.city, this.rand);
            this.city.creaturesToAdd.add(newMouse); // add the new mouse to the city
        }
        // Mouse dies after 30 rounds
        else if (numRounds == 30) {
            die(); // kill mouse
        }
        // Increment number of rounds
        numRounds++;
    }

    //make a random turn
    public void randomTurn() {
        // Turn in random direction 20% of the time (1/5)
        if (rand.nextInt(5) == 0) {
            this.setDir(rand.nextInt(4));
        }
    }


}