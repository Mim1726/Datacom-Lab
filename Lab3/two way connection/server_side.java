import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

 public class server_side {
    public static void main(String[] args) throws IOException{
        ServerSocket ss = new ServerSocket (5555);
        System.out.println("Server is connected at port no: " + ss.getLocalPort());

        System.out.println("Server is connecting\n");
        System.out.println("Waiting for the client\n");
        Socket s = ss.accept();
        System.out.println("Client request is accepted at port no: " + s.getPort());

        System.out.println("Server’s Communication Port: "+s.getLocalPort());
        DataInputStream input = new DataInputStream(s.getInputStream());
        DataOutputStream output = new DataOutputStream(s.getOutputStream());
        BufferedReader serverInput = new BufferedReader(new InputStreamReader(System.in));
        String str = "", reply = "";

        while(!str.equals("stop")){
            str = input.readUTF();
            System.out.println("Client Says: "+str );

            System.out.print("Server: ");
            reply = serverInput.readLine();
            output.writeUTF(reply);
            output.flush();
        }
        s.close();
        input.close();
        output.close();
        serverInput.close();
    }
}
