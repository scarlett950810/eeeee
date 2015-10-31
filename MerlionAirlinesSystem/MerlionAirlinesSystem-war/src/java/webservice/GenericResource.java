/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice;

import imas.distribution.sessionbean.FlightLookupSessionBeanLocal;
import imas.inventory.entity.BookingClassEntity;
import imas.planning.entity.FlightEntity;
import imas.planning.entity.AirportEntity;
import imas.planning.entity.RouteEntity;
import java.text.DecimalFormat;
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
    public List<TicketFlightEntity> getOneWayFlightsByRouteDate(@QueryParam("origin") String origin, @QueryParam("destination") String destination,@QueryParam("departureD") String departureD,@QueryParam("bcName") String bcName) throws ParseException 
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
         SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("d MMM yyyy HH:mm");
         SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("EEE MMM d yyyy");
         SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat("h:mm a");
         
        List<TicketFlightEntity> test = new ArrayList<>();
        for(FlightEntity f: flights){
            List<BookingClassEntity> bcs = new ArrayList<>();
            if(!bcName.equals("All Classes")){
             bcs = flightLookup.getAvailableBookingClassUnderFlightUnderSeatClass(f, bcName, 1);
            }else{
                List<BookingClassEntity> bcs1 = flightLookup.getAvailableBookingClassUnderFlightUnderSeatClass(f, "First Class", 1);
                List<BookingClassEntity> bcs2 = flightLookup.getAvailableBookingClassUnderFlightUnderSeatClass(f, "Business Class", 1);
                List<BookingClassEntity> bcs3 = flightLookup.getAvailableBookingClassUnderFlightUnderSeatClass(f, "Premium Economy Class", 1);
                List<BookingClassEntity> bcs4 = flightLookup.getAvailableBookingClassUnderFlightUnderSeatClass(f, "Economy Class", 1);
                bcs = bcs1;
                bcs.addAll(bcs2);
                bcs.addAll(bcs3);
                bcs.addAll(bcs4);
            }
            for(BookingClassEntity bc: bcs){
            TicketFlightEntity f1 = new TicketFlightEntity();
            f1.setId(f.getId());
            f1.setFlightNo(f.getFlightNo());
            f1.setDepartureDate(simpleDateFormat1.format(f.getDepartureDate()));
            f1.setArrivalDate(simpleDateFormat1.format(f.getArrivalDate()));
             DecimalFormat df = new DecimalFormat("0.0");
             
            f1.setPrice(bc.getPrice());
            f1.setBookingClassName(bc.getSeatClass());
            f1.setOrigin(origin);
            f1.setDestination(destination);
            f1.setOriAirportName(originA.getAirportName());
            f1.setOriAirportCode(originA.getAirportCode());
            f1.setDesAirportName(destinationA.getAirportName());
            f1.setDesAirportCode(destinationA.getAirportCode());
            f1.setAircraftTailN(f.getAircraft().getAircraftType().getIATACode());
            f1.setDepDayWE(simpleDateFormat2.format(f.getDepartureDate()));
            f1.setDepTimeE(simpleDateFormat3.format(f.getDepartureDate()));
            f1.setAriDayWE(simpleDateFormat2.format(f.getArrivalDate()));
            f1.setAriTimeE(simpleDateFormat3.format(f.getArrivalDate()));
            long diff = f.getArrivalDate().getTime() - f.getDepartureDate().getTime();
            double diff1 = diff / (60 * 60 * 1000) % 24;
 
            String timeD = df.format(diff1);
            f1.setTimeDuration(timeD);
            test.add(f1);
            }
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
