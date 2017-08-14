package edu.monash.monplan.repository;

import com.threewks.gaetools.objectify.repository.UuidRepository;
import com.threewks.gaetools.search.gae.SearchConfig;
import edu.monash.monplan.model.Unit;
import org.springframework.stereotype.Repository;

@Repository
public class UnitRepository extends UuidRepository<Unit> {

    public UnitRepository(SearchConfig searchConfig) {
        super(Unit.class, searchConfig);
    }

}
