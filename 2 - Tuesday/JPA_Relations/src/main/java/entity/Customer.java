package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;

@Entity
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    private String firstName;
    private String lastName;
    
    @ElementCollection
    @CollectionTable(
            name = "HOBBIES",
            joinColumns = @JoinColumn(name = "Customer_ID")
    )

    @Column(name = "HOBBY")
    private List<String> hobbies = new ArrayList();
    
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "PHONES",
            joinColumns = @JoinColumn(name = "Customer_ID")
    )
    
    @MapKeyColumn(name = "PHONE")
    @Column(name = "Description")
    private Map<String, String> phones = new HashMap();
            
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
    
    public void addHobby(String hobby) {
        this.hobbies.add(hobby);
    }
    
    public String getHobbies() {
        String res = "";
        for (String s : this.hobbies) {
            res += s + ", ";
        }
        return res;
    }
    
    public void addPhone(String phoneNo, String description) {
        this.phones.put(phoneNo, description);
    }
    
    public String getPhoneDescription(String phoneNo) {
        return this.phones.get(phoneNo);
    }
    
}
