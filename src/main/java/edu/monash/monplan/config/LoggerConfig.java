//package edu.monash.monplan.config;
//
//import edu.monash.monplan.model.Course;
//import edu.monash.monplan.prebuilts.MonPlanRequestLoggingFilter;
//import edu.monash.monplan.service.LogService;
//import org.monplan.abstraction_layer.MonPlanService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Lazy;
//
//import javax.annotation.PostConstruct;
//
//@Configuration
//@ComponentScan("edu.monash.monplan")
//public class LoggerConfig {
//
//    @Bean
//    @Lazy
//    public MonPlanRequestLoggingFilter requestLoggingFilter() {
//        MonPlanRequestLoggingFilter filter = new MonPlanRequestLoggingFilter();
//        filter.setIncludeQueryString(true);
//        filter.setIncludePayload(true);
//        filter.setMaxPayloadLength(1000);
//
//        filter.setIncludeHeaders(false);
//        filter.setIncludeClientInfo(false);
//        return filter;
//    }
//
//}
