package my_api;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class GenericExceptionMapper implements ExceptionMapper<Throwable> {
	//Map the uncatched generic exceptions into server error message for the client.

	
	public Response toResponse(Throwable ex) {
		ErrorMessage errorMessage = new ErrorMessage();		
		errorMessage.setStatus(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()); 
		errorMessage.setCode(5001);
		errorMessage.setMessage(Response.Status.INTERNAL_SERVER_ERROR.getReasonPhrase());
				
		return Response.status(errorMessage.getStatus())
				.entity(errorMessage)
				.type(MediaType.APPLICATION_JSON)
				.build();	
	}
}

