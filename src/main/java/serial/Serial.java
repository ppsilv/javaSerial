package serial;


import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

/**
 * This version of the TwoWaySerialComm example makes use of the 
 * SerialPortEventListener to avoid polling.
 *
 */
public class Serial
{
    OutputStream out;
    SerialReader input;
    CommPort commPort;
    static byte[] buffer_out = new byte[10024];
    static String buffer_string= new String();
    static int len = 0;
    
    public Serial()
    {
        super();
    }
    
    void close() throws Exception
    {
    	commPort.close();
    }
    void connect ( String portName ) throws Exception
    {
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        if ( portIdentifier.isCurrentlyOwned() )
        {
            System.out.println("Error: Port is currently in use");
        }
        else
        {
             commPort = portIdentifier.open(this.getClass().getName(),2000);

            if ( commPort instanceof SerialPort )
            {
                SerialPort serialPort = (SerialPort) commPort;
                serialPort.setSerialPortParams(9600,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);

                InputStream in = serialPort.getInputStream();
                out = serialPort.getOutputStream();

                //(new Thread(new SerialWriter(out))).start();
                input = new SerialReader(in);
                serialPort.addEventListener(input);
                serialPort.notifyOnDataAvailable(true);
                Thread.sleep(3000);
                out.write("yes".getBytes());   

            }
            else
            {
                System.out.println("Error: Only serial ports are handled by this example.");
            }
        }     
    }

    /**
     * Handles the input coming from the serial port. A new line character
     * is treated as the end of a block in this example. 
     */
    public static class SerialReader implements SerialPortEventListener 
    {
        private InputStream in;
        byte[] buffer = new byte[10024];
        
        public SerialReader ( InputStream in )
        {
            this.in = in;
        }

        public void serialEvent(SerialPortEvent arg0) {
            int data;

            try {
                while ( ( data = in.read()) > -1 ){
                    if ( data == '\n' ) {
                    	//System.out.println(len);
                    	buffer_out=Arrays.copyOf(buffer, len);
                        break;
                    }
                    buffer[len++] = (byte) data;                    
                }
            }catch ( IOException e ){
                e.printStackTrace();
                System.exit(-1);
            }             
        }
    }

    /** 
    public static class SerialWriter implements Runnable 
    {
        OutputStream out;

        public SerialWriter ( OutputStream out )
        {
            this.out = out;
        }

        public void run ()
        {
            try
            {                
                int c = 0;
                while ( ( c = System.in.read()) > -1 )
                {
                    this.out.write(c);
                }                
            }
            catch ( IOException e )
            {
                e.printStackTrace();
                System.exit(-1);
            }            
        }
    }*/

   public String getSensor(){
       try{                
           this.out.write("A".getBytes());              
       }
       catch ( IOException e ){
           e.printStackTrace();
       }  
       try {
    	   Thread.sleep(3);
       } catch (InterruptedException e) {
    	   // TODO Auto-generated catch block
    	   e.printStackTrace();
       }
       return ("").trim();
   }

    public static void main ( String[] args ){
        Serial test;
        int i;
        
       // GUI g = new GUI();
        
        GUI.main001(null);
        /*
        try {
            test = new Serial();
            Arrays.fill( buffer_out, (byte) '0' );
            test.connect("COM5");
            for( i=0; i<150; i++ ) {
            	len=0;
                
	            test.getSensor();
	            Thread.sleep(150);
	            String buf = new String(buffer_out);
            	System.out.println(i+"******************************************************************");
	            System.out.print( "|" + buf );
            }
            test.close();
        } catch ( Exception e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        */
    }
}