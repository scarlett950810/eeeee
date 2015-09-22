/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.inventory.sessionbean;

import imas.inventory.entity.BookingClassEntity;
import imas.inventory.entity.TicketEntity;
import imas.inventory.entity.YieldManagementRuleEntity;
import imas.planning.entity.FlightEntity;
import imas.planning.entity.RouteEntity;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Scarlett
 */
@Stateless
public class YieldManagementSessionBean implements YieldManagementSessionBeanLocal {

    @PersistenceContext
    private EntityManager entityManager;

    @EJB
    private CostManagementSessionBeanLocal costManagementSessionBean;

    @EJB
    private SeatsManagementSessionBeanLocal seatsManagementSessionBean;

    // TODO: after a new flight is set to departured, re-compute the route popularity
    @Override
    public Double getRoutePopularity(RouteEntity route) {
        Double totalRevenue = 0.0;
        Integer totalSeatNo = 0;
        Double routeDistance = route.getDistance();
        
        Query queryForFlightsUnderRoute = entityManager.createQuery("SELECT f FROM FlightEntity f WHERE f.route = :route");
        queryForFlightsUnderRoute.setParameter("route", route);
        List<FlightEntity> flightsUnderRoute = (List<FlightEntity>) queryForFlightsUnderRoute.getResultList();
//        System.out.println(flightsUnderRoute);
        for (FlightEntity f : flightsUnderRoute) {
            Query queryForAllTicketsUnderFlight = entityManager.createQuery("SELECT t FROM TicketEntity t "
                    + "WHERE t.bookingClass.flight = :flight");
            queryForAllTicketsUnderFlight.setParameter("flight", f);
            List<TicketEntity> tickets = queryForAllTicketsUnderFlight.getResultList();

            for (TicketEntity t : tickets) {
                totalSeatNo = totalSeatNo + 1;
                totalRevenue = totalRevenue + t.getBookingClass().getPrice();
            }

            return totalRevenue / (totalSeatNo * routeDistance);

        }
        return 0.0;
    }

    @Override
    public double getEconomyClassTotalCost(FlightEntity flight) {
        double costPerSeat = costManagementSessionBean.getCostPerSeatPerMile() * flight.getDistance();
        int economyCapacity = seatsManagementSessionBean.getEconomyClassCapacity(flight);

        return costPerSeat * economyCapacity;
    }

    @Override
    public double getTotalEconomyClassRevenue(FlightEntity flight) {
        double totalRevenueTillNow = 0.0;

        Query queryForAllCurrentTicketsUnderFlight = entityManager.createQuery("SELECT t FROM TicketEntity t "
                + "WHERE t.bookingClass.flight = :flight");
        queryForAllCurrentTicketsUnderFlight.setParameter("flight", flight);
        List<TicketEntity> tickets = queryForAllCurrentTicketsUnderFlight.getResultList();

        for (TicketEntity t : tickets) {
            totalRevenueTillNow = totalRevenueTillNow + t.getBookingClass().getPrice();
        }
        return totalRevenueTillNow;
    }

    @Override
    public int getFromNowToDepartureInDay(FlightEntity flight) {
        //        TODO: change to the commented line.
        //        int nowToDeparture = new Date().compareTo(flight.getEstimatedDepartureTime());

        return 0;
    }

    // Name for economy class booking classes:
    // Full Service Economy, Economy Plus, Standard Economy, Economy Save, Economy Super Save
    // Full Service Economy
    @Override
    public int getTotalNumberOfSoldEconomyClass1Ticket(FlightEntity flight) {
        Query queryForAllCurrentTicketsUnderFlight = entityManager.createQuery("SELECT t FROM TicketEntity t "
                + "WHERE t.bookingClass.flight = :flight AND t.bookingClass.name = :bookingClassName");
        queryForAllCurrentTicketsUnderFlight.setParameter("flight", flight);
        queryForAllCurrentTicketsUnderFlight.setParameter("bookingClassName", "Full Service Economy");

        List<TicketEntity> tickets = queryForAllCurrentTicketsUnderFlight.getResultList();
        return tickets.size();
    }

    // Economy Plus
    @Override
    public int getTotalNumberOfSoldEconomyClass2Ticket(FlightEntity flight) {
        Query queryForAllCurrentTicketsUnderFlight = entityManager.createQuery("SELECT t FROM TicketEntity t "
                + "WHERE t.bookingClass.flight = :flight AND t.bookingClass.name = :bookingClassName");
        queryForAllCurrentTicketsUnderFlight.setParameter("flight", flight);
        queryForAllCurrentTicketsUnderFlight.setParameter("bookingClassName", "Economy Plus");

        List<TicketEntity> tickets = queryForAllCurrentTicketsUnderFlight.getResultList();
        return tickets.size();
    }

    // Standard Economy
    @Override
    public int getTotalNumberOfSoldEconomyClass3Ticket(FlightEntity flight) {
        Query queryForAllCurrentTicketsUnderFlight = entityManager.createQuery("SELECT t FROM TicketEntity t "
                + "WHERE t.bookingClass.flight = :flight AND t.bookingClass.name = :bookingClassName");
        queryForAllCurrentTicketsUnderFlight.setParameter("flight", flight);
        queryForAllCurrentTicketsUnderFlight.setParameter("bookingClassName", "Standard Economy");

        List<TicketEntity> tickets = queryForAllCurrentTicketsUnderFlight.getResultList();
        return tickets.size();
    }

    // Economy Save
    @Override
    public int getTotalNumberOfSoldEconomyClass4Ticket(FlightEntity flight) {
        Query queryForAllCurrentTicketsUnderFlight = entityManager.createQuery("SELECT t FROM TicketEntity t "
                + "WHERE t.bookingClass.flight = :flight AND t.bookingClass.name = :bookingClassName");
        queryForAllCurrentTicketsUnderFlight.setParameter("flight", flight);
        queryForAllCurrentTicketsUnderFlight.setParameter("bookingClassName", "Economy Save");

        List<TicketEntity> tickets = queryForAllCurrentTicketsUnderFlight.getResultList();
        return tickets.size();
    }

    // Economy Super Save
    @Override
    public int getTotalNumberOfSoldEconomyClass5Ticket(FlightEntity flight) {
        Query queryForAllCurrentTicketsUnderFlight = entityManager.createQuery("SELECT t FROM TicketEntity t "
                + "WHERE t.bookingClass.flight = :flight AND t.bookingClass.name = :bookingClassName");
        queryForAllCurrentTicketsUnderFlight.setParameter("flight", flight);
        queryForAllCurrentTicketsUnderFlight.setParameter("bookingClassName", "Economy Super Save");

        List<TicketEntity> tickets = queryForAllCurrentTicketsUnderFlight.getResultList();
        return tickets.size();
    }

    @Override
    public int getTotalNumberOfSoldEconomyClassesTicket(FlightEntity flight) {
        Query queryForAllCurrentTicketsUnderFlight = entityManager.createQuery("SELECT t FROM TicketEntity t "
                + "WHERE t.bookingClass.flight = :flight AND t.bookingClass.seatClass = :seatClass");
        queryForAllCurrentTicketsUnderFlight.setParameter("flight", flight);
        queryForAllCurrentTicketsUnderFlight.setParameter("seatClass", "Economy Class");

        List<TicketEntity> tickets = queryForAllCurrentTicketsUnderFlight.getResultList();
        return tickets.size();
    }

    @Override
    public BookingClassEntity getEconomyClass1(FlightEntity flight) {
        Query queryForEconomyClass1CurrentQuota = entityManager.createQuery("SELECT bc FROM BookingClassEntity bc");
        queryForEconomyClass1CurrentQuota.setParameter("flight", flight);
        List<BookingClassEntity> bookingClasses = queryForEconomyClass1CurrentQuota.getResultList();
        for (BookingClassEntity bce : bookingClasses) {
            if (bce.isEconomyClass1BookingClassEntity()) {
                return bce;
            }
        }
        return null;
    }

    @Override
    public BookingClassEntity getEconomyClass2(FlightEntity flight) {
        Query queryForEconomyClass2CurrentQuota = entityManager.createQuery("SELECT bc FROM BookingClassEntity bc");
        queryForEconomyClass2CurrentQuota.setParameter("flight", flight);
        List<BookingClassEntity> bookingClasses = queryForEconomyClass2CurrentQuota.getResultList();
        for (BookingClassEntity bce : bookingClasses) {
            if (bce.isEconomyClass2BookingClassEntity()) {
                return bce;
            }
        }
        return null;
    }

    @Override
    public BookingClassEntity getEconomyClass3(FlightEntity flight) {
        Query queryForEconomyClass3CurrentQuota = entityManager.createQuery("SELECT bc FROM BookingClassEntity bc");
        queryForEconomyClass3CurrentQuota.setParameter("flight", flight);
        List<BookingClassEntity> bookingClasses = queryForEconomyClass3CurrentQuota.getResultList();
        for (BookingClassEntity bce : bookingClasses) {
            if (bce.isEconomyClass3BookingClassEntity()) {
                return bce;
            }
        }
        return null;
    }

    @Override
    public BookingClassEntity getEconomyClass4(FlightEntity flight) {
        Query queryForEconomyClass4CurrentQuota = entityManager.createQuery("SELECT bc FROM BookingClassEntity bc");
        queryForEconomyClass4CurrentQuota.setParameter("flight", flight);
        List<BookingClassEntity> bookingClasses = queryForEconomyClass4CurrentQuota.getResultList();
        for (BookingClassEntity bce : bookingClasses) {
            if (bce.isEconomyClass4BookingClassEntity()) {
                return bce;
            }
        }
        return null;
    }

    @Override
    public BookingClassEntity getEconomyClass5(FlightEntity flight) {
        Query queryForEconomyClass5CurrentQuota = entityManager.createQuery("SELECT bc FROM BookingClassEntity bc");
        queryForEconomyClass5CurrentQuota.setParameter("flight", flight);
        List<BookingClassEntity> bookingClasses = queryForEconomyClass5CurrentQuota.getResultList();
        for (BookingClassEntity bce : bookingClasses) {
            if (bce.isEconomyClass5BookingClassEntity()) {
                return bce;
            }
        }
        return null;
    }

    /*
     If a flight is selling well,
     (justified by long time from departure but already earned substantial revenue),
     reallocate quota from lower WTP class to high WTP class.
    
     When time to departure > 60 days && totalRevenue > (parameter) * totalCost, 
     reallocate EconomyClass3 and EconomyClass4 quotas to EconomyClass1 and EconomyClass2 quotas uniformly, 
     amount = half of left quota from 2 or (3&4) which ever is lower.
     */
    @Override
    public void runYieldManagementRule1(YieldManagementRuleEntity yieldManagementRuleEntity) {
        FlightEntity flight = yieldManagementRuleEntity.getFlight();

        Integer nowToDeparture = getFromNowToDepartureInDay(flight);

        Double totalCost = getEconomyClassTotalCost(flight);
        Double totalRevenueTillNow = getTotalEconomyClassRevenue(flight);
        Double totalRevenueToTotalCostRatio = totalRevenueTillNow / totalCost;

        if (nowToDeparture > yieldManagementRuleEntity.getTimeToDepartureInDaysParameter()
                && totalRevenueToTotalCostRatio > yieldManagementRuleEntity.getTotalRevenueToTotalCostParameter()) {
            BookingClassEntity economyClass1 = getEconomyClass1(flight);
            BookingClassEntity economyClass2 = getEconomyClass2(flight);
            BookingClassEntity economyClass3 = getEconomyClass3(flight);
            BookingClassEntity economyClass4 = getEconomyClass4(flight);

            Integer class2QuotaLeft
                    = economyClass2.getQuota() - getTotalNumberOfSoldEconomyClass2Ticket(flight);
            Integer class3QuotaLeft
                    = economyClass3.getQuota() - getTotalNumberOfSoldEconomyClass3Ticket(flight);
            Integer class4QuotaLeft
                    = economyClass4.getQuota() - getTotalNumberOfSoldEconomyClass4Ticket(flight);
            Integer class3and4QuotaLeft
                    = class3QuotaLeft + class4QuotaLeft;

            Integer quotaToMove;
            if (class2QuotaLeft > class3and4QuotaLeft) {
                quotaToMove = class3and4QuotaLeft / 2;
            } else {
                quotaToMove = class2QuotaLeft / 2;
            }

            Integer quotaToMoveTo1 = quotaToMove / 2;
            Integer quotaToMoveTo2 = quotaToMove - quotaToMoveTo1;
            Integer quotaToMoveFrom3 = (int) (((double) class3QuotaLeft) / (class3QuotaLeft + class4QuotaLeft));
            Integer quotaToMoveFrom4 = quotaToMove - quotaToMoveFrom3;

            economyClass1.setQuota(economyClass1.getQuota() + quotaToMoveTo1);
            economyClass2.setQuota(economyClass2.getQuota() + quotaToMoveTo2);
            economyClass3.setQuota(economyClass3.getQuota() - quotaToMoveFrom3);
            economyClass4.setQuota(economyClass4.getQuota() - quotaToMoveFrom4);

            YieldManagementRuleEntity yieldManagementRuleEntityToUpdate
                    = entityManager.find(YieldManagementRuleEntity.class, yieldManagementRuleEntity.getId());
            yieldManagementRuleEntityToUpdate.setEnabled(false);

        }

    }

    /*
     When time to departure < 60 days && totalRevenue < (parameter) * totalCost, 
     reallocate EconomyClass2 quotas to EconomyClass3 and EconomyClass4 quotas and EconomyClass5 quotas uniformly,
     amount = half of left quota from 2 or (3&4) which ever is lower.
     */
    @Override
    public void runYieldManagementRule2(YieldManagementRuleEntity yieldManagementRuleEntity) {
        FlightEntity flight = yieldManagementRuleEntity.getFlight();

        Integer nowToDeparture = getFromNowToDepartureInDay(flight);

        Double totalCost = getEconomyClassTotalCost(flight);
        Double totalRevenueTillNow = getTotalEconomyClassRevenue(flight);
        Double totalRevenueToTotalCostRatio = totalRevenueTillNow / totalCost;

        if (nowToDeparture < yieldManagementRuleEntity.getTimeToDepartureInDaysParameter()
                && totalRevenueToTotalCostRatio < yieldManagementRuleEntity.getTotalRevenueToTotalCostParameter()) {
            BookingClassEntity economyClass2 = getEconomyClass2(flight);
            BookingClassEntity economyClass3 = getEconomyClass3(flight);
            BookingClassEntity economyClass4 = getEconomyClass4(flight);
            BookingClassEntity economyClass5 = getEconomyClass5(flight);

            Integer class2QuotaLeft = economyClass2.getQuota() - getTotalNumberOfSoldEconomyClass2Ticket(flight);
            Integer class3QuotaLeft = economyClass3.getQuota() - getTotalNumberOfSoldEconomyClass3Ticket(flight);
            Integer class4QuotaLeft = economyClass4.getQuota() - getTotalNumberOfSoldEconomyClass4Ticket(flight);
            Integer class3and4QuotaLeft = class3QuotaLeft + class4QuotaLeft;

            // total quota moved from class 2.
            Integer quotaToMoveFrom2;
            if (class2QuotaLeft > class3and4QuotaLeft) {
                quotaToMoveFrom2 = class3and4QuotaLeft / 2;
            } else {
                quotaToMoveFrom2 = class2QuotaLeft / 2;
            }

            Integer quotaToMoveTo3 = quotaToMoveFrom2 / 3;
            Integer quotaToMoveTo4 = quotaToMoveFrom2 / 3;
            Integer quotaToMoveTo5 = quotaToMoveFrom2 - quotaToMoveTo3 - quotaToMoveTo4;

            economyClass2.setQuota(economyClass2.getQuota() - quotaToMoveFrom2);
            economyClass3.setQuota(economyClass3.getQuota() + quotaToMoveTo3);
            economyClass4.setQuota(economyClass4.getQuota() + quotaToMoveTo4);
            economyClass5.setQuota(economyClass5.getQuota() + quotaToMoveTo5);

            YieldManagementRuleEntity yieldManagementRuleEntityToUpdate
                    = entityManager.find(YieldManagementRuleEntity.class, yieldManagementRuleEntity.getId());
            yieldManagementRuleEntityToUpdate.setEnabled(false);

        }

    }

    /*
     When time to departure < 10 days && percentage sold (= economy class tickets sold / total economy class capacity) < 90%, 
     reallocate quota for EconomyClass2 & EconomyClass3 & EconomyClass4 to 0 to EconomyClass1 EconomyClass5 uniformly.
     */
    @Override
    public void runYieldManagementRule3(YieldManagementRuleEntity yieldManagementRuleEntity) {
        FlightEntity flight = yieldManagementRuleEntity.getFlight();

        Integer nowToDeparture = getFromNowToDepartureInDay(flight);
        Integer economyClassTicketSold = getTotalNumberOfSoldEconomyClassesTicket(flight);
        Integer economyClassCapacity = seatsManagementSessionBean.getEconomyClassCapacity(flight);
        Double percentageSold = ((double) economyClassTicketSold / economyClassCapacity);

        if (nowToDeparture < yieldManagementRuleEntity.getTimeToDepartureInDaysParameter()
                && percentageSold < yieldManagementRuleEntity.getPercentageSoldParameter()) {
            BookingClassEntity economyClass1 = getEconomyClass1(flight);
            BookingClassEntity economyClass2 = getEconomyClass2(flight);
            BookingClassEntity economyClass3 = getEconomyClass3(flight);
            BookingClassEntity economyClass4 = getEconomyClass4(flight);
            BookingClassEntity economyClass5 = getEconomyClass5(flight);

            Integer totalQuotaLeft = economyClass1.getQuota() + economyClass2.getQuota()
                    + economyClass3.getQuota() + economyClass4.getQuota() + economyClass5.getQuota()
                    - getTotalNumberOfSoldEconomyClass1Ticket(flight) - getTotalNumberOfSoldEconomyClass2Ticket(flight)
                    - getTotalNumberOfSoldEconomyClass3Ticket(flight) - getTotalNumberOfSoldEconomyClass4Ticket(flight)
                    - getTotalNumberOfSoldEconomyClass5Ticket(flight);

            Integer quotaToClass1 = totalQuotaLeft / 2;
            Integer quotaToClass5 = totalQuotaLeft - quotaToClass1;

            economyClass1.setQuota(quotaToClass1);
            economyClass2.setQuota(0);
            economyClass3.setQuota(0);
            economyClass4.setQuota(0);
            economyClass5.setQuota(quotaToClass5);

            YieldManagementRuleEntity yieldManagementRuleEntityToUpdate
                    = entityManager.find(YieldManagementRuleEntity.class, yieldManagementRuleEntity.getId());
            yieldManagementRuleEntityToUpdate.setEnabled(false);

        }

    }

    /*
     When time to departure > 10 days, 
     if quota for EconomyClass1 or EconomyClass2 < 3 && EconomyClass3 or EconomyClass4 or EconomyClass5 > 30, 
     reallocate 10 quota from EconomyClass3 or EconomyClass4 or EconomyClass5 to EconomyClass1 or EconomyClass2.
     */
    @Override
    public void runYieldManagementRule4(YieldManagementRuleEntity yieldManagementRuleEntity) {
        FlightEntity flight = yieldManagementRuleEntity.getFlight();

        Integer nowToDeparture = getFromNowToDepartureInDay(flight);
        BookingClassEntity economyClass1 = getEconomyClass1(flight);
        BookingClassEntity economyClass2 = getEconomyClass2(flight);
        BookingClassEntity economyClass3 = getEconomyClass3(flight);
        BookingClassEntity economyClass4 = getEconomyClass4(flight);
        BookingClassEntity economyClass5 = getEconomyClass5(flight);

        Integer economyClass1LeftQuota = economyClass1.getQuota() - getTotalNumberOfSoldEconomyClass1Ticket(flight);
        Integer economyClass2LeftQuota = economyClass2.getQuota() - getTotalNumberOfSoldEconomyClass2Ticket(flight);
        Integer economyClass3LeftQuota = economyClass3.getQuota() - getTotalNumberOfSoldEconomyClass3Ticket(flight);
        Integer economyClass4LeftQuota = economyClass4.getQuota() - getTotalNumberOfSoldEconomyClass4Ticket(flight);
        Integer economyClass5LeftQuota = economyClass5.getQuota() - getTotalNumberOfSoldEconomyClass5Ticket(flight);

        Double changeEconomyClass3and4and5To1Or2Percentage = yieldManagementRuleEntity.getChangeEconomyClass3and4and5To1Or2PercentageParameter();
        int quotaForChangeEconomyClass2and3and4To1Or2 = (int) ((economyClass3LeftQuota + economyClass4LeftQuota + economyClass5LeftQuota) * changeEconomyClass3and4and5To1Or2Percentage);

        if (quotaForChangeEconomyClass2and3and4To1Or2 == 0) {
            yieldManagementRuleEntity.setEnabled(false);
        } else {
            int quotaForChangeFromClass3 = (int) (quotaForChangeEconomyClass2and3and4To1Or2 * ((double) economyClass3LeftQuota / (economyClass3LeftQuota + economyClass4LeftQuota + economyClass5LeftQuota)));
            int quotaForChangeFromClass4 = (int) (quotaForChangeEconomyClass2and3and4To1Or2 * ((double) economyClass4LeftQuota / (economyClass3LeftQuota + economyClass4LeftQuota + economyClass5LeftQuota)));
            int quotaForChangeFromClass5 = quotaForChangeEconomyClass2and3and4To1Or2 - quotaForChangeFromClass3 - quotaForChangeFromClass4;
            
            if (economyClass1.getQuota() != 0
                    && economyClass1LeftQuota < yieldManagementRuleEntity.getEconomyClass1RemainingQuotaParameter()) {
                
                economyClass1.setQuota(economyClass1.getQuota() + quotaForChangeEconomyClass2and3and4To1Or2);
                economyClass3.setQuota(economyClass3.getQuota() - quotaForChangeFromClass3);
                economyClass4.setQuota(economyClass4.getQuota() - quotaForChangeFromClass4);
                economyClass5.setQuota(economyClass5.getQuota() - quotaForChangeFromClass5);
                
            } else if (economyClass2LeftQuota < yieldManagementRuleEntity.getEconomyClass2RemainingQuotaParameter()) {
                
                economyClass2.setQuota(economyClass2.getQuota() + quotaForChangeEconomyClass2and3and4To1Or2);
                economyClass3.setQuota(economyClass3.getQuota() - quotaForChangeFromClass3);
                economyClass4.setQuota(economyClass4.getQuota() - quotaForChangeFromClass4);
                economyClass5.setQuota(economyClass5.getQuota() - quotaForChangeFromClass5);
                
            }

        }

    }

    @Override
    public void insertRules() {
        Query queryForFlights = entityManager.createQuery("SELECT f FROM FlightEntity f");
        List<FlightEntity> flights = queryForFlights.getResultList();
        FlightEntity flight1 = flights.get(0);
        FlightEntity flight2 = flights.get(1);
        
        YieldManagementRuleEntity yieldManagementRuleEntity1 = 
                new YieldManagementRuleEntity().YieldManagementRule1Entity(flight1, 60, 1.5);
        YieldManagementRuleEntity yieldManagementRuleEntity2 = 
                new YieldManagementRuleEntity().YieldManagementRule2Entity(flight1, 60, 1.0);
        YieldManagementRuleEntity yieldManagementRuleEntity3 = 
                new YieldManagementRuleEntity().YieldManagementRule3Entity(flight1, 10, 0.8);
        YieldManagementRuleEntity yieldManagementRuleEntity4 = 
                new YieldManagementRuleEntity().YieldManagementRule4Entity(flight1, 15, 3, 5, 0.3);
        YieldManagementRuleEntity yieldManagementRuleEntity11 = 
                new YieldManagementRuleEntity().YieldManagementRule1Entity(flight2, 60, 1.5);
        YieldManagementRuleEntity yieldManagementRuleEntity12 = 
                new YieldManagementRuleEntity().YieldManagementRule2Entity(flight2, 60, 1.0);
        YieldManagementRuleEntity yieldManagementRuleEntity13 = 
                new YieldManagementRuleEntity().YieldManagementRule3Entity(flight2, 10, 0.8);
        YieldManagementRuleEntity yieldManagementRuleEntity14 = 
                new YieldManagementRuleEntity().YieldManagementRule4Entity(flight2, 15, 3, 8, 0.5);
        
        entityManager.persist(yieldManagementRuleEntity1);
        entityManager.persist(yieldManagementRuleEntity2);
        entityManager.persist(yieldManagementRuleEntity3);
        entityManager.persist(yieldManagementRuleEntity4);
        entityManager.persist(yieldManagementRuleEntity11);
        entityManager.persist(yieldManagementRuleEntity12);
        entityManager.persist(yieldManagementRuleEntity13);
        entityManager.persist(yieldManagementRuleEntity14);
        
    }

    @Override
    public void autoCreateRulesForFlight(FlightEntity flight) {
        Double popularity = flight.getRoute().getPopularity();
        Query queryForMinMax = entityManager.createQuery("SELECT r.popularity FROM RouteEntity r ORDER BY r.popularity ASC");
        int size = queryForMinMax.getResultList().size();
        Double maxPopularity = (Double) queryForMinMax.getResultList().get(size - 1);
        Double minPopularity = (Double) queryForMinMax.getResultList().get(0);
        
        Double normalizedPopularity = normalizePopularity(popularity, maxPopularity, minPopularity);
        
        createRule1(flight, normalizedPopularity);
        createRule2(flight, normalizedPopularity);
        createRule3(flight, normalizedPopularity);
        createRule4(flight, normalizedPopularity);
    }
    
    private Double normalizePopularity(Double popularity, Double max, Double min) {
        return (popularity - min) / (max - min); 
    }
    
    private void createRule1(FlightEntity flight, Double normalizedPopularity) {
//        A = 40 + np* (80-40)
        Integer a = (int) (40 + normalizedPopularity * (80 - 40));
//        B = 1 + np* (2-1)
        Double b = 1 + normalizedPopularity * (2 - 1);
        YieldManagementRuleEntity rule1 = new YieldManagementRuleEntity().YieldManagementRule1Entity(flight, a, b);
        entityManager.persist(rule1);
    }
    
    private void createRule2(FlightEntity flight, Double normalizedPopularity) {
//        A = 40 + np* (80-40)
        Integer a = (int) (40 + normalizedPopularity * (80 - 40));
//        B = 0.5+ np* (1.2-0.5)
        Double b = 0.5 + normalizedPopularity * (1.2 - 0.5);
        YieldManagementRuleEntity rule2 = new YieldManagementRuleEntity().YieldManagementRule2Entity(flight, a, b);
        entityManager.persist(rule2);
    }
    
    private void createRule3(FlightEntity flight, Double normalizedPopularity) {
//        A = 10 + np* (20-10)
        Integer a = (int) (10 + normalizedPopularity * (20 - 10));
//        B = 0.6+ np* (0.8-0.6)
        Double b = 0.6 + normalizedPopularity * (0.8 - 0.6);
        YieldManagementRuleEntity rule3 = new YieldManagementRuleEntity().YieldManagementRule3Entity(flight, a, b);
        entityManager.persist(rule3);
    }
    
    private void createRule4(FlightEntity flight, Double normalizedPopularity) {
//        A = 10 + np* (20-10)
        Integer a = (int) (10 + normalizedPopularity * (20 - 10));
//        B1 = 3+ np* (10-3), B2 = 5 + np* (12-5)
        Integer b1 = (int) (3 + normalizedPopularity * (10 - 3));
        Integer b2 = (int) (5 + normalizedPopularity * (12 - 5));
//        C = 0.1 + np* (0.5-0.1)
        Double c = 0.1 + normalizedPopularity * (0.5 - 0.1);
        YieldManagementRuleEntity rule4 = new YieldManagementRuleEntity().YieldManagementRule4Entity(flight, a, b1, b2, c);
        entityManager.persist(rule4);
    }
}
