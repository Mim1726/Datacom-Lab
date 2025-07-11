import java.io.*;
import java.net.*;
import java.util.Random;

public class Client {
    public static void main(String[] args) throws IOException {
        String ip = "10.33.2.88";
        int port = 5007;
        String generator = "1101";


        Socket s = new Socket(ip, port);
        System.out.println("Client connected to the server on Handshaking port " + port);
        System.out.println("Client’s Communication Port: " + s.getLocalPort());
        System.out.println("Client is Connected");

        
        BufferedReader input = new BufferedReader(new FileReader("input.txt"));
        String txt = input.readLine();
        input.close();

        System.out.println("File Content: " + txt);


        StringBuilder sb = new StringBuilder();
        for (char c : txt.toCharArray()) {
            sb.append(String.format("%8s", Integer.toBinaryString(c)).replace(' ', '0'));
        }

        System.out.println("Converted Binary Data: " + sb);

        
        int k = generator.length();
        String sb2 = sb + "0".repeat(k - 1);
        System.out.println("After Appending zeros Data to Divide: " + sb2);

        String r = calculateCRC(sb2, generator);
        System.out.println("CRC Remainder: " + r);

        String x = sb + r;
        System.out.println("Transmitted Codeword to Server: " + x);

        //cc=corrupted string
        String cc = flipRandomBit(x);
        System.out.println("Corrupted Codeword (Bit Flipped): " + cc);


        DataOutputStream dos = new DataOutputStream(s.getOutputStream());
        dos.writeUTF(x);

        dos.close();
        s.close();
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

        return s.substring(s.length() - (k - 1));
    }

    public static String flipRandomBit(String s) {
        Random r = new Random();
        int i = r.nextInt(s.length());

        // c = flipped bit
        char c;
        if (s.charAt(i) == '0') {
            c = '1';
        } else {
            c = '0';
        }

        return s.substring(0, i) + c + s.substring(i + 1);
    }
}
