import java.net.* ;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
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




public class ReciptionAndPlay extends InitResources implements Runnable{

	public static final int PACKETSIZE = 100 ;
	private final int port = 45000 ;


	public ReciptionAndPlay(){  

		super.captureAudio();  //Initialize the resources

	}


  public void run(){

	  try{
	       
	    // Construct the socket
	    DatagramSocket socket = new DatagramSocket( this. port ) ;	        
	    		
	    // Create a packet
	    DatagramPacket packet = new DatagramPacket( new byte[VoicePacket.SIZE], VoicePacket.SIZE) ;	        

      for( ;; ){

	      try{
	                  
	        // Receive a packet (blocking)
	        socket.receive( packet ) ;	          	    
	        VoicePacket voicepacket = new VoicePacket(packet.getData());  //deserialize the voicepacket

	        System.out.println("Packet get " +voicepacket.getSequenceNumber());   //print the sequence number
	        this.getSourceDataLine().write( voicepacket.getData(), 0, PACKETSIZE); //playing the audio   
	        		         
	      }catch(Exception e){

	        System.out.println( e ) ;
	        		
	     	}	  
	     	                 
	    } 

	  }catch( Exception e ){

	    System.out.println( e ) ;
	    		
	  }

	}

}