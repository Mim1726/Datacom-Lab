import java.io.*;
import java.net.*;
import java.util.*;

public class server {
    public static void main(String[] args) throws IOException {
        int port = 6056;
        ServerSocket ss = new ServerSocket(port);
        System.out.println("Server is connected at port no: " + port);
        System.out.println("Server is connecting");
        System.out.println("Waiting for the client");

        // cs = clientSocket
        Socket cs = ss.accept();
        System.out.println("Client request is accepted at port no: " + cs.getPort());
        System.out.println("Server’s Communication Port: " + port);


        DataInputStream in = new DataInputStream(cs.getInputStream());

        int[] chipCode = {1, 0, 1}; 
        int fileIndex = 1;
        List<Integer> spreadBits = new ArrayList<>();

        while (true) {
            int bit;
            try {
                bit = in.readInt();
            } catch (EOFException e) {
                break;
            }

            if (bit == -1) break;  
            if (bit == -2) {
                processFile(spreadBits, chipCode, fileIndex++);
                spreadBits.clear();
            } else {
                spreadBits.add(bit);
            }
        }

        cs.close();
        ss.close();
    }

    public static void processFile(List<Integer> spreadBits, int[] chip, int fileIndex) throws IOException {
        System.out.println("===== Despreading File: input" + fileIndex + ".txt =====");

        List<Integer> recoveredBits = new ArrayList<>();

        for (int i = 0; i <= spreadBits.size() - chip.length; i += chip.length) {
            int oneCount = 0;
            for (int j = 0; j < chip.length; j++) {
                int xor = spreadBits.get(i + j) ^ chip[j];
                if (xor == 1) oneCount++;
            }
            int recoveredBit = (oneCount > chip.length / 2) ? 1 : 0;
            recoveredBits.add(recoveredBit);
        }

    
        System.out.print("Reconstructed Binary: ");
        for (int b : recoveredBits) System.out.print(b);
        System.out.println();

        StringBuilder recoveredText = new StringBuilder();
        for (int i = 0; i <= recoveredBits.size() - 8; i += 8) {
            int ascii = 0;
            for (int j = 0; j < 8; j++) {
                ascii = (ascii << 1) | recoveredBits.get(i + j);
            }
            recoveredText.append((char) ascii);
        }

        System.out.println("Recovered Character(s): " + recoveredText);

        String outputFile = "recovered_input" + fileIndex + ".txt";
        BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile));
        bw.write(recoveredText.toString());
        bw.close();

        System.out.println("Final output written to: " + outputFile + "\n");
    }
}

