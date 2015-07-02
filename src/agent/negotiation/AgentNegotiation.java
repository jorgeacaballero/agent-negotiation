/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agent.negotiation;

import agent.nodes.Agent;

/**
 *
 * @author jorgecaballero
 */
public class AgentNegotiation {

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception{
        
        int[][] baseMatrixP = new int[2][2];
        int[][] baseMatrixQ = new int[2][2];
        
        baseMatrixP[0][0] = 20;
        baseMatrixP[0][1] = 15;
        baseMatrixP[1][0] = 24;
        baseMatrixP[1][1] = 30;
        
        baseMatrixQ[0][0] = 12;
        baseMatrixQ[0][1] = 20;
        baseMatrixQ[1][0] = 19;
        baseMatrixQ[1][1] = 10;
        
        Agent p;
        Agent q;
        p = new Agent('p', baseMatrixP);
        q = new Agent('q', baseMatrixQ);
        
        boolean run = true;
        int agent = 0;
        p.findBestQuadrant();
        p.printMatrix();
        q.findBestQuadrant();
        q.printMatrix();
        System.out.println("===================================");
        while(run){
            Thread.sleep(300);
            if (agent == 0) {
                //Agent P
                if (p.inGame) {
                    //Modo compensación
                    if (!q.receiveProposal(p.sendProposal())) {
                        agent = 1;
                    }
                } else {
                    //Modo garantia
                    //q.sendWarranty();
                    run = false;
                }
            } else {
                //Agent Q
                if (q.inGame) {
                    //Modo compensación
                    if (!p.receiveProposal(q.sendProposal())) {
                        agent = 0;
                    }
                } else {
                    //Modo garantia
                    //p.sendWarranty();
                    run = false;
                }
            }
        }
        System.exit(0);
    }
    
}
