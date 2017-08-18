package edu.monash.monplan.config;

import com.googlecode.objectify.ObjectifyService;
import edu.monash.monplan.model.Course;
import edu.monash.monplan.model.Unit;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Arrays;

@Configuration
public class ObjectifyConfig {

    @PostConstruct
    public void init() {
        registerObjectifyEntities();
    }

    private void registerObjectifyEntities() {
        register(Unit.class);
        register(Course.class);
    }


    private void register(Class<?>... entityClasses) {
        Arrays.stream(entityClasses)
                .forEach(ObjectifyService::register);
    }

}
