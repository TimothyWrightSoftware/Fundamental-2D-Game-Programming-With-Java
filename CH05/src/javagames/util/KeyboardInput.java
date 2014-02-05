package javagames.util;


import java.awt.event.*;

public class KeyboardInput implements KeyListener {
   
   private boolean[] keys;
   private int[] polled;
   
   public KeyboardInput() {
      keys = new boolean[ 256 ];
      polled = new int[ 256 ];
   }

   public boolean keyDown( int keyCode ) {
      return polled[ keyCode ] > 0;
   }
   
   public boolean keyDownOnce( int keyCode ) {
      return polled[ keyCode ] == 1;
   }
   
   public synchronized void poll() {
      for( int i = 0; i < keys.length; ++i ) {
         if( keys[i] ) {
            polled[i]++;
         } else {
            polled[i] = 0;
         }
      }
   }

   public synchronized void keyPressed( KeyEvent e ) {
      int keyCode = e.getKeyCode();
      if( keyCode >= 0 && keyCode < keys.length ) {
         keys[ keyCode ] = true;
      }
   }

   public synchronized void keyReleased( KeyEvent e ) {
      int keyCode = e.getKeyCode();
      if( keyCode >= 0 && keyCode < keys.length ) {
         keys[ keyCode ] = false;
      }
   }

   public void keyTyped( KeyEvent e ) {
      // Not needed
   }
}
