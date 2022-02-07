

import metro.*;
import utils.MetroApplicationUtils;

import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException, NoSuchFieldException, IllegalAccessException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите имя файла, хранящего информацию о карте метрополитена:");
        String fileName = "./resources/" + scanner.nextLine();
        MetroGraph metro = new MetroGraph();
        MetroApplicationUtils metroManager = new MetroApplicationUtils(fileName, metro);
        if (metroManager.isFileExist()) {
            metroManager.help();
            metroManager.metroApplication();
        } else {
            System.out.println("error: Incorrect file");
        }

    }

}
