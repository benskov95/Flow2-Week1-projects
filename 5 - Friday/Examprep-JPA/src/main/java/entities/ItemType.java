package entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.OneToMany;

@Entity
public class ItemType implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int item_id;
    private String name;
    private String description;
    private int price;
    
    @OneToMany(mappedBy = "item")
    @JsonbTransient
    private List<OrderLine> orderLines;

    public ItemType(String name, String description, int price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }
    
    public ItemType() {
    }

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public List<OrderLine> getOrderLines() {
        return orderLines;
    }

    public void setOrderLine(List<OrderLine> orderLines) {
        this.orderLines = orderLines;
    }
    
    

}
