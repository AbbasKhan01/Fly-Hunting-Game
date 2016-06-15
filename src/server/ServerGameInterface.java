package server;

import java.awt.Canvas;
import java.awt.*;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.*;
import javax.imageio.*;
import java.awt.event.*;
import java.net.URL;
import java.awt.image.BufferStrategy;
import java.util.Random;
import java.util.ArrayList;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.JFrame;



public class ServerGameInterface  implements Runnable {

		final int WIDTH = 750;
		final int HEIGHT = 650;
				
		//Fly attributes
		private int fly_size_x = 50;
		private int fly_size_y = 60;		
		private int fly_pos_x = 0;
		private int fly_pos_y = 0;
		
		//Player Attributes
		private int score = 0;		
	
		private BufferedImage image;
			   
	   JFrame frame;
	   Canvas canvas;
	   BufferStrategy bufferStrategy;
	   
	   public ServerGameInterface(){
	      frame = new JFrame("Fly Hunting Game!");
	      
	      JPanel panel = (JPanel) frame.getContentPane();
	      panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
	      panel.setLayout(new FlowLayout());
	      
	      try {                
	          image = ImageIO.read(new File("C:/Users/Haroon Khaleeq/workspace/TK1_P1/src/client/fly1.png"));
	      } catch (IOException ex) {
	          // handle exception...
	      }
	      setRandomPosition();
	      
	      canvas = new Canvas();
	      canvas.setBounds(0, 0, WIDTH, HEIGHT);
	      canvas.setIgnoreRepaint(true);
	      	      
	      panel.add(canvas);	      	      
	      canvas.addMouseListener(new MouseControl());
	      
	      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	      frame.pack();
	      frame.setResizable(false);
	      frame.setVisible(true);
	      
	      canvas.createBufferStrategy(2);
	      bufferStrategy = canvas.getBufferStrategy();
	      
	      Toolkit toolkit = Toolkit.getDefaultToolkit();
	      Image image = toolkit.getImage("src/client/flap.png");
	      Cursor c = toolkit.createCustomCursor(image , new Point(panel.getX(), 
	    		  panel.getY()), "img");
	      panel.setCursor (c);     
	      
	      canvas.requestFocus();
	   }
	        
	   private class MouseControl implements MouseListener {
		    public void mousePressed(MouseEvent e) {}
		    public void mouseReleased(MouseEvent e) {}
		    public void mouseEntered(MouseEvent e) {}
		    public void mouseExited(MouseEvent e) {}
		    
		    public void mouseClicked(MouseEvent e) {
		    	int mouse_clicked_x = e.getX();
		        int mouse_clicked_y = e.getY();		        
		        boolean isFlyHunted = checkFlyIsHunted(mouse_clicked_x, mouse_clicked_y);
		        
		        if(isFlyHunted == true){		        	
		        	setRandomPosition();
		        	//System.out.println("Fly is Hunted!");
		        	score = score + 1;
		        	//System.out.println("Player Score: " + score);
		        }
		        else{
		        	//System.out.println("Fly is not Hunted!");
		        }
		        
		    }		    
	   }
	   
	   long desiredFPS = 60;
	   long desiredDeltaLoop = (1000*1000*1000)/desiredFPS;
	    
	   boolean running = true;
	   
	   public void run(){
	      
	      long beginLoopTime;
	      long endLoopTime;
	      long currentUpdateTime = System.nanoTime();
	      long lastUpdateTime;
	      long deltaLoop;
	      
	      while(running){
	         beginLoopTime = System.nanoTime();
	         
	         render();
	         
	         lastUpdateTime = currentUpdateTime;
	         currentUpdateTime = System.nanoTime();
	         update((int) ((currentUpdateTime - lastUpdateTime)/(1000*1000)));
	         
	         endLoopTime = System.nanoTime();
	         deltaLoop = endLoopTime - beginLoopTime;
	           
	           if(deltaLoop > desiredDeltaLoop){
	               //Do nothing. We are already late.
	           }else{
	               try{
	                   Thread.sleep((desiredDeltaLoop - deltaLoop)/(1000*1000));
	               }catch(InterruptedException e){
	                   //Do nothing
	               }
	           }
	      }
	   }
	   
	   private void render() {		   
	      Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
	      g.clearRect(0, 0, WIDTH, HEIGHT);
	      g.drawImage(image, fly_pos_x, fly_pos_y, null);	      
	      updateScore(g);     
	      g.dispose();
	      bufferStrategy.show();
	   }
	   
	   private double x = 0;
	   protected void update(int deltaTime){
	      x += deltaTime * 0.9;	      
	      while(x > 950){
	         x -= 950;
	      }
	   }
	   
	   protected void updateScore(Graphics2D g){
		   g.drawString("Your Score: " + score, 15, 15);
	   }
	
	   public void setRandomPosition(){		   
		   
		   Random randomGenerator = new Random();		   
		   int x = randomGenerator.nextInt(WIDTH-fly_size_x);
		   int y = randomGenerator.nextInt(HEIGHT-fly_size_y);
		   
		   fly_pos_x = x;
		   fly_pos_y = y;
	   }
	  	      
	   private boolean checkFlyIsHunted(int mouse_clicked_x, int mouse_clicked_y){
		   
		   if(((mouse_clicked_x >= fly_pos_x) && (mouse_clicked_x <= (fly_pos_x + fly_size_x)))
				  && ((mouse_clicked_y >= fly_pos_y) && (mouse_clicked_y <= (fly_pos_y + fly_size_y)))) {
			   //System.out.println("Fly is hunted.");
			   return true;
		   }
		   else{
			   //System.out.println("Fly is not hunted.");
			   return false;   
		   }	   
		   		   
	   }
	   
	   public static void main(String [] args){
		   ServerGameInterface ex = new ServerGameInterface();
	      new Thread(ex).start();
	   }
	   
}

