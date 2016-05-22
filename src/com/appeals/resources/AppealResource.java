package com.appeals.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;

import com.appeals.activities.CreateAppealActivity;
import com.appeals.activities.GetAppealActivity;
import com.appeals.activities.RemoveAppealActivity;
import com.appeals.activities.UpdateAppealActivity;
import com.appeals.object.Identifier;
import com.appeals.representation.AppealDeletionException;
import com.appeals.representation.AppealsRepresentation;
import com.appeals.representation.AppealsUri;
import com.appeals.representation.InvalidAppealException;
import com.appeals.representation.Link;
import com.appeals.representation.NoSuchAppealException;
import com.appeals.representation.Representation;
import com.appeals.representation.UpdateException;


@Path("/appeals")
public class AppealResource {
	
    private @Context UriInfo uriInfo;

    public AppealResource() {
    }

    /**
     * Used in test cases only to allow the injection of a mock UriInfo.
     * 
     * @param uriInfo
     */
    public AppealResource(UriInfo uriInfo) {
        this.uriInfo = uriInfo;  
    }
	
    @POST
    @Consumes("application/vnd-cse564-appeals+xml")
    @Produces("application/vnd-cse564-appeals+xml")
    public Response createAppeal(String appealxml) {
        
    	Response response;
        
        try {
        	AppealsRepresentation responseRepresentation = new CreateAppealActivity().create(AppealsRepresentation.fromXmlString(appealxml).getAppeal(), new AppealsUri(uriInfo.getRequestUri()));
            response = Response.created(responseRepresentation.getUpdateLink().getUri()).entity(responseRepresentation).build();
        } catch (InvalidAppealException ioe) {
            response = Response.status(Status.BAD_REQUEST).build();
        } catch (Exception ex) {
            response = Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
                
        return response;
    }
    
    @DELETE
    @Path("/{appealID}")
    @Produces("application/vnd-cse564-appeals+xml")
    public Response deleteAppeal() {
        
        Response response;
        
        try {
        	AppealsRepresentation removedAppeal = new RemoveAppealActivity().delete(new AppealsUri(uriInfo.getRequestUri()));
            response = Response.status(Status.OK).entity(removedAppeal).build();
        } catch (NoSuchAppealException nsoe) {
            response = Response.status(Status.NOT_FOUND).build();
        } catch(AppealDeletionException ode) {
            response = Response.status(Status.NOT_ACCEPTABLE).header("Allow", "GET").build();
        } catch (Exception ex) {
            response = Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
                
        return response;
    }
    
    @GET
    @Path("/{appealID}")
    @Produces("application/vnd-cse564-appeals+xml")
    public Response getAppealInfo() {
        
        Response response;
        
        try {
        	AppealsRepresentation retrievedAppeal = new GetAppealActivity().retrieveByUri(new AppealsUri(uriInfo.getRequestUri()));
            response = Response.status(Status.OK).entity(retrievedAppeal).build();
        } catch (NoSuchAppealException nsoe) {
            response = Response.status(Status.NOT_FOUND).build();
        } catch (Exception ex) {
            response = Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
                
        return response;
    }
    
    @PUT
    @Path("/{appealID}")
    @Consumes("application/vnd-cse564-appeals+xml")
    @Produces("application/vnd-cse564-appeals+xml")
    public Response updateAppeal(String appealxml) {
        
    	Response response;
        
        try {
        	AppealsRepresentation responseRepresentation = new UpdateAppealActivity().update(AppealsRepresentation.fromXmlString(appealxml).getAppeal(), new AppealsUri(uriInfo.getRequestUri()));
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
