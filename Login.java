/****************************************************************
*  
*  
*  Description
*  This is a Login frame of client side application in chat System.
*  It is used to just take the user name 
*  
*  Remarks
*  Before running the Login application make sure the server is
*  running.
*  If server is running then only you can execute your application.
******************************************************************/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

// Login class which takes a user name and passed it to client class
public class Login implements ActionListener{
  JFrame frame1;
  JTextField tf;
  JButton button;
  JLabel heading;
  JLabel label;
  public static void main(String[] args){
  new Login();
  }
  public Login(){
  frame1 = new JFrame("Login Page");
  tf=new JTextField();
  button=new JButton("Login");
  heading=new JLabel("Chat Server");
  heading.setFont(new Font("Impact", Font.BOLD,40));
  label=new JLabel("Enter you Login Name");
  label.setFont(new Font("Serif", Font.PLAIN, 24));
  JPanel panel = new JPanel();
  button.addActionListener(this);
  panel.add(heading);panel.add(tf);panel.add(label);
  panel.add(button);
  heading.setBounds(30,20,280,80);
  label.setBounds(20,100,250,60);
  tf.setBounds(50,150,150,30);
  button.setBounds(70,200,90,30);
  frame1.add(panel);
  panel.setLayout(null);
  frame1.setSize(300, 300);
  frame1.setVisible(true);
  frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }
  // pass the user name to MyClient class
  public void actionPerformed(ActionEvent e){
  String name="";
  try{
  name=tf.getText();
  frame1.dispose();
  MyClient mc=new MyClient(name);
  }catch (IOException te){}
  }
}