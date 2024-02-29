import java.io.File;
import java.util.Scanner;

public class Main {


    public static void main(String[] args) {
        checkFile();
        System.out.println("Введите текст и нажмите <Enter>: ");
        String text = new Scanner(System.in).nextLine();
        System.out.println("Длина текста: " + text.length());


    }


    public static void checkFile(){
        int numberOfFiles = 0;

        while (true) {

            String path = new Scanner(System.in).nextLine();
            File file = new File(path);
            boolean fileExists = file.exists();
            boolean isDirectory = file.isDirectory();

            if (!fileExists || isDirectory){
                System.out.println("Указанный файл не существует или это папка.");
                continue;
            }
            else {
                System.out.println("Путь указан верно.");
                numberOfFiles=numberOfFiles+1;
                System.out.println("Это файл номер " + numberOfFiles);
            }

        }

    }
}
