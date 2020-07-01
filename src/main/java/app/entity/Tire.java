package app.entity;

import javax.persistence.*;

@Entity
@Table(name = "tire")
public class Tire {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    private int size;

    private String producent;

    private double price;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getProducent() {
        return producent;
    }

    public void setProducent(String producent) {
        this.producent = producent;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
