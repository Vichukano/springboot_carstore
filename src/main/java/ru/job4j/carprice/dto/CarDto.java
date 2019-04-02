package ru.job4j.carprice.dto;

/**
 * Dto car object for add car form.
 */
public class CarDto {
    private String name;
    private Double price;
    private String color;
    private Integer mileage;
    private String desc;
    private Long body;
    private Long engine;
    private Long transmission;

    public CarDto() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getMileage() {
        return mileage;
    }

    public void setMileage(Integer mileage) {
        this.mileage = mileage;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Long getBody() {
        return body;
    }

    public void setBody(Long body) {
        this.body = body;
    }

    public Long getEngine() {
        return engine;
    }

    public void setEngine(Long engine) {
        this.engine = engine;
    }

    public Long getTransmission() {
        return transmission;
    }

    public void setTransmission(Long transmission) {
        this.transmission = transmission;
    }
}
