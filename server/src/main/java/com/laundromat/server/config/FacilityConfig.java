package com.laundromat.server.config;
 
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.laundromat.server.db.Query;
import com.laundromat.server.facility.Facility;
import com.laundromat.server.model.Machine;

@Configuration
public class FacilityConfig {

    @Bean
    public Facility facility(Query query) {
        Machine[] washers = Query.allWashers();
        Machine[] dryers = Query.allDryers();

        Facility f = new Facility(washers, dryers);

        f.startMonitoringQueues();

        return f;
    }
}
 