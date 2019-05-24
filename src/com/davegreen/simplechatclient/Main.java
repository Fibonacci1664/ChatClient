/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.davegreen.simplechatclient;

/**
 *
 * @author Dave
 */
public class Main
{

    /**
     * @param args the command line arguments
     */
    
//    // Run this from the client side
//    public static void main(String[] args)
//    {
//        SimpleChatClientA scc = new SimpleChatClientA();
//        
//        scc.buildGUI();
//    }
    
    // Run this from the sever side
    public static void main(String[] args)
    {
        SimpleChatServer scs = new SimpleChatServer();
        
        scs.go();
    }
}
