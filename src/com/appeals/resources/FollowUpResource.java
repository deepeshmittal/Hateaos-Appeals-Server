package com.appeals.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;

import com.appeals.activities.AddAppealFollowUpActivity;
import com.appeals.representation.AppealsRepresentation;
import com.appeals.representation.AppealsUri;
import com.appeals.representation.NoSuchAppealException;
import com.appeals.representation.UpdateException;
import com.appeals.object.Identifier;
import com.appeals.representation.Link;
import com.appeals.representation.Representation;


@Path("/followup")
public class FollowUpResource {
	
    private @Context UriInfo uriInfo;

    public FollowUpResource() {
    }

    /**
     * Used in test cases only to allow the injection of a mock UriInfo.
     * 
     * @param uriInfo
     */
    public FollowUpResource(UriInfo uriInfo) {
        this.uriInfo = uriInfo;  
    }
	
    @PUT
    @Path("/{appealID}")
    @Consumes("application/vnd-cse564-appeals+xml")
    @Produces("application/vnd-cse564-appeals+xml")
    public Response createAppeal(String appealxml) {
        
    	Response response;
        
        try {
        	AppealsRepresentation responseRepresentation = new AddAppealFollowUpActivity().followup(AppealsRepresentation.fromXmlString(appealxml).getAppeal(), new AppealsUri(uriInfo.getRequestUri()));
            response = Response.status(Status.OK).entity(responseRepresentation).build();
        } catch (NoSuchAppealException nsoe) {
            response = Response.status(Status.NOT_FOUND).build();
        } catch(UpdateException ode) {
        	Identifier identifier = new AppealsUri(uriInfo.getRequestUri()).getId();
            Link link = new Link(Representation.SELF_REL_VALUE, new AppealsUri(uriInfo.getBaseUri().toString() + "appeals/" + identifier));
            response = Response.status(Status.FORBIDDEN).entity(link).build();
        } catch (Exception ex) {
            response = Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
                
        return response;
    }

}
