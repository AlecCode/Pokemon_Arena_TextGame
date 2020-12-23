package com;

/**
 * Created by Alec on 2017-11-30.
 */
public class attack {
    private String atkName;     //atkName holds the name of the attack
    private int atkCost;        //atkCost holds the energy cost of the attack
    private int atkDMG;         //atkDmg holds the damage of the attack
    private int ogAtkDmg;       //ogAtkDmg holds the original damage of the attack
    private String atkSpecial;  //atkSpecial holds the special effect of the attack

    //This method takes information and assigns it to the appropriate variables
    public attack(String atks, int atkCost, int atkDMG, String atkSpecial){
        this.atkName = atks;
        this.atkCost = atkCost;
        this.atkDMG = atkDMG;
        this.ogAtkDmg = atkDMG;
        this.atkSpecial = atkSpecial;
    }

    //This applies the "disabled" effect to the attack
    public void disabled(){
        if((this.atkDMG - 10) < 0){
            this.atkDMG = 0;
        }
        else{
            this.atkDMG -= 10;
        }
    }

    //This removes the "disabled" effect from the attack
    public void undisabled(){
        this.atkDMG = ogAtkDmg;
    }

    //These are the accessor methods
    public String getAtkName() { return atkName; }
    public int getAtkCost(){
        return atkCost;
    }
    public int getAtkDMG(){
        return atkDMG;
    }
    public String getAtkSpecial(){
        return atkSpecial;
    }
}
