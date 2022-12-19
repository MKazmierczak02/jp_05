import java.io.*;
import java.net.*;

public class Polacz implements Runnable{

    ServerSocket server;
    public static int port = 8090;
    public static Socket socket;
    public static ObjectInputStream ois;
    public static ObjectOutputStream oos;
    public static String msg;
    Thread t;
    Okno okno_glowne;

public Polacz (){
    t= new Thread(this);
    t.start();
}

    @Override
    public void run() {

        try {
            System.out.println("Server start");
            server = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            System.out.println("Waiting for client..");
            socket = server.accept();
            System.out.println("Socket accepted");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        msg="";

        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            ois = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        while (!msg.equals("exit")){
            try {
                msg = (String)ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Brak klienta");
                throw new RuntimeException(e);
            }

            if(!msg.equals("")){

                if (msg.equals("Hello")){
                    
                } else if (msg.equals("exit")) {
                    try {
                        oos.close();
                        ois.close();
                        socket.close();
                        server.close();
                        System.out.println("Koniec polaczenia");
                        System.exit(0);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }




        }


    }
}
