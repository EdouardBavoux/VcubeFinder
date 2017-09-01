package my_api;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class IncorrectLocationCodeExceptionMapper implements ExceptionMapper<IncorrectLocationCodeException> {
	//Map the IncorrectLocationCodeException into relevant error message for the client.

	public Response toResponse(IncorrectLocationCodeException ex) {
		return Response.status(ex.getStatus())
				.entity(new ErrorMessage(ex))
				.type(MediaType.APPLICATION_JSON).
				build();
	}

}