package edu.monash.monplan.repository;

import com.threewks.gaetools.search.gae.SearchConfig;
import edu.monash.monplan.model.Log;
import org.monplan.abstraction_layer.MonPlanRepository;
import org.springframework.stereotype.Repository;

@Repository
public class LogRepository extends MonPlanRepository<Log> {

    public LogRepository(SearchConfig searchConfig) {
        super(Log.class, searchConfig, null, null);
    }

}
