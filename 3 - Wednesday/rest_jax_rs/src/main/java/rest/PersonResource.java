package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.PersonDTO;
import dto.PersonsDTO;
import exceptions.MissingInput;
import exceptions.PersonNotFound;
import utils.EMF_Creator;
import facades.PersonFacade;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("person")
public class PersonResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static final PersonFacade FACADE =  PersonFacade.getPersonFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
            
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String demo() {
        return "{\"msg\":\"Person API is up and running\"}";
    }
    
    @POST
    @Path("/add")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public String addPerson(String person) throws MissingInput {
        PersonDTO pDTO = GSON.fromJson(person, PersonDTO.class);
        pDTO = FACADE.addPerson(pDTO);
        String jsonString = GSON.toJson(pDTO);
        return jsonString;
    }
    
    @DELETE
    @Path("/delete/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public String deletePerson(@PathParam("id") int id) throws PersonNotFound {
        PersonDTO pDTO = FACADE.deletePerson(id);
        String jsonString = GSON.toJson(pDTO);
        return jsonString;
    }
    
    @GET
    @Path("/get/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public String getPerson(@PathParam("id") int id) throws PersonNotFound {
        PersonDTO pDTO = FACADE.getPerson(id);
        String jsonString = GSON.toJson(pDTO);
        return jsonString;
    }
    
    @GET
    @Path("/all")
    @Produces({MediaType.APPLICATION_JSON})
    public String getAllPersons() {
        PersonsDTO persons = FACADE.getAllPersons();
        String jsonString = GSON.toJson(persons);
        return jsonString;
    }
    
    @PUT
    @Path("/edit/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public String editPerson(@PathParam("id") int id, String person) throws PersonNotFound, MissingInput {
        PersonDTO pDTO = GSON.fromJson(person, PersonDTO.class);
        pDTO.setId(id);
        pDTO = FACADE.editPerson(pDTO);
        String jsonString = GSON.toJson(pDTO);
        return jsonString;
    }
    
    @GET
    @Path("/count")
    @Produces({MediaType.APPLICATION_JSON})
    public String getPersonCount() {
        long count = FACADE.getPersonCount();
        return "{\"count\":"+count+"}"; 
    }
    
}
