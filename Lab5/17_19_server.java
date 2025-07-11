import java.io.*;
import java.net.*;

public class server2 {
    public static void main(String[] args) throws IOException {
        int port = 5007;
        ServerSocket ss = new ServerSocket(port);
        System.out.println("Server is connected at port no: " + port);
        System.out.println("Server is connecting");
        System.out.println("Waiting for the client");

        // cs = clientSocket
        Socket cs = ss.accept();
        System.out.println("Client request is accepted at port no: " + cs.getPort());
        System.out.println("Server’s Communication Port: " + port);

        DataInputStream dis = new DataInputStream(cs.getInputStream());
        String x = dis.readUTF();  

        System.out.println("Received Codeword: " + x);

        
        String generator = "1101";  
        String remainder = calculateCRC(x, generator);
        System.out.println("Calculated Remainder: " + remainder);

        if (remainder.equals("0".repeat(generator.length() - 1))) {
            System.out.println("No error detected in transmission.");
        } else {
            System.out.println("Error detected in transmission!");
        }

        dis.close();
        cs.close();
        ss.close();
    }

    
    public static String calculateCRC(String s, String generator) {
        int k = generator.length();
        char[] s2 = s.toCharArray();
        char[] gen = generator.toCharArray();

        
        for (int i = 0; i <= s.length() - k; i++) {
            if (s2[i] == '1') {
                
                for (int j = 0; j < k; j++) {
                    if (s2[i + j] == gen[j]) {
                        s2[i + j] = '0';
                    } 
                    else {
                        s2[i + j] = '1';
                    }
                }
            }
        }

        
        return new String(s2, s.length() - (k - 1), k - 1);
    }
}
