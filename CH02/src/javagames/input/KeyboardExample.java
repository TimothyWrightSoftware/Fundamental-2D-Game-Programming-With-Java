package javagames.input;

import java.awt.event.*;
import javax.swing.*;
import javagames.util.*;


public class KeyboardExample extends JFrame implements Runnable {
   
   private volatile boolean running;
   private Thread gameThread;
   private KeyboardInput keys;
   
   public KeyboardExample() {
      keys = new KeyboardInput();
   }
   
   protected void createAndShowGUI() {
      
      setTitle( "Keyboard Input" );
      setSize( 320, 240 );
      addKeyListener( keys );
      setVisible( true );
      
      gameThread = new Thread( this );
      gameThread.start();
   }
   
   public void run() {
      running = true;
      while( running ) {
         gameLoop();
      }
   }
   
   public void gameLoop() {
      keys.poll();
      if( keys.keyDownOnce( KeyEvent.VK_SPACE ) ) {
         System.out.println( "VK_SPACE" );
      }
      if( keys.keyDown( KeyEvent.VK_UP ) ) {
         System.out.println( "VK_UP" );
      }
      if( keys.keyDown( KeyEvent.VK_DOWN ) ) {
         System.out.println( "VK_DOWN" );
      }
      if( keys.keyDown( KeyEvent.VK_LEFT ) ) {
         System.out.println( "VK_LEFT" );
      }
      if( keys.keyDown( KeyEvent.VK_RIGHT ) ) {
         System.out.println( "VK_RIGHT" );
      }
      try {
         Thread.sleep( 10 );
      } catch( InterruptedException ex ) { }
   }
   
   protected void onWindowClosing() {
      try {
         running = false;
         gameThread.join();
      } catch( InterruptedException e ) {
         e.printStackTrace();
      }
      System.exit( 0 );
   }
   
   public static void main( String[] args ) {
      final KeyboardExample app = new KeyboardExample();
      app.addWindowListener( new WindowAdapter() {
         public void windowClosing( WindowEvent e ) {
            app.onWindowClosing();
         }
      });
      SwingUtilities.invokeLater( new Runnable() {
         public void run() {
            app.createAndShowGUI();
         }
      });
   }

}
