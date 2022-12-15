import java.util.Random;

public class Cat extends Creature {
    protected int timeSinceLast;

    public Cat(int x, int y, City cty, Random rnd) {
        super(x, y, cty, rnd); // call the constructor of the superclass
        this.timeSinceLast = 0; // initialize the number of rounds
        this.lab = LAB_YELLOW; // set the color to blue
        this.stepLen = 2; // set the step length to 2
    }

    public void step(){
        // A cat searches up to 20 grid points (as measured by the GridPoint.distance() method) for a mouse to chase.
        Creature closest = null;
        int closestDist = 20;
        for (Creature c : this.city.creatures) {
            if (c instanceof Mouse && this.point.dist(c.point) <= 20 && this.point.dist(c.point) <= closestDist) {
                this.lab = LAB_CYAN; // set the color to cyan
                closest = c;
            }
        }

        if (closest != null) {// Move, based on where the mouse is
            if (this.point.x < closest.point.x) {
                // Mouse is to the right, move right
                this.point.x += this.stepLen;
            }
            else if (this.point.x > closest.point.x) {
                // Mouse is to the left, move left
                this.point.x -= this.stepLen;
            }
            else if (this.point.y < closest.point.y) {
                // Mouse is same x but below, move down
                this.point.y += this.stepLen;
            }
            else {
                // Mouse is same x but above, move up
                this.point.y -= this.stepLen;
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
            return;
        }

        // Should it turn?
        this.randomTurn();

        this.lab = LAB_YELLOW; // set the color to yellow since not chasing

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
        for (Creature c : this.city.creatures) {
            // If the cat eats a poison mouse, the cat dies
            if (c instanceof PoisonMouse && this.point.dist(c.point) == 0) {
                // Ate poison mouse, die
                this.die();
                return;
            }
            // If the cat eats a mouse, the mouse dies
            if (c instanceof Mouse && this.point.dist(c.point) == 0) {
                c.die();
                this.timeSinceLast = 0;
            }
        }
        this.timeSinceLast++;

        // If a cat doesn’t eat a mouse within 50 moves, the cat becomes a zombie.
        if (this.timeSinceLast >= 50) {
            ZombieCat newZombie = new ZombieCat(this.getX(), this.getY(), this.city, this.rand);
            this.city.creaturesToAdd.add(newZombie); // add the new zombie to the city
            die(); // kill the old cat
        }
    }

    //make a random turn
    public void randomTurn() {
        // Turn in random direction 5% of the time (1/20)
        if (rand.nextInt(20) == 0) {
            this.setDir(rand.nextInt(4));
        }
    }


}