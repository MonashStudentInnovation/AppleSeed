package hello.repository;

import com.threewks.gaetools.objectify.repository.LongRepository;
import com.threewks.gaetools.search.gae.SearchConfig;
import hello.model.Unit;
import org.springframework.stereotype.Repository;

@Repository
public class UnitRepository extends LongRepository<Unit> {

    public UnitRepository(SearchConfig searchConfig) {
        super(Unit.class, searchConfig);
    }

}
