

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException, NoSuchFieldException, IllegalAccessException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите имя файла, хранящего информацию о карте метрополитена:");
        String fileName = "./main/resources/" + scanner.nextLine();
        MetroGraph metro = new MetroGraph();
        MetroApplicationUtils metroManager = new 
                MetroApplicationUtils(fileName, metro);
        if (metroManager.isFileExist()) {
            metroManager.metroApplication();
        } else {
            System.out.println("error: Incorrect file");
        }

    }

}
