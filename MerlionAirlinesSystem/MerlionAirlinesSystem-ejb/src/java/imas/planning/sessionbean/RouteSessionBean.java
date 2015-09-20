/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package imas.planning.sessionbean;

import imas.planning.entity.AirportEntity;
import imas.planning.entity.RouteEntity;
import imas.planning.entity.RouteGroupEntity;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author ruicai
 */
@Stateless
public class RouteSessionBean implements RouteSessionBeanLocal {
    @PersistenceContext
    private EntityManager em;
    
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public Boolean checkRoute(AirportEntity origin, AirportEntity destination) {
        Query query = em.createQuery("SELECT a FROM RouteEntity a WHERE a.originAirport = :origin AND a.destinationAirport = :destination");
        query.setParameter("origin", origin);
        query.setParameter("destination", destination);
        List<RouteEntity> route = (List<RouteEntity>) query.getResultList();
        if (route.isEmpty()) {
            System.out.print("empty");
            return true;
        } else {
            System.out.print("full1");
            return false;

        }
    }

    @Override
    public void addRoute(AirportEntity origin, AirportEntity destination) {

        RouteEntity route = new RouteEntity(origin, destination);
        RouteEntity reverseRoute = new RouteEntity(destination, origin);
        route.setReverseRoute(reverseRoute);
        reverseRoute.setReverseRoute(route);
        em.persist(route);
        em.persist(reverseRoute);

    }
    

    @Override
    public Boolean connectHubSpoke(String hub, String spoke) {
        Query query = em.createQuery("SELECT a FROM AirportEntity a WHERE a.airportName = :hubN");
        query.setParameter("hubN", hub);
        AirportEntity hubEntity = (AirportEntity) query.getSingleResult();
        query = em.createQuery("SELECT a FROM AirportEntity a WHERE a.airportName = :spokeN");
        query.setParameter("spokeN", spoke);
        AirportEntity spokeEntity = (AirportEntity) query.getSingleResult();
        if (checkRoute(hubEntity, spokeEntity)) {
            addRoute(hubEntity, spokeEntity);
            return true;
        }
        else
            return false;
    }
    
    
    
    @Override
    public List<AirportEntity> retrieveHubs() {
        Query query = em.createQuery("SELECT a FROM AirportEntity a WHERE a.hubOrSpoke = :true");
        query.setParameter("true", true);

        return (List<AirportEntity>) query.getResultList();
    }

    @Override
    public List<AirportEntity> retrieveSpokes() {
        Query query = em.createQuery("SELECT a FROM AirportEntity a WHERE a.hubOrSpoke = :false");
        query.setParameter("false", false);

        return (List<AirportEntity>) query.getResultList();
    }

    @Override
    public void deleteRoute(AirportEntity hub, AirportEntity spoke) {
        Query query = em.createQuery("SELECT a FROM RouteEntity a WHERE a.originAirport = :hub AND a.destinationAirport = :spoke");
        query.setParameter("hub", hub);
        query.setParameter("spoke", spoke);
        if (!query.getResultList().isEmpty()) {
            RouteEntity route = (RouteEntity) query.getSingleResult();
            RouteEntity reverseRoute = route.getReverseRoute();
            em.remove(route);
            em.remove(reverseRoute);
        }
    }

    @Override
    public List<RouteEntity> retrieveAllRoutes() {
        Query query = em.createQuery("SELECT a FROM RouteEntity a");
        System.out.println("Debug into retrieveAllRoute function");

        return (List<RouteEntity>) query.getResultList();
    }

    @Override
    public List<String> retrieveAllConnectionName() {
        System.out.println("Debug into retrieveAllConnectionName function");
        List<RouteEntity> routes = retrieveAllRoutes();
        System.out.println("Debug after retrieveAllRoutes function");

        List<String> routesName = new ArrayList<String>();
        for (Object o : routes) {
            RouteEntity r = (RouteEntity) o;
            String origin = r.getOriginAirport().getAirportName();
            String destination = r.getDestinationAirport().getAirportName();
            String routeName = origin + " to " + destination;
            routesName.add(routeName);

        }
        System.out.println("Debug 1");

        return routesName;
    }

    @Override
    public void deleteRoutesByName(String origin, String destination) {
        Query q = em.createQuery("SELECT a FROM AirportEntity a WHERE a.airportName = :name");
        q.setParameter("name", origin);
        AirportEntity originAirportEntity = (AirportEntity) q.getSingleResult();
        q = em.createQuery("SELECT a FROM AirportEntity a WHERE a.airportName = :name");
        q.setParameter("name", destination);
        AirportEntity destinationAirportEntity = (AirportEntity) q.getSingleResult();

        deleteRoute(originAirportEntity, destinationAirportEntity);

    }

    @Override
    public void updateRoutesInfo(List<RouteEntity> routes) {
        Query query;
        for (Object o : routes) {
            RouteEntity routeNew = (RouteEntity) o;
            query = em.createQuery("SELECT a FROM RouteEntity a WHERE a.id = :id");
            query.setParameter("id", routeNew.getId());

            RouteEntity routeOri = (RouteEntity) query.getSingleResult();
            routeOri.setDistance(routeNew.getDistance());
            System.out.println("Distance" + routeNew.getDistance());
            routeOri.setFlightHours(routeNew.getFlightHours());
            System.out.println("before persist");
            em.persist(routeOri);
        }
    }

    @Override
    public void updateRouteInfo(RouteEntity route) {
        Query query = em.createQuery("SELECT a FROM RouteEntity a WHERE a.id = :id");
        query.setParameter("id", route.getId());
        RouteEntity routeOri = (RouteEntity) query.getSingleResult();
        routeOri.setDistance(route.getDistance());
        System.out.println("Distance" + route.getDistance());
        routeOri.setFlightHours(route.getFlightHours());
        System.out.println("before persist");

    }
    @Override
    public List<RouteEntity> retrieveRoutesWithinRange(Double max, Double min) {
        Query q = em.createQuery("SELECT a FROM RouteEntity a WHERE a.distance >= :min AND a.distance <= :max");
        q.setParameter("min", min);
        q.setParameter("max", max);
        if(!q.getResultList().isEmpty()){
            return (List<RouteEntity>)q.getResultList();
        }
        return new ArrayList<RouteEntity>();
                
    }

    @Override
    public List<RouteEntity> filterRoutesToConnections(List<RouteEntity> routes) {
        System.out.println("Enter filterRoutesToConneci"+routes.size());
        List<RouteEntity> routesFiltered = new ArrayList<RouteEntity>();
        if(!routes.isEmpty()){
        for(Object o: routes){
            RouteEntity route = (RouteEntity)o;
            for(Object obj: routes){
                RouteEntity routeIfReverse = (RouteEntity)obj;
                System.out.println("before if");
                if(route.getReverseRoute()!=null){
                if(route.getReverseRoute().getId().equals(routeIfReverse.getId())){
                    System.out.println("Enter if statement for comparing the ID");

                    routesFiltered.add(route);
                    routes.set(routes.indexOf(routeIfReverse), new RouteEntity());
                    System.out.println("After remove");
                   
                } }
                System.out.println("After if");
                    
            }
        System.out.println("After for");    
        }
        }
        System.out.println("Leave filterRoutesToConneci and routes");
        return routesFiltered;
    }

    @Override
    public void createRouteGroup(String groupCode, Double maxRange, Double minRange, ArrayList<RouteEntity> routesGrped) {
        RouteGroupEntity routeGrp = new RouteGroupEntity(groupCode, minRange, maxRange);
        System.out.println("routesGrped: "+routesGrped.get(0).getOriginAirport().getAirportName());
        routeGrp.setGroup(routesGrped);
        if(!routesGrped.isEmpty()){
            for(RouteEntity rt: routesGrped){
                System.out.println("before find em ");

                RouteEntity route = em.find(RouteEntity.class, rt.getId());
                System.out.println("after find em0 ");
                route.setRouteGroup(routeGrp);
                System.out.println("after find em ");

            }
        }
        System.out.println("before persist ");

        em.persist(routeGrp);
                        System.out.println("after persist em ");

    }

    @Override
    public Boolean availabilityCheck(Double range) {
        Query query = em.createQuery("SELECT a FROM AircraftTypeEntity a WHERE a.aircraftRange >= :range");
        query.setParameter("range", range);
        if(!query.getResultList().isEmpty()){
            return true;
        }
        else
            return false;
        
    }

    @Override
    public void AddDistToRoute(String hub, String spoke, Double distance) {
        Query query = em.createQuery("SELECT a FROM RouteEntity a WHERE a.originAirport.airportName =:hub AND a.destinationAirport.airportName = :spoke ");
        query.setParameter("hub", hub);
        query.setParameter("spoke", spoke);
        RouteEntity route1 = (RouteEntity)query.getSingleResult();
        route1.setDistance(distance);
        query = em.createQuery("SELECT a FROM RouteEntity a WHERE a.destinationAirport.airportName =:hub AND a.originAirport.airportName = :spoke ");
        query.setParameter("hub", hub);
        query.setParameter("spoke", spoke);
        RouteEntity route2 = (RouteEntity)query.getSingleResult();
        route2.setDistance(distance);
    }

    
    
    
    
    
}
