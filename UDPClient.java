/*
 *  Author : Madushan W.P.L.
 *  Reg No : E/12/211
 *  Date   : 25/02/2016
 *
 *  This is udp client  . 
 */
 

//import network and input output packages
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


public class UDPClient{


	//Give a standard packet size. 
  	private final static int packetsize = 500 ;
  	private static DatagramSocket socket = null ;
  	private static InetAddress host = null;
	private static int port         = 0;



	/***********************************************************************/


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
            System.out.println("Available mixers:");
            Mixer mixer = null;
            for (int cnt = 0; cnt < mixerInfo.length; cnt++) {
                System.out.println(cnt + " " + mixerInfo[cnt].getName());
                mixer = AudioSystem.getMixer(mixerInfo[cnt]);

                Line.Info[] lineInfos = mixer.getTargetLineInfo();
                if (lineInfos.length >= 1 && lineInfos[0].getLineClass().equals(TargetDataLine.class)) {
                    System.out.println(cnt + " Mic is supported!");
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

            captureAndPlay(); //playing the audio

        } catch (LineUnavailableException e) {
            System.out.println(e);
            System.exit(0);
        }
      
    }




    private static void captureAndPlay() {
        byteArrayOutputStream = new ByteArrayOutputStream();
        stopCapture = false;
        try {
            int readCount;
            while (!stopCapture) {
                readCount = targetDataLine.read(tempBuffer, 0, tempBuffer.length);  //capture sound into tempBuffer
                if (readCount > 0) {
                    byteArrayOutputStream.write(tempBuffer, 0, readCount);
                   // sourceDataLine.write(this.tempBuffer, 0, 500); 

                     //Q2: Construct the datagram packet
	       			DatagramPacket packet  = new DatagramPacket(tempBuffer, tempBuffer.length , host , port);
	       			//sourceDataLine.write(packet.getData(), 0, 500); 
	       			System.out.println(new String(packet.getData()) ) ;

	        		// Send the packet
	        		socket.send( packet ) ;

                }
            }
            byteArrayOutputStream.close();
        } catch (IOException e) {
            System.out.println(e);
            System.exit(0);
        }
    }

   

	/************************************************************************/

   
   
  	public static void main( String args[] ){

	    // Check the whether the arguments are given

	    if( args.length != 2 ){
	        System.out.println( "usage: java DatagramClient host port" ) ;
	        return ;         
	    }     
	      
	    
		try{

	        //Q1: Create a datagram socket object here
	    	socket = new DatagramSocket();
		 
		    // Convert the arguments to ensure that they are valid
	        host = InetAddress.getByName( args[0] ) ;
	        port         = Integer.parseInt( args[1] ) ;
	      
	    
	       // byte [] data = "The message watnts to pass".getBytes() ;

	        captureAudio();	 
	       
	                 
			   
	    }catch( Exception e ){

	        System.out.println( e ) ;

	    } finally{

	       socket.close() ;
	      
	    }
     
   }
}
