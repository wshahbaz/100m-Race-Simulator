/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shahbazw100msimulation;

import java.text.DecimalFormat;

/**
 *
 * @author Wais Shahbaz
 */
public class ShahbazWRunner {

    //these variables hold the core data of each runner
    private String name;
    private String character;
    private String accessory;
    private double speed = 0;
    private double acceleration = 0;
    private double stamina = 0;
    private double reactions = 0;
    private double decelleration = 0;

    //these variables strictly hold the number of skill points per catagory
    //they will hold the final values of each skill point, after the character and accessory changes are put in
    private int speedStat = 0;
    private int accelStat = 0;
    private int stamStat = 0;
    private int reactStat = 0;

    //this variable holds the final time of the runner
    private double time;
    //these variables are used in calculating the final time for the runner 
    private double maxSpeedTime;
    private double maxSpeedDistance;
    private double maxSpeedConstant;
    private double maxSpeedDistanceConstant;

    
    /**
     * this contsructor initializes a runner object. 
     * It's purpose is to gather stats chosen by user before they're altered by character and accessory
     * pre: String rName, int rSpeed, rAccel, rStamina, rReactions 
     * post: a runner object containing its name, and core skills (before character and accessory update them)
     */
    public ShahbazWRunner(String rName, int rSpeed, int rAccel, int rStamina, int rReactions) {
        name = rName;
        speedStat = rSpeed;
        accelStat = rAccel;
        stamStat = rStamina;
        reactStat = rReactions;
    }

    /**
     * this contsructor initializes a runner object pre: String rName,runner,
     * item; int rSpeed, rAccel, rStamina, rReactions post: a runner object
     * containing its core data variables
     */
    public void finalizeRunner(String runner, String item, int rSpeed, int rAccel, int rStamina, int rReactions) {
        
        //Strings defining which character and item were chosen
        character = runner;
        accessory = item;

        //max speed is stat with base 13m/s; each skill point increases it by 0.5
        speed = 13 + 0.5 * rSpeed;
        //acceleration is stat with base 1.95m/s^2; each skill point increases it by 0.04
        acceleration = 1.95 + 0.04 * rAccel;
        //stamina is stat with base as 50%; each skill point decreases it by 4%
        stamina = 0.5 - 0.04 * rStamina;
        //reactions is stat with base 0.71s; each skill point decreases it by 0.05s
        reactions = 0.71 - 0.075 * rReactions;

        //this method gets the time for the runner to complete the race
        getRunStats();
    }

    /**
     * this method configures the data into a final quadratic to find time for
     * tunner to complete race pre: none post: quadratic equation is set up for
     * calcTime method to calculate time (solutions)
     */
    public void getRunStats() {
        //Start with phase1 
        //find time to reach max speed: s'(t) = acceleration * time
        maxSpeedTime = speed / acceleration;
        //getting distance travelled to reach max speed: s(t) = acceleration/2 * time^2
        maxSpeedDistance = (acceleration / 2) * Math.pow(maxSpeedTime, 2);

        /**
         * now we have sufficient data to begin phase 2 get new acceleration
         * (factor stamina in) this is going to act as s"(t)
         *
         */
        decelleration = -1 * acceleration * stamina;
        /**
         * trying to find s'(t) s'(t) = acceleration * time + maxSpeedConstant
         * need to find a constant value so that s'(maxSpeedTime) = maxSpeed
         *
         */
        maxSpeedConstant = speed - decelleration * maxSpeedTime;
        /**
         * now trying to find s(t) need to find a constant value so that
         * s(maxSpeedTime) = maxSpeedDistance s(t) = (acceleration / 2) * time^2
         * + maxSpeedConstant * time + maxSpeedDistanceConstant *
         */
        maxSpeedDistanceConstant = maxSpeedDistance - (decelleration / 2) * Math.pow(maxSpeedTime, 2) - maxSpeedConstant * maxSpeedTime;

        /**
         * now we have complete quadratic equation in the form s(t) =
         * (acceleration/2) * time^2 + maxSpeedConstant*time +
         * maxSpeedDistanceConstant factor in the s(t) = 0 to get solvable
         * quadratic 0 = (acceleration/2) * time^2 + maxSpeedConstant*time +
         * (maxSpeedDistanceConstant-100) run through calcTime method to return
         * root of quadratic and answer*
         */
        time = calcTime((decelleration / 2), maxSpeedConstant, (maxSpeedDistanceConstant - 100));

        //finally factor in reactions variable to get final time
        time += reactions;
    }

    /**
     * this method calculates the time for runner pre: 3 terms, a,b,c which
     * represent a quadratic in form ax^2 +bx+c post: time variable updated
     */
    public double calcTime(double aTerm, double bTerm, double cTerm) {
        //we will always desire the positive root due to the nature of the graph and its roots
        //therefore the negative root is not needed
        double xPos;
        double discriminant = Math.sqrt(Math.pow(bTerm, 2) - 4 * aTerm * cTerm);
        double disPos = discriminant;
        double denom = 2 * aTerm;
        xPos = (-bTerm + disPos) / denom;
        return xPos;
    }

    //method returns time of runner
    public double getTime() {
        return time;
    }

    //method returns time in a 4 decimal place format
    public String displayTime() {
        DecimalFormat decFour = new DecimalFormat("#0.0000");
        return decFour.format(time);
    }

    /**
     * this method creates a filing String for a runner pre: none post: String
     * containing all of runner's core data
     */
    public String toFileResult() {

        return "Name: " + name + "\r\n" + "Runner: " + character + "\r\nTime to complete race: " + displayTime() + " seconds\r\n" + "Speed: " + speedStat
                + "\r\nAcceleration: " + accelStat + "\r\nStamina: " + stamStat + "\r\nReactions: " + reactStat + "\r\n" + "Accessory: " + accessory + "\r\n";
    }

    /**
     * this method creates a String to display in results tab pre: none post:
     * String containing runner name and time to complete race
     */
    public String toStringRaceResult() {
        String result = name + ". Time: " + displayTime() + "s.";
        return result;
    }

    //method returns name
    public String getName() {
        return name;
    }

    /**
     * this method compares to runner objects, specifically their time pre:
     * runner object post: int of -1 or 1, depending on if time was lower or
     * higher, respectively
     */
    public int compareTo(ShahbazWRunner bro) {
        ShahbazWRunner testBro = (ShahbazWRunner) bro;
        if (this.time < testBro.getTime()) {
            return -1;
        }
        return 1;
    }
}
