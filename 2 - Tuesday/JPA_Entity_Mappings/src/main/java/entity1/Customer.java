package entity1;

import entity2.Address;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    private String firstName;
    private String lastName;
    
    /* I put the Customer and Address entity classes in separate 
    folders because I didn't get the option to create a relationship
    between them when they were in the same folder.*/

//    @OneToOne
//    private Address address;
    
    // BEFORE USING @JoinColumn
    /* Generates 3 tables: CUSTOMER, ADDRESS and CUSTOMER_ADDRESS.
    The Customer table is created from this (Customer) entity class, and has
    all of its fields/attributes (ID, firstName, lastName). The same applies for
    the Address table. The third table, CUSTOMER_ADDRESS, contains two
    columns: 'Customer_ID' and 'addresses_id'. It basically seems to serve as
    a link table between Customer and Address, and links customers with any
    number of addresses they might have. This also means there is no direct
    relationship between Customer and Address.*/
//    @JoinColumn (name = "Customer_id")
//    @OneToMany
    
    /*The 'mappedBy' argument is used to determine
    which specific entity is used to create a relationship
    with another entity (indicated by what is below the
    annotation, so Address here). This is also reflected
    in the Address class, where there now is a Customer
    attribute.
    
    The extra step required for OneToMany bidrectional
    relationships is a setter method - specifically, the 
    setCustomer method in the Address entity class. If
    the customer object is not set before persisting,
    no customer IDs will be added to the address table
    as foreign keys. Therefore, I set the Customer objects
    on the Address objects in the addCustomers method in
    the Tester class. */
//    @OneToMany(mappedBy = "customer")
    
    /* Running the addCustomers method with a bidirectional
    many to many relationship seems to do the same as unidirectional
    one to many - it creates 3 tables (all with the same names
    too), that is. Customer and Address are their own separate
    tables like before, and the new one, customer_address, contains
    the relations between customer and address with their foreign
    keys. It is, like mentioned before, a link table which ties two
    other tables together. In the case of a many to many relationship,
    the purpose of this is to ensure that we can have multiples of each
    of the linked tables - as in, a customer can have many addresses,
    and an address can have many customers.*/
    @ManyToMany
    private List<Address> addresses = new ArrayList();
    

    public Customer() {
    }

    public Customer(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
     public void addAddress(Address address) {
        this.addresses.add(address);
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }
     
}
