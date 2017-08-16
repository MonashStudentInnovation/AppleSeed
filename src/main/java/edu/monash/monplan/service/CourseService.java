package edu.monash.monplan.service;

import edu.monash.monplan.model.Course;
import edu.monash.monplan.repository.CourseRepository;
import org.monplan.exceptions.FailedOperationException;
import org.monplan.exceptions.NotFoundException;
import org.springframework.stereotype.Service;


import edu.monash.monplan.model.Unit;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
/**
 * Course Service
 */
public class CourseService {

    private final CourseRepository courseRepository;

    /**
     * Save Course
     * @param course
     * @return
     */
    private Course save(final Course course) {
        if (course.getId() == null){
            // we initialise a new UUID if we don't have a UUID for the new Unit
            course.init();
        }
        return courseRepository.put(course);
    }

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public Course getByCourseCode(String courseCode) {
        // TODO: This doesn't work yet.
        List<Course> courses = courseRepository.getByField("courseCode", courseCode);
        System.out.println("course code: " + courseCode);
        // The search result is either 0 or 1 units.
        if (courses.size() == 1) {
            return courses.get(0);
        }
        return null;

    }

    public Course addCourse(Course course){
        return this.save(course);
    }


    /**
     * Lists the first 5000 courses.
     * TODO: Parametrize number of courses to list.
     * @return a list of courses.
     */
    public List<Course> listAllUnits() {
        return courseRepository.list(5000);
    }

    @Async
    public void delete(String id) {
        // TODO: Integrate with new delete in unit.
        Course course = courseRepository.get(id);
        if (course == null) {
            throw new NotFoundException();
        }
        courseRepository.deleteByKey(id);

        course = courseRepository.get(id);
        if (course != null) {
            throw new FailedOperationException();
        }
    }

    public Course find(String id){
        return courseRepository.get(id);
    }

    public List<Course> getByName(String courseName) {return courseRepository.listByCourseName(courseName);}


}
