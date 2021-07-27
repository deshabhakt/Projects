package com.dbkt.currencyconverter.model;

public class DbRow {
    private int id;
    private String short_name;
    private String long_name;
    private Double rates;
    private String flag_location;
    private String base_currency;
    private String timestamp;

    public DbRow() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShort_name() {
        return short_name;
    }

    public void setShort_name(String short_name) {
        this.short_name = short_name;
    }

    public String getLong_name() {
        return long_name;
    }

    public void setLong_name(String long_name) {
        this.long_name = long_name;
    }

    public Double getRates() {
        return rates;
    }

    public void setRates(Double rates) {
        this.rates = rates;
    }

    public String getFlag_location() {
        return flag_location;
    }

    public void setFlag_location(String flag_location) {
        this.flag_location = flag_location;
    }

    public String getBase_currency() {
        return base_currency;
    }

    public void setBase_currency(String base_currency) {
        this.base_currency = base_currency;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
