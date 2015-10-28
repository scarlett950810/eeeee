/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice;

import imas.distribution.sessionbean.FlightLookupSessionBeanLocal;
import imas.planning.entity.FlightEntity;
import imas.planning.entity.AirportEntity;
import imas.planning.entity.RouteEntity;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.naming.InitialContext;
import javax.naming.NamingException;
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
    
    @EJB
    AppFlightLookupLocal appFlightLookup;
    
    @EJB
    FlightLookupSessionBeanLocal flightLookup;
    
    @Context
    private UriInfo context;
    
    /**
     * Creates a new instance of GenericResource
     */
    public GenericResource() {
    }
    
    @GET
    @Path(value = "getOneWayFlightsByRouteDate")
    @Produces("application/json")
    public List<FlightEntity> getOneWayFlightsByRouteDate(@QueryParam("origin") String origin, @QueryParam("destination") String destination,@QueryParam("departureD") String departureD) throws ParseException 
    {
        System.err.println("Server running: getOneWayflightsby route and date execute");
        System.err.println("destination"+destination +"origin"+origin+"departure"+departureD);
        List<FlightEntity> flights = new ArrayList<FlightEntity>();
        AirportEntity originA = appFlightLookup.getOneAirportFromCityName(origin);
        AirportEntity destinationA = appFlightLookup.getOneAirportFromCityName(destination);
        if(originA == null || destinationA == null){
            System.err.println("cannot find origin or destination");
            return null;
        }
        System.err.println("origin"+originA);
        System.err.println("destination"+destinationA);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yy");
        Date depD = simpleDateFormat.parse(departureD);
        RouteEntity route = flightLookup.getDirectRoute(originA, destinationA);
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(depD);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        Date lowB = cal.getTime();
        System.err.println("Lower Bound in getOneWayFlightsByRouteDate: "+ lowB);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        Date highB = cal.getTime();
        System.err.println("Higher Bound in getOneWayFlightsByRouteDate: "+ highB);
        
        flights = flightLookup.getAvailableFlights(route, depD, highB);
        System.err.println("flights"+flights);
        List<FlightEntity> test = new ArrayList<FlightEntity>();
        for(FlightEntity f: flights){
            FlightEntity f1 = new FlightEntity();
            f1.setId(f.getId());
            f1.setFlightNo(f.getFlightNo());
            f1.setDepartureDate(f.getDepartureDate());
            f1.setArrivalDate(f.getArrivalDate());
            
            test.add(f1);
            
        }
        
        
        
        return test;
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
