package model;

/**
 * TODO: Date to date data type
 */

import java.time.LocalDate;

public class ZReading{
    private String date;
    private double total;

    public ZReading(String date, double total){
        this.date = date;
        this.total = total;
    }

    public String getDate(){
        return date;
    }

    public double getTotal(){
        return total;
    }
}