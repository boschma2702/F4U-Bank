package com.bank.configuration;

import com.bank.service.time.TimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(TimeService.class)
public class TimeServiceConfiguration {

    @Autowired
    private TimeService timeService;

}
