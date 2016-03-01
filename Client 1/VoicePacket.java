
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Date;

class VoicePacket {

	static final int SIZE = Integer.SIZE ;

	/**
	 * ByteBuffer is a handy type for storing binary data.
	 */
	final ByteBuffer buf;


	/**
	 * Constructs a VoicePacket from the given values. 
	 */
	public VoicePacket(int sequence ) {

		this.buf = ByteBuffer.allocate(SIZE);		
		this.buf.putInt( sequence );
		//this.buf.put( tempBuffer );

		
	}

	/**
	 * Constructs a VoicePacket from the given stream. 
	 */	
	public VoicePacket(DataInputStream sin) throws IOException {
		byte[] a = new byte[SIZE];
		sin.readFully(a);
		this.buf = ByteBuffer.wrap(a);
	}


	/**
	 * @return the underlying data array.
	 */
	byte[] data() {
		return buf.array();
	}


	 /* Reading data rendered as a string. 
	 */
	@Override

	public String toString() {
	  //byte [] buf1 = new byte[500];
	 

		return "ID:, pulse:" + buf.getInt() ;
	}


	/**
	 * A quick unit test for the class. 
	 */
	public static void main(String[] args) throws IOException {

		
		try{
			//byte [] data = new byte[500] ;
			VoicePacket p1 = new VoicePacket(1);

			System.out.println(p1);

	   }catch (Exception e) {
	   	System.out.println(e);
	   	
	   }
		
		// DataInputStream din = new DataInputStream( 
		// 		new ByteArrayInputStream(p1.data()));
		
		// Reading p2 = new Reading(din);
		
		// assert Arrays.equals(p1.data(), p2.data());
	}
	
}