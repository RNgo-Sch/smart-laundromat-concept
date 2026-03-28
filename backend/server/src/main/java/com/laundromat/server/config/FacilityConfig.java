package com.laundromat.server.config;
 
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.laundromat.server.facility.Facility;
import com.laundromat.server.model.Machine;
 
@Configuration
public class FacilityConfig {

    @Bean
    public Facility facility() {
        // TODO: Replace with Supabase fetch
        Machine[] washers = { new Machine(), new Machine() };
        Machine[] dryers = { new Machine(), new Machine() };

        Facility f = new Facility(washers, dryers);

        f.startMonitoringQueues();

        return f;
    }
}
 