package ru.voskhod.tik;

public class TurnOut {
    private String time;
    private Integer total;
    private Integer current;
    private Integer data;

    public Integer getData() {
        return data;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public void setCurrent(Integer current) {
        this.current = current;
    }

    public String getTime() {
        return time;
    }

    public Integer getTotal() {
        return total;
    }

    public Integer getCurrent() {
        return current;
    }
    public TurnOut(Integer total, Integer current, String time) {
        this.total = total;
        this.current = current;
        this.time = time;
    }
}
