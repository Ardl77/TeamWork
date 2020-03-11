package TeamWork;
//server
import java.awt.Color;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;


public class GameServer extends JFrame implements Runnable {
    private Socket s=null;
    private ServerSocket ss=null;
    private ArrayList<ChatThread> clients=new ArrayList<ChatThread>();

    public GameServer()throws Exception{
        this.setTitle("GameServer");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBackground(Color.yellow);
        this.setSize(200, 100);
        this.setVisible(true);
        ss=new ServerSocket(9999);// creat a port
        new Thread(this).start();// start thread loop
    }

    // override of the run function
    // This thread is used to receive the thread waiting for the client to continuously connect
    public void run() {
        try {
            while(true) {
                s = ss.accept();//waiting for connection
                ChatThread ct = new ChatThread(s);// When a client connects, create a thread for this client
                clients.add(ct);// and add this thread to the thread array
                ct.start();// Start the thread of this thread, and then communication can be realized
            }
        }catch(Exception ex) {
            ex.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this,
                    "The game exits abnormally!");
            System.exit(0);
        }

    }

   // This thread is used to receive communication between the server and a client (for the server)
    class ChatThread extends Thread{
        private Socket s=null;
        private BufferedReader br=null;
        private PrintStream ps=null;
        private boolean canRun=true;

// Using threads for input and output (communication)
        public ChatThread(Socket s)throws Exception
        {
            this.s=s;
            br=new BufferedReader(
                    new InputStreamReader(s.getInputStream()));
            ps=new PrintStream(s.getOutputStream());
        }

       // Transfer the information obtained from customers to other customers
        public void run() {
            try {
                while(canRun) {
                    String str=br.readLine();// Read the information from the Socket
                    System.out.println(str);
                    sendMessage(str);

                }
            }catch (Exception ex) {
                canRun=false;
                clients.remove(this);//Remove this thread from the client's array

            }
        }
    }
    //Send information to other clients to achieve communication between clients
    public void sendMessage(String msg) {
        for(ChatThread ct: clients) {
            ct.ps.println(msg);
        }
    }

    public static void main(String[] args) throws Exception {
        GameServer server=new GameServer();
    }
}