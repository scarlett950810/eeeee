/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.planning;

import imas.planning.entity.FlightEntity;
import imas.planning.entity.RouteEntity;
import imas.planning.sessionbean.RouteSessionBeanLocal;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author ruicai
 */
@Named(value = "scheduleManagedBean")
@ViewScoped
public class ScheduleManagedBean implements Serializable {

    private Integer flightTimes;
    private String iterationPeriod;
    @EJB
    private RouteSessionBeanLocal routeSession;
    private RouteEntity routeSelected;
    private Integer yearSelected;
    private Date dateOperate;
    private List<FlightEntity> flights;
    private List<Integer> serialNo;
    private Integer count = 0;
    private List<Date> departureDates;
    private List<FlightEntity> flightsGenerated;
    private Integer planningPeriod;

    /**
     * Creates a new instance of ScheduleManagedBean
     */
    public ScheduleManagedBean() {
    }

    @PostConstruct
    public void init() {
        System.out.println("init()");

        flightTimes = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("flightTimes");
        iterationPeriod = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("iterationPeriod");
        routeSelected = (RouteEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("routeSelected");
        System.out.println("init()");
        planningPeriod = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("planningPeriod");

        yearSelected = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("yearSelected");
        dateOperate = (Date) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("dateOperate");
        flights = (List<FlightEntity>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("flights");
        serialNo = (List<Integer>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("serialNo");
        System.out.println("init()");

        System.out.println("test" + (RouteEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("routeSelected"));

    }

//    public void generateFlightsByDay() throws ParseException, IOException {
//        flightsGenerated = new ArrayList<FlightEntity>();
//        Date departureDateTemp = dateOperate;
//
//        SimpleDateFormat myFormat = new SimpleDateFormat("dd MM yyyy");
//        String inputString1 = "31 12 " + yearSelected;
//
//        Date date1 = myFormat.parse(inputString1);
//        long diff = date1.getTime() - dateOperate.getTime();
//        System.out.println("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
//        long diffDays = diff / (24 * 60 * 60 * 1000);
//        System.err.println("diffdays:" + diffDays);
//        Calendar cal = Calendar.getInstance();
//
//        int days = (int) diffDays;
//        for (int i = 0; i < days+1; i++) {
//            for (FlightEntity f : flights) {
//                FlightEntity f1 = new FlightEntity(yearSelected);
//
//                //set the departure time of flight in the flights 
//                f1.setDepartureDate(combineTwoDate(f.getDepartureDate(), departureDateTemp));
//                f1.setArrivalDate(null);
//                f1.setReverseFlight(new FlightEntity(yearSelected));
//                f1.getReverseFlight().setRoute(routeSelected.getReverseRoute());
//                f1.setRoute(f.getRoute());
//                f1.getReverseFlight().setDepartureDate(combineTwoDate(f.getReverseFlight().getDepartureDate(), departureDateTemp));
//                f1.getReverseFlight().setArrivalDate(null);
//                f1.getReverseFlight().setReverseFlight(f1);
//                routeSession.saveReturnFlights(f1);
//                System.err.println("generatebyday" + f1);
//            }
//            System.err.println("before add one day");
//            cal.setTime(departureDateTemp);
//            cal.add(Calendar.DATE, 1);
//            departureDateTemp = cal.getTime();          
//            System.err.println("departuredayafter add one day" + departureDateTemp);
//        }
//        
//        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("yearSelected", yearSelected);
//        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("routeSelected", routeSelected);
//        FacesContext fc = FacesContext.getCurrentInstance();
//        ExternalContext ec = fc.getExternalContext();
//        ec.redirect("planningDisplayFlightsGenerated.xhtml");
//    }
    public void generateFlightsByDay() throws ParseException, IOException {
        flightsGenerated = new ArrayList<FlightEntity>();
        Date departureDateTemp = dateOperate;
        Date startingDate = dateOperate;
//        SimpleDateFormat myFormat = new SimpleDateFormat("dd MM yyyy");
//        String inputString1 = "31 12 " + yearSelected;

//        Date date1 = myFormat.parse(inputString1);
//        long diff = date1.getTime() - dateOperate.getTime();
//        System.out.println("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
//        long diffDays = diff / (24 * 60 * 60 * 1000);
//        System.err.println("diffdays:" + diffDays);
//        Calendar cal = Calendar.getInstance();
//        int days = (int) diffDays;
        Calendar cal = Calendar.getInstance();
        cal.setTime(startingDate);
        yearSelected = cal.get(Calendar.YEAR);
        System.err.println("yearselected" + yearSelected);
        cal.add(Calendar.YEAR, planningPeriod);
        Date EndingDate = cal.getTime();

        while (departureDateTemp.compareTo(EndingDate) <= 0) {
            for (FlightEntity f : flights) {
                FlightEntity f1 = new FlightEntity(yearSelected);

                //set the departure time of flight in the flights 
                Date departureT = combineTwoDate(f.getDepartureDate(), departureDateTemp);
                f1.setDepartureDate(departureT);
                f1.setFlightNo(f.getFlightNo());
                cal.setTime(departureT);
                cal.add(Calendar.MINUTE, (int) (routeSelected.getFlightHours() * 60 + 0.5d));
                
                f1.setArrivalDate(cal.getTime());
                f1.setReverseFlight(new FlightEntity(yearSelected));
                f1.getReverseFlight().setRoute(routeSelected.getReverseRoute());
                f1.setRoute(f.getRoute());
                f1.setFlightNo(f.getFlightNo());
                f1.getReverseFlight().setFlightNo(f.getReverseFlight().getFlightNo());
                departureT = combineTwoDate(f.getReverseFlight().getDepartureDate(), departureDateTemp);
                f1.getReverseFlight().setDepartureDate(departureT);
                f1.setFlightNo(f.getReverseFlight().getFlightNo());
                cal.setTime(departureT);
                cal.add(Calendar.MINUTE, (int) (routeSelected.getFlightHours() * 60 + 0.5d));
                
                f1.getReverseFlight().setArrivalDate(cal.getTime());
                
                f1.getReverseFlight().setReverseFlight(f1);
                routeSession.saveReturnFlights(f1);
                System.err.println("generatebyday" + f1);
            }
            System.err.println("before add one day");
            cal.setTime(departureDateTemp);
            cal.add(Calendar.DATE, 1);
            departureDateTemp = cal.getTime();
            System.err.println("departuredayafter add one day" + departureDateTemp);
        }

        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("yearSelected", yearSelected);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("routeSelected", routeSelected);
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        ec.redirect("planningDisplayFlightsGenerated.xhtml");
    }

//    public void generateFlightsByWeek() throws IOException {
//        flightsGenerated = new ArrayList<FlightEntity>();
//        Date departureDateTemp = dateOperate;
//        Date startingDate = dateOperate;
////        SimpleDateFormat myFormat = new SimpleDateFormat("dd MM yyyy");
////        String inputString1 = "31 12 " + yearSelected;
//
////        Date date1 = myFormat.parse(inputString1);
////        long diff = date1.getTime() - dateOperate.getTime();
////        System.out.println("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
////        long diffDays = diff / (24 * 60 * 60 * 1000);
////        System.err.println("diffdays:" + diffDays);
////        Calendar cal = Calendar.getInstance();
////        int days = (int) diffDays;
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(startingDate);
//        yearSelected = cal.get(Calendar.YEAR);
//        System.err.println("yearselected" + yearSelected);
//        cal.add(Calendar.YEAR, planningPeriod);
//        Date EndingDate = cal.getTime();
//
//        while (departureDateTemp.compareTo(EndingDate) <= 0) {
//            for (FlightEntity f : flights) {
//                FlightEntity f1 = new FlightEntity(yearSelected);
//
//                //set the departure time of flight in the flights 
//                f1.setDepartureDate(combineThreeDate(departureDateTemp,f.getWeekDay(),f.getDepartureDate()));
//                f1.setArrivalDate(f.getArrivalDate());
//                f1.setReverseFlight(new FlightEntity(yearSelected));
//                f1.getReverseFlight().setRoute(routeSelected.getReverseRoute());
//                f1.setRoute(f.getRoute());
//                f1.getReverseFlight().setDepartureDate(combineThreeDate(departureDateTemp,f.getReverseFlight().getWeekDay(), f.getReverseFlight().getDepartureDate()));
//                f1.getReverseFlight().setArrivalDate(f.getReverseFlight().getArrivalDate());
//                f1.getReverseFlight().setReverseFlight(f1);
//                routeSession.saveReturnFlights(f1);
//                System.err.println("generatebyday" + f1);
//            }
//            System.err.println("before add one day");
//            cal.setTime(departureDateTemp);
//            cal.add(Calendar.DATE, 7);
//            departureDateTemp = cal.getTime();
//            System.err.println("departuredayafter add one day" + departureDateTemp);
//        }
//
//        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("yearSelected", yearSelected);
//        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("routeSelected", routeSelected);
//        FacesContext fc = FacesContext.getCurrentInstance();
//        ExternalContext ec = fc.getExternalContext();
//        ec.redirect("planningDisplayFlightsGenerated.xhtml");
//        //since operating Date
//    }

    //ai got mininue and hour ...
    public Date combineThreeDate(Date a, String weekday, Date time) {
        Calendar cal = Calendar.getInstance();
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(time);
        cal.setTime(a);
        int orig = cal.get(Calendar.DAY_OF_WEEK);
        int amt = 0;
        if (weekday != null) {
            switch (weekday) {
                case "Sunday":
                    amt = 1;
                    break;
                case "Monday":
                    amt = 2;
                    break;
                case "Tuesday":
                    amt = 3;
                    break;
                case "Wednesday":
                    amt = 4;
                    break;
                case "Thursday":
                    amt = 5;
                    break;
                case "Friday":
                    amt = 6;
                    break;
                case "Saturdday":
                    amt = 7;
                    break;
            }
        } else {
            System.err.println("wrong weekday which is null");
            return null;
        }
        if (amt >= orig) {
            cal.add(Calendar.DATE, amt - orig);
        } else {
            cal.add(Calendar.DATE, amt + 7 - orig);
        }

        cal.set(Calendar.HOUR_OF_DAY, cal1.get(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, cal1.get(Calendar.MINUTE));
        cal.set(Calendar.SECOND, cal1.get(Calendar.SECOND));
        System.err.println("combine date" + cal.getTime());
        return cal.getTime();

    }
    public String getTimeName(Date date){
        if(date ==null)
            return "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        return dateFormat.format(date);
        
    }

    public Date combineTwoDate(Date a1, Date a2) {
        Calendar cal = Calendar.getInstance();
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(a1);
        cal.setTime(a2);
        cal.set(Calendar.HOUR_OF_DAY, cal1.get(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, cal1.get(Calendar.MINUTE));
        cal.set(Calendar.SECOND, cal1.get(Calendar.SECOND));
        System.err.println("combine date" + cal.getTime());
        return cal.getTime();
    }

    public List<String> getWeekDays() {
        List<String> weekD = new ArrayList<String>();
        weekD.add("Monday");
        weekD.add("Tuesday");
        weekD.add("Wednesday");
        weekD.add("Thursday");
        weekD.add("Friday");
        weekD.add("Saturday");
        weekD.add("Sunday");
        return weekD;

    }

    public long getDifferenceDays(Date d1, Date d2) {
        long diff = d2.getTime() - d1.getTime();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    public List<FlightEntity> getFlightsGenerated() {
        return flightsGenerated;
    }
    public String getMindate(Date d){
        if(d!=null){
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.MINUTE, 30);
        return dateFormat.format(cal.getTime());
        }
        else
            return "";
    }

    public void setFlightsGenerated(List<FlightEntity> flightsGenerated) {
        this.flightsGenerated = flightsGenerated;
    }

    public void dateSelectListener(SelectEvent event) {
        System.err.println("Hello");
        FlightEntity flightEntity = (FlightEntity) event.getComponent().getAttributes().get("flight");

        System.err.println("flightEntity: " + flightEntity.getDepartureDate());
        Calendar cal = Calendar.getInstance();
        cal.setTime(flightEntity.getDepartureDate());
        
        cal.add(Calendar.MINUTE, (int)(routeSelected.getFlightHours()*60+0.5d));
        Date halfHourBack = cal.getTime();
        flightEntity.setArrivalDate(halfHourBack);
        cal.add(Calendar.MINUTE, 60);
        flightEntity.getReverseFlight().setDepartureDate(cal.getTime());
    }

    public Integer countPlusOne() {
        count = count + 1;
        return count;
    }

    public Integer getCount() {
        return count;
    }

    public List<Date> getDepartureDates() {
        return departureDates;
    }

    public void setDepartureDates(List<Date> departureDates) {
        this.departureDates = departureDates;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer returnIndex(FlightEntity flight1) {
        System.out.println(flights);
        System.out.println(flight1.toString());
        return flights.indexOf(flight1);
    }

    public Integer getFlightTimes() {
        return flightTimes;
    }

    public void setFlightTimes(Integer flightTimes) {
        this.flightTimes = flightTimes;
    }

    public String getIterationPeriod() {
        return iterationPeriod;
    }

    public void setIterationPeriod(String iterationPeriod) {
        this.iterationPeriod = iterationPeriod;
    }

    public RouteSessionBeanLocal getRouteSession() {
        return routeSession;
    }

    public void setRouteSession(RouteSessionBeanLocal routeSession) {
        this.routeSession = routeSession;
    }

    public RouteEntity getRouteSelected() {
        return routeSelected;
    }

    public void setRouteSelected(RouteEntity routeSelected) {
        this.routeSelected = routeSelected;
    }

    public Integer getYearSelected() {
        return yearSelected;
    }

    public void setYearSelected(Integer yearSelected) {
        this.yearSelected = yearSelected;
    }

    public Date getDateOperate() {
        return dateOperate;
    }

    public void setDateOperate(Date dateOperate) {
        this.dateOperate = dateOperate;
    }

    public List<FlightEntity> getFlights() {
        return flights;
    }

    public void setFlights(List<FlightEntity> flights) {
        this.flights = flights;
    }

    public List<Integer> getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(List<Integer> serialNo) {
        this.serialNo = serialNo;
    }

}
