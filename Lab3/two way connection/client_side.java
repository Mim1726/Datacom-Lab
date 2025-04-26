/* Client Side Code */
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

public class client_side {
    public static void main(String[] args) throws IOException{
        Socket s = new Socket ("10.33.2.103", 6000);
        System.out.println("Client Connected at server Handshaking port " + s.getPort());

        System.out.println("Client’s communcation port " + s.getLocalPort());
        System.out.println("Client is Connected");
        System.out.println("Enter the messages that you want to send and send \"stop\" to close the connection:");

        DataOutputStream output = new DataOutputStream(s.getOutputStream());
        DataInputStream input = new DataInputStream(s.getInputStream());
        BufferedReader read = new BufferedReader(new InputStreamReader(System.in));

        String str = "", reply = "";
        while(!str.equals("stop")){
            System.out.print("Client: ");
            str = read.readLine();
            output.writeUTF(str);
            output.flush();

            reply = input.readUTF();
            System.out.println("Server Says: " + reply);
        }
        output.close();
        input.close();
        read.close();
        s.close();
    }
}
