
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Date;

class VoicePacket {

	public static final int SIZE = (Integer.SIZE + ReciptionAndPlay.PACKETSIZE * Byte.SIZE)/8 ; //get size for buffer

	final ByteBuffer buf;
	final int sequence ;	

	//constructor for serialize 	
	public VoicePacket(int sequence,byte [] tempBuffer ){

		buf = ByteBuffer.allocate(SIZE);		 //allocate space for temp buffer and set packet headers
		buf.putInt( sequence );
		buf.put( tempBuffer );
		this.sequence = sequence ;
				
	}

	//constructor fordesirialize
	public VoicePacket(byte [] data) throws IOException {

		buf = ByteBuffer.wrap(data);	
		sequence =	buf.getInt(0) ;
		
	}


	@Override
	public String toString() {

	 	return "sequence number:" + buf.getInt(0) ;

	}


	byte[] getData() {  //get the real voice data

		byte [] temp = new byte[ReciptionAndPlay.PACKETSIZE];

		for (int i = 4; i < buf.array().length; i++)
      temp[i-4] = buf.array()[i];
		
		return temp;

	}


	byte[] getVoicePacket() {

		return buf.array();  // get formated array

	}


	int getSequenceNumber() {

		return buf.getInt(0);

	}	

	/**
	 * A quick unit test for the class. 
	 */
	public static void main(String[] args) throws IOException	{

		byte[] bytes = new byte[100];
    Arrays.fill( bytes, (byte) 1 );

    VoicePacket p1 = new VoicePacket(1,bytes);       
    assert Arrays.equals(bytes , new VoicePacket(p1.getVoicePacket()).getData() );
               		
	}
	
}