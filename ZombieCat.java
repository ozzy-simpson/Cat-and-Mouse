import java.util.Random;

public class ZombieCat extends Cat {

    public ZombieCat(int x, int y, City cty, Random rnd) {
        super(x, y, cty, rnd); // call the constructor of the superclass
        this.lab = LAB_RED; // set the color to red
        this.stepLen = 3; // set the step length to 3
    }

    public void step(){
        Creature closest = null;
        int closestDist = 40;
        for (Creature c : this.city.creatures) {
            // If the creature is a mouse or a cat, and it is within 40 grid points, chase it
            if ((c instanceof Mouse || (c instanceof Cat && !(c instanceof ZombieCat))) && this.point.dist(c.point) <= 40 && this.point.dist(c.point) <= closestDist) {
                this.lab = LAB_BLACK; // set the color to black
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

        this.lab = LAB_RED; // set the color to red since not chasing

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
        // A cat eats a mouse if they end up on the same location. That is, the mouse should die and be removed from the simulation.
        // When a zombie cat ends up on the same location as a cat, the cat turns into a zombie cat.
        for (Creature c : this.city.creatures) {
            // If cat eats poison mouse, zombie cat becomes regular cat
            if (c instanceof PoisonMouse && this.point.dist(c.point) == 0) {
                Cat newCat = new Cat(c.getX(), c.getY(), c.city, c.rand);
                this.city.creaturesToAdd.add(newCat); // add the new cat to the city
                this.die(); // kill the old zombie cat
                this.timeSinceLast = 0;
            }
            // If the zombie cat eats a mouse, the mouse dies
            if (c instanceof Mouse && this.point.dist(c.point) == 0) {
                c.die();
                this.timeSinceLast = 0;
            }
            // If the zombie cat eats a cat, the cat becomes a zombie cat
            if (c instanceof Cat && !(c instanceof ZombieCat) && this.point.dist(c.point) == 0) {
                ZombieCat newZombie = new ZombieCat(c.getX(), c.getY(), c.city, c.rand);
                this.city.creaturesToAdd.add(newZombie); // add the new zombie to the city
                c.die(); // kill the old cat
                this.timeSinceLast = 0;
            }
        }
        this.timeSinceLast++;

        // If a zombie cat doesn’t eat within 100 moves, the zombie cat dies.
        if (this.timeSinceLast >= 100) {
            die();
        }
    }


}