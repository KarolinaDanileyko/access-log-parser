package ru.courses.pars;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Statistics {
    int totalTraffic;
    private LocalDateTime minTime;
    private LocalDateTime maxTime;
    private HashSet<String> addreses;//список всех существующих страниц сайта
    private HashSet<String> badAddreses;//список всех НЕсуществующих страниц сайта (404)

    private HashMap<String, Integer> statisticsOs;//статистика ОС
    private HashMap<String, Integer> statisticsBrowsers;//статистика браузеров

    public Statistics() { //конструктор без параметров, в котором должны инициализироваться переменные класса
        this.minTime = null;
        this.maxTime = null;
        this.totalTraffic = 0;
        this.addreses = new HashSet<>();
        this.badAddreses=new HashSet<>();
        this.statisticsOs = new HashMap<>();
        this.statisticsBrowsers=new HashMap<>();
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

        if(statisticsOs.containsKey(osName)){//если содержит ключ, совпадающий с пришедшей в логе ОС

            Integer i= statisticsOs.get(osName);//записывает кол-во найденных ОС
            i++;
            statisticsOs.put(osName, i);//обновляет значение кол-ва встречаемых этих ОС

        } else {
            statisticsOs.put(osName, 1);//если встретилось первый раз (не содержится в map)
        }

        String browserType=log.getUserAgent().getBrowser().toString();

        if(statisticsBrowsers.containsKey(browserType)){//если содержит ключ, совпадающий с пришедшем в логе браузера

            Integer i= statisticsBrowsers.get(browserType);//записывает кол-во найденных браузеров
            i++;
            statisticsBrowsers.put(browserType, i);//обновляет значение кол-ва встречаемых этих браузеров

        } else {
            statisticsBrowsers.put(browserType, 1);//если встретилось первый раз (не содержится в map)
        }


        addExistingAddreses(log);
        addBadAddreses(log);

    }

    private void addExistingAddreses(LogEntry log){//метод добавления существующего (200) адреса в общий список
        if(log.getStatusCodes()==200){//если код 200
            addreses.add(log.getPath());//добавить страницу в HashSet
        }
    }

    private void addBadAddreses(LogEntry log){//метод добавления НЕсуществующих (404) страниц в общий список
        if(log.getStatusCodes()==404){//если код 404
            badAddreses.add(log.getPath());//добавить страницу в HashSet
        }
    }


    public HashMap<String, Double> getOsStatictics(){// метод, возвращающий статистику операционных систем
        int i = 0; //общее кол-во всех ОС
        HashMap<String, Double> result=new HashMap<>();
        for(Map.Entry<String,Integer> entry :statisticsOs.entrySet()){//цикл for-each обходит все элементы коллекции
           i += entry.getValue();
        }

        for(Map.Entry<String,Integer> entry :statisticsOs.entrySet()){//цикл for-each обходит все элементы коллекции
            result.put(entry.getKey(), Double.valueOf(entry.getValue())/i); //разделить конкретную ОС на общее кол-во, записать в HashMap<String, Double> (Ключ , Значение )
        }
        return result;
    }

    public HashMap<String, Double> getBrowsersStatictics(){// метод, возвращающий статистику браузеров
        int i = 0; //общее кол-во всех браузеров
        HashMap<String, Double> result=new HashMap<>();
        for(Map.Entry<String,Integer> entry :statisticsBrowsers.entrySet()){//цикл for-each обходит все элементы коллекции
            i += entry.getValue();
        }

        for(Map.Entry<String,Integer> entry :statisticsBrowsers.entrySet()){//цикл for-each обходит все элементы коллекции
            result.put(entry.getKey(), Double.valueOf(entry.getValue())/i); //разделить конкретный браузер на общее кол-во, записать в HashMap<String, Double> (Ключ , Значение )
        }
        return result;
    }


    public long getTrafficRate() { //метод, вычисляющий объём часового трафика
        Duration duration = Duration.between(maxTime, minTime);//разница
        long hours = duration.toHours(); //разнца в часах
        return (totalTraffic / hours);
    }

    public HashSet<String> getExistingAddreses() {//метод возврата списка всех сохранённых существующих страниц с кодом 200
        return addreses;
    }

    public HashSet<String> getBadAddreses() {
        return badAddreses;
    }

}
