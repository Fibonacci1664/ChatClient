/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.davegreen.simplechatclient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Dave
 */
public class SimpleChatServer
{
    private ArrayList clientOutputStreams;
    
    public class ClientHandler implements Runnable
    {
        BufferedReader reader;
        Socket sock;
        
        public ClientHandler(Socket clientSocket)
        {
            try
            {
                sock = clientSocket;
                InputStreamReader isr = new InputStreamReader(sock.getInputStream());
                reader = new BufferedReader(isr);
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }
                
        @Override
        public void run()
        {
            String message;
            
            try
            {
                while((message = reader.readLine()) != null)
                {
                    System.out.println("read " + message);
                    tellEveryone(message);
                }
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }
        
        // Close inner class.
    }
    
    public void go()
    {
        clientOutputStreams = new ArrayList();
        
        try
        {
            ServerSocket serverSock = new ServerSocket(5000);
            
            while(true)
            {
                Socket clientSocket = serverSock.accept();
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
                clientOutputStreams.add(writer);
                
                Thread t = new Thread(new ClientHandler(clientSocket));
                t.start();
                System.out.println("You've got a connection!");                
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    
    public void tellEveryone(String message)
    {
        Iterator it = clientOutputStreams.iterator();
        
        while(it.hasNext())
        {
            try
            {
                PrintWriter writer = (PrintWriter) it.next();
                writer.println(message);
                writer.flush();
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }
}
