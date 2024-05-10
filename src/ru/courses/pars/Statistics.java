package ru.courses.pars;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.stream.Collectors;

public class Statistics {
    int totalTraffic;
    private LocalDateTime minTime;
    private LocalDateTime maxTime;
    private HashSet<String> addresses;//список всех существующих страниц сайта
    private HashSet<String> badAddresses;//список всех НЕсуществующих страниц сайта (404)
    private HashMap<String, Integer> statisticsOs;//статистика ОС
    private HashMap<String, Integer> statisticsBrowsers;//статистика браузеров
    private HashMap<String, Integer> statisticsOfAddresses; //мапа для адресов сайтов и кол-ва их посещений
    private HashMap<String, Integer> statisticsOfIp; //мапа для ip пользователей и кол-ва его посещений
    private int countOfFail;//переменная для подсчёта ошибочных запросов

    public Statistics() { //конструктор без параметров, в котором должны инициализироваться переменные класса
        this.minTime = null;
        this.maxTime = null;
        this.totalTraffic = 0;
        this.addresses = new HashSet<>();
        this.badAddresses = new HashSet<>();
        this.statisticsOs = new HashMap<>();
        this.statisticsBrowsers = new HashMap<>();
        this.statisticsOfAddresses = new HashMap<>();
        this.statisticsOfIp = new HashMap<>();
    }

    public void addEntry(LogEntry log) {//метод, принимающий в качестве параметра объект класса LogEntry
        totalTraffic += log.getSize(); //суммировать все size в totalTraffic
        LocalDateTime time = log.getTime(); //записали из лога дату и время

        if (minTime == null || maxTime == null) {
            minTime = time;
            maxTime = time;
        }

        if (minTime.isAfter(time)) {
            minTime = time;
        }

        if (maxTime.isBefore(time)) {
            maxTime = time;
        }

        String osName = log.getUserAgent().getOsType().toString();
        //иф заполнения мапы OC
        if (statisticsOs.containsKey(osName)) {//если содержит ключ, совпадающий с пришедшей в логе ОС

            Integer i = statisticsOs.get(osName);//записывает кол-во найденных ОС
            i++;
            statisticsOs.put(osName, i);//обновляет значение кол-ва встречаемых этих ОС

        } else {
            statisticsOs.put(osName, 1);//если встретилось первый раз (не содержится в map)
        }

        String browserType = log.getUserAgent().getBrowser().toString();
        //иф заполнения мапы браузеров
        if (statisticsBrowsers.containsKey(browserType)) {//если содержит ключ, совпадающий с пришедшем в логе браузера

            Integer i = statisticsBrowsers.get(browserType);//записывает кол-во найденных браузеров
            i++;
            statisticsBrowsers.put(browserType, i);//обновляет значение кол-ва встречаемых этих браузеров

        } else {
            statisticsBrowsers.put(browserType, 1);//если встретилось первый раз (не содержится в map)
        }

        String address = log.getPath();
        //иф заполнения мапы для адресов сайтов и кол-ва их посещений
        if (!log.getUserAgent().isBot()) {
            if (statisticsOfAddresses.containsKey(address)) {

                Integer i = statisticsOfAddresses.get(address);
                i++;
                statisticsOfAddresses.put(address, i);

            } else {
                statisticsOfAddresses.put(address, 1);
            }

        }

        if (log.getStatusCodes() >= 400 && log.getStatusCodes() < 600) {
            countOfFail++;//увеличиваем если код ответа содержит коды от 400 до 599
        }

        String ip = log.getIp();
        //иф для заполнения мапы ip и посещений
        if (!log.getUserAgent().isBot()) {
            if (statisticsOfIp.containsKey(ip)) {

                Integer i = statisticsOfIp.get(ip);
                i++;
                statisticsOfIp.put(ip, i);

            } else {
                statisticsOfIp.put(ip, 1);
            }

        }
        addExistingAddresses(log);
        addBadAddresses(log);
    }

    private void addExistingAddresses(LogEntry log) {//метод добавления существующего (200) адреса в общий список
        if (log.getStatusCodes() == 200) {//если код 200
            addresses.add(log.getPath());//добавить страницу в HashSet
        }
    }

    private void addBadAddresses(LogEntry log) {//метод добавления НЕсуществующих (404) страниц в общий список
        if (log.getStatusCodes() == 404) {//если код 404
            badAddresses.add(log.getPath());//добавить страницу в HashSet
        }
    }

    public double countOfFailByHours() {//Метод подсчёта среднего количества ошибочных запросов в час
        return countOfFail / (double) getHours(); //поделить кол-во ошибочных запросов на кол-во часов
    }


    public HashMap<String, Double> getOsStatictics() {// метод возвращает статистику операционных систем
        int i = 0; //общее кол-во всех ОС
        HashMap<String, Double> result = new HashMap<>();
        for (Map.Entry<String, Integer> entry : statisticsOs.entrySet()) {//цикл for-each обходит все элементы коллекции
            i += entry.getValue();
        }

        for (Map.Entry<String, Integer> entry : statisticsOs.entrySet()) {//цикл for-each обходит все элементы коллекции
            result.put(entry.getKey(), Double.valueOf(entry.getValue()) / i); //разделить конкретную ОС на общее кол-во, записать в HashMap<String, Double> (Ключ , Значение )
        }
        return result;
    }

    public HashMap<String, Double> getBrowsersStatictics() {//метод возвращает статистику браузеров
        int i = 0; //общее кол-во всех браузеров
        HashMap<String, Double> result = new HashMap<>();
        for (Map.Entry<String, Integer> entry : statisticsBrowsers.entrySet()) {//цикл for-each обходит все элементы коллекции
            i += entry.getValue();
        }

        for (Map.Entry<String, Integer> entry : statisticsBrowsers.entrySet()) {//цикл for-each обходит все элементы коллекции
            result.put(entry.getKey(), Double.valueOf(entry.getValue()) / i); //разделить конкретный браузер на общее кол-во, записать в HashMap<String, Double> (Ключ , Значение )
        }
        return result;
    }

    public long getTrafficRate() { //метод возвращает объём часового трафика
        return (totalTraffic / getHours());
    }

    private long getHours() {//метод возвращает общее количество часов в логе
        Duration duration = Duration.between(minTime, maxTime);
        return duration.toHours();
    }

    public Map<String, Double> getAddressesByHours() {//Метод подсчёта среднего количества посещений сайта за час
        return statisticsOfAddresses.entrySet()// Метод entrySet() возвращает список всех пар (ключ, знач) в HashMap
                .stream()//запуск потока от пар мапы
                .collect(Collectors.toMap( //метод collect вычитывает данные из потока и возвращает их в виде коллекции, метод toMap преобразует поток в мап (ключ, значение)
                        entry -> entry.getKey(),//ключ - сайт
                        entry -> entry.getValue() / (double) getHours()));//значение -(кол-во посещений сайта/кол-во часов в логе)

    }

    public double getAverageAttendance() {//Метод расчёта средней посещаемости одним пользователем
        int i = 0; //общее кол-во всех посещений
        HashMap<String, Double> result = new HashMap<>();
        for (Map.Entry<String, Integer> entry : statisticsOfIp.entrySet()) {//цикл for-each обходит все элементы коллекции
            i += entry.getValue();
        }
        return i/(double) statisticsOfIp.size(); //общее кол-во посещений пользователями делить на число уникальных IP
    }


    public HashSet<String> getExistingAddresses() {//метод возврата списка существующих страниц
        return addresses;
    }

    public HashSet<String> getBadAddresses() {//метод возврата списка несуществующих страниц
        return badAddresses;
    }

}
