package edu.monash.monplan.service;

import edu.monash.monplan.model.Course;
import org.monplan.abstraction_layer.MonPlanRepository;
import org.monplan.abstraction_layer.MonPlanService;
import org.springframework.stereotype.Service;

@Service
public class CourseService extends MonPlanService<Course> {

    public CourseService(MonPlanRepository<Course> repository) {
        super(repository);
    }
}
