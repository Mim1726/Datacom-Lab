/* Client Side Code */
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

public class 17_19_ClientOneWay {
    public static String textToBinary(String text) {
        StringBuilder binary = new StringBuilder();
        for (char ch : text.toCharArray()) {
            binary.append(String.format("%8s", Integer.toBinaryString(ch)).replace(' ', '0'));
        }
        return binary.toString();
    }
    public static String bitStuff(String data) {
        StringBuilder stuffed = new StringBuilder();
        int count = 0;
        for (int i = 0; i < data.length(); i++) {
            char bit = data.charAt(i);
            stuffed.append(bit);
            if (bit == '1') {
                count++;
                if (count == 5) {
                    stuffed.append('0'); 
                    count = 0;
                }
            } else {
                count = 0;
            }
        }
        return stuffed.toString();
    }
    public static void main(String[] args) throws IOException{
        Socket s = new Socket ("10.33.2.103", 6002);
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
            String binary = textToBinary(str);
            String stuffedBinary = bitStuff(binary);
            System.out.println("Stuffed Binary: " + stuffedBinary);
            output.writeUTF(str);
            output.writeUTF(stuffedBinary);
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