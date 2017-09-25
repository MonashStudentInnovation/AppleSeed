package edu.monash.monplan.controller;

import edu.monash.monplan.model.Course;
import edu.monash.monplan.service.MonPlanService;
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
                                             @RequestParam(value="courseName", required=false) String[] courseNames) {
        return this.getByParams(courseCodes, courseNames);
    }

    @RequestMapping(path = "/{courseId}", method = RequestMethod.PUT)
    public ResponseEntity updateCourseById(@PathVariable(value="courseId") String courseId,
                                           @RequestBody Course course) {
        return this.updateById(courseId, course);
    }

    @RequestMapping(path = "/{courseId}", method = RequestMethod.DELETE)
    public ResponseEntity updateCourseById(@PathVariable(value="courseId") String courseId) {
        return this.deleteById(courseId);
    }
}
