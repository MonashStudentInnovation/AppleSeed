package edu.monash.monplan.repository;

import com.threewks.gaetools.logger.Logger;
import com.threewks.gaetools.objectify.repository.StringRepository;
import com.threewks.gaetools.search.Is;
import com.threewks.gaetools.search.Result;
import com.threewks.gaetools.search.Search;
import com.threewks.gaetools.search.gae.SearchConfig;
import edu.monash.monplan.model.Course;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CourseRepository extends StringRepository<Course> {
    private static final int SEARCH_LIMIT = 1000;

    public CourseRepository(SearchConfig searchConfig) {
        super(Course.class, searchConfig);
    }

    public List<Course> listByCourseName(String courseName) {
        return search(courseName);
    }

    public List<Course> search(String courseName) {
        Search<Course, String> search = createSearch(courseName);
        Result<Course, String> result = search.run();
        if (result.getReturnedRecordCount() == SEARCH_LIMIT) {
            Logger.error("Search returned maximum records - dashboard will be missing items. Filters: " + courseName);
        }
        return new ArrayList<>(result.getResults());
    }

    private Search<Course, String> createSearch(String courseName) {
        Search<Course, String> search = search();
        search = search.limit(SEARCH_LIMIT);
        search = search.field(Course.SearchFields.CourseName, Is.EqualTo, courseName.toUpperCase());

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
