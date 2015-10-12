/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice;

import imas.planning.entity.FlightEntity;
import java.util.Date;
import java.util.List;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *
 * @author ruicai
 */
@Path("generic")
public class GenericResource {

    @Context
    private UriInfo context;
   
    /**
     * Creates a new instance of GenericResource
     */
    public GenericResource() {
    }
    
    @GET
    @Path(value = "getOneWayFlightsByRouteDate")
    @Produces(MediaType.APPLICATION_JSON)
    public FlightsList getOneWayFlightsByRouteDate(@QueryParam("origin") String origin, @QueryParam("destination") String destination,@QueryParam("departureD") Date departureD) 
    {
        System.err.println("Server running: getOneWayflightsby route and date execute");
        
        
        return new FlightsList();
    }

    /**
     * Retrieves representation of an instance of webservice.GenericResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    public String getJson() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    /**
     * PUT method for updating or creating an instance of GenericResource
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/json")
    public void putJson(String content) {
    }
}
