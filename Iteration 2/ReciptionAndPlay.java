import java.util.*;
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
import java.net.MulticastSocket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;



public class ReciptionAndPlay extends InitResources implements Runnable{

	public static final int PACKETSIZE = 500 ;
	private final int port = 45000 ;
	private InetAddress host = null;
	private MulticastSocket socket = null ;


	public ReciptionAndPlay(InetAddress host){  

		super.captureAudio();  //Initialize the resources
		this.host =host;

	}
	

 	 public void run(){

		try{
		       
		    //to create a new multicast socket   
		    socket =   new MulticastSocket(this. port);   
		    socket.joinGroup(host);	
		    socket.setLoopbackMode(true);	       

		    //InetAddress hostAddress = this.getLocalAddress() ;	 
		    		
		    // Create a packet
		    DatagramPacket packet = new DatagramPacket( new byte[VoicePacket.getSize()], VoicePacket.getSize()) ;	 

		    // to get starting system time.
      		long startTime = System.currentTimeMillis();       

	        for( ;; ){

		      	try{
		                  
			        socket.receive( packet ) ;	
			        //deserialize the voicepacket		                	    
			        VoicePacket voicepacket = VoicePacket.deserialize(packet.getData());
					Statictics.handlePacket(voicepacket.getSequenceNumber());       
			        long endTime = System.currentTimeMillis(); 			      	

			        if ((endTime - startTime )/1000 >= 60) {
			        	System.out.println(Statictics.stats());
			        	startTime = endTime ;
			        	Statictics.setLostCount(0);
			        	Statictics.setUnOrderdCount(0);		        	
			        }
			        //playing the audio   
			        this.getSourceDataLine().write( voicepacket.getData(), 0, PACKETSIZE); 

		        		         
		     	}catch(Exception e){
		        	System.out.println( e ) ;		        		
		     	}	  
		     	                 
		    } 

		}catch( Exception e ){
		    System.out.println( e ) ;		    		
		}finally{		  	
		  	try{
		  		socket.close();
		  		socket.leaveGroup(host);
		  	}catch(Exception e){
		        System.out.println( e ) ;		        		
		    }	  
		}

	}
    
}