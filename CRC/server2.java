package CRC;

import java.io.*;
import java.net.*;

public class server2 {
    public static void main(String[] args) throws IOException {
        int port = 6009;
        System.out.println("Server is connected at port no: " + port);
        System.out.println("Server is connecting");
        System.out.println("Waiting for the client");

        ServerSocket ss = new ServerSocket(port);
        Socket s = ss.accept();

        System.out.println("Client request is accepted at port no: " + s.getPort());
        System.out.println("Server's Communication Port: " + s.getLocalPort());

        DataInputStream dis = new DataInputStream(s.getInputStream());

        String original = dis.readUTF();   
        String received = dis.readUTF();   
        String generator = dis.readUTF();  

        System.out.println("Received Codeword: " + received);

    
        String padded = received + "0".repeat(generator.length() - 1);
        String remainder = calculateCRC(padded, generator);
        System.out.println("Calculated Remainder: " + remainder);

        if (remainder.matches("0+")) {
            System.out.println("No error detected in transmission.");
        } else {
            System.out.println("Error detected in transmission!");
            int errors = countBitErrors(original, received);
            System.out.println("Estimated number of bit errors: " + errors);
        }

        dis.close();
        s.close();
        ss.close();
    }

    public static String calculateCRC(String input, String generator) {
        int k = generator.length();
        char[] data = input.toCharArray();
        char[] gen = generator.toCharArray();

        for (int i = 0; i <= data.length - k; i++) {
            if (data[i] == '1') {
                for (int j = 0; j < k; j++) {
                    data[i + j] = (data[i + j] == gen[j]) ? '0' : '1';
                }
            }
        }

        return new String(data).substring(data.length - (k - 1));
    }

    public static int countBitErrors(String a, String b) {
        int count = 0;
        for (int i = 0; i < Math.min(a.length(), b.length()); i++) {
            if (a.charAt(i) != b.charAt(i)) {
                count++;
            }
        }
        return count;
    }
}









