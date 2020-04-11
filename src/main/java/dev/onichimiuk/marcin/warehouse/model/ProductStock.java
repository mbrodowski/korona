package dev.onichimiuk.marcin.warehouse.model;

import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

@Data
@Entity
@Table(name = "product_stocks")
public class ProductStock {
    @Id
    @GeneratedValue(generator = "inc")
    @GenericGenerator(name = "inc", strategy = "increment")
    private Long id;
    private String productCode;
    private Long amount;

    @JsonIgnore
    @ManyToOne
    private Warehouse warehouse;
}
