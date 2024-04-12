package ru.courses.main;
import ru.courses.exceptions.MyException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        String path=checkFile();//получение правильного пути к файлу
        file(path);//вызов метода чтения файла
        System.out.println("Нажмите <Enter> для выхода.");
        String text = new Scanner(System.in).nextLine();//создали объект сканера и указали источник данных (текст в консоли)

    }

    public static String checkFile(){
        System.out.println("Введите путь и нажмите <Enter>: ");

        while (true) {//бесконечный цикл

            String path = new Scanner(System.in).nextLine();//запрашивается в консоли путь к файлу
            File file = new File(path);//создали объект типа Файл
            boolean fileExists = file.exists();//определяем существует ли файл
            boolean isDirectory = file.isDirectory();//определяем папка ли это

            if (!fileExists || isDirectory){//если файла не существует или это папка
                System.out.println("Указанный файл не существует или это папка.");
                continue;
            }
            else {
                System.out.println("Путь указан верно.");
                return path;
            }

        }

    }

    public static void file(String path){//код, который будет построчно читать указанный файл

        try {
            FileReader fileReader = new FileReader(path);//создали чтеца fileReader файла по указанному пути (не может читать построчно)
            BufferedReader reader = new BufferedReader(fileReader);//созали чтеца reader на основе fileReader, который может читать построчно

            String line;
            int i=0;//счётчик строк
            int maxLenght=0;
            int minLenght=0;
            int yandexBot=0;
            int googlebot=0;
            while ((line = reader.readLine()) != null) {//пока строка существует

                int length = line.length();//записываем длину строки

                if (length>1024){
                    throw new MyException("Это строка длиннее 1024 символов!");
                }

                if (length>maxLenght){
                    maxLenght=length;
                }

                if (length<minLenght || i==0){
                    minLenght=length;
                }

                String[] splited=line.split("\"-\"");//создаём массив и складываем разделённые строки по "-"
                String lastBlock = splited[splited.length-1];//записываем последний блок

                if(lastBlock.contains("(") ) {

                    String[] splitedUserAgent=lastBlock.split("\\(");//создаём массив и складываем разделённые строки по "("

                    String[] parts = splitedUserAgent[splitedUserAgent.length-1].split(";");
                    if (parts.length >= 2) {
                        String fragment = parts[1];
                        fragment=fragment.replace(" ", "");
                        int indexSlash=fragment.indexOf("/");

                        if(indexSlash>=0){
                            fragment=fragment.substring(0,indexSlash);

                            if (fragment.contains("YandexBot")){
                                yandexBot++;
                            }

                            if (fragment.contains("Googlebot")){
                                googlebot++;
                            }
                        }


                    }
                }

                i++;//считаем кол-во строк в файле

            }
            fileReader.close();
//            System.out.println(maxLenght + " - самая длинная строка.");
//            System.out.println(minLenght + " - самая короткая строка.");
            System.out.println(i + " - количество строк.");
            System.out.println(yandexBot + " количество запросов от YandexBot.");
            System.out.println(googlebot + " количество запросов от Googlebot.");
            System.out.println((double)yandexBot* 100/i + " доля запросов от YandexBot.");
            System.out.println((double)googlebot* 100/i + " доля запросов от Googlebot.");

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}

//C:\Users\kdanileyko\Desktop\Study\access.log