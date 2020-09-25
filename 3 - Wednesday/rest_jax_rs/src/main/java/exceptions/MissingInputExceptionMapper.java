package exceptions;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class MissingInputExceptionMapper implements ExceptionMapper<MissingInputException> {

    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public Response toResponse(MissingInputException e) {
        Logger.getLogger(MissingInputExceptionMapper.class.getName())
                .log(Level.SEVERE, null, e);
        ExceptionDTO error = new ExceptionDTO(400, e.getMessage());
        return Response
                .status(400)
                .entity(gson.toJson(error))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

}
