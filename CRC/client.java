package CRC;

import java.io.*;
import java.net.*;
import java.util.*;

public class client {
    public static void main(String[] args) throws IOException {
        String ip = "192.168.0.109"; 
        int port = 6010;
        String generator = "1101"; 

        Socket s = new Socket(ip, port);
        System.out.println("Client connected to the server on Handshaking port " + port);
        System.out.println("Client's Communication Port: " + s.getLocalPort());
        System.out.println("Client is Connected");

        
        BufferedReader input = new BufferedReader(new FileReader("input.txt"));
        String txt = input.readLine();
        input.close();
        System.out.println("File Content: " + txt);

        StringBuilder sb = new StringBuilder();
        for (char c : txt.toCharArray()) {
            sb.append(String.format("%8s", Integer.toBinaryString(c)).replace(' ', '0'));
        }
        String binaryData = sb.toString();
        System.out.println("Converted Binary Data: " + binaryData);

    
        int k = generator.length();
        String padded = binaryData + "0".repeat(k - 1);
        System.out.println("After Appending zeros Data to Divide: " + padded);

    
        String crc = calculateCRC(padded, generator);
        System.out.println("CRC Remainder: " + crc);

        
        String codeword = binaryData + crc;
        System.out.println("Transmitted Codeword to Server: " + codeword);


        Scanner sc = new Scanner(System.in);
        System.out.print("Enter number of bits to flip for error simulation: ");
        int flipCount = sc.nextInt();


        String corrupted = flipMultipleBits(codeword, flipCount);
        System.out.println("Corrupted Codeword (Bit flipped): " + corrupted);

    
        DataOutputStream dos = new DataOutputStream(s.getOutputStream());
        dos.writeUTF(codeword);   
        dos.writeUTF(corrupted); 
        dos.writeUTF(generator); 

        dos.close();
        s.close();
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

    public static String flipMultipleBits(String s, int numBitsToFlip) {
        Random r = new Random();
        char[] chars = s.toCharArray();
        int length = s.length();
        Set<Integer> flippedIndices = new HashSet<>();

        while (flippedIndices.size() < numBitsToFlip) {
            int i = r.nextInt(length);
            if (!flippedIndices.contains(i)) {
                chars[i] = (chars[i] == '0') ? '1' : '0';
                flippedIndices.add(i);
            }
        }

        return new String(chars);
    }
}

