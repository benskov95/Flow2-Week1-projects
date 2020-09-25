package entity2;

import entity1.Customer;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class Address implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    private String street;
    private String city;
    
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "Customer_ID")
//    private Customer customer;
    
    @ManyToMany(mappedBy = "addresses")
    private List<Customer> customers;
    
    
    
    /*There seems to be no difference between creating a unidirectional
    one to one relationship between Customer and Address, and creating
    a bidirectional relationship between them (at least not in the database).
    The main difference between unidirectional and bidirectional is that the
    primary key of the Customer table is shared directly with the Address
    table in a bidrectional relationship, while the Address table would have
    its own unique primary key (which would be used as a foreign key in
    the Customer table) in a unidirectional relationship. 
    
    This basically means that every time a customer is added to the 
    Customer table, a new address would have to be added as well
    (since the ADDRESS_ID in Customer would otherwise point to a
    non-existent address). Therefore, all addresses (or at least their
    IDs) are unique to each customer, and two customers can't have
    the same address ID. 
    
    That is at least how it's supposed to work from what I can gather
    online. Looking at the tables generated, however, I see no indication
    that Customer and Address have a shared primary key.*/
//    @OneToOne(mappedBy = "address")
//    private Customer customer;
//    

    public Address() {
    }

    public Address(String street, String city) {
        this.street = street;
        this.city = city;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    // For exercise 4
//    public Customer getCustomer() {
//        return customer;
//    }
//
//    public void setCustomer(Customer customer) {
//        this.customer = customer;
//    }

    public void addCustomer(Customer customer) {
        this.customers.add(customer);
    }
    
    public List<Customer> getCustomers() {
        return customers;
    } 
}
