package rest;

import dto.PersonDTO;
import entities.Address;
import entities.Person;
import utils.EMF_Creator;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.parsing.Parser;
import java.net.URI;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

//@Disabled
public class PersonResourceTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";
    private static Person p1,p2;
    private static Address a1, a2;
    
    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() {
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        
        httpServer = startServer();
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;
    }
    
    @AfterAll
    public static void closeTestServer(){
         EMF_Creator.endREST_TestWithDB();
         httpServer.shutdownNow();
    }
    
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        a1 = new Address("Gamlegade 3", 3822, "Guldborg");
        a2 = new Address("Sejvej 15", 3700, "Rønne");
        p1 = new Person("Erik", "Andersen", "83271281", a1);
        p2 = new Person("Maja", "Svensen", "66378212", a2);
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Person.deleteAll").executeUpdate();
            em.persist(p1);
            em.persist(p2); 
            em.getTransaction().commit();
        } finally { 
            em.close();
        }
    }
    
    @Test
    public void testServerIsUp() {
        System.out.println("Testing is server UP");
        given().when().get("/person").then().statusCode(200);
    }
   
    @Test
    public void testDemo() throws Exception {
        given()
        .contentType("application/json")
        .get("/person").then()
        .assertThat()
        .statusCode(HttpStatus.OK_200.getStatusCode())
        .body("msg", equalTo("Person API is up and running"));   
    }
    
    
    @Test
    public void testAddPerson() throws Exception {
        given()
        .contentType("application/json")
        .body(new PersonDTO("Test", "Testsen", "12345678", a1.getStreet(), a1.getZip(), a1.getCity()))
        .when()
        .post("/person/add")
        .then()
        .body("firstName", equalTo("Test"))
        .body("lastName", equalTo("Testsen"))
        .body("phone", equalTo("12345678"))
        .body("street", equalTo(a1.getStreet()));
    }
    
    @Test
    public void testDeletePerson() throws Exception {
        given()
        .contentType("application/json")
        .delete("/person/delete/{id}", p1.getId())
        .then().assertThat()
        .statusCode(HttpStatus.OK_200.getStatusCode())
        .body("phone", equalTo(p1.getPhone()));
        
        given()
        .contentType("application/json")
        .get("/person/get/{id}", p1.getId())
        .then().assertThat()
        .statusCode(HttpStatus.NOT_FOUND_404.getStatusCode())
        .body("message", equalTo("Person with the provided ID was not found."));
    }
    
    @Test
    public void testGetPerson() {
        given()
        .contentType("application/json")
        .get("/person/get/{id}", p2.getId())
        .then().assertThat()
        .statusCode(HttpStatus.OK_200.getStatusCode())
        .body("firstName", equalTo(p2.getFirstName()));
    }
    
    @Test
    public void testGetAllPersons() {
        List<PersonDTO> personsDTOs;
        personsDTOs = given()
                .contentType("application/json")
                .when()
                .get("/person/all")
                .then()
                .extract().body().jsonPath().getList("all", PersonDTO.class);
        
        PersonDTO p1DTO = new PersonDTO(p1);
        PersonDTO p2DTO = new PersonDTO(p2);
        assertThat(personsDTOs, containsInAnyOrder(p1DTO, p2DTO));
    }
    
    @Test
    public void testEditPerson() {
        String current = p1.getLastName();
        given()
        .contentType("application/json")
        .body(new PersonDTO(p1.getFirstName(), "Henriksen", p1.getPhone(), a2.getStreet(), a2.getZip(), a2.getCity()))
        .when()
        .put("/person/edit/{id}", p1.getId())
        .then()
        .body("firstName", equalTo(p1.getFirstName()))
        .body("lastName", not(current))
        .body("lastName", equalTo("Henriksen"))
        .body("phone", equalTo(p1.getPhone()))
        .body("city", equalTo(a2.getCity()));
    }
    
    @Test
    public void testGetPersonCount() throws Exception {
        given()
        .contentType("application/json")
        .get("/person/count").then()
        .assertThat()
        .statusCode(HttpStatus.OK_200.getStatusCode())
        .body("count", equalTo(2));   
    }
    
    @Test
    public void testPersonNotFoundException() {
        Person p3 = new Person();
        given()
        .contentType("application/json")
        .get("/person/get/{id}", p3.getId())
        .then().assertThat()
        .statusCode(HttpStatus.NOT_FOUND_404.getStatusCode())
        .body("message", equalTo("Person with the provided ID was not found."));
    }
    
    @Test
    public void testMissingInputException() {
        given()
        .contentType("application/json")
        .body(new PersonDTO())
        .when()
        .post("/person/add")
        .then().assertThat()
        .statusCode(HttpStatus.BAD_REQUEST_400.getStatusCode())
        .body("message", equalTo("First name, last name, phone or address info is missing."));
    }
    
}
