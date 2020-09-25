package entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class OrderLine implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderLine_id;
    private int quantity;

    @ManyToOne
    private ItemType item;

    @ManyToOne
    private Ordre ordre;

    public OrderLine(ItemType item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    public OrderLine() {
    }

    public int getOrderLine_id() {
        return orderLine_id;
    }

    public void setOrderLine_id(int orderLine_id) {
        this.orderLine_id = orderLine_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public ItemType getItem() {
        return item;
    }

    public void setItem(ItemType item) {
        this.item = item;
    }

    public Ordre getOrdre() {
        return ordre;
    }

    public void setOrdre(Ordre ordre) {
        this.ordre = ordre;
    }

}
