package dev.onichimiuk.marcin.warehouse;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "warehouses")
class Warehouse {
    @Id
    @GeneratedValue(generator="inc")
    @GenericGenerator(name="inc", strategy = "increment")
    private  Integer id;
    private  String city;
    private  Integer x;
    private  Integer y;
    private  Boolean rice;
    private  Boolean pasta;
    private  Boolean water;

    /**
     * Hibernate needs it. (JPA)
     */
    @SuppressWarnings("unused")
    public Warehouse() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Boolean getRice() {
        return rice;
    }

    public void setRice(Boolean rice) {
        this.rice = rice;
    }

    public Boolean getPasta() {
        return pasta;
    }

    public void setPasta(Boolean pasta) {
        this.pasta = pasta;
    }

    public Boolean getWater() {
        return water;
    }

    public void setWater(Boolean water) {
        this.water = water;
    }
}
