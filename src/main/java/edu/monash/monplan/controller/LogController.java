package edu.monash.monplan.controller;

import edu.monash.monplan.model.Course;
import edu.monash.monplan.model.Log;
import edu.monash.monplan.prebuilts.MonPlanRequestLoggingFilter;
import edu.monash.monplan.service.LogService;
import org.monplan.abstraction_layer.MonPlanController;
import org.monplan.abstraction_layer.MonPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/logs")
public class LogController extends MonPlanController<Log> {

    @Autowired
    public LogController(MonPlanService<Log> service) {
        super(service);
    }

    // @RequestBody will serialize the body of the POST request into a course object.
    // Fields in the POST body that don't exist in course will not be taken and fields in course may be null.
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity createLog(@RequestBody Log log) {
        return this.create(log);
    }

    @RequestMapping(path = "/{logId}", method = RequestMethod.GET)
    public ResponseEntity getCourseById(@PathVariable(value="logId") String logId) {
        return this.getById(logId);
    }

//    @RequestMapping(method = RequestMethod.GET)
//    public ResponseEntity getCoursesByParams(@RequestParam(value="courseCode", required=false) String[] courseCodes,
//                                             @RequestParam(value="courseName", required=false) String[] courseNames,
//                                             @RequestParam(value="itemsPerPage", required=false) Integer itemsPerPage,
//                                             @RequestParam(value="pageNumber", required=false) Integer pageNumber) {
//        return this.getByParams(courseCodes, courseNames, itemsPerPage, pageNumber);
//    }
//
//    @RequestMapping(path = "/{courseId}", method = RequestMethod.PUT)
//    public ResponseEntity updateCourseById(@PathVariable(value="courseId") String courseId,
//                                           @RequestBody Course course) {
//        return this.updateById(courseId, course);
//    }
//
//    @RequestMapping(path = "/{courseId}", method = RequestMethod.DELETE)
//    public ResponseEntity deleteCourseById(@PathVariable(value="courseId") String courseId) {
//        return this.deleteById(courseId);
//    }
}
