package edu.monash.monplan.service;

import edu.monash.monplan.model.Course;
import edu.monash.monplan.repository.CourseRepository;
import org.monplan.InsufficientResourcesException;
import org.monplan.exceptions.FailedOperationException;
import org.monplan.exceptions.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Async;

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

    public Course getCourseByCourseCode(String courseCode) {
        List<Course> courses = courseRepository.getByField("courseCode", courseCode);
        System.out.println("course code: " + courseCode);
        // The search result is either 0 or 1 units.
        if (courses.size() == 1) {
            return courses.get(0);
        }
        return null;
    }

    public Course createCourse(Course course) throws FailedOperationException {
        // Check that course has an id.
        if (course.getId() != null) {
            // Ensure id is not duplicated.
            Course courseWithId = courseRepository.get(course.getId());

            // If courseWithId is null means the id is duplicated.
            if (courseWithId != null) {
                throw new FailedOperationException(String.format(
                        "Course id %s is already in use.", courseWithId.getId()));
            }
        }
        String courseCode = course.getCourseCode();
        this.save(course);
        // Try to get the course we saved from datastore.
        Course savedCourse = this.getCourseByCourseCode(courseCode);
        if (savedCourse == null) {
            // Could not save in datastore.
            throw new FailedOperationException(String.format("Course %s could not be saved.", courseCode));
        }
        return this.save(course);
    }


    /**
     * Lists the first 5000 courses.
     * TODO: Parametrize number of courses to list.
     * @return a list of courses.
     */
    public List<Course> listAllCourses() {
        return courseRepository.list(5000);
    }

    public List<Course> getCoursesByCourseName(String courseName) {
        return courseRepository.listByCourseName(courseName);
    }

    public Course updateCourseByCourseId(Course course) throws InsufficientResourcesException, NotFoundException {
        if (course.getId() == null) {
            throw new InsufficientResourcesException("UPDATE operation failed. Course id not provided.");
        }

        if (courseRepository.get(course.getId()) == null) {
            throw new NotFoundException(String.format(
                    "UPDATE operation failed. Course id %s was not found", course.getId()));
        }

        return courseRepository.put(course);
    }

    @Async
    public void delete(String id) throws NotFoundException, FailedOperationException {
        Course course = courseRepository.get(id);
        if (course == null) {
            throw new NotFoundException(
                    String.format("DELETE operation failed. Course id %s not found, could not delete.", id));
        }
        courseRepository.deleteByKey(id);

        course = courseRepository.get(id);
        if (course != null) {
            throw new FailedOperationException(
                    String.format("DELETE operation failed. Could not delete course with id %s in datastore.", id));

        }
    }

    public Course find(String id){
        return courseRepository.get(id);
    }


}
