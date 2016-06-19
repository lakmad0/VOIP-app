
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Date;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;


public class VoicePacket implements Serializable{

	private final int sequence ;
	private final byte [] tempBuffer;
	public static int SIZE = 0;


	public VoicePacket(int sequence,byte [] tempBuffer ){

		this.sequence = sequence ;
		this.tempBuffer = tempBuffer ;
				
	}


	public static byte[] serialize(VoicePacket obj) throws IOException {

        try(ByteArrayOutputStream byteOutPutStream = new ByteArrayOutputStream()){
            try( ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteOutPutStream) ){
                objectOutputStream.writeObject(obj);
            }
            return byteOutPutStream.toByteArray();
        }

    }
    
    
    //to deserialize the byte stream
    public static VoicePacket deserialize(byte[] bytes) throws IOException, ClassNotFoundException {

        try(ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes)){
            try( ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream) ){
                return (VoicePacket)objectInputStream .readObject();
            }
        }

    }


    public int getSequenceNumber(){

    	return this.sequence ;
    }

    public byte[] getData(){

    	return this.tempBuffer ;
    }

    public static int getSize(){

    	try{

    		if (SIZE == 0) {
    			SIZE = VoicePacket.serialize(new VoicePacket(1	,new byte[ReciptionAndPlay.PACKETSIZE])).length;    			
    		}    		

    	}catch (Exception e) {

    		System.out.println(e);
    		
    	}
    	return SIZE ;  	

    }
    //unit test
    public static void main(String[] args) throws IOException	{

    	try{
    		
    		VoicePacket p1 = new VoicePacket(1	,new byte[ReciptionAndPlay.PACKETSIZE]) ;
    	    VoicePacket p2 = VoicePacket.deserialize( VoicePacket.serialize(p1) );

    	    assert p1.equals( p2 );

    	}catch (Exception e) {
    		System.out.println(e);
    	}
    	     
	    
	               		
	}
	
}