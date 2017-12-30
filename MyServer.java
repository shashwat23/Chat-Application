/****************************************************************
*  
*  Description
*  This is a Server Side application of Chat System.
*  This application is used for receiving the messages from any client
*  and send to each and every client and in this we can maintain the
*  list of all online users.
*
******************************************************************/

import java.io.*;
import java.net.*;
import java.util.*;

public class MyServer{
  ServerSocket ss;
  Socket s;
  ArrayList al=new ArrayList();
  ArrayList al1=new ArrayList();
  ArrayList al2=new ArrayList();
  ArrayList alname=new ArrayList();
  Socket s1,s2;
  MyServer()throws IOException{
  ss=new ServerSocket(1004);  // create server socket
  while(true){
  s=ss.accept();  //accept the client socket
  s1=ss.accept();
  s2=ss.accept();
  al.add(s);  // add the client socket in arraylist
  al1.add(s1);
  al2.add(s2);
  System.out.println("Client is Connected");//new thread for maintaning the list of user name
  MyThread2 m=new MyThread2(s2,al2,alname);    
  Thread t2=new Thread(m);
  t2.start();
//new thread for receive and sending the messages
  MyThread r=new MyThread(s,al);
  Thread t=new Thread(r);
  t.start();
  // new thread for update the list of user name
  MyThread1 my=new MyThread1(s1,al1,s,s2);     Thread t1=new Thread(my);
  t1.start();
  }
  }
  public static void main(String[] args){
  try{
  new MyServer();  
  }catch (IOException e){}
  }
}
//class is used to update the list of user name
class MyThread1 implements Runnable{
  Socket s1,s,s2;
  static ArrayList al1;
  DataInputStream ddin;
  String sname;
  MyThread1(Socket s1,ArrayList al1,Socket s,Socket s2){
  this.s1=s1;
  this.al1=al1;
  this.s=s;
  this.s2=s2;
  }
  public void run(){  
  try{
  ddin=new DataInputStream(s1.getInputStream());
  while(true){
  sname=ddin.readUTF();
  System.out.println("Exit  :"+sname);//remove the logout user name from arraylist
  MyThread2.alname.remove(sname);   MyThread2.every();
  al1.remove(s1);
  MyThread.al.remove(s);
  MyThread2.al2.remove(s2);
  if(al1.isEmpty())
  System.exit(0); //all client has been logout
  }
  }catch(Exception ie){}
  }
}

// class is used to maintain the list of all online users
class MyThread2 implements Runnable{
  Socket s2;
  static ArrayList al2;
  static ArrayList alname;
  static DataInputStream din1;  
  static DataOutputStream dout1;

  MyThread2(Socket s2,ArrayList al2,ArrayList alname){
  this.s2=s2;
  this.al2=al2;
  this.alname=alname;
  }
  public void run(){
  try{
  din1= new DataInputStream(s2.getInputStream());// store the user name in arraylist
  alname.add(din1.readUTF());
  every();
  }catch(Exception oe){}
  }
  // send the list of user name to all client
  static void every()throws Exception{
  Iterator i1=al2.iterator();
  Socket st1;  

  while(i1.hasNext()){
  st1=(Socket)i1.next();
  dout1=new DataOutputStream(st1.getOutputStream());
  ObjectOutputStream obj=new ObjectOutputStream(dout1);
//write the list of users in stream of all clients  obj.writeObject(alname); 
  dout1.flush();
  obj.flush();
  }
  }
}
//class is used to receive the message and //send it to all clients
class MyThread implements Runnable{
  Socket s;
  static ArrayList al;
  DataInputStream din;
  DataOutputStream dout;

  MyThread(Socket s, ArrayList al){
  this.s=s;
  this.al=al;
  }
  public void run(){
  String str;
  int i=1;
  try{
  din=new DataInputStream(s.getInputStream());
  }catch(Exception e){}
  
  while(i==1){
  try{
  
  str=din.readUTF(); //read the message
  distribute(str);
  }catch (IOException e){}
  }
  }
  // send it to all clients
  public void distribute(String str)throws IOException{
  Iterator i=al.iterator();
  Socket st;
  while(i.hasNext()){
  st=(Socket)i.next();
  dout=new DataOutputStream(st.getOutputStream());
  dout.writeUTF(str);
  dout.flush();
  }
  }
}

