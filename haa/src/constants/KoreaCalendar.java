// 
// Decompiled by Procyon v0.5.36
// 

package constants;

import java.util.Calendar;

public class KoreaCalendar
{
    private String years;
    private String months;
    private String days;
    private String hours;
    private String mins;
    private String secs;
    private int yeal;
    private int month;
    private int dayt;
    private int hour;
    private int min;
    private int sec;
    
    public KoreaCalendar() {
        final Calendar ocal = Calendar.getInstance();
        this.years = "" + ocal.get(1) + "";
        this.months = "" + (ocal.get(2) + 1) + "";
        this.days = "" + ocal.get(5) + "";
        this.hours = "" + ocal.get(11) + "";
        this.mins = "" + ocal.get(12) + "";
        this.secs = "" + ocal.get(13) + "";
        this.yeal = ocal.get(1);
        this.month = ocal.get(2) + 1;
        this.dayt = ocal.get(5);
        this.hour = ocal.get(11);
        this.min = ocal.get(12);
        this.sec = ocal.get(13);
        if (this.month < 10) {
            this.months = "0" + this.month;
        }
        if (this.dayt < 10) {
            this.days = "0" + this.dayt;
        }
        if (this.hour < 10) {
            this.hours = "0" + this.hour;
        }
        if (this.min < 10) {
            this.mins = "0" + this.min;
        }
        if (this.sec < 10) {
            this.secs = "0" + this.sec;
        }
    }
    
    public String getYears() {
        return this.years;
    }
    
    public String getMonths() {
        return this.months;
    }
    
    public String getDays() {
        return this.days;
    }
    
    public String getHours() {
        return this.hours;
    }
    
    public String getMins() {
        return this.mins;
    }
    
    public String getSecs() {
        return this.secs;
    }
    
    public int getYeal() {
        return this.yeal;
    }
    
    public int getMonth() {
        return this.month;
    }
    
    public int getDayt() {
        return this.dayt;
    }
    
    public int getHour() {
        return this.hour;
    }
    
    public int getMin() {
        return this.min;
    }
    
    public int getSec() {
        return this.sec;
    }
}
