package joensson.clock;
import java.io.File;
import java.io.IOException;
 
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.swing.JOptionPane;
/**
 * To be able to play sounds I had to search the internet, and found this which 
 * worked. To be able use it as I wanted I've made some smaller changes.
 * 
 * I found it at: http://www.daniweb.com/forums/thread17484.html
 * 
 * @author someone at that forum called "George2"
 *
 */
public class PlaySound {
	
public PlaySound( String fileName ) {
      File soundFile = new File( fileName );
 
      try {
         // Create a stream from the given file.
         // Throws IOException or UnsupportedAudioFileException
         AudioInputStream audioInputStream = AudioSystem.getAudioInputStream( soundFile );
         // AudioSystem.getAudioInputStream( inputStream ); // alternate audio stream from inputstream
         playAudioStream( audioInputStream );
      } catch ( Exception e ) {
    	  JOptionPane.showMessageDialog(null,"Problem med filen "+fileName+":\n"+e.getMessage());
         //System.out.println( "Problem with file " + fileName + ":" );
         //e.printStackTrace();
      }
   } // playSound
 
   /** Plays audio from the given audio input stream. */
   public static void playAudioStream( AudioInputStream audioInputStream ) {
      // Audio format provides information like sample rate, size, channels.
      AudioFormat audioFormat = audioInputStream.getFormat();
      //System.out.println( "Play input audio format=" + audioFormat );
 
      // Open a data line to play our type of sampled audio.
      // Use SourceDataLine for play and TargetDataLine for record.
      DataLine.Info info = new DataLine.Info( SourceDataLine.class, audioFormat );
      if ( !AudioSystem.isLineSupported( info ) ) {
    	  JOptionPane.showMessageDialog(null,"Formatet stöds inte: Bara wav-filer stöds.");
        //System.out.println( "Play.playAudioStream does not handle this type of audio on this system." );
         return;
      }
      
     try {
         // Create a SourceDataLine for play back (throws LineUnavailableException).  
         SourceDataLine dataLine = (SourceDataLine) AudioSystem.getLine( info );
        // System.out.println( "SourceDataLine class=" + dataLine.getClass() );
 
         // The line acquires system resources (throws LineAvailableException).
        dataLine.open( audioFormat );

         // Adjust the volume on the output line.
         if( dataLine.isControlSupported( FloatControl.Type.MASTER_GAIN ) ) {
            FloatControl volume = (FloatControl) dataLine.getControl( FloatControl.Type.MASTER_GAIN );
            volume.setValue( 100.0F );
        }
 
         // Allows the line to move data in and out to a port.
         dataLine.start();
 
         // Create a buffer for moving data from the audio stream to the line.   
         int bufferSize = (int) audioFormat.getSampleRate() * audioFormat.getFrameSize();
        byte [] buffer = new byte[ bufferSize ];
 
         // Move the data until done or there is an error.
        try {
            int bytesRead = 0;
            while ( bytesRead >= 0 ) {
               bytesRead = audioInputStream.read( buffer, 0, buffer.length );
               if ( bytesRead >= 0 ) {
                  // System.out.println( "Play.playAudioStream bytes read=" + bytesRead +
                  //    ", frame size=" + audioFormat.getFrameSize() + ", frames read=" + bytesRead / audioFormat.getFrameSize() );
                  // Odd sized sounds throw an exception if we don't write the same amount.
                  int framesWritten = dataLine.write( buffer, 0, bytesRead );
               }
            } // while
         } catch ( IOException e ) {
        	 JOptionPane.showMessageDialog(null,e.getMessage());
            //e.printStackTrace();
         }
 
         //System.out.println( "Play.playAudioStream draining line." );
         // Continues data line I/O until its buffer is drained.
         dataLine.drain();
 
         //System.out.println( "Play.playAudioStream closing line." );
         // Closes the data line, freeing any resources such as the audio device.
         dataLine.close();
      } catch ( LineUnavailableException e ) {
         e.printStackTrace();
      }
   } // playAudioStream
} // PlaySound

