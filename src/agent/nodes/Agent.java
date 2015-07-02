/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agent.nodes;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author jorgecaballero
 */
public class Agent {

    public boolean inGame;
    protected int[][] matrix;
    protected char id;
    protected int[] quadrant;
    protected int[] secondBest;
    protected int quantum;
    protected boolean status;
    protected int secondHighest;
    protected ArrayList usedQuadrants;
    protected int lastQuadrant;
    ArrayList<Integer> elements;

    public Agent(char id, int[][] matrix) {
        this.matrix = matrix;
        this.id = id;
        this.quadrant = new int[2];
        this.secondBest = new int[2];
        this.inGame = true;
        this.quantum = 1;
        this.usedQuadrants = new ArrayList();
        this.lastQuadrant = 2;
        this.elements = new ArrayList<>();
    }
    
    public void printMatrix() {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                System.out.printf("%d\t", matrix[i][j]);
            }
            System.out.println();
        }
    }
    
    public void findBestQuadrant() {
        
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                elements.add(matrix[i][j]); 
                if (matrix[i][j] > matrix[quadrant[0]][quadrant[1]]) {
                    quadrant[0] = i;
                    quadrant[1] = j;
                }                            
            }
        }
        
        Collections.sort(elements);
        secondHighest = elements.get(lastQuadrant);
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                if (matrix[i][j] == secondHighest) {
                    secondBest[0] = i;
                    secondBest[1] = j;
                }
            }
        }
        System.out.printf("Best quadrant for %c is [%d][%d] = %d , second best is [%d][%d] = %d\n",id, quadrant[0],quadrant[1],matrix[quadrant[0]][quadrant[1]], secondBest[0],secondBest[1],secondHighest);
    }
    
    public void planB(){
        try {
            System.out.println(id + ": Entering Plan B");
            lastQuadrant--;
            if (lastQuadrant != -1) {
                if (!usedQuadrants.contains(quadrant)) {
                    usedQuadrants.add(quadrant);
                    quadrant[0] = secondBest[0];
                    quadrant[1] = secondBest[1];
                    secondHighest = elements.get(lastQuadrant);
                    //System.out.printf("%c: quadrant[%d][%d] = %d\n",id, quadrant[0],quadrant[1],matrix[quadrant[0]][quadrant[1]]);
                    for (int i = 0; i < 2; i++) {
                        for (int j = 0; j < 2; j++) {
                            if (matrix[i][j] == secondHighest) {
                                secondBest[0] = i;
                                secondBest[1] = j;
                            }
                        }
                    }
                    quantum = 0;
                    System.out.printf("%c: Highest: %d, Second Best: %d\n", id, matrix[quadrant[0]][quadrant[1]], matrix[secondBest[0]][secondBest[1]]);
                } else {
                    System.err.println("Out of quadrants");
                    System.exit(0);
                }
            } 
            
        } catch(Exception e) {
            System.out.println("ERR planB");
            e.printStackTrace();
        }
        
    }
    
    public ArrayList sendProposal() {
        ArrayList retVal = new ArrayList();
        //Check if quadrant-quantum is not less than any other quadrant
        System.out.printf(id+": Checking if matrix - quantum >= second best for %c\n", id);
        if (matrix[quadrant[0]][quadrant[1]] - quantum >= matrix[secondBest[0]][secondBest[1]]) {
            System.out.printf("%c: true (%d - %d = %d) >= (%d), creating proposal. Quantum in: ", id, matrix[quadrant[0]][quadrant[1]], quantum,matrix[quadrant[0]][quadrant[1]] - quantum, matrix[secondBest[0]][secondBest[1]]);
            //Esta bien, mandar prop
            retVal.add(quadrant[0]); //0
            retVal.add(quadrant[1]); //1
            retVal.add(quantum);     //2
            System.out.println(quantum);
            quantum++;
            inGame = true;
        } else {
            System.out.printf("%c: false, moving quadrant\n", id);
            planB();
            inGame = false;
            if (matrix[quadrant[0]][quadrant[1]] - quantum >= matrix[secondBest[0]][secondBest[1]]) {
                //Esta bien, mandar prop
                retVal.add(quadrant[0]); //0
                retVal.add(quadrant[1]); //1
                retVal.add(quantum);     //2
            }
        }
        return retVal;
    }

    public boolean receiveProposal(ArrayList answer) {
        //Check if its the same quadrant
        try {
            if (!answer.isEmpty()) {
                if (((int)answer.get(0) == quadrant[0]) && ((int)answer.get(1) == quadrant[1])) {
                    System.out.printf("%c: ACCEPT, Same quadrant achieved. [%d][%d]\n===================================\n",id,quadrant[0],quadrant[1]);
                    inGame = false;
                    return true;
                } else {
                    //Check proposal
                    //If Quantum + the proposed quadrant is > the secondHighest quadrant accept
                    if (matrix[(Integer)answer.get(0)][(Integer)answer.get(1)] + (Integer)answer.get(2) > matrix[quadrant[0]][quadrant[1]]) {
                        System.out.printf("%c: ACCEPT with %d\n",id,matrix[(Integer)answer.get(0)][(Integer)answer.get(1)] + (Integer)answer.get(2));
                        // FUCK YEAH, aceptamos
                        inGame = false;
                        return true;
                    } else {
                        // NI POR PUTAS, deny
                        System.err.printf("%c: DENIED, Not working for %c. %d is not higer than %d\n ",id,id,matrix[(Integer)answer.get(0)][(Integer)answer.get(1)] + (Integer)answer.get(2),matrix[quadrant[0]][quadrant[1]]);
                        inGame = true;
                        return false;
                    }
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return false;
        }
    }

    public String sendWarranty() {
        return "Hola";
    }

    public void receiveWarranty() {

    }

}
