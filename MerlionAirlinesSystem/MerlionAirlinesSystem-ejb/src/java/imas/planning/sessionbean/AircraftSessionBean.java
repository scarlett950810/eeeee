/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.planning.sessionbean;

import imas.planning.entity.AircraftEntity;
import imas.planning.entity.AircraftGroupEntity;
import imas.planning.entity.AircraftTypeEntity;
import imas.planning.entity.AirportEntity;
import imas.planning.entity.SeatEntity;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Scarlett
 */
@Stateless
public class AircraftSessionBean implements AircraftSessionBeanLocal {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<AircraftGroupEntity> getAircraftGroups() {
        Query query = em.createQuery("SELECT a FROM AircraftGroupEntity a");
        List<AircraftGroupEntity> aircraftGroups = (List<AircraftGroupEntity>) query.getResultList();
        return aircraftGroups;
    }

    @Override
    public List<AirportEntity> getAirports() {
        Query query = em.createQuery("SELECT a FROM AirportEntity a");
        List<AirportEntity> airports = (List<AirportEntity>) query.getResultList();
        return airports;
    }

    @Override
    public List<String> getSeatClasses() {
        List<String> seatClasses = new ArrayList();
        seatClasses.add("First Class");
        seatClasses.add("Business Class");
        seatClasses.add("Premuim Economy Class");
        seatClasses.add("Economy Class");

        return seatClasses;
    }

    @Override
    public List<AircraftTypeEntity> getAircraftTypes() {
        Query query = em.createQuery("SELECT a FROM AircraftTypeEntity a");
        List<AircraftTypeEntity> aircraftTypes = (List<AircraftTypeEntity>) query.getResultList();
        return aircraftTypes;
    }

    @Override
    public void addAircraft(String tailId, AircraftTypeEntity aircraftType, Double purchasePrice, Double deprecation, Double netAssetValue, 
            Double aircraftLife, Double operationYear, String conditionDescription,AirportEntity airportHub, AirportEntity currentAirport, 
            AircraftGroupEntity aircraftGroup, int FirstClassColumnNo, int FirstClassRowNo, int BusinessClassColumnNo, int BusinessClassRowNo, 
            int PremiumEconomyClassColumnNo, int PremiumEconomyClassRowNo, int EconomyClassColumnNo, int EconomyClassRowNo) {
        System.out.print("AircraftSessionBean.addAircraft called.");
        
        AircraftEntity newAircraft = new AircraftEntity(tailId, aircraftType, purchasePrice, deprecation, netAssetValue, aircraftLife, operationYear, conditionDescription, airportHub, currentAirport, aircraftGroup);
        em.persist(newAircraft);
        int startRow = 1;
        createSeats(newAircraft, FirstClassColumnNo, startRow, FirstClassRowNo, "First Class");
        startRow = startRow + FirstClassRowNo;
        createSeats(newAircraft, BusinessClassColumnNo, startRow, BusinessClassRowNo, "Business Class");
        startRow = startRow + BusinessClassRowNo;
        createSeats(newAircraft, PremiumEconomyClassColumnNo, startRow, PremiumEconomyClassRowNo, "Premium Economy Class");
        startRow = startRow + PremiumEconomyClassRowNo;
        createSeats(newAircraft, EconomyClassColumnNo, startRow, EconomyClassRowNo, "Economy Class");
    }

    private void createSeats(AircraftEntity aircraft, int column, int startRow, int row, String seatClass) {
        char endAlphabet = 'A';
        switch (column) {
            case 4:
                endAlphabet = 'D';
                break;
            case 6:
                endAlphabet = 'F';
                break;
            case 7:
                endAlphabet = 'G';
                break;
        }

        for (int i = startRow; i < startRow + row; i++) {
            for (char alphabet = 'A'; alphabet <= endAlphabet; alphabet++) {
                String seatNo = Integer.toString(i) + alphabet;
                SeatEntity newSeat = new SeatEntity(aircraft, seatNo, seatClass);
                em.persist(newSeat);
            }
        }
    }
}
