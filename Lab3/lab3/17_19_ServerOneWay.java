import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

 public class 17_19_ServerOneWay {
    public static String bitDeStuff(String data) {
        StringBuilder destuffed = new StringBuilder();
        int count = 0;

        for (int i = 0; i < data.length(); i++) {
            char bit = data.charAt(i);
            if (bit == '1') {
                count++;
                destuffed.append(bit);
                if (count == 5) {
                    i++; 
                    count = 0;
                }
            } 
            else {
                destuffed.append(bit);
                count = 0;
            }
        }
        return destuffed.toString();
    }
    public static String binaryToText(String binary) {
        StringBuilder text = new StringBuilder();
        for (int i = 0; i + 7 < binary.length(); i += 8) {
            String byteStr = binary.substring(i, i + 8);
            int charCode = Integer.parseInt(byteStr, 2);
            text.append((char) charCode);
        }
        return text.toString();
    }
    public static void main(String[] args) throws IOException{
        ServerSocket ss = new ServerSocket (6002);
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

        while (!str.equals("stop")) {
            str = input.readUTF(); 
            System.out.println("Received Stuffed Binary: " + str);
      
            String destuffedBinary = bitDeStuff(str);
            String originalMessage = binaryToText(destuffedBinary);
      
            System.out.println("Client Says (destuffed): " + destuffedBinary);
            System.out.println("Client Says (original): " + originalMessage);
            System.out.flush();
            
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