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
    private HashMap<String, Integer> statisticsOs;//статистика операционных систем пользователей сайта

    public Statistics() { //конструктор без параметров, в котором должны инициализироваться переменные класса
        this.minTime = null;
        this.maxTime = null;
        this.totalTraffic = 0;
        this.addreses = new HashSet<>();
        this.statisticsOs = new HashMap<>();
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
            statisticsOs.put(log.getUserAgent().getOsType().toString(), 1);//если встретилось первый раз (не содержится в map)
        }

        addExistingAddreses(log);

    }

    private void addExistingAddreses(LogEntry log){//метод добавления существующего (200) адреса в общий список
        if(log.getStatusCodes()==200){//если код 200
            addreses.add(log.getPath());//добавить страницу в HashSet
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

    public long getTrafficRate() { //метод, вычисляющий объём часового трафика
        Duration duration = Duration.between(maxTime, minTime);//разница
        long hours = duration.toHours(); //разнца в часах
        return (totalTraffic / hours);
    }

    public HashSet<String> getExistingAddreses() {//метод возврата списка всех сохранённых существующих страниц с кодом 200
        return addreses;
    }

}
