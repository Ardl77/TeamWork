package TeamWork;

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
        ss=new ServerSocket(9999);
        new Thread(this).start();
    }

    public void run() {
        try {
            while(true) {
                s = ss.accept();
                ChatThread ct = new ChatThread(s);
                clients.add(ct);
                ct.start();
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

        public ChatThread(Socket s)throws Exception
        {
            this.s=s;
            br=new BufferedReader(
                    new InputStreamReader(s.getInputStream()));
            ps=new PrintStream(s.getOutputStream());
        }

        public void run() {
            try {
                while(canRun) {
                    String str=br.readLine();
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