package edu.monash.monplan.repository;

import com.threewks.gaetools.search.gae.SearchConfig;
import edu.monash.monplan.model.Course;
import org.springframework.stereotype.Repository;

@Repository
public class CourseRepository extends MonPlanRepository<Course> {

    public CourseRepository(SearchConfig searchConfig) {
        super(Course.class, searchConfig, Course.codeField, Course.nameField);
    }
}
