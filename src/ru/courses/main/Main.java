package ru.courses.main;

import ru.courses.exceptions.MyException;
import ru.courses.pars.LogEntry;
import ru.courses.pars.Statistics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        String path = checkFile();//получение правильного пути к файлу, например: C:\Users\kdanileyko\Desktop\Study\access.log
        file(path);//вызов метода чтения файла
        System.out.println("Нажмите <Enter> для выхода.");
        String text = new Scanner(System.in).nextLine();//создали объект сканера и указали источник данных (текст в консоли)
    }

    public static String checkFile() {
        System.out.println("Введите путь и нажмите <Enter>: ");

        while (true) {//бесконечный цикл

            String path = new Scanner(System.in).nextLine();//запрашивается в консоли путь к файлу
            File file = new File(path);//создали объект типа Файл
            boolean fileExists = file.exists();//определяем существует ли файл
            boolean isDirectory = file.isDirectory();//определяем папка ли это

            if (!fileExists || isDirectory) {//если файла не существует или это папка
                System.out.println("Указанный файл не существует или это папка.");
                continue;
            } else {
                System.out.println("Путь указан верно.");
                return path;
            }

        }

    }

    public static void file(String path) {//код, который будет построчно читать указанный файл

        try {
            FileReader fileReader = new FileReader(path);//создали чтеца fileReader файла по указанному пути (не может читать построчно)
            BufferedReader reader = new BufferedReader(fileReader);//созали чтеца reader на основе fileReader, который может читать построчно
            String line;
            int i = 0;//счётчик строк
            int maxLenght = 0;
            int minLenght = 0;
            int yandexBot = 0;
            int googlebot = 0;

            Statistics statistics = new Statistics();

            while ((line = reader.readLine()) != null) {//пока строка существует

                int length = line.length();//записываем длину строки
                if (length > 1024) {
                    throw new MyException("Это строка длиннее 1024 символов!");
                }

                if (length > maxLenght) {
                    maxLenght = length;
                }

                if (length < minLenght || i == 0) {
                    minLenght = length;
                }

                LogEntry logEntry = new LogEntry(line);
                statistics.addEntry(logEntry);


                i++;//считаем кол-во строк в файле

            }
            fileReader.close();

            System.out.println("Объём часового трафика: " + statistics.getTrafficRate());
            System.out.println("Количество строк в файле: " + i);
            System.out.println("Список существующих страниц сайта: " + statistics.getExistingAddresses());
            System.out.println("Список несуществующих страниц: " + statistics.getBadAddresses());
            System.out.println("Статистика ОС с долей каждой: " + statistics.getOsStatictics());
            System.out.println("Статистика браузеров с долей каждого: " + statistics.getBrowsersStatictics());
            System.out.println("Статистика среднего кол-ва посещений сайта за час: " + statistics.getAddressesByHours());
            System.out.println("Статистика среднего кол-ва ошибочных запросов в час " + statistics.countOfFailByHours());
            System.out.println("Статистика средней посещаемости одним пользователем: " + statistics.getAverageAttendance());
            System.out.println("Пиковая посещаемость сайта в секунду: " + statistics.getPeakTrafficInSecond());
            System.out.println("Список сайтов, со страниц которых есть ссылки на текущий сайт: " + statistics.getReferers());
            System.out.println("Максимальная посещаемость одним пользователем: " + statistics.maxUserRequests());


        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


}

