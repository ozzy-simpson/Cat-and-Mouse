import java.util.Random;

public class PoisonMouse extends Mouse {
    private int numRounds;

    public PoisonMouse(int x, int y, City cty, Random rnd) {
        super(x, y, cty, rnd); // call the constructor of the superclass
        this.numRounds = 0; // initialize the number of rounds
        this.lab = LAB_ORANGE; // set the color to orange
    }

    public void takeAction(){
        // After 20 rounds, mouse creates new poison mouse
        if (numRounds == 20) {
            PoisonMouse newMouse = new PoisonMouse(this.getX(), this.getY(), this.city, this.rand);
            this.city.creaturesToAdd.add(newMouse); // add the new mouse to the city
        }
        // Mouse dies after 2 rounds
        else if (numRounds == 25) {
            die(); // kill mouse
        }
        // Increment number of rounds
        numRounds++;
    }


}