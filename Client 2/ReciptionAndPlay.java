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




public class ReciptionAndPlay extends InitResources implements Runnable{

	private final int packetsize = 100 ;
	private final int port = 45001 ;

	public ReciptionAndPlay(){
		super.captureAudio();
	}



    public void run(){

	    try{
	       
	        // Construct the socket
	        DatagramSocket socket = new DatagramSocket( this. port ) ;
	        System.out.println( "The server is ready..." ) ;
	    		
	    	// Create a packet
	        DatagramPacket packet = new DatagramPacket( new byte[this.packetsize], (this.packetsize)) ;
	        //this.captureAudio();

            for( ;; ){

	            try{
	                   
	               	// Receive a packet (blocking)
	          	    socket.receive( packet ) ;    

	          	    // Print the packet
	          	   //System.out.println( new String(packet.getData()) ) ;

	        	    this.getSourceDataLine().write(packet.getData(), 0, this.packetsize); //playing the audio   
	        		         
	            }catch(Exception e){

	        		System.out.println( e ) ;
	        		
	        	}
	                    
	        } 

	    }catch( Exception e ){

	        System.out.println( e ) ;
	    		
	    }

	}

}