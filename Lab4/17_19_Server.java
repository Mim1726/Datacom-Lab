import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket (6004);
        System.out.println("Server is connected at port no: " + ss.getLocalPort());
        System.out.println("Server is connecting\n");
        System.out.println("Waiting for the client\n");
        Socket s = ss.accept();
        System.out.println("Client request is accepted at port no: " + s.getPort());
        System.out.println("Server’s Communication Port: "+s.getLocalPort());

        int num = 3;
        int T = 2;

        DataInputStream dis = new DataInputStream(s.getInputStream());
        FileOutputStream[] outputs = new FileOutputStream[num];

        for (int i = 0; i < num; i++) {
            outputs[i] = new FileOutputStream("output" + (i + 1) + ".txt");
        }

        boolean key = true;
        int round = 1;

        while (key) {
            try {
                StringBuilder sb = new StringBuilder();
                boolean check = true;

                for (int i = 0; i < num; i++) {
                    StringBuilder temp = new StringBuilder();
                    for (int t = 0; t < T; t++) {
                        byte b = dis.readByte();
                        if (b != '#') {
                            outputs[i].write(b);
                            temp.append((char) b);
                            check = false; 
                        } else {
                            temp.append('#');
                        }
                    }
                    sb.append(temp).append(" ");
                }

                if (!check) {
                    System.out.println("Round " + round + ": " + sb.toString().trim());
                    round++;
                }

            } 
            catch (EOFException e) {
                key = false;
            }
        }

        for (FileOutputStream fos : outputs) fos.close();
        dis.close();
        s.close();
        ss.close();
        System.out.println("Server finished writing output files.");
    }
}
