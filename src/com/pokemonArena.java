package com;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * Created by Alec Mai on 2017-11-30.
 * This is Pokemon Arena, where a player can choose four pokemon to a team and battle the rest of the pokemon that were not chosen.
 * Each pokemon has:
 *      A Name
 *      Health
 *      Energy
 *      A Type
 *      A Weakness
 *      A Resistance
 *      Attacks
 *
 * When each pokemon's health reaches zero they faint and can no longer participate in battle.
 * The player wins if they can beat all the opposing pokemon.
 * The player loses if all of their pokemon are defeated.
 */

public class pokemonArena {
    private static int oppPkmIndex;     //oppPkmIndex holds the opponent's current pokemon index in the "opponents" arraylist
    private static int playerPkmIndex;  //playerPkmIndex holds the player's cuurent pokemon index in the "playerTeam" araylist

    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);                                //in is used to get input
        Random rand = new Random();                                         //rand is used as a random number generator
        Scanner pkmIndex = new Scanner(new File("pokemon.txt"));   //pkmIndex holds the data from the "pokemon.txt" file
        ArrayList<pokemon> pokedex = inputReader(pkmIndex);                 //pokedex holds the information from the "pokemon.txt" file as pokemon objects

        ArrayList<pokemon> playerTeam = new ArrayList<pokemon>();           //playerTeam holds the player's selection of pokemon
        ArrayList<Integer> unavailablePkm = new ArrayList<Integer>();       //unavailablePkm holds the pokemon that have been selected by the user and cannot be in the opponent's team
        int teamSize = 0;                                                   //teamSize holds the size of the player's team

        //These hold whether the player has lost or the computer has lost
        boolean playerLost = true;
        boolean compLost = true;

        int first = rand.nextInt(2);    //first holds who goes first

        //This gets the input from the user
        System.out.println("Welcome to Pokemon Arena!\nHere you will be able to live out your wildest fighting dreams!");
        System.out.println("Here you will choose FOUR Pokemon to be on your team and battle your way to the end.\nAre you up to the challenge?");
        //This while loop is used to wait for the user to enter the correct number of valid inputs
        while(true){

            //This if statement checks for confirmation of the player's team
            if(teamSize == 4){
                System.out.println("You have chosen:");
                for(pokemon pkm : playerTeam){
                    System.out.println(pkm.getName());
                }
                System.out.println("Will this be your final team? (Y/N)");

                String answer = in.nextLine();  //answer holds input

                //This checks for confirmation from the player
                boolean confirmation;
                if(answer.equalsIgnoreCase("y") || answer.equalsIgnoreCase("yes")){
                    confirmation = true;
                }
                else{
                    confirmation = false;
                }

                System.out.println(); //This is for formatting

                //This checks if the user has confirmed the team or not
                //If not, it resets the teams
                if(confirmation){
                    break;
                }
                else{
                    teamSize = 0;
                    playerTeam.clear();
                    unavailablePkm.clear();
                }
            }

            System.out.println("To view the Pokedex enter: Pokedex");
            System.out.println("To view an individual Pokemon enter: Search");
            System.out.println("Enter the Pokedex ID of the Pokemon you would Like to add to your team!");

            String input = in.nextLine();   //input holds the player's input

            //These if/else statements check for the type of command
            //This outputs the full pokedex
            if(input.equalsIgnoreCase("pokedex")){
                showPokedex(pokedex);
            }
            //This outputs one entry from the pokedex
            else if(input.equalsIgnoreCase("search")){
                System.out.println("Which Pokemon would you like to view?");
                int lookUp = in.nextInt();
                showPokemon(pokedex, lookUp % (pokedex.size() + 1));
            }
            //This removes the last entry into the player's team
            else if(input.equalsIgnoreCase("remove")){
                if(teamSize >= 1){
                    System.out.println("You removed " + playerTeam.get(teamSize-1).getName() + " from your team.");
                    playerTeam.remove(playerTeam.get(teamSize-1));
                    teamSize--;
                }
                else{
                    System.out.println("There are no Pokemon to remove!");
                }
            }
            //This checks to see if the input is a valid pokemon
            else{
                //This try/catch statement is used to differentiate between a pokemon index and a pokemon name
                try{
                    if(!unavailablePkm.contains(Integer.valueOf(input)-1)){
                        playerTeam.add(pokedex.get(Integer.valueOf(input)-1));
                        teamSize++;
                        unavailablePkm.add(Integer.valueOf(input)-1);

                        System.out.println("\nYou chose: " + playerTeam.get(teamSize-1).getName());
                        System.out.println("If you want to remove your previous selection enter: Remove");
                    }
                    else{
                        System.out.println("This Pokemon is unavailable!\n");
                    }
                }
                catch(Exception e){
                    //e.printStackTrace(); for debugging
                    System.out.println("Failed to add Pokemon.");
                }
            }
        }

        //This adds the rest of the non-player pokemon to the opponent team
        ArrayList<pokemon> opponents = new ArrayList<pokemon>();
        for(int i = 0; i < pokedex.size(); i++){
            if(!unavailablePkm.contains(i)){
                opponents.add(pokedex.get(i));
            }
        }

        //This displays who goes first
        if(first == 0){
            System.out.println("You will go first!");
        }
        else{
            System.out.println("The opponent will go first!");
        }

        //This gets the pokemon the player wants to send out first
        System.out.println("Who will you send out?");
        while(true){
            playerPkmIndex = -1;    //playerPkmIndex is set to -1 so it can be caught later in case of no valid inputs

            for(int i = 0; i < playerTeam.size(); i++){
                System.out.println((i+1) + ". " + playerTeam.get(i).getName());
            }

            String chosen = in.nextLine();  //chosen holds the input from the user

            //This try/catch statement is used to differentiate between string and integer input
            try{
                playerPkmIndex = Integer.valueOf(chosen)-1;
            }
            catch(Exception e){
                for(int i = 0; i < playerTeam.size(); i++){
                    if(chosen.equalsIgnoreCase(playerTeam.get(i).getName())){
                        playerPkmIndex = i;
                    }
                }
            }

            System.out.println();   //This is for formatting
            //This if/else statement is used to determine if the selection was valid or not
            if(playerPkmIndex >= 0){
                System.out.println(playerTeam.get(playerPkmIndex).getName() + " I choose you!");
                break;
            }
            else{
                System.out.println("That is not a valid Pokemon!");
            }
        }

        //This randomizes the first opponent
        oppPkmIndex = rand.nextInt(opponents.size());

        //This is the game loop
        while(true){
            //This if statement ends the game when no undefeated pokemon can be chosen by a side
            if(opponents.get(oppPkmIndex).faint() || playerTeam.get(playerPkmIndex).faint()){
                break;
            }

            //This if else statement is used to differentiate attack order
            if(first == 0){
                playerMove(playerTeam, opponents);
                System.out.println(); //This is for formatting
                oppAtk(playerTeam, opponents);
                System.out.println(); //This is for formatting
            }
            else{
                oppAtk(playerTeam, opponents);
                System.out.println(); //This is for formatting
                playerMove(playerTeam, opponents);
                System.out.println(); //This is for formatting
            }

            //This adds energy to both sides after each turn
            for(pokemon pkm : playerTeam){
                pkm.addEnergy(10);
            }
            for(pokemon pkm : opponents){
                pkm.addEnergy(10);
            }
        }

        //These check which side has no pokemon that are undefeated
        for(pokemon pkm : playerTeam){
            if(!pkm.faint()){
                playerLost = false;
            }
        }
        for(pokemon pkm : opponents){
            if(!pkm.faint()){
                compLost = false;
            }
        }

        //These if/else statements display either the winning message, defeat message, or an error message
        if(compLost && !playerLost){
            System.out.println("Congratulations! You win!");
            System.out.println("You are the ultimate trainer!");
        }
        else if(playerLost && !compLost){
            System.out.println("You lose.");
            System.out.println("Train harder and next time will be different!");
        }
        else{
            System.out.println("Something went wrong. Please restart the game.");
        }
    }

    //This method reads input from files and outputs a an arraylists of pokemon objects
    static ArrayList inputReader(Scanner pkmIndex){
        int numPokemon = Integer.valueOf(pkmIndex.nextLine());  //numPokemon holds the amount of pokemon information in the file
        ArrayList<pokemon> pokedex = new ArrayList<pokemon>();  //pokedex holds the pokemon information as pokemon objects

        //This loop splits all the pokemon information on one line
        for(int i = 0; i < numPokemon; i++){
            String rawPkmInfo = pkmIndex.nextLine();            //rawPkmInfo holds the line of pokemon information
            String[] pkmInfo = rawPkmInfo.split(",");     //pkmInfo splits the "rawPkmInfo" among the "," characters

            String pkmName = pkmInfo[0];                        //pkmName holds the name of the pokemon
            int pkmHealth = Integer.valueOf(pkmInfo[1]);        //pkmHealth holds the health of the pokemon
            String pkmType = pkmInfo[2];                        //pkmType holds the type of the pokemon
            String pkmResist = pkmInfo[3];                      //pkmResist holds the resistance of the pokemon
            String pkmWeak = pkmInfo[4];                        //pkmWeak holds the weakness of the pokemon
            int numOfAttacks = Integer.valueOf(pkmInfo[5]);     //numOfAttacks holds the number of attacks the pokemon has
            String[] atkNames = new String[numOfAttacks];       //atkNames holds the names of the attacks
            int[] atkCosts = new int[numOfAttacks];             //atkCosts holds the energy costs of the attacks
            int[] atkDmgs = new int[numOfAttacks];              //atkDmgs holds the damage values of the attacks
            String[] atkSpecials = new String[numOfAttacks];    //atkSpecials holds the specials of the attacks
            //This loop adds all the individual attack information of each attack
            for(int j = 0; j < numOfAttacks; j++){
                atkNames[j] = pkmInfo[6+j*4];                   //This is the attack name
                atkCosts[j] = Integer.valueOf(pkmInfo[7+j*4]);  //This is the attack cost
                atkDmgs[j] = Integer.valueOf(pkmInfo[8+j*4]);   //This is the attack damage
                atkSpecials[j] = pkmInfo[9+j*4];                //This is the attack special
            }

            //This creates a new pokemon object and adds it into the "pokedex" arraylist
            pokedex.add(new pokemon(pkmName, pkmHealth, 50, pkmType, pkmResist, pkmWeak, numOfAttacks, atkNames, atkCosts, atkDmgs, atkSpecials));
        }

        return pokedex;
    }

    //This method outputs all the information in the "pokedex" arraylist
    static void showPokedex(ArrayList<pokemon> pokedex){
        for(int i = 1; i < pokedex.size()+1; i++){
            System.out.printf("%-4s%-20s%-20s%-20s%-20s\n", i +": ", pokedex.get(i-1).getName(), "Type: "+ pokedex.get(i-1).getType(), "Health: "+ pokedex.get(i-1).getHealth(), "Energy: "+ pokedex.get(i-1).getEnergy());
            System.out.println("Attacks:");
            pokedex.get(i-1).showAttackData();
            System.out.println(); //This is for formatting
        }
    }

    //This method outputs the information for one entry in the "pokedex" arraylist
    static void showPokemon(ArrayList<pokemon> pokedex, int pkmNum){
        System.out.printf("%-4s%-12s%-12s%-20s%-20s\n", pkmNum +": ", pokedex.get(pkmNum-1).getName(), "Health: "+ pokedex.get(pkmNum-1).getHealth(), "Type: "+ pokedex.get(pkmNum-1).getType(), "Energy: "+ pokedex.get(pkmNum-1).getEnergy());
        System.out.println("Attacks:");
        pokedex.get(pkmNum-1).showAttackData();
        System.out.println(); //This is for formatting
    }

    //This method operates the player turn
    static void playerMove(ArrayList<pokemon> playerTeam, ArrayList<pokemon> opponents){
        Scanner in = new Scanner(System.in);                    //This gets input

        pokemon playerPkm = playerTeam.get(playerPkmIndex);     //playerPkm holds the current player pokemon
        pokemon opponentPkm = opponents.get(oppPkmIndex);       //opponentPkm holds the current opponent pokemon

        System.out.println("Enemy Pokemon:");
        System.out.printf("%-12s%-15s%-15s%-15s%n", opponentPkm.getName(), "Type: " + opponentPkm.getType(), "Health: "+ opponentPkm.getHealth(), "Energy: "+ opponentPkm.getEnergy());
        System.out.println("Your Pokemon:");
        System.out.printf("%-12s%-15s%-15s%-15s%n", playerPkm.getName(), "Type: " + playerPkm.getType(), "Health: "+ playerPkm.getHealth(), "Energy: "+ playerPkm.getEnergy() + "\n");

        //This if/else statement determines whether or not the player pokemon is defeated or not
        if(!playerPkm.faint()){
            while(true){
                System.out.println("What will "+ playerPkm.getName() +" do?");
                System.out.println("1. Attack\n2. Retreat\n3. Pass");

                String action = in.nextLine();  //This gets input

                //These if/else statements determine what attack the player has chosen
                if(action.equalsIgnoreCase("attack") || action.equalsIgnoreCase("1")){
                    int atkIndex = -1;  //atkIndex holds the attack the player wants their pokemon to preform
                                        //atkIndex is set to -1 to check if the input was valid or not later

                    while(true){
                        System.out.println("What will " + playerPkm.getName() + " do?");
                        for(int i = 0; i < playerPkm.getAttacks().size(); i++){
                            attack atk = playerPkm.getAttacks().get(i);
                            System.out.printf((i+1) + ". %-15sDamage: %-3dSpecial: %-15sCost: %-3d\n", atk.getAtkName(), atk.getAtkDMG(), atk.getAtkSpecial(), atk.getAtkCost());
                        }

                        String move = in.nextLine();    //This gets input
                        System.out.println(); //This is for formatting

                        //This try/catch statement differentiates between integer and string input
                        try{
                            atkIndex = Integer.valueOf(move)-1;
                        }
                        catch (Exception e){
                            for(int i = 0; i < playerPkm.getAttacks().size(); i++){
                                if(playerPkm.getAttacks().get(i).getAtkName().equalsIgnoreCase(move)){
                                    atkIndex = i;
                                }
                            }
                        }

                        //These if/else statements determine whther or not the pokemon can execute the attack
                        if(atkIndex < 0 || atkIndex > playerPkm.getAttacks().size()){
                            System.out.println("That is not a valid attack!");
                        }
                        else if(playerPkm.getEnergy() - playerPkm.getAttack(atkIndex).getAtkCost() < 0){
                            System.out.println(playerPkm.getName() + " is too tired to do this attack!");
                        }
                        else{
                            System.out.println(playerPkm.getName() + " used " + playerPkm.getAttack(atkIndex).getAtkName());
                            playerPkm.attack(opponentPkm, playerPkm.getAttack(atkIndex));
                            break;
                        }
                    }
                    break;
                }
                else if(action.equalsIgnoreCase("retreat") || action.equalsIgnoreCase("2")){
                    int newPkmIndex = -1;   //newPkmIndex holds the index of the pokemon the player wants to switch to
                                            //newPkmIndex is set to -1 to check if the input was valid or not

                    //This while loop "waits" for the user to enter a valid input
                    while(true){
                        System.out.println("Which Pokemon will you switch to?");
                        for(int i = 0; i < playerTeam.size(); i++){
                            System.out.println((i+1) + ". " + playerTeam.get(i).getName());
                        }

                        String newPkm = in.nextLine();  //This gets input

                        //This try/catch statement differentiates between integer and string input
                        try{
                            newPkmIndex = Integer.valueOf(newPkm) - 1;
                        }
                        catch(Exception e){
                            for(int i = 0; i < playerTeam.size(); i++){
                                if(playerTeam.get(i).getName().equalsIgnoreCase(newPkm)){
                                    newPkmIndex = i;
                                }
                            }
                        }

                        //These if/else statements determine whether or not the input was a valid pokemon
                        if(newPkmIndex < 0){
                            System.out.println("That is not a valid Pokemon!");
                        }
                        else if(playerTeam.get(newPkmIndex).faint()){
                            System.out.println("That Pokemon has fainted!");
                        }
                        else{
                            playerPkmIndex = newPkmIndex;
                            System.out.println(playerTeam.get(playerPkmIndex).getName() + " I choose you!");
                            break;
                        }
                    }
                    break;
                }
                else if(action.equalsIgnoreCase("pass") || action.equalsIgnoreCase("3")){
                    System.out.println(playerPkm.getName() + " rested!");
                    break;
                }
                else{
                    System.out.println("Invalid command.");
                }
            }
        }
        else{
            int newPkmIndex = -1;   //newPkmIndex holds the index of the new pokemon the player wants to switch to
                                    //newPkmIndex is set to -1 to check if the input was valid or not later

            System.out.println(playerPkm.getName() + " fainted!");

            //This while loop "waits" until the player enters a valid input
            while(true){

                //This checks if all the player's pokemon are fainted
                    //If yes, it breaks the loop
                    //If no, it continues
                boolean defeated = true;    //defeated holds whether or not all of the player's pokemon have been defeated
                for(pokemon pkm : playerTeam){
                    if(!pkm.faint()){
                        defeated = false;
                    }
                }
                if(defeated){
                    break;
                }

                System.out.println("Which Pokemon will you switch to?");
                for(int i = 0; i < playerTeam.size(); i++){
                    System.out.println((i+1) + ". " + playerTeam.get(i).getName());
                }

                String newPkm = in.nextLine();  //This gets input

                //This try/catch statement differentiates between integer and string input
                try{
                    newPkmIndex = Integer.valueOf(newPkm)-1;
                    playerTeam.get(newPkmIndex);
                }
                catch(Exception e){
                    for(int i = 0; i < playerTeam.size(); i++){
                        if(playerTeam.get(i).getName().equalsIgnoreCase(newPkm)){
                            newPkmIndex = i;
                        }
                    }
                }

                //This if/else statement determines whether or not the pokemon the play chose is valid or not
                if(newPkmIndex < 0){
                    System.out.println("That is not a valid Pokemon!");
                }
                else if(playerTeam.get(newPkmIndex).faint()){
                    System.out.println("That Pokemon has fainted!");
                }
                else if(newPkmIndex == playerTeam.indexOf(playerPkm)){
                    System.out.println("That Pokemon is already on the field!");
                }
                else{
                    playerPkmIndex = newPkmIndex;
                    System.out.println(playerTeam.get(playerPkmIndex).getName() + " I choose you!");

                    //Each time a pokemon is defeated:
                    //If the player's pokemon are disabled they are "un-disabled"
                    //Each player's pokemon that have not been defeated is healed 20 hp points
                    //Each player's pokemon that have not been defeated regains all their energy
                    for(pokemon pkm : playerTeam){
                        if(!pkm.faint()){
                            pkm.changeStatus("undisable");
                            pkm.addHealth(20);
                            pkm.addEnergy(50);
                        }
                    }

                    break;
                }
            }
        }
    }

    //This method handles the opponent's turn
    static void oppAtk(ArrayList<pokemon> playerTeam, ArrayList<pokemon> opponents){
        Random rand = new Random();                                     //rand is a random number generator

        pokemon playerPkm = playerTeam.get(playerPkmIndex);             //playerPkm holds the current player pokemon
        pokemon opponentPkm = opponents.get(oppPkmIndex);               //opponentPkm holds the current opponent pokemon
        ArrayList<Integer> invalidAttacks = new ArrayList<Integer>();   //invalid attacks holds the attacks that cannot be preformed by the opponent's pokemon

        //This if/else statement determines if the opponent pokemon has been defeated or not
        if(!opponentPkm.faint()){
            //This while loop "waits" for the opponent to pick a random attack that is executable
                //If no attacks are executable, the opponent passes
            while(true){
                int attack = rand.nextInt(opponentPkm.getAttacks().size()); //attack holds the randomized attack index
                int remainingEnergy = opponentPkm.getEnergy() - opponentPkm.getAttacks().get(attack).getAtkCost();  //remaining energy holds the ammount of energy that would be remaining after the attack

                //This if/else statement determines if the opponont's pokemon has enough energy to preform the attack
                if(!(remainingEnergy < 0)){
                    System.out.println("Enemy " + opponentPkm.getName() + " used " + opponentPkm.getAttacks().get(attack).getAtkName());
                    //This preforms the attack
                    opponentPkm.attack(playerPkm, opponentPkm.getAttacks().get(attack));
                    //This clears the "invalidAttacks" arraylist because the attacks will have to be re-evaluated after every attack
                    invalidAttacks.clear();
                    break;
                }
                else{
                    invalidAttacks.add(attack);
                }

                //This if statement forces the opponent to pass if there are no valid attacks
                if(invalidAttacks.size() == opponentPkm.getAttacks().size()){
                    System.out.println("Enemy " + opponentPkm.getName() + " rested!");
                    break;
                }
            }
        }
        else if(opponentPkm.faint()){
            while(true){

                //This checks if the opponent has any undefeated pokemon
                boolean defeated = true;
                for(pokemon pkm : opponents){
                    if(!pkm.faint()){
                        defeated = false;
                    }
                }
                if(defeated){
                    break;
                }

                int newOppPkm = rand.nextInt(opponents.size()); //newOppPkm holds the new pokemon index
                //This if statement determines if the pokemon is fainted or not
                if(!opponents.get(newOppPkm).faint()){
                    System.out.println("Enemy " + opponentPkm.getName() + " has fainted!");
                    System.out.println("Enemy trainer sent out " + opponents.get(newOppPkm).getName() + "!");

                    oppPkmIndex = newOppPkm;

                    //Each time a pokemon is defeated:
                    //If the player's pokemon are disabled they are "un-disabled"
                    //Each player's pokemon that have not been defeated is healed 20 hp points
                    //Each player's pokemon that have not been defeated regains all their energy
                    for(pokemon pkm : playerTeam){
                        if(!pkm.faint()){
                            pkm.changeStatus("undisable");
                            pkm.addHealth(20);
                            pkm.addEnergy(50);
                        }
                    }

                    break;
                }
            }
        }
    }
}
