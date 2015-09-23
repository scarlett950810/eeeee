/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.inventory;

import imas.inventory.entity.BookingClassEntity;
import imas.inventory.sessionbean.inventoryRevenueManagementSessionBeanLocal;
import imas.planning.entity.FlightEntity;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.primefaces.event.SlideEndEvent;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.HorizontalBarChartModel;

/**
 *
 * @author Howard
 */
@Named(value = "inventoryRevenueManagementManagedBean")
@SessionScoped
public class InventoryRevenueManagementManagedBean implements Serializable {

    @EJB
    private inventoryRevenueManagementSessionBeanLocal inventoryRevenueManagementSessionBean;

    private List<FlightEntity> flightList;
    private List<FlightEntity> fliteredFlight;
    private FlightEntity selectedFlight;
    private List<BookingClassEntity> bookingClassList;
    private BookingClassEntity economyOne;
    private BookingClassEntity economyTwo;
    private BookingClassEntity economyThree;
    private BookingClassEntity bookingClass;
    private double newPricing;
    private boolean close = true; // This attribute is to identify whether the quota dialog box is closed by the cross or not
    private int economyClassSeats;
    private int sum;
    private int economyOneQuota;
    private int economyTwoQuota;
    private int economyThreeQuota;
    private HorizontalBarChartModel horizontalBarModel;

    public InventoryRevenueManagementManagedBean() {
    }

    @PostConstruct
    public void init() {
        fetchFlights();
        
    }

    public void fetchFlights() {
        flightList = inventoryRevenueManagementSessionBean.fetchFlight();
    }

    public List<FlightEntity> getFlightList() {
        return flightList;
    }

    public void setFlightList(List<FlightEntity> flightList) {
        this.flightList = flightList;
    }

    public List<FlightEntity> getFliteredFlight() {
        return fliteredFlight;
    }

    public void setFliteredFlight(List<FlightEntity> fliteredFlight) {
        this.fliteredFlight = fliteredFlight;
    }

    public FlightEntity getSelectedFlight() {
        return selectedFlight;
    }

    public void setSelectedFlight(FlightEntity selectedFlight) {
        this.selectedFlight = selectedFlight;
    }

    public List<BookingClassEntity> getBookingClassList() {
        return bookingClassList;
    }

    public void setBookingClassList(List<BookingClassEntity> bookingClassList) {
        this.bookingClassList = bookingClassList;
    }

    public void viewBookingClass() throws IOException {
        
        bookingClassList = inventoryRevenueManagementSessionBean.fetchBookingClass(selectedFlight.getId());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("bookingClassList", bookingClassList);
        economyClassSeats = inventoryRevenueManagementSessionBean.checkSeatsCapacity(selectedFlight);
        close = true;
        createHorizontalBarModel();
        for (BookingClassEntity bookingClassList1 : bookingClassList) {
            switch (bookingClassList1.getName()) {
                case "Economy Class 1":
                    economyOne = bookingClassList1;
                    break;
                case "Economy Class 2":
                    economyTwo = bookingClassList1;
                    break;
                case "Economy Class 3":
                    economyThree = bookingClassList1;
                    break;
            }
        }

        economyOneQuota = economyOne.getQuota();
        economyTwoQuota = economyTwo.getQuota();
        economyThreeQuota = economyThree.getQuota();

        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        ec.redirect("inventoryBookingClassManagement.xhtml");
    }

    public Integer getSoldSeats(Long bookingClassID) {
        return inventoryRevenueManagementSessionBean.computeSoldSeats(bookingClassID);
    }

    public void returnBack() throws IOException {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        ec.redirect("inventoryRevenueManagement.xhtml");
    }

    public void updateBookingClassQuota() throws IOException {
        close = false;
        inventoryRevenueManagementSessionBean.updateBookingClassQuota(economyOne.getId(), economyOneQuota);
        inventoryRevenueManagementSessionBean.updateBookingClassQuota(economyTwo.getId(), economyTwoQuota);
        inventoryRevenueManagementSessionBean.updateBookingClassQuota(economyThree.getId(), economyThreeQuota);
        bookingClassList = inventoryRevenueManagementSessionBean.fetchBookingClass(selectedFlight.getId());
        System.out.print("update quota method called");
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        ec.redirect("inventoryBookingClassManagement.xhtml");
    }

    public BookingClassEntity getEconomyOne() {
        return economyOne;
    }

    public void setEconomyOne(BookingClassEntity economyOne) {
        this.economyOne = economyOne;
    }

    public BookingClassEntity getEconomyTwo() {
        return economyTwo;
    }

    public void setEconomyTwo(BookingClassEntity economyTwo) {
        this.economyTwo = economyTwo;
    }

    public BookingClassEntity getEconomyThree() {
        return economyThree;
    }

    public void setEconomyThree(BookingClassEntity economyThree) {
        this.economyThree = economyThree;
    }

    public BookingClassEntity getBookingClass() {
        return bookingClass;
    }

    public void setBookingClass(BookingClassEntity bookingClass) {
        this.bookingClass = bookingClass;
    }

    public double getNewPricing() {
        return newPricing;
    }

    public void setNewPricing(double newPricing) {
        this.newPricing = newPricing;
    }

    public void updatePricing() throws IOException {
        inventoryRevenueManagementSessionBean.updateBookingClassPricing(bookingClass.getId(), newPricing);
        bookingClassList = inventoryRevenueManagementSessionBean.fetchBookingClass(selectedFlight.getId());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("bookingClassList", bookingClassList);
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        ec.redirect("inventoryBookingClassManagement.xhtml");
        System.out.print("invoked");
    }

    public void closeQuotaForm() {
        if (close == true) {
            System.out.print(economyOne.getQuota());
            System.out.print(economyTwo.getQuota());
            System.out.print(economyThree.getQuota());
            economyOneQuota = economyOne.getQuota();
            economyTwoQuota = economyTwo.getQuota();
            economyThreeQuota = economyThree.getQuota();
            System.out.print("closeQuotaForm called");
        }
    }

    public boolean isClose() {
        return close;
    }

    public void setClose(boolean close) {
        this.close = close;
    }

    public int getEconomyClassSeats() {
        return economyClassSeats;
    }

    public void setEconomyClassSeats(int economyClassSeats) {
        this.economyClassSeats = economyClassSeats;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public void onSlideEndOne(SlideEndEvent event) {
        economyOneQuota = event.getValue();
    }

    public void onSlideEndTwo(SlideEndEvent event) {
        economyTwoQuota = event.getValue();
    }

    public void onSlideEndThree(SlideEndEvent event) {
        economyThreeQuota = event.getValue();
    }

    public int getEconomyOneQuota() {
        return economyOneQuota;
    }

    public void setEconomyOneQuota(int economyOneQuota) {
        this.economyOneQuota = economyOneQuota;
    }

    public int getEconomyTwoQuota() {
        return economyTwoQuota;
    }

    public void setEconomyTwoQuota(int economyTwoQuota) {
        this.economyTwoQuota = economyTwoQuota;
    }

    public int getEconomyThreeQuota() {
        return economyThreeQuota;
    }

    public void setEconomyThreeQuota(int economyThreeQuota) {
        this.economyThreeQuota = economyThreeQuota;
    }

    private void createHorizontalBarModel() {
        horizontalBarModel = new HorizontalBarChartModel();
 
        
        ChartSeries sold = new ChartSeries();
        ChartSeries unSold = new ChartSeries();
        sold.setLabel("Sold");
        unSold.setLabel("Unsold");
        for(int i=0; i< bookingClassList.size(); i++){
            int temp = inventoryRevenueManagementSessionBean.computeSoldSeats(bookingClassList.get(i).getId());
            int quota = bookingClassList.get(i).getQuota();
            sold.set(bookingClassList.get(i).getName(), 20);
            unSold.set(bookingClassList.get(i).getName(), quota - temp);
        }
 
 
        horizontalBarModel.addSeries(sold);
        horizontalBarModel.addSeries(unSold);
         
        horizontalBarModel.setTitle("Booking Class Ticket Sales Condition");
        horizontalBarModel.setLegendPosition("e");
        horizontalBarModel.setStacked(true);
        horizontalBarModel.setAnimate(true);
         
        Axis xAxis = horizontalBarModel.getAxis(AxisType.X);
        xAxis.setLabel("Number of Tickets");
        xAxis.setMin(0);
        xAxis.setMax(200);
        xAxis.setTickCount(21);
         
        Axis yAxis = horizontalBarModel.getAxis(AxisType.Y);
        yAxis.setLabel("Types of Booking Classes");        
    }

    public HorizontalBarChartModel getHorizontalBarModel() {
        return horizontalBarModel;
    }
    
    
}
