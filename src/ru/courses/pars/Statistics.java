package ru.courses.pars;

import java.time.Duration;
import java.time.LocalDateTime;

public class Statistics {
    int totalTraffic;
    private LocalDateTime minTime;
    private LocalDateTime maxTime;

    public Statistics() { //конструктор без параметров, в котором должны инициализироваться переменные класса
        this.minTime = null;
        this.maxTime = null;
        this.totalTraffic = 0;
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

    }

    public long getTrafficRate() { //метод, вычисляющий объём часового трафика
        Duration duration = Duration.between(maxTime, minTime);//разница
        long hours = duration.toHours(); //разнца в часах
        return (totalTraffic / hours);
    }
}
