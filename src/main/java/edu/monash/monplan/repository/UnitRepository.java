package edu.monash.monplan.repository;

import com.threewks.gaetools.logger.Logger;
import com.threewks.gaetools.objectify.repository.StringRepository;
import com.threewks.gaetools.search.Is;
import com.threewks.gaetools.search.Result;
import com.threewks.gaetools.search.Search;
import com.threewks.gaetools.search.gae.SearchConfig;
import edu.monash.monplan.model.Unit;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UnitRepository extends StringRepository<Unit> {

    private static final int SEARCH_LIMIT = 1000;

    public UnitRepository(SearchConfig searchConfig) {
        super(Unit.class, searchConfig);
    }

    public List<Unit> listByUnitName(String careerName) {
        return search(careerName);
    }

    public List<Unit> search(String unitName) {
        Search<Unit, String> search = createSearch(unitName);
        Result<Unit, String> result = search.run();
        if (result.getReturnedRecordCount() == SEARCH_LIMIT) {
            Logger.error("Search returned maximum records - dashboard will be missing items. Filters: " + unitName);
        }
        return new ArrayList<>(result.getResults());
    }

    private Search<Unit, String> createSearch(String careerName) {
        Search<Unit, String> search = search();
        search = search.limit(SEARCH_LIMIT);
        search = search.field(Unit.SearchFields.UnitName, Is.EqualTo, careerName.toUpperCase());

//        if (filters != null) {
//            if (filters.getApplicationStatus() != null) {
//                search = search.field(Application.SearchFields.Status, Is.EqualTo, filters.getApplicationStatus());
//            } else {
//                search = search.field(Application.SearchFields.Status, Is.EqualTo, asList(ApplicationStatus.notDrafted()));
//            }
//            if (StringUtils.isNotBlank(filters.getNameSearchTerm())) {
//                search = search.field(Application.SearchFields.StudentNameSearchableText, Is.EqualTo, filters.getNameSearchTerm().toUpperCase());
//            }
//        }

        return search;
    }
}
