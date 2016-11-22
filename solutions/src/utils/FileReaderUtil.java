package utils;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by campbell on 2015/12/07.
 */
public class FileReaderUtil {
    public static String[] readAllLines(String filename) {
        ArrayList<String> lines = new ArrayList<>();

        try {
            FileReader fileReader = new FileReader(filename);
            BufferedReader reader = new BufferedReader(fileReader);

            String line = reader.readLine();
            while(line != null) {
                lines.add(line);
                line = reader.readLine();
            }

            reader.close();
            fileReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lines.toArray(new String[lines.size()]);
    }
}
