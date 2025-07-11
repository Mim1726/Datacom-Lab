import java.io.*;
import java.net.*;

public class client {
    public static void main(String[] args) throws IOException {
        String ip = "10.33.2.87";
        int port = 6056;
        int[] chip = {1, 0, 1};

        String[] files = {"input1.txt", "input2.txt"};

        Socket s = new Socket(ip, port);
        DataOutputStream out = new DataOutputStream(s.getOutputStream());

        System.out.println("Client connected to server at port " + port);
        System.out.println("Client communication port: " + s.getLocalPort() + "\n");

        for (String file : files) {
            System.out.println("===== Processing File: " + file + " =====");

            BufferedReader input = new BufferedReader(new FileReader(file));
            String txt = input.readLine();
            input.close();

            System.out.println("File Content: " + txt);
            System.out.print("Chip Sequence: ");
            for (int bit : chip) System.out.print(bit + " ");
            System.out.println("\n");

            StringBuilder signal = new StringBuilder();

            for (char ch : txt.toCharArray()) {
                int ascii = (int) ch;
                String binary = String.format("%8s", Integer.toBinaryString(ascii)).replace(' ', '0');

                System.out.println("Character: " + ch);
                System.out.println("ASCII: " + ascii);
                System.out.println("8-bit Binary: " + binary);
                System.out.println("Spreading Steps:");

                for (char c : binary.toCharArray()) {
                    int bit = c - '0';
                    StringBuilder spread = new StringBuilder();
                    for (int x : chip) {
                        int xor = bit ^ x;
                        spread.append(xor);
                        signal.append(xor).append(" ");
                        out.writeInt(xor);
                    }
                    System.out.println("Bit " + bit + " XOR Chip → Spread: " + spread);
                }
                System.out.println();
            }

            out.writeInt(-2);

            String sp_file = "spread_" + file;
            BufferedWriter bw = new BufferedWriter(new FileWriter(sp_file));
            bw.write(signal.toString().trim());
            bw.close();

            System.out.println("Spread signal written to: " + sp_file + "\n");
        }

        out.writeInt(-1);
        s.close();
    }
}
