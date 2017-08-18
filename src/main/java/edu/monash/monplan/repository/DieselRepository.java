package edu.monash.monplan.repository;

import com.threewks.gaetools.objectify.repository.LongRepository;
import com.threewks.gaetools.search.gae.SearchConfig;
import edu.monash.monplan.model.Diesel;
import org.springframework.stereotype.Repository;

@Repository
public class DieselRepository extends LongRepository<Diesel> {

    public DieselRepository(SearchConfig searchConfig) {
        super(Diesel.class, searchConfig);
    }

}
