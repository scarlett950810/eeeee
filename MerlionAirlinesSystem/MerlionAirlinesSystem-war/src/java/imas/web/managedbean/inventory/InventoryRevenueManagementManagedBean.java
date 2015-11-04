/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.web.managedbean.inventory;

import imas.distribution.sessionbean.FlightLookupSessionBeanLocal;
import imas.inventory.entity.BookingClassEntity;
import imas.inventory.sessionbean.InventoryRevenueManagementSessionBeanLocal;
import imas.planning.entity.FlightEntity;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.primefaces.event.SlideEndEvent;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.HorizontalBarChartModel;

/**
 *
 * @author Howard
 */
@ManagedBean
@SessionScoped
public class InventoryRevenueManagementManagedBean implements Serializable {
    
    @EJB
    private FlightLookupSessionBeanLocal flightLookupSessionBean;
    
    @EJB
    private InventoryRevenueManagementSessionBeanLocal inventoryRevenueManagementSessionBean;
    
    private List<FlightEntity> flightList;
    private List<FlightEntity> fliteredFlight;
    private FlightEntity selectedFlight;
    private List<BookingClassEntity> bookingClassList;
    private BookingClassEntity economyOne;
    private BookingClassEntity economyTwo;
    private BookingClassEntity economyThree;
    private BookingClassEntity economyFour;
    private BookingClassEntity economyFive;
    private BookingClassEntity economyAgency;
    private BookingClassEntity bookingClass;
    private double newPricing;
    private boolean close = true; // This attribute is to identify whether the quota dialog box is closed by the cross or not
    private int economyClassSeats;
    private int sum;
    private int economyOneQuota;
    private int economyTwoQuota;
    private int economyThreeQuota;
    private int economyFourQuota;
    private int economyFiveQuota;
    private int economyAgencyQuota;
    
    private HorizontalBarChartModel horizontalBarModel;

    public InventoryRevenueManagementManagedBean() {
    }

    @PostConstruct
    public void init() {
        flightList = flightLookupSessionBean.getAllSellingFlights();
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
        
        bookingClassList = selectedFlight.getBookingClasses();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("bookingClassList", bookingClassList);
        economyClassSeats = inventoryRevenueManagementSessionBean.checkSeatsCapacity(selectedFlight);
        close = true;
        createHorizontalBarModel();
        
        for (BookingClassEntity bookingClassList1 : bookingClassList) {
            if(bookingClassList1.isEconomyClass1BookingClassEntity()){
                economyOne = bookingClassList1;
            }else if(bookingClassList1.isEconomyClass2BookingClassEntity()){
                economyTwo = bookingClassList1;
            }else if(bookingClassList1.isEconomyClass3BookingClassEntity()){
                economyThree = bookingClassList1;
            }else if(bookingClassList1.isEconomyClass4BookingClassEntity()){
                economyFour = bookingClassList1;
            }else if(bookingClassList1.isEconomyClass5BookingClassEntity()){
                economyFive = bookingClassList1;
            }else if(bookingClassList1.isEconomyClassAgencyBookingClassEntity()){
                economyAgency = bookingClassList1;
            }
        }

        economyOneQuota = economyOne.getQuota();
        economyTwoQuota = economyTwo.getQuota();
        economyThreeQuota = economyThree.getQuota();
        economyFourQuota = economyFour.getQuota();
        economyFiveQuota = economyFive.getQuota();
        economyAgencyQuota = economyAgency.getQuota();
        
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
        bookingClassList = selectedFlight.getBookingClasses();
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
        bookingClassList = selectedFlight.getBookingClasses();
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
        System.out.print(economyOneQuota);
    }

    public void onSlideEndTwo(SlideEndEvent event) {
        economyTwoQuota = event.getValue();
    }

    public void onSlideEndThree(SlideEndEvent event) {
        economyThreeQuota = event.getValue();
    }
    
    public void onSlideEndFour(SlideEndEvent event) {
        economyFourQuota = event.getValue();
    }
    
    public void onSlideEndFive(SlideEndEvent event) {
        economyFiveQuota = event.getValue();
    }
    
    public void onSlideEndAgency(SlideEndEvent event) {
        economyAgencyQuota = event.getValue();
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

    public BookingClassEntity getEconomyFour() {
        return economyFour;
    }

    public void setEconomyFour(BookingClassEntity economyFour) {
        this.economyFour = economyFour;
    }

    public BookingClassEntity getEconomyFive() {
        return economyFive;
    }

    public void setEconomyFive(BookingClassEntity economyFive) {
        this.economyFive = economyFive;
    }

    public BookingClassEntity getEconomyAgency() {
        return economyAgency;
    }

    public void setEconomyAgency(BookingClassEntity economyAgency) {
        this.economyAgency = economyAgency;
    }

    public int getEconomyFourQuota() {
        return economyFourQuota;
    }

    public void setEconomyFourQuota(int economyFourQuota) {
        this.economyFourQuota = economyFourQuota;
    }

    public int getEconomyFiveQuota() {
        return economyFiveQuota;
    }

    public void setEconomyFiveQuota(int economyFiveQuota) {
        this.economyFiveQuota = economyFiveQuota;
    }

    public int getEconomyAgencyQuota() {
        return economyAgencyQuota;
    }

    public void setEconomyAgencyQuota(int economyAgencyQuota) {
        this.economyAgencyQuota = economyAgencyQuota;
    }

    
    private void createHorizontalBarModel() {
        horizontalBarModel = new HorizontalBarChartModel();
 
        
        ChartSeries sold = new ChartSeries();
        ChartSeries unSold = new ChartSeries();
        sold.setLabel("Sold");
        unSold.setLabel("Unsold");
        for(int i=bookingClassList.size()-1; i>=0 ; i--){
            int temp = inventoryRevenueManagementSessionBean.computeSoldSeats(bookingClassList.get(i).getId());
            int quota = bookingClassList.get(i).getQuota();
            sold.set(bookingClassList.get(i).getName(), temp);
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
    
    public void editYieldManagementRules() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("selectedSellingFlightToManage", selectedFlight);
        FacesContext.getCurrentInstance().getExternalContext().redirect("inventoryRulesManagement.xhtml");
    }
    
    public void editTermsAndConditions() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("selectedSellingFlightToManage", selectedFlight);
        FacesContext.getCurrentInstance().getExternalContext().redirect("inventoryTermsAndConditionsManagement.xhtml");
    }
    
    public void setAllFlightYearsBack(boolean departured, int year) {
        flightLookupSessionBean.setAllFlightYearsBack(departured, year);
    }
    
}
