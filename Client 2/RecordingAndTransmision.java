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


public class RecordingAndTransmision extends Thread{


	//Give a standard packet size. 
    	private final static int packetsize = 500 ;
        private DatagramSocket socket = null ;
        private InetAddress host = null;
	    private  final int port  = 45000;

	// /***********************************************************************/


	private boolean stopCapture = false;
	private ByteArrayOutputStream byteArrayOutputStream;
	public static AudioFormat audioFormat;
	public static TargetDataLine targetDataLine;
	public static AudioInputStream audioInputStream;
	public static SourceDataLine sourceDataLine;
	private byte tempBuffer[] = new byte[500];



	public static AudioFormat getAudioFormat() {
	    float sampleRate = 16000.0F;
	    int sampleSizeInBits = 16;
	    int channels = 2;
	    boolean signed = true;
	    boolean bigEndian = true;
	    return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
	}



	public static void captureAudio() {
        
        try {
            Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();    //get available mixers
            //System.out.println("Available mixers:");
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

            audioFormat = RecordingAndTransmision.getAudioFormat();     //get the audio format
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


    private void captureAndSend() {
        this.byteArrayOutputStream = new ByteArrayOutputStream();
        this.stopCapture = false;
        try {
            int readCount;
            while (!this.stopCapture) {
                readCount = targetDataLine.read(this.tempBuffer, 0, this.tempBuffer.length);  //capture sound into tempBuffer
                if (readCount > 0) {
                    this.byteArrayOutputStream.write(tempBuffer, 0, readCount);
                   // sourceDataLine.write(this.tempBuffer, 0, 500); 

                     //Q2: Construct the datagram packet
	       			DatagramPacket packet  = new DatagramPacket(this.tempBuffer, this.tempBuffer.length , this.host , port);
	       			//sourceDataLine.write(packet.getData(), 0, 500); 
	       			//System.out.println(new String(packet.getData()) ) ;

	        		// Send the packet
	        		this.socket.send( packet ) ;

                }
            }
            this.byteArrayOutputStream.close();
        } catch (IOException e) {
            System.out.println(e);
            System.exit(0);
        }
    }



    /**************************************************************************/


    public void run(){

    	try{

	    	this.socket = new DatagramSocket();
	    	RecordingAndTransmision.captureAudio();	
	    	captureAndSend();

    	 }catch( Exception e ){

	        System.out.println( e ) ;

	    } finally{

	       socket.close() ;
	      
	    }
	} 
   

    /************************************************************************/


	public  RecordingAndTransmision(InetAddress host){

		this.host = host;

	}


	/***************************************************************************/

   
   
  	public static void main( String args[] ){

	    // Check the whether the arguments are given

	    if( args.length != 1 ){
	        System.out.println( "usage: java DatagramClient host " ) ;
	        return ;         
	    }

	    try{

		    RecordingAndTransmision rat = new RecordingAndTransmision(InetAddress.getByName( args[0] ));
		    rat.start();


	    ReciptionAndPlay  rap = new ReciptionAndPlay();
	    rap.start();

		}catch(Exception e){
			System.out.println(e);
		}


   }
}

class ReciptionAndPlay extends Thread{

	private final static int packetsize = 500 ;
	private final static int port = 45001 ;



    public void run(){

	    try{

	       
	        // Construct the socket
	        DatagramSocket socket = new DatagramSocket( port ) ;

	        //System.out.println( "The server is ready..." ) ;
	    		
	    	// Create a packet
	        DatagramPacket packet = new DatagramPacket( new byte[packetsize], packetsize ) ;
	        RecordingAndTransmision.captureAudio();	

            for( ;; ){

	            try{
	                   
	               	// Receive a packet (blocking)
	          	    socket.receive( packet ) ;    

	          	    // Print the packet
	          	   //System.out.println( new String(packet.getData()) ) ;

	        	    RecordingAndTransmision.sourceDataLine.write(packet.getData(), 0, 500); //playing the audio   
	        		         
	            }catch(Exception e){

	        		System.out.println( e ) ;
	        		
	        	}
	                    
	        } 

	    }catch( Exception e ){

	        System.out.println( e ) ;
	    		
	    }

	}

}