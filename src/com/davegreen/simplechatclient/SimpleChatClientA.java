/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.davegreen.simplechatclient;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

/**
 *
 * @author Dave
 */
public class SimpleChatClientA
{
    private JTextArea incoming;
    private BufferedReader reader;
    private JTextField outgoing;
    private JScrollPane qScroller;
    private PrintWriter writer;
    private Socket clientSock;
    private JFrame frame;
    private JPanel panel;
    private JButton button;
    
    public SimpleChatClientA()
    {
        frame = new JFrame();
        panel = new JPanel();
        button = new JButton();
        outgoing = new JTextField(25);
        incoming = new JTextArea(15, 50);
        qScroller = new JScrollPane(incoming);
    }
    
    public void buildGUI()
    {
        button.setPreferredSize(new Dimension(80, 25));
        button.setText("SEND!");
        button.setVisible(true);
        button.addActionListener(new SendButtonListener());
        
        outgoing.setPreferredSize(new Dimension(0, 25));
        outgoing.setVisible(true);
        
        incoming.setLineWrap(true);
        incoming.setWrapStyleWord(true);
        incoming.setEditable(false);
        
        qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        
        panel.setPreferredSize(new Dimension(400, 200));
        
        panel.add(outgoing);
        panel.add(button);
        panel.add(qScroller);
        
        frame.setSize(400, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setTitle("CHAT CLIENT");
        frame.getContentPane().add(BorderLayout.CENTER, panel);
        frame.setVisible(true);
        
        frame.requestFocus();
        
        setUpNetworking();
        
        Thread readerThread = new Thread(new IncomingReader());
        readerThread.start();
    }
    
    private void setUpNetworking()
    {
        try
        {
            clientSock = new Socket("127.0.0.1", 5000);
            InputStreamReader isr = new InputStreamReader(clientSock.getInputStream());
            reader = new BufferedReader(isr);
            writer = new PrintWriter(clientSock.getOutputStream());
            
            System.out.println("networking established!");
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }
    
    public class SendButtonListener implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent e)
        {
            try
            {
                writer.println(outgoing.getText());
                writer.flush();
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
            
            outgoing.setText("");
            outgoing.requestFocus();
        }
    }
    
    public class IncomingReader implements Runnable
    {

        @Override
        public void run()
        {
            String message;
            
            try
            {
                while((message = reader.readLine()) != null)
                {
                    System.out.println("Read " + message);
                    incoming.append(message + "\n");
                }
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }
}
