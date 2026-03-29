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
        Machine[] washers = { new Machine(0), new Machine(1) };
        Machine[] dryers = { new Machine(2), new Machine(3) };

        Facility f = new Facility(washers, dryers);

        f.startMonitoringQueues();

        return f;
    }
}
 