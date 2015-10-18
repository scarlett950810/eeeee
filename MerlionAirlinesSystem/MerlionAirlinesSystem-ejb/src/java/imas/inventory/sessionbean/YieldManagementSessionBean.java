/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.inventory.sessionbean;

import imas.inventory.entity.BookingClassEntity;
import imas.distribution.entity.TicketEntity;
import imas.inventory.entity.YieldManagementRuleEntity;
import imas.planning.entity.FlightEntity;
import imas.planning.entity.RouteEntity;
import java.util.Date;
import java.util.List;
import java.util.Objects;
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
    private BookingClassesManagementSessionBeanLocal bookingClassesManagementSessionBean;

    // TODO: after a new flight is set to departured, re-compute the route popularity
    @Override
    public Double getRoutePopularity(RouteEntity route) {
        Double totalRevenue = 0.0;
        Integer totalSeatNo = 0;
        Double routeDistance = route.getDistance();

        Query queryForFlightsUnderRoute = entityManager.createQuery("SELECT f FROM FlightEntity f WHERE f.route = :route");
        queryForFlightsUnderRoute.setParameter("route", route);
        List<FlightEntity> flightsUnderRoute = (List<FlightEntity>) queryForFlightsUnderRoute.getResultList();

        for (FlightEntity f : flightsUnderRoute) {
            Query queryForAllTicketsUnderFlight = entityManager.createQuery("SELECT t FROM TicketEntity t "
                    + "WHERE t.flight = :flight");
            queryForAllTicketsUnderFlight.setParameter("flight", f);
            List<TicketEntity> tickets = queryForAllTicketsUnderFlight.getResultList();

            if (tickets.isEmpty()) {
                return 0.5;
            }

            for (TicketEntity t : tickets) {
                totalSeatNo = totalSeatNo + 1;
                totalRevenue = totalRevenue + t.getPrice();
            }

            return totalRevenue / (totalSeatNo * routeDistance);

        }
        return 0.5;
    }

    @Override
    public double getEconomyClassTotalCost(FlightEntity flight) {

        double costPerSeat = costManagementSessionBean.getCostPerSeatPerMile(flight.getRoute()) * flight.getRoute().getDistance();
        int economyCapacity = bookingClassesManagementSessionBean.getEconomyClassCapacity(flight);

        return costPerSeat * economyCapacity;
    }

    @Override
    public double getTotalEconomyClassRevenue(FlightEntity flight) {
        double totalRevenueTillNow = 0.0;

        Query queryForAllCurrentTicketsUnderFlight = entityManager.createQuery("SELECT t FROM TicketEntity t "
                + "WHERE t.flight = :flight");
        queryForAllCurrentTicketsUnderFlight.setParameter("flight", flight);
        List<TicketEntity> tickets = queryForAllCurrentTicketsUnderFlight.getResultList();

        for (TicketEntity t : tickets) {
            totalRevenueTillNow = totalRevenueTillNow + t.getPrice();
        }
        return totalRevenueTillNow;
    }

    @Override
    public int getFromNowToDepartureInDay(FlightEntity flight) {

        long diff = flight.getDepartureDate().getTime() - new Date().getTime();
        int nowToDeparture = (int) (diff / (24 * 60 * 60 * 1000));

        return nowToDeparture;
    }

    // Name for economy class booking classes:
    // Full Service Economy, Economy Plus, Standard Economy, Economy Save, Economy Super Save
    // Full Service Economy
    @Override
    public int getTotalNumberOfSoldEconomyClass1Ticket(FlightEntity flight) {
        Query queryForAllCurrentTicketsUnderFlight = entityManager.createQuery("SELECT t FROM TicketEntity t "
                + "WHERE t.flight = :flight AND t.bookingClassName = :bookingClassName");
        queryForAllCurrentTicketsUnderFlight.setParameter("flight", flight);
        queryForAllCurrentTicketsUnderFlight.setParameter("bookingClassName", "Full Service Economy");

        List<TicketEntity> tickets = queryForAllCurrentTicketsUnderFlight.getResultList();
        return tickets.size();
    }

    // Economy Plus
    @Override
    public int getTotalNumberOfSoldEconomyClass2Ticket(FlightEntity flight) {
        Query queryForAllCurrentTicketsUnderFlight = entityManager.createQuery("SELECT t FROM TicketEntity t "
                + "WHERE t.flight = :flight AND t.bookingClassName = :bookingClassName");
        queryForAllCurrentTicketsUnderFlight.setParameter("flight", flight);
        queryForAllCurrentTicketsUnderFlight.setParameter("bookingClassName", "Economy Plus");

        List<TicketEntity> tickets = queryForAllCurrentTicketsUnderFlight.getResultList();
        return tickets.size();
    }

    // Standard Economy
    @Override
    public int getTotalNumberOfSoldEconomyClass3Ticket(FlightEntity flight) {
        Query queryForAllCurrentTicketsUnderFlight = entityManager.createQuery("SELECT t FROM TicketEntity t "
                + "WHERE t.flight = :flight AND t.bookingClassName = :bookingClassName");
        queryForAllCurrentTicketsUnderFlight.setParameter("flight", flight);
        queryForAllCurrentTicketsUnderFlight.setParameter("bookingClassName", "Standard Economy");

        List<TicketEntity> tickets = queryForAllCurrentTicketsUnderFlight.getResultList();
        return tickets.size();
    }

    // Economy Save
    @Override
    public int getTotalNumberOfSoldEconomyClass4Ticket(FlightEntity flight) {
        Query queryForAllCurrentTicketsUnderFlight = entityManager.createQuery("SELECT t FROM TicketEntity t "
                + "WHERE t.flight = :flight AND t.bookingClassName = :bookingClassName");
        queryForAllCurrentTicketsUnderFlight.setParameter("flight", flight);
        queryForAllCurrentTicketsUnderFlight.setParameter("bookingClassName", "Economy Save");

        List<TicketEntity> tickets = queryForAllCurrentTicketsUnderFlight.getResultList();
        return tickets.size();
    }

    // Economy Super Save
    @Override
    public int getTotalNumberOfSoldEconomyClass5Ticket(FlightEntity flight) {
        Query queryForAllCurrentTicketsUnderFlight = entityManager.createQuery("SELECT t FROM TicketEntity t "
                + "WHERE t.flight = :flight AND t.bookingClassName = :bookingClassName");
        queryForAllCurrentTicketsUnderFlight.setParameter("flight", flight);
        queryForAllCurrentTicketsUnderFlight.setParameter("bookingClassName", "Economy Super Save");

        List<TicketEntity> tickets = queryForAllCurrentTicketsUnderFlight.getResultList();
        return tickets.size();
    }

    @Override
    public int getTotalNumberOfSoldEconomyClassesTicket(FlightEntity flight) {
        Query queryForAllCurrentTicketsUnderFlight = entityManager.createQuery("SELECT t FROM TicketEntity t "
                + "WHERE t.flight = :flight AND t.seatClass = :seatClass");
        queryForAllCurrentTicketsUnderFlight.setParameter("flight", flight);
        queryForAllCurrentTicketsUnderFlight.setParameter("seatClass", "Economy Class");

        List<TicketEntity> tickets = queryForAllCurrentTicketsUnderFlight.getResultList();
        return tickets.size();
    }

    @Override
    public BookingClassEntity getEconomyClass1(FlightEntity flight) {
        Query queryForEconomyClass1 = entityManager.createQuery("SELECT bc FROM BookingClassEntity bc WHERE bc.flight = :flight");
        queryForEconomyClass1.setParameter("flight", flight);
        List<BookingClassEntity> bookingClasses = queryForEconomyClass1.getResultList();
        for (BookingClassEntity bce : bookingClasses) {
            if (bce.isEconomyClass1BookingClassEntity()) {
                return bce;
            }
        }
        return null;
    }

    @Override
    public BookingClassEntity getEconomyClass2(FlightEntity flight) {
        Query queryForEconomyClass2 = entityManager.createQuery("SELECT bc FROM BookingClassEntity bc WHERE bc.flight = :flight");
        queryForEconomyClass2.setParameter("flight", flight);
        List<BookingClassEntity> bookingClasses = queryForEconomyClass2.getResultList();
        for (BookingClassEntity bce : bookingClasses) {
            if (bce.isEconomyClass2BookingClassEntity()) {
                return bce;
            }
        }
        return null;
    }

    @Override
    public BookingClassEntity getEconomyClass3(FlightEntity flight) {
        Query queryForEconomyClass3 = entityManager.createQuery("SELECT bc FROM BookingClassEntity bc WHERE bc.flight = :flight");
        queryForEconomyClass3.setParameter("flight", flight);
        List<BookingClassEntity> bookingClasses = queryForEconomyClass3.getResultList();
        for (BookingClassEntity bce : bookingClasses) {
            if (bce.isEconomyClass3BookingClassEntity()) {
                return bce;
            }
        }
        return null;
    }

    @Override
    public BookingClassEntity getEconomyClass4(FlightEntity flight) {
        Query queryForEconomyClass4 = entityManager.createQuery("SELECT bc FROM BookingClassEntity bc WHERE bc.flight = :flight");
        queryForEconomyClass4.setParameter("flight", flight);
        List<BookingClassEntity> bookingClasses = queryForEconomyClass4.getResultList();
        for (BookingClassEntity bce : bookingClasses) {
            if (bce.isEconomyClass4BookingClassEntity()) {
                return bce;
            }
        }
        return null;
    }

    @Override
    public BookingClassEntity getEconomyClass5(FlightEntity flight) {
        Query queryForEconomyClass5 = entityManager.createQuery("SELECT bc FROM BookingClassEntity bc WHERE bc.flight = :flight");
        queryForEconomyClass5.setParameter("flight", flight);
        List<BookingClassEntity> bookingClasses = queryForEconomyClass5.getResultList();
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
     reallocate EconomyClass3 and EconomyClass4 and EconomyClass5 quotas to EconomyClass1 and EconomyClass2 quotas uniformly, 
     amount = half of left quota from 3&4&5.
     */
    @Override
    public void runYieldManagementRule1(YieldManagementRuleEntity yieldManagementRuleEntity) {
//        System.out.println("rule 1");

        FlightEntity flight = yieldManagementRuleEntity.getFlight();

        Integer nowToDeparture = getFromNowToDepartureInDay(flight);

        Double totalCost = getEconomyClassTotalCost(flight);
        Double totalRevenueTillNow = getTotalEconomyClassRevenue(flight);
        Double totalRevenueToTotalCostRatio = totalRevenueTillNow / totalCost;

//        System.out.println("totalRevenueTillNow = " + totalRevenueTillNow);
//        System.out.println("totalCost = " + totalCost);
//        System.out.println("yieldManagementRuleEntity.getTotalRevenueToTotalCostParameter() = " + yieldManagementRuleEntity.getTotalRevenueToTotalCostParameter());
        if (nowToDeparture > yieldManagementRuleEntity.getTimeToDepartureInDaysParameter()
                && totalRevenueToTotalCostRatio > yieldManagementRuleEntity.getTotalRevenueToTotalCostParameter()) {

//            System.out.println("rule 1 run.");
            BookingClassEntity economyClass1 = getEconomyClass1(flight);
            BookingClassEntity economyClass2 = getEconomyClass2(flight);
            BookingClassEntity economyClass3 = getEconomyClass3(flight);
            BookingClassEntity economyClass4 = getEconomyClass4(flight);
            BookingClassEntity economyClass5 = getEconomyClass5(flight);

            Integer class3QuotaLeft
                    = economyClass3.getQuota() - getTotalNumberOfSoldEconomyClass3Ticket(flight);
            Integer quotaToMoveFrom3 = class3QuotaLeft / 2;
            Integer class4QuotaLeft
                    = economyClass4.getQuota() - getTotalNumberOfSoldEconomyClass4Ticket(flight);
            Integer quotaToMoveFrom4 = class4QuotaLeft / 2;
            Integer class5QuotaLeft
                    = economyClass5.getQuota() - getTotalNumberOfSoldEconomyClass5Ticket(flight);
            Integer quotaToMoveFrom5 = class5QuotaLeft / 2;

            Integer totalQuotaToMove
                    = quotaToMoveFrom3 + quotaToMoveFrom4 + quotaToMoveFrom5;

            Integer quotaToMoveTo1 = totalQuotaToMove / 2;
            Integer quotaToMoveTo2 = totalQuotaToMove - quotaToMoveTo1;

            economyClass1.setQuota(economyClass1.getQuota() + quotaToMoveTo1);
            economyClass2.setQuota(economyClass2.getQuota() + quotaToMoveTo2);
            economyClass3.setQuota(economyClass3.getQuota() - quotaToMoveFrom3);
            economyClass4.setQuota(economyClass4.getQuota() - quotaToMoveFrom4);
            economyClass5.setQuota(economyClass5.getQuota() - quotaToMoveFrom5);

            disableYieldManagementRule(yieldManagementRuleEntity);

        }

    }

    /*
     When time to departure < 60 days && totalRevenue < (parameter) * totalCost, 
     reallocate EconomyClass1 and EconomyClass2 quotas to EconomyClass3 and EconomyClass4 quotas and EconomyClass5 quotas uniformly,
     amount = half of left quota from 1 and 2.
     */
    @Override
    public void runYieldManagementRule2(YieldManagementRuleEntity yieldManagementRuleEntity) {
//        System.out.println("in rule 2");

        FlightEntity flight = yieldManagementRuleEntity.getFlight();

        Integer nowToDeparture = getFromNowToDepartureInDay(flight);

        Double totalCost = getEconomyClassTotalCost(flight);
        Double totalRevenueTillNow = getTotalEconomyClassRevenue(flight);
        Double totalRevenueToTotalCostRatio = totalRevenueTillNow / totalCost;

        if (nowToDeparture < yieldManagementRuleEntity.getTimeToDepartureInDaysParameter()
                && totalRevenueToTotalCostRatio < yieldManagementRuleEntity.getTotalRevenueToTotalCostParameter()) {

//            System.out.println("rule2 runed.");
            BookingClassEntity economyClass1 = getEconomyClass1(flight);
            BookingClassEntity economyClass2 = getEconomyClass2(flight);
            BookingClassEntity economyClass3 = getEconomyClass3(flight);
            BookingClassEntity economyClass4 = getEconomyClass4(flight);
            BookingClassEntity economyClass5 = getEconomyClass5(flight);

            Integer class1QuotaLeft = economyClass1.getQuota() - getTotalNumberOfSoldEconomyClass1Ticket(flight);
            Integer class2QuotaLeft = economyClass2.getQuota() - getTotalNumberOfSoldEconomyClass2Ticket(flight);

            Integer quotaToMoveFrom1 = class1QuotaLeft / 2;
            Integer quotaToMoveFrom2 = class2QuotaLeft / 2;
            Integer quotaToMove = class1QuotaLeft + class2QuotaLeft;
            Integer quotaToMoveTo3 = quotaToMove / 3;
            Integer quotaToMoveTo4 = quotaToMove / 3;
            Integer quotaToMoveTo5 = quotaToMove - quotaToMoveTo3 - quotaToMoveTo4;

//            System.out.println("quotaToMoveFrom1 = " + quotaToMoveFrom1);
//
//            System.out.println("quotaToMoveFrom2 = " + quotaToMoveFrom2);
//            System.out.println("quotaToMoveTo3 = " + quotaToMoveTo3);
//            System.out.println("quotaToMoveTo4 = " + quotaToMoveTo4);
//            System.out.println("quotaToMoveTo5 = " + quotaToMoveTo5);
            economyClass1.setQuota(economyClass1.getQuota() - quotaToMoveFrom1);
            economyClass2.setQuota(economyClass2.getQuota() - quotaToMoveFrom2);
            economyClass3.setQuota(economyClass3.getQuota() + quotaToMoveTo3);
            economyClass4.setQuota(economyClass4.getQuota() + quotaToMoveTo4);
            economyClass5.setQuota(economyClass5.getQuota() + quotaToMoveTo5);

            disableYieldManagementRule(yieldManagementRuleEntity);

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
        Integer economyClassCapacity = bookingClassesManagementSessionBean.getEconomyClassCapacity(flight);
        Double percentageSold = ((double) economyClassTicketSold / economyClassCapacity);

        if (nowToDeparture < yieldManagementRuleEntity.getTimeToDepartureInDaysParameter()
                && percentageSold < yieldManagementRuleEntity.getPercentageSoldParameter()) {

//            System.out.println("rule3 called.");
            BookingClassEntity economyClass1 = getEconomyClass1(flight);
            BookingClassEntity economyClass2 = getEconomyClass2(flight);
            BookingClassEntity economyClass3 = getEconomyClass3(flight);
            BookingClassEntity economyClass4 = getEconomyClass4(flight);
            BookingClassEntity economyClass5 = getEconomyClass5(flight);

            Integer totalClass1Sold = getTotalNumberOfSoldEconomyClass1Ticket(flight);
            Integer totalClass2Sold = getTotalNumberOfSoldEconomyClass2Ticket(flight);
            Integer totalClass3Sold = getTotalNumberOfSoldEconomyClass3Ticket(flight);
            Integer totalClass4Sold = getTotalNumberOfSoldEconomyClass4Ticket(flight);
            Integer totalClass5Sold = getTotalNumberOfSoldEconomyClass5Ticket(flight);

            Integer Class2and3and4totalQuotaLeft = economyClass2.getQuota() + economyClass3.getQuota()
                    + economyClass4.getQuota() - totalClass2Sold - totalClass3Sold - totalClass4Sold;

//            System.out.println("Class2and3and4totalQuotaLeft = " + Class2and3and4totalQuotaLeft);
            Integer quotaToClass1 = Class2and3and4totalQuotaLeft / 2;
            Integer quotaToClass5 = Class2and3and4totalQuotaLeft - quotaToClass1;

//            System.out.println("quotaToClass1 = " + quotaToClass1 + "; quotaToClass5 = " + quotaToClass5);
            economyClass1.setQuota(economyClass1.getQuota() + quotaToClass1);
            economyClass2.setQuota(totalClass2Sold);
            economyClass3.setQuota(totalClass3Sold);
            economyClass4.setQuota(totalClass4Sold);
            economyClass5.setQuota(economyClass5.getQuota() + quotaToClass5);

            disableYieldManagementRule(yieldManagementRuleEntity);

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

        if (nowToDeparture > yieldManagementRuleEntity.getTimeToDepartureInDaysParameter()) {

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
            int quotaForChangeFromClass3 = (int) (changeEconomyClass3and4and5To1Or2Percentage * economyClass3LeftQuota);
            int quotaForChangeFromClass4 = (int) (changeEconomyClass3and4and5To1Or2Percentage * economyClass4LeftQuota);
            int quotaForChangeFromClass5 = (int) (changeEconomyClass3and4and5To1Or2Percentage * economyClass5LeftQuota);
            int quotaForChangeEconomyClass2and3and4To1Or2 = quotaForChangeFromClass3 + quotaForChangeFromClass4 + quotaForChangeFromClass5;

            if (quotaForChangeEconomyClass2and3and4To1Or2 == 0) {
                disableYieldManagementRule(yieldManagementRuleEntity);
            } else {

                if (economyClass1.getQuota() != 0
                        && economyClass1LeftQuota < yieldManagementRuleEntity.getEconomyClass1RemainingQuotaParameter()) {

//                    System.out.println("rule4 runed. economyClass 1 sold out");
                    economyClass1.setQuota(economyClass1.getQuota() + quotaForChangeEconomyClass2and3and4To1Or2);
                    economyClass3.setQuota(economyClass3.getQuota() - quotaForChangeFromClass3);
                    economyClass4.setQuota(economyClass4.getQuota() - quotaForChangeFromClass4);
                    economyClass5.setQuota(economyClass5.getQuota() - quotaForChangeFromClass5);

                } else if (economyClass2LeftQuota < yieldManagementRuleEntity.getEconomyClass2RemainingQuotaParameter()) {

//                    System.out.println("rule4 runed. economyClass 2 sold out");
                    economyClass2.setQuota(economyClass2.getQuota() + quotaForChangeEconomyClass2and3and4To1Or2);
                    economyClass3.setQuota(economyClass3.getQuota() - quotaForChangeFromClass3);
                    economyClass4.setQuota(economyClass4.getQuota() - quotaForChangeFromClass4);
                    economyClass5.setQuota(economyClass5.getQuota() - quotaForChangeFromClass5);

                }

            }
        } else {
            disableYieldManagementRule(yieldManagementRuleEntity);
        }
    }

    @Override
    public void insertRules() {
        Query queryForFlights = entityManager.createQuery("SELECT f FROM FlightEntity f");
        List<FlightEntity> flights = queryForFlights.getResultList();
        FlightEntity flight1 = flights.get(0);
        FlightEntity flight2 = flights.get(1);

        YieldManagementRuleEntity yieldManagementRuleEntity1
                = new YieldManagementRuleEntity().YieldManagementRule1Entity(flight1, 60, 1.5);
        YieldManagementRuleEntity yieldManagementRuleEntity2
                = new YieldManagementRuleEntity().YieldManagementRule2Entity(flight1, 60, 1.0);
        YieldManagementRuleEntity yieldManagementRuleEntity3
                = new YieldManagementRuleEntity().YieldManagementRule3Entity(flight1, 10, 0.8);
        YieldManagementRuleEntity yieldManagementRuleEntity4
                = new YieldManagementRuleEntity().YieldManagementRule4Entity(flight1, 15, 3, 5, 0.3);
        YieldManagementRuleEntity yieldManagementRuleEntity11
                = new YieldManagementRuleEntity().YieldManagementRule1Entity(flight2, 60, 1.5);
        YieldManagementRuleEntity yieldManagementRuleEntity12
                = new YieldManagementRuleEntity().YieldManagementRule2Entity(flight2, 60, 1.0);
        YieldManagementRuleEntity yieldManagementRuleEntity13
                = new YieldManagementRuleEntity().YieldManagementRule3Entity(flight2, 10, 0.8);
        YieldManagementRuleEntity yieldManagementRuleEntity14
                = new YieldManagementRuleEntity().YieldManagementRule4Entity(flight2, 15, 3, 8, 0.5);

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
    public void updateAllRoutePopularity() {
        Query queryAllRoutes = entityManager.createQuery("SELECT r FROM RouteEntity r");
        List<RouteEntity> routes = queryAllRoutes.getResultList();
        for (RouteEntity route : routes) {
            route.setPopularity(getRoutePopularity(route));
        }
    }

    @Override
    public void autoCreateRulesForFlight(FlightEntity flight) {
//        System.out.println("autoCreateRulesForFlight");
        updateAllRoutePopularity();

        Double normalizedPopularity = getNormalizedPopularity(flight.getRoute());

        createRule1(flight, normalizedPopularity);
        createRule2(flight, normalizedPopularity);
        createRule3(flight, normalizedPopularity);
        createRule4(flight, normalizedPopularity);
    }

    @Override
    public double getNormalizedPopularity(RouteEntity route) {
        Double popularity = route.getPopularity();
        Query queryForMinMax = entityManager.createQuery("SELECT r.popularity FROM RouteEntity r ORDER BY r.popularity ASC");
        int size = queryForMinMax.getResultList().size();

        Double normalizedPopularity = 0.5;

        if (size != 0) {
            Double maxPopularity = (Double) queryForMinMax.getResultList().get(size - 1);
            Double minPopularity = (Double) queryForMinMax.getResultList().get(0);
            if (!Objects.equals(maxPopularity, minPopularity)) {
                normalizedPopularity = normalizePopularity(popularity, maxPopularity, minPopularity);
            }

        }
        return normalizedPopularity;
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
        rule1.setFlight(flight);
        entityManager.persist(rule1);
    }

    private void createRule2(FlightEntity flight, Double normalizedPopularity) {
//        A = 40 + np* (80-40)
        Integer a = (int) (40 + normalizedPopularity * (80 - 40));
//        B = 0.5+ np* (1.2-0.5)
        Double b = 0.5 + normalizedPopularity * (1.2 - 0.5);
        YieldManagementRuleEntity rule2 = new YieldManagementRuleEntity().YieldManagementRule2Entity(flight, a, b);
        rule2.setFlight(flight);
        entityManager.persist(rule2);
    }

    private void createRule3(FlightEntity flight, Double normalizedPopularity) {
//        A = 10 + np* (20-10)
        Integer a = (int) (10 + normalizedPopularity * (20 - 10));
//        B = 0.6+ np* (0.8-0.6)
        Double b = 0.6 + normalizedPopularity * (0.8 - 0.6);
        YieldManagementRuleEntity rule3 = new YieldManagementRuleEntity().YieldManagementRule3Entity(flight, a, b);
        rule3.setFlight(flight);
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
        rule4.setFlight(flight);
        entityManager.persist(rule4);
    }

    @Override
    public void disableYieldManagementRule(YieldManagementRuleEntity yieldManagementRuleEntity) {
        YieldManagementRuleEntity yieldManagementRuleEntityToUpdate
                = entityManager.find(YieldManagementRuleEntity.class, yieldManagementRuleEntity.getId());
        yieldManagementRuleEntityToUpdate.setEnabled(false);
    }

}
