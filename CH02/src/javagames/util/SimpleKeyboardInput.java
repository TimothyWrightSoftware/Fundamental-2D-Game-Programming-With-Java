package javagames.util;

import java.awt.event.*;

public class SimpleKeyboardInput implements KeyListener {
   
   private boolean[] keys;
   
   public SimpleKeyboardInput() {
      keys = new boolean[ 256 ];
   }

   public synchronized boolean keyDown( int keyCode ) {
      return keys[ keyCode ];
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
