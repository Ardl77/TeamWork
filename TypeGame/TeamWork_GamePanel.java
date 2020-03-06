package TeamWork;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.util.Random;
import javax.swing.*;

public class GamePanel extends JPanel implements ActionListener,KeyListener,Runnable{
    private int life = 10;
    private char keyChar;
    private JLabel lbMoveChar=new JLabel();
    private JLabel lbLife=new JLabel();
    private Socket s=null;
    private Timer timer=new Timer(100,this);
    private Random rnd=new Random();
    private BufferedReader br=null;
    private PrintStream ps=null;
    private boolean canRun=true;

    public GamePanel() {

        this.setLayout(null);
        this.setBackground(Color.DARK_GRAY);
        this.setSize(240,320);


        this.add(lbLife);
        lbLife.setFont(new Font("",Font.BOLD,20));
        lbLife.setBackground(Color.YELLOW);
        lbLife.setForeground(Color.PINK);
        lbLife.setBounds(0,0,this.getWidth(),20);


        this.add(lbMoveChar);
        lbMoveChar.setFont(new Font("",Font.BOLD,20));
        lbMoveChar.setForeground(Color.YELLOW);
        this.init();
        this.addKeyListener(this);

        try {
            s=new Socket("127.0.0.1",9999);
            JOptionPane.showMessageDialog(this, "Successful");
            InputStream is=s.getInputStream();
            br=new BufferedReader(new InputStreamReader(is));
            OutputStream os=s.getOutputStream();
            ps=new PrintStream(os);
            new Thread(this).start();

        }catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(this, "Game exits abnormally！");
            System.exit(0);
        }
        timer.start();
    }


    // Implement the random start position of the dropped letters
    public void init() {
        lbLife.setText("life"+life);
        String str=String.valueOf((char)('A'+rnd.nextInt(26)));
        lbMoveChar.setText(str);
        lbMoveChar.setBounds(rnd.nextInt(this.getWidth()),0, 20, 20);
    }

    public void run() {
        try {
            while(canRun) {
                String str=br.readLine();
                int score=Integer.parseInt(str);
                life+=score;
                checkFail();
            }
        }catch(Exception ex) {
            canRun=false;
            javax.swing.JOptionPane.showMessageDialog(this, "The game exits abnormally!");
            System.exit(0);
        }

    }


    // Timer to control the whereabouts of the moving letters, this operation is performed every 100ms
    public void actionPerformed(ActionEvent e) {
        if (lbMoveChar.getY() >= this.getHeight()) {
            life--;
            checkFail();
        }
        try {

            if (life >10) {
                lbMoveChar.setLocation(lbMoveChar.getX(), lbMoveChar.getY() + 15);
            }
            if (life >= 20) {
                lbMoveChar.setLocation(lbMoveChar.getX(), lbMoveChar.getY() + 20);
            }
            if (life >= 100) {
                lbMoveChar.setLocation(lbMoveChar.getX(), lbMoveChar.getY() + 50);
            }else{lbMoveChar.setLocation(lbMoveChar.getX(), lbMoveChar.getY() + 10);}

        }catch (Exception e1){
            e1.printStackTrace();

        }
    }


        public void checkFail()
//Check if the health value is less than 0 and exit the game if it is less than 0
    {
        init();
        if(life<=0) {
            timer.stop();
            javax.swing.JOptionPane.showMessageDialog(this, "Game Over");
            System.exit(0);
        }
    }

    public void keyPressed(KeyEvent e) {
        keyChar=e.getKeyChar();
        String keyStr=String.valueOf(keyChar).toUpperCase();
        try {
            if(keyStr.equals(lbMoveChar.getText())) {
                life+=2;
                ps.println("-1");
            }else {
                life--;
            }
            checkFail();
        }catch(Exception ex) {
            ex.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this,
                    "The game exits abnormally!");
            System.exit(0);
        }
    }
    public void keyReleased(KeyEvent arg0) {}

    public void keyTyped(KeyEvent arg0) {}
}
