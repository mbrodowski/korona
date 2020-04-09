package dev.onichimiuk.marcin.warehouse;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "warehouses")
public class Warehouse {
    @Id
    @GeneratedValue(generator = "inc")
    @GenericGenerator(name = "inc", strategy = "increment")
    private Integer id;
    private String city;
    private Integer x;
    private Integer y;
    private Integer rice;
    private Integer pasta;
    private Integer water;

    /**
     * Hibernate needs it. (JPA)
     */
    @SuppressWarnings("unused")
    public Warehouse() {
    }

    public Warehouse(Integer id, String city, Integer x, Integer y, Integer... productsAmount) {
        this.id = id;
        this.city = city;
        this.x = x;
        this.y = y;
        rice = productsAmount[0];
        pasta = productsAmount[1];
        water = productsAmount[2];
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

    public Integer getRice() {
        return rice;
    }

    public void setRice(Integer rice) {
        this.rice = rice;
    }

    public Integer getPasta() {
        return pasta;
    }

    public void setPasta(Integer pasta) {
        this.pasta = pasta;
    }

    public Integer getWater() {
        return water;
    }

    public void setWater(Integer water) {
        this.water = water;
    }
}
