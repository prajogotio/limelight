package limelight;

import java.io.*;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by prajogotio on 15/2/15.
 */
public class ReaderTest {

    public static void main(String[] args) {
        try {
            InputStream inputStream = ReaderTest.class.getResourceAsStream("jams/sugar.txt");
            Scanner scanner = new Scanner(inputStream);
            System.out.println(scanner.nextLine());
            System.out.println(scanner.nextInt() + " " + scanner.nextInt());
            int k;
            for (int i = 0; i < 7; ++i) {
                k = scanner.nextInt();
                for(int j = 0; j < k; ++j) {
                    System.out.print(scanner.nextDouble() + " ");
                }
                System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
