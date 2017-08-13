package hello.config;

import com.googlecode.objectify.ObjectifyService;
import hello.model.Diesel;
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
        register(Diesel.class);
    }


    private void register(Class<?>... entityClasses) {
        Arrays.stream(entityClasses)
                .forEach(ObjectifyService::register);
    }

}
