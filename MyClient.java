/****************************************************************
*  
*  Description
*  This is a client side of chat application.
*  This application is used to sending and receiving the messages
*  and in this we can maintain the list of all online users
*  
*  Remarks
*  
******************************************************************/
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Iterator;

//create the GUI of the client side
public class MyClient extends WindowAdapter implements ActionListener{
  JFrame frame;
  JList list;
  JList list1;
  JTextField tf;
  DefaultListModel model;
  DefaultListModel model1;
  JButton button;
  JButton lout;
  JScrollPane scrollpane;
  JScrollPane scrollpane1;
  JLabel label;
  Socket s,s1,s2;
  DataInputStream din;
  DataOutputStream dout;
  DataOutputStream dlout;
  DataOutputStream dout1;
  DataInputStream din1;
  String name;
  
  MyClient(String name)throws IOException{
  frame = new JFrame("Client Side");
  tf=new JTextField();
  model=new DefaultListModel();
  model1=new DefaultListModel();
  label=new JLabel("Message");
  list=new JList(model);
  list1=new JList(model1);
  button=new JButton("Send");
  lout=new JButton("Logout");
  scrollpane=new JScrollPane(list);
  scrollpane1=new JScrollPane(list1);
  JPanel panel = new JPanel();
  button.addActionListener(this);
  lout.addActionListener(this);
  panel.add(tf);panel.add(button);panel.add(scrollpane);
  panel.add(label);panel.add(lout);
  panel.add(scrollpane1);
  scrollpane.setBounds(10,20,180,150);
  scrollpane1.setBounds(250,20,100,150);
  label.setBounds(20,180,80,30);
  tf.setBounds(100,180,140,30);
  button.setBounds(260,180,90,30);
  lout.setBounds(260,230,90,30);
  frame.add(panel);
  panel.setLayout(null);
  frame.setSize(400, 400);
 frame.setVisible(true);
  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  this.name=name;
  frame.addWindowListener(this);
  s=new Socket("localhost",1004);  //creates a socket object
  s1=new Socket("localhost",1004);
  s2=new Socket("localhost",1004);
  //create inputstream for a particular socket
  din=new DataInputStream(s.getInputStream());
  //create outputstream
  dout=new DataOutputStream(s.getOutputStream());
  //sending a message for login
  dout.writeUTF(name+" has Logged in");  
  dlout=new DataOutputStream(s1.getOutputStream());
  dout1=new DataOutputStream(s2.getOutputStream());
  din1=new DataInputStream(s2.getInputStream());

// creating a thread for maintaning the list of user name
  My1 m1=new My1(dout1,model1,name,din1);
  Thread t1=new Thread(m1);
  t1.start();  
  //creating a thread for receiving a messages
  My m=new My(din,model);
  Thread t=new Thread(m);
  t.start();
  }
  public void actionPerformed(ActionEvent e){
  // sending the messages
  if(e.getSource()==button){  
  String str="";
  str=tf.getText();
  tf.setText("");
  str=name+": > "+str;
  try{
  dout.writeUTF(str);
  System.out.println(str);
  dout.flush();
  }catch(IOException ae){System.out.println(ae);}
  }
  // client logout
  if (e.getSource()==lout){
  frame.dispose();
  try{
 //sending the message for logout
  dout.writeUTF(name+" has Logged out");
  dlout.writeUTF(name);
  dlout.flush();
  Thread.currentThread().sleep(1000);
  System.exit(1);
  }catch(Exception oe){}
  }
  }
  public void windowClosing(WindowEvent w){
  try{
  dlout.writeUTF(name);
  dlout.flush();  
  Thread.currentThread().sleep(1000);
  System.exit(1);
  }catch(Exception oe){}
  }
}

// class is used to maintaning the list of user name
class My1 implements Runnable{
  DataOutputStream dout1;
  DefaultListModel model1;  
  DataInputStream din1;
  String name,lname;
  ArrayList alname=new ArrayList(); //stores the list of user names
  ObjectInputStream obj; // read the list of user names
  int i=0;
  My1(DataOutputStream dout1,DefaultListModel model1,
   String name,DataInputStream din1){
  this.dout1=dout1;
  this.model1=model1;
  this.name=name;
  this.din1=din1;
  }
  public void run(){
  try{
  dout1.writeUTF(name);  // write the user name in output stream
  while(true){
  obj=new ObjectInputStream(din1);
  //read the list of user names
  alname=(ArrayList)obj.readObject(); 
  if(i>0)
  model1.clear(); 
  Iterator i1=alname.iterator();
  System.out.println(alname);
  while(i1.hasNext()){
  lname=(String)i1.next();
  i++;
 //add the user names in list box
  model1.addElement(lname);
  }
  }
  }catch(Exception oe){}
  }
}
//class is used to received the messages
class My implements Runnable{
  DataInputStream din;
  DefaultListModel model;
  My(DataInputStream din, DefaultListModel model){
  this.din=din;
  this.model=model;
  }
  public void run(){
  String str1="";
  while(true){
  try{
  str1=din.readUTF(); // receive the message
  // add the message in list box
  model.addElement(str1);
  }catch(Exception e){}
  }
  }
}