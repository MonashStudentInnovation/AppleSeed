package edu.monash.monplan.service;

import edu.monash.monplan.model.Course;
import edu.monash.monplan.repository.MonPlanRepository;
import org.springframework.stereotype.Service;

@Service
public class CourseService extends MonPlanService<Course> {

    public CourseService(MonPlanRepository<Course> repository) {
        super(repository);
    }
}
