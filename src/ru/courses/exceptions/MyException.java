package ru.courses.exceptions;

public class MyException extends RuntimeException{//непроверяемое исключение


    public MyException(String message) {
        super(message);
    }


}
