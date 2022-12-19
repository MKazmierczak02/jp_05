import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class SocketClientExample {
 
    public void startClient()
            throws IOException, InterruptedException {

        InetSocketAddress hostAddress = new InetSocketAddress("localhost", 8090);
        SocketChannel client = SocketChannel.open(hostAddress);
 
        System.out.println("Client... started");
        
        String threadName = Thread.currentThread().getName();
 
        // Send messages to server
        String messages = threadName + ": Test";

        byte[] message = new byte[]{Byte.parseByte(new String(messages))};
        ByteBuffer buffer = ByteBuffer.wrap(message);
        client.write(buffer);
        buffer.clear();
        Thread.sleep(5000);

        client.close();            
    }
}

