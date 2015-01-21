package javagames.render;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import javagames.util.*;


public class FullScreenRenderingExample extends JFrame implements Runnable {
   
   private FrameRate frameRate;
   private BufferStrategy bs;
   private volatile boolean running;
   private Thread gameThread;
   private GraphicsDevice graphicsDevice;
   private DisplayMode currentDisplayMode;

   public FullScreenRenderingExample() {
      frameRate = new FrameRate();
   }
   
   protected void createAndShowGUI() {
      
      setIgnoreRepaint( true );
      setUndecorated( true );
      setBackground( Color.BLACK );
      
      GraphicsEnvironment ge = 
         GraphicsEnvironment.getLocalGraphicsEnvironment();
      graphicsDevice = ge.getDefaultScreenDevice();
      currentDisplayMode = graphicsDevice.getDisplayMode();
      if( !graphicsDevice.isFullScreenSupported() ) {
         System.err.println( "ERROR: Not Supported!!!" );
         System.exit( 0 );
      }
      
      graphicsDevice.setFullScreenWindow( this );
      graphicsDevice.setDisplayMode( getDisplayMode() );
      
      createBufferStrategy( 2 );
      bs = getBufferStrategy();
      
      addKeyListener( new KeyAdapter() {
         public void keyPressed( KeyEvent e ) {
            if( e.getKeyCode() == KeyEvent.VK_ESCAPE ) {
               shutDown();
            }
         }
      });
      
      gameThread = new Thread( this );
      gameThread.start();
   }
   
   private DisplayMode getDisplayMode() {
	   // Make sure to use a display mode for your system.
	   // The DisplayMode.REFRESH_RATE_UNKNOWN and
	   // DisplayMode.BIT_DEPTH_MULTI flags may be required.
      return new DisplayMode( 
         800, 600, 32, DisplayMode.REFRESH_RATE_UNKNOWN );
   }
   
   public void run() {
      running = true;
      frameRate.initialize();
      while( running ) {
         gameLoop();
      }
   }
   
   public void gameLoop() {
      do {
         do {
            Graphics g = null;
            try {
               g = bs.getDrawGraphics();
               g.clearRect( 0, 0, getWidth(), getHeight() );
               render( g );
            } finally {
               if( g != null ) {
                  g.dispose();
               }
            }
         } while( bs.contentsRestored() );
         bs.show();
      } while( bs.contentsLost() );
   }
   
   private void render( Graphics g ) {
      frameRate.calculate();
      g.setColor( Color.GREEN );
      g.drawString( frameRate.getFrameRate(), 30, 30 );
      g.drawString( "Press ESC to exit...", 30, 60 );
   }
   
   protected void shutDown() {
      try {
         running = false;
         gameThread.join();
         System.out.println( "Game loop stopped!!!" );
         graphicsDevice.setDisplayMode( currentDisplayMode );
         graphicsDevice.setFullScreenWindow( null );
         System.out.println("Display Restored...");
      } catch( InterruptedException e ) {
         e.printStackTrace();
      }
      System.exit( 0 );
   }
   
   public static void main( String[] args ) {
      final FullScreenRenderingExample app = new FullScreenRenderingExample();
      SwingUtilities.invokeLater( new Runnable() {
         public void run() {
            app.createAndShowGUI();
         }
      });
   }

}
