/*
 *  Author : Madushan W.P.L.
 *  Reg No : E/12/211
 *  Date   : 25/02/2016
 *
 *    This is udp Servers
 */
 
import java.net.* ;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;


public class UDPServer{

    //Give a standard packet size
    private final static int packetsize = 500 ;


/*******************************************************************************/
    static  boolean stopCapture = false;
    static ByteArrayOutputStream byteArrayOutputStream;
    static AudioFormat audioFormat;
    static TargetDataLine targetDataLine;
    static AudioInputStream audioInputStream;
    static SourceDataLine sourceDataLine;
    static byte tempBuffer[] = new byte[500];



    private static AudioFormat getAudioFormat() {
        float sampleRate = 16000.0F;
        int sampleSizeInBits = 16;
        int channels = 2;
        boolean signed = true;
        boolean bigEndian = true;
        return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
    }



    private static void captureAudio() {
        
        try {
            Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();    //get available mixers
           // System.out.println("Available mixers:");
            Mixer mixer = null;
            for (int cnt = 0; cnt < mixerInfo.length; cnt++) {
                //System.out.println(cnt + " " + mixerInfo[cnt].getName());
                mixer = AudioSystem.getMixer(mixerInfo[cnt]);

                Line.Info[] lineInfos = mixer.getTargetLineInfo();
                if (lineInfos.length >= 1 && lineInfos[0].getLineClass().equals(TargetDataLine.class)) {
                   // System.out.println(cnt + " Mic is supported!");
                    break;
                }
            }

            audioFormat = getAudioFormat();     //get the audio format
            DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);

            targetDataLine = (TargetDataLine) mixer.getLine(dataLineInfo);
            targetDataLine.open(audioFormat);
            targetDataLine.start();

            DataLine.Info dataLineInfo1 = new DataLine.Info(SourceDataLine.class, audioFormat);
            sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo1);
            sourceDataLine.open(audioFormat);
            sourceDataLine.start();
            
            //Setting the maximum volume
            FloatControl control = (FloatControl)sourceDataLine.getControl(FloatControl.Type.MASTER_GAIN);
            control.setValue(control.getMaximum());

            //captureAndPlay(); //playing the audio

        } catch (LineUnavailableException e) {
            System.out.println(e);
            System.exit(0);
        }
      
    }


/*********************************************************************************/


    public static void main( String args[] ){

        // Check the whether the arguments are given
        if( args.length != 1 ){

          System.out.println( "usage: DatagramServer port" ) ;
          return ;
        }

        try{

            // Convert the argument to ensure that is it valid
            int port = Integer.parseInt( args[0] ) ;

            // Construct the socket
            DatagramSocket socket = new DatagramSocket( port ) ;

            System.out.println( "The server is ready..." ) ;
    		
    	  	 // Create a packet
            DatagramPacket packet = new DatagramPacket( new byte[packetsize], packetsize ) ;
            captureAudio();



            for( ;; ){

                try{
                   
                 	// Receive a packet (blocking)
          		    socket.receive( packet ) ;    

          		    // Print the packet
          		   System.out.println( new String(packet.getData()) ) ;

        		    sourceDataLine.write(packet.getData(), 0, 500); //playing the audio   
        		         
                }catch(Exception e){

        			System.out.println( e ) ;
        		
        		}
                    
            } 

        }catch( Exception e ){

            System.out.println( e ) ;
    		
        }

  }

}
