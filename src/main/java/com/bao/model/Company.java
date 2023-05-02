package com.bao.model;

public class Company {
    private Integer id;
    private String name;
    private String foundationDate;
    private Integer capital;
    private String country;
    private Boolean isHeadQuarter;

    // Getter, Setter
    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFoundationDate() {
        return foundationDate;
    }

    public Integer getCapital() {
        return capital;
    }

    public String getCountry() {
        return country;
    }

    public Boolean getHeadQuarter() {
        return isHeadQuarter;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFoundationDate(String foundationDate) {
        this.foundationDate = foundationDate;
    }

    public void setCapital(Integer capital) {
        this.capital = capital;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setHeadQuarter(Boolean headQuarter) {
        isHeadQuarter = headQuarter;
    }

    @Override
    public String toString() {
        return "Company{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", foundationDate='" + foundationDate + '\'' +
                ", capital='" + capital + '\'' +
                ", country='" + country + '\'' +
                ", isHeadQuarter='" + isHeadQuarter + '\'' +
                '}';
    }

}
