
package imas.departure.sessionbean;

import imas.common.entity.CabinCrewEntity;
import imas.common.entity.PilotEntity;
import imas.planning.entity.FlightEntity;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Lei
 */
@Stateless
public class TimeManagementSessionBean implements TimeManagementSessionBeanLocal {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<FlightEntity> fetchComingFlights(String base) {
        Query query = em.createQuery("SELECT f FROM FlightEntity f WHERE f.route.originAirport.airportCode = :base");
        query.setParameter("base", base);

        List<FlightEntity> flights = (List<FlightEntity>) query.getResultList();
        List<FlightEntity> comingFlights = new ArrayList<FlightEntity>();

        Calendar cal = Calendar.getInstance();
        Date currentDate = new Date();
        currentDate = new Date(currentDate.getTime() - (1000 * 60 * 60));
//        System.out.print(currentDate);
        cal.add(Calendar.YEAR, 2);
        cal.add(Calendar.HOUR, 24);
        Date limitDate = cal.getTime();
//        System.out.print(limitDate);

        if (flights.isEmpty()) {
            return null;
        } else {
            for (int i = 0; i < flights.size(); i++) {
                if (flights.get(i).getDepartureDate().after(currentDate) && flights.get(i).getDepartureDate().before(limitDate)
                        && (flights.get(i).getActualDepartureDate() == null || flights.get(i).getActualArrivalDate() == null)) {
                    comingFlights.add(flights.get(i));
                }

            }
            Collections.sort(flights);
            return comingFlights;
        }
    }

    @Override
    public int updateActualDepartureTime(FlightEntity selectedFlight) {
        if (selectedFlight.getActualDepartureDate() == null) {
            Calendar cal = Calendar.getInstance();
            Date currentDate = cal.getTime();
            selectedFlight.setActualDepartureDate(currentDate);
            em.merge(selectedFlight);
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public int updateActualArriveTime(FlightEntity selectedFlight) {
        if (selectedFlight.getActualArrivalDate() == null) {
            Calendar cal = Calendar.getInstance();
            Date currentDate = cal.getTime();
            selectedFlight.setActualArrivalDate(currentDate);
            em.merge(selectedFlight);

            List<CabinCrewEntity> cabinCrews = selectedFlight.getCabinCrews();
            List<PilotEntity> pilots = selectedFlight.getPilots();

            for (int i = 0; i < cabinCrews.size(); i++) {
                cabinCrews.get(i).setWorkingStatus("available");
                em.merge(cabinCrews.get(i));
            }
            for (int j = 0; j < pilots.size(); j++) {
                pilots.get(j).setWorkingStatus("available");
                em.merge(pilots.get(j));
            }

            return 1;
        } else {
            return 0;
        }
    }

}
