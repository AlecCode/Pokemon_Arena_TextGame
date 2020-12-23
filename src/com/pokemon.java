package com;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Alec on 2017-11-30.
 */
public class pokemon {
    private String name;                                    //name holds the name of the pokemon
    private int health;                                     //health holds the health of the pokemon
    private int maxHealth;                                  //maxHealth holds the maximum health of the pokemon
    private int energy;                                     //energy holds the energy of the pokemon
    private int maxEnergy;                                  //maxEnergy holds the maximum energy of the pokemon
    private String type;                                    //type holds the type of the pokemon
    private String resistance;                              //resistance holds the resistance of the pokemon
    private String weakness;                                //weakness holds the weakness of the pokemon
    private ArrayList<attack> attacks = new ArrayList<attack>(); //attacks holds all of the attacks of the pokemon
    private boolean stunned;                                //stunned holds whether or not the pokemon is stunned

    //This method takes information and assigns it to the appropriate variables
    public pokemon(String name, int health, int energy, String type, String resistance, String weakness, int numOfAtks, String[]atks, int[]atkCost, int[] atkDMG, String[] atkSpecial){
        this.name = name;
        this.health = health;
        this.maxHealth = health;
        this.energy = energy;
        this.maxEnergy = energy;
        this.type = type;
        this.resistance = resistance;
        this.weakness = weakness;

        for(int i = 0; i < numOfAtks; i++){
            this.attacks.add(new attack(atks[i] , atkCost[i], atkDMG[i], atkSpecial[i]));
        }
    }

    //This method calculates the damage of the attack and subtracts it from the health of the target pokemon
    public void attack(pokemon target, attack atk){
        Random rand = new Random();     //rand is a random number generator
        int dmg = atk.getAtkDMG();      //dmg is the dammage of the attack
        int ogDmg = dmg;                //ogDmg holds the original damage

        //These if/else statements apply damage modifiers depending on resistance/weakness
        if(this.type.equalsIgnoreCase(target.weakness)){
            dmg *= 2;
        }
        else if(this.type.equalsIgnoreCase(target.resistance)){
            dmg /= 2;
        }

        //These if/else statements apply the special effects of the attacks
        if(atk.getAtkSpecial().equalsIgnoreCase("stun")){
            int stunChance = rand.nextInt(2);   //stunChance holds whether or not the attack stuns the target pokemon
            if(stunChance == 1){
                target.changeStatus("stun");
                System.out.println(target.name + " became stunned!");
            }
        }
        else if(atk.getAtkSpecial().equalsIgnoreCase("wild card")){
            int wildChance = rand.nextInt(2);   //wildChance holds whether or not the attack will succeed or not
            if(wildChance == 1){
                dmg = 0;
                System.out.println("The attack failed!");
            }
        }
        else if(atk.getAtkSpecial().equalsIgnoreCase("wild storm")){
            int stormChance = 1;                      //stormChance holds whether or not the attack will continue or not
            int repeats = 1;                          //repeats holds the number of times the attack repeats
            while(stormChance == 1){
                stormChance = rand.nextInt(2);
                dmg += ogDmg;
                repeats++;
            }
            System.out.println("Attacked " + repeats + " time(s)!");
        }
        else if(atk.getAtkSpecial().equalsIgnoreCase("disable")){
            target.changeStatus("disable");
            System.out.println(target.getName() + " became disabled!");
        }
        else if(atk.getAtkSpecial().equalsIgnoreCase("recharge")){
            System.out.println(this.name + " recharged!");
            this.energy += 20;
        }

        //This if/else statement determines whether or not the pokemon is stunned or not
        if(!this.stunned){
            target.takeDamage(dmg);
            this.energy -= atk.getAtkCost();

            //This if/else statement displays the effectiveness of the attack
            if(this.type.equalsIgnoreCase(target.getWeakness())){
                System.out.println("The attack was super effective!");
            }
            else if(this.type.equalsIgnoreCase(target.getResistance())){
                System.out.println("The attack was not very effective!");
            }

            System.out.println(this.name + " did " + dmg + " damage to " + target.getName() + "!");
        }
        else{
            System.out.println(this.name + " is stunned!");
            this.changeStatus("stun");
        }
    }

    //This method returns whether or not the pokemon has fainted or not
    public boolean faint(){
        if(this.health <= 0){
            return true;
        }
        else{
            return false;
        }
    }

    //This method adds health to the pokemon
    public void addHealth(int h){   //h holds the amount of health to be added
        if(this.health + h <= this.maxHealth){
            this.health += h;
        }
        else{
            this.health = this.maxHealth;
        }
    }

    //This method adds energy to the pokemon
    public void addEnergy(int e){  //e holds the amount of energy to be added
        if(this.energy + e <= this.maxEnergy){
            this.energy += e;
        }
        else{
            this.energy = this.maxEnergy;
        }
    }

    //This method subtracts damage from the health of the pokemon
    public void takeDamage(int dmg){
        this.health -= dmg;
    }

    //This method changes the statuses of the pokemon
    public void changeStatus(String stat){
        if(stat.equalsIgnoreCase("stun") && !this.stunned){
            this.stunned = true;
        }
        else if(stat.equalsIgnoreCase("stun") && this.stunned){
            this.stunned = false;
        }
        else if(stat.equalsIgnoreCase("disable")){
            for(attack i : this.attacks){
                i.disabled();
            }
        }
        else if(stat.equalsIgnoreCase("undisable")){
            for(attack i : this.attacks){
                i.undisabled();
            }
        }
    }

    //These are accessor methods
    public String getName(){
        return this.name;
    }
    public int getHealth(){
        return this.health;
    }
    public String getType(){
        return this.type;
    }
    public String getResistance(){
        return this.resistance;
    }
    public String getWeakness(){
        return this.weakness;
    }
    public int getEnergy(){
        return this.energy;
    }
    public boolean getStunned(){
        return this.stunned;
    }

    //This method displays the data for the pokemon's attacks
    public void showAttackData(){
        for(attack atk : this.attacks){
            String name = atk.getAtkName();         //name holds the name of the attack
            int dmg = atk.getAtkDMG();              //dmg holds the damage of the attack
            String special = atk.getAtkSpecial();   //special holds the special effect of the attack
            int cost = atk.getAtkCost();            //cost holds the energy cost of the attack
            System.out.printf("\t%-10s%-10s%-20s%-10s\n", name, "\tDamage: " + dmg, "\tSpecial: " + special, "\tCost: "+ cost);
        }
    }

    //This method returns a specified attack in the "attacks" array
    public attack getAttack(int atk){   //atk holds the index of the attack that is to be returned
        return this.attacks.get(atk);
    }

    //This method returns the entire "attacks" array
    public ArrayList<attack> getAttacks(){
        return this.attacks;
    }
}

