package ru.courses.pars;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class LogEntry {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH);

    private final String ipAddr;
    private final LocalDateTime time;
    private final RequestType metod;
    private final String path;
    private final int statusCodes;
    private final int size;
    private final String referer;
    private final UserAgent userAgent;

    public LogEntry(String logLine) {//конструктор разбирает строку на составляющие и устанавливает значения всех полей класса

        String[] splited = logLine.split("\" \"");//создаём массив и складываем разделённые строки по пробелу в кавычках
        String firstBlock = splited[0]; //записываем первый блок (айпи, дата, тип запроса, путь, код ответа, размер, реферер
        firstBlock = firstBlock.replace("\"", "");//убираем кавычки
        String[] splitedFirstBlock = firstBlock.split("(\\]\\s)|(\\s\\[)|(\\s(?!\\+))");

        try { //проверка, что в массиве есть все необходимые ячейки
            this.ipAddr = splitedFirstBlock[0];
            this.time = LocalDateTime.parse(splitedFirstBlock[3], formatter);
            this.metod = RequestType.valueOf(splitedFirstBlock[4]);
            this.path = splitedFirstBlock[5];
            this.statusCodes = Integer.parseInt(splitedFirstBlock[7]);
            this.size = Integer.parseInt(splitedFirstBlock[8]);
            this.referer = splitedFirstBlock[9];
        } catch (Exception e) {
            System.out.println("неверный формат строки" + e.getMessage());
            throw e;
        }

        String lastBlock = splited[splited.length - 1];//записываем последний блок в переменную
        this.userAgent = new UserAgent(lastBlock);

    }

    public String getip() {
        return ipAddr;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public RequestType getMetod() {
        return metod;
    }

    public String getPath() {
        return path;
    }

    public int getStatusCodes() {
        return statusCodes;
    }

    public int getSize() {
        return size;
    }

    public String getReferer() {
        return referer;
    }

    public UserAgent getUserAgent() {
        return userAgent;
    }
}
