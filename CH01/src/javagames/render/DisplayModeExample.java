package javagames.render;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;


public class DisplayModeExample extends JFrame {
   
   class DisplayModeWrapper {
      
      private DisplayMode dm;
      
      public DisplayModeWrapper( DisplayMode dm ) {
         this.dm = dm;
      }
      
      public boolean equals( Object obj ) {
         DisplayModeWrapper other = (DisplayModeWrapper)obj;
         if( dm.getWidth() != other.dm.getWidth() )
            return false;
         if( dm.getHeight() != other.dm.getHeight() )
            return false;
         return true;
      }

      public String toString() {
         return "" + dm.getWidth() + " x " + dm.getHeight();
      }
   }
   
   private JComboBox displayModes;
   private GraphicsDevice graphicsDevice;
   private DisplayMode currentDisplayMode;
   
   public DisplayModeExample() {
      GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
      graphicsDevice = ge.getDefaultScreenDevice();
      currentDisplayMode = graphicsDevice.getDisplayMode();
   }
   
   private JPanel getMainPanel() {
      JPanel p = new JPanel();
      displayModes = new JComboBox( listDisplayModes() );
      p.add( displayModes );
      JButton enterButton = new JButton( "Enter Full Screen" );
      enterButton.addActionListener( new ActionListener() {
         public void actionPerformed( ActionEvent e ) {
            onEnterFullScreen();
         }
      });
      p.add( enterButton );
      JButton exitButton = new JButton( "Exit Full Screen" );
      exitButton.addActionListener( new ActionListener() {
         public void actionPerformed( ActionEvent e ) {
            onExitFullScreen();
         }
      });
      p.add( exitButton );
      return p;
   }
   
   private DisplayModeWrapper[] listDisplayModes() {
      ArrayList<DisplayModeWrapper> list = new ArrayList<DisplayModeWrapper>();
      for( DisplayMode mode : graphicsDevice.getDisplayModes() ) {
         //if( mode.getBitDepth() == 32 ) { // linux bug-fix Jan 2015
            DisplayModeWrapper wrap = new DisplayModeWrapper( mode );
            if( !list.contains( wrap ) ) {
               list.add( wrap );
            }
         //}
      }
      return list.toArray( new DisplayModeWrapper[0] );
   }
   
   protected void createAndShowGUI() {
      Container canvas = getContentPane();
      canvas.add( getMainPanel() );
      canvas.setIgnoreRepaint( true );
      setDefaultCloseOperation( EXIT_ON_CLOSE );
      setTitle( "Display Mode Test" );
      pack();
      setVisible( true );
   }
   
   protected void onEnterFullScreen() {
      if( graphicsDevice.isFullScreenSupported() ) {
         DisplayMode newMode = getSelectedMode();
         graphicsDevice.setFullScreenWindow( this );
         graphicsDevice.setDisplayMode( newMode );
      } 
   }
   
   protected void onExitFullScreen() {
      graphicsDevice.setDisplayMode( currentDisplayMode );
      graphicsDevice.setFullScreenWindow( null );
   }

   protected DisplayMode getSelectedMode() {
      DisplayModeWrapper wrapper = 
         (DisplayModeWrapper)displayModes.getSelectedItem();
      DisplayMode dm = wrapper.dm;
      int width = dm.getWidth();
      int height = dm.getHeight();
      int bit = dm.getBitDepth(); // linux Bug-Fix Jan 2015
      int refresh = DisplayMode.REFRESH_RATE_UNKNOWN;
      return new DisplayMode( width, height, bit, refresh );
   }
   
   public static void main( String[] args ) {
      final DisplayModeExample app = new DisplayModeExample();
      SwingUtilities.invokeLater( new Runnable() {
         public void run() {
            app.createAndShowGUI();
         }
      });
   }
}
