package edu.monash.monplan.controller;

import edu.monash.monplan.model.Course;
import org.monplan.abstraction_layer.MonPlanService;
import org.monplan.abstraction_layer.MonPlanController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/courses")
public class CourseController extends MonPlanController<Course> {

    @Autowired
    public CourseController(MonPlanService<Course> service) {
        super(service);
    }

    // @RequestBody will serialize the body of the POST request into a course object.
    // Fields in the POST body that don't exist in course will not be taken and fields in course may be null.
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity createCourse(@RequestBody Course course) {
        return this.create(course);
    }

    @RequestMapping(path = "/{courseId}", method = RequestMethod.GET)
    public ResponseEntity getCourseById(@PathVariable(value="courseId") String courseId) {
        return this.getById(courseId);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getCoursesByParams(@RequestParam(value="courseCode", required=false) String[] courseCodes,
                                             @RequestParam(value="courseName", required=false) String[] courseNames,
                                             @RequestParam(value="itemsPerPage", required=false) Integer itemsPerPage,
                                             @RequestParam(value="pageNumber", required=false) Integer pageNumber) {
        return this.getByParams(courseCodes, courseNames, itemsPerPage, pageNumber);
    }

    @RequestMapping(path = "/{courseId}", method = RequestMethod.PUT)
    public ResponseEntity updateCourseById(@PathVariable(value="courseId") String courseId,
                                           @RequestBody Course course) {
        return this.updateById(courseId, course);
    }

    @RequestMapping(path = "/{courseId}", method = RequestMethod.DELETE)
    public ResponseEntity deleteCourseById(@PathVariable(value="courseId") String courseId) {
        return this.deleteById(courseId);
    }
}
