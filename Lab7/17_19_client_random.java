import java.io.*;
import java.net.*;
import java.util.*;

public class client2 {
    public static void main(String[] args) throws IOException {
        String ip = "10.33.2.87";
        int port = 6056;
        int[] chip = {1, 0, 1};
        double percent = 0.1;

        String[] files = {"input1.txt", "input2.txt"};

        Socket s = new Socket(ip, port);
        DataOutputStream out = new DataOutputStream(s.getOutputStream());

        Random r = new Random();

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

            StringBuilder clean = new StringBuilder();
            StringBuilder noisy = new StringBuilder();

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
                    StringBuilder spreadNoisy = new StringBuilder();

                    for (int x : chip) {
                        int xor = bit ^ x;
                        clean.append(xor).append(" ");

                        int noisyBit = xor;
                        if (r.nextDouble() < percent) {
                            noisyBit = 1 - xor;  
                        }
                        noisy.append(noisyBit).append(" ");

                        out.writeInt(noisyBit);
                        spread.append(xor);
                    }

                    System.out.println("Bit " + bit + " XOR Chip → Spread: " + spread);
                }
                System.out.println();
            }

            out.writeInt(-2);

            String cleanFile = "spread_clean_" + file;
            BufferedWriter bwClean = new BufferedWriter(new FileWriter(cleanFile));
            bwClean.write(clean.toString().trim());
            bwClean.close();

            String noisyFile = "spread_noisy_" + file;
            BufferedWriter bwNoisy = new BufferedWriter(new FileWriter(noisyFile));
            bwNoisy.write(noisy.toString().trim());
            bwNoisy.close();

            System.out.println("Clean spread signal written to: " + cleanFile);
            System.out.println("Noisy spread signal written to: " + noisyFile + "\n");
        }

        out.writeInt(-1);
        s.close();
    }
}
