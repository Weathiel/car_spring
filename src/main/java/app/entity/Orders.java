package app.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "orders")
public class Orders implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    @OneToOne(fetch = FetchType.EAGER)
    Engine engine;
    @OneToOne(fetch = FetchType.EAGER)
    Tire tire;
    @OneToOne(fetch = FetchType.EAGER)
    Color color;
    @OneToOne(fetch = FetchType.EAGER)
    Details details;
    private Boolean archivized;
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    private double price;

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean getArchivized() {
        return archivized;
    }

    public void setArchivized(Boolean archivized) {
        this.archivized = archivized;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Engine getEngine() {
        return engine;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    public Tire getTire() {
        return tire;
    }

    public void setTire(Tire tire) {
        this.tire = tire;
    }

    public Details getDetails() {
        return details;
    }

    public void setDetails(Details details) {
        this.details = details;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
