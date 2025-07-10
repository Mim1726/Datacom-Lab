import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket s = new Socket("10.33.2.87", 6004);
        System.out.println("Client Connected at server Handshaking port " + s.getPort());
        System.out.println("Client’s communcation port " + s.getLocalPort());
        System.out.println("Client is Connected");

        int num = 3;
        int T = 2;  
        String[] filenames = {"input1.txt", "input2.txt", "input3.txt"};

        DataOutputStream dos = new DataOutputStream(s.getOutputStream());
        FileInputStream[] inputs = new FileInputStream[num];

        for (int i = 0; i < num; i++) {
            inputs[i] = new FileInputStream(filenames[i]);
        }

        boolean key = false;
        int round = 1;

        while (true) {
            key = true;
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < num; i++) {
                StringBuilder temp = new StringBuilder();
                for (int t = 0; t < T; t++) {
                    int data = inputs[i].read();
                    if (data == -1) {
                        temp.append('#');
                    } 
                    else {
                        temp.append((char) data);
                        key = false;
                    }
                }
                sb.append(temp).append(" ");
            }

            String str = sb.toString();

            if (key) break;
            for (int i = 0, idx = 0; i < num; i++) {
                for (int t = 0; t < T; t++, idx++) {
                    char ch = str.charAt(idx + i); 
                    dos.writeByte(ch);
                }
            }

            System.out.println("Round " + round + ": " + str);
            round++;
        }

        for (FileInputStream fis : inputs) fis.close();
        dos.close();
        s.close();
        System.out.println("Client finished sending data.");
    }
}
