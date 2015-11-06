/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GDS.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author Scarlett
 */
@Entity
public class GDSAirportEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;            // Name of airport. May or may not contain the City name.
    private String city;            // Main city served by airport. May be spelled differently from Name.
    private String country;         // Country or territory where airport is located.
    private String IATAorFAA;       // 3-letter FAA code, for airports located in Country "United States of America".
                                    // 3-letter IATA code, for all other airports.
                                    // Blank if not assigned.
    private String ICAO;            // 4-letter ICAO code. Blank if not assigned.
    private Double latitude;        // Decimal degrees, usually to six significant digits. Negative is South, positive is North.
    private Double longitude;       // Decimal degrees, usually to six significant digits. Negative is West, positive is East.
    private Double altitude;        // In feet.
    private Double timezone;        // Hours offset from UTC. Fractional hours are expressed as decimals, eg. India is 5.5.
    private char DST;               // Daylight savings time. One of E (Europe), A (US/Canada), S (South America), O (Australia), Z (New Zealand), N (None) or U (Unknown). See also: Help: Time
    private String tz;              // database time zone	Timezone in "tz" (Olson) format, eg. "America/Los_Angeles".
    
    public GDSAirportEntity() {
    }

    public GDSAirportEntity(String name, String city, String country, String IATAorFAA, String ICAO, Double latitude, Double longitude, Double altitude, Double timezone, char DST, String tz) {
        this.name = name;
        this.city = city;
        this.country = country;
        this.IATAorFAA = IATAorFAA;
        this.ICAO = ICAO;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.timezone = timezone;
        this.DST = DST;
        this.tz = tz;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getIATAorFAA() {
        return IATAorFAA;
    }

    public void setIATAorFAA(String IATAorFAA) {
        this.IATAorFAA = IATAorFAA;
    }

    public String getICAO() {
        return ICAO;
    }

    public void setICAO(String ICAO) {
        this.ICAO = ICAO;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getAltitude() {
        return altitude;
    }

    public void setAltitude(Double altitude) {
        this.altitude = altitude;
    }

    public Double getTimezone() {
        return timezone;
    }

    public void setTimezone(Double timezone) {
        this.timezone = timezone;
    }

    public char getDST() {
        return DST;
    }

    public void setDST(char DST) {
        this.DST = DST;
    }

    public String getTz() {
        return tz;
    }

    public void setTz(String tz) {
        this.tz = tz;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GDSAirportEntity)) {
            return false;
        }
        GDSAirportEntity other = (GDSAirportEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "imas.planning.entity.GDSAirportEntity[ id=" + id + " ]";
    }

}
