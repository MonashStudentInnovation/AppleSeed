package edu.monash.monplan.controller;

import edu.monash.monplan.controller.response.ResponseMessage;
import edu.monash.monplan.model.Course;
import edu.monash.monplan.model.Unit;
import edu.monash.monplan.service.CourseService;
import org.monplan.InsufficientResourcesException;
import org.monplan.exceptions.FailedOperationException;
import org.monplan.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// TODO: Change naming to CRUD.
// TODO: Fix search.

@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService){
        this.courseService = courseService;
    }

    // HTTP CREATE.

    @RequestMapping(path = "", method = RequestMethod.POST)
    ResponseEntity createCourse(@RequestBody Course course) {
        try {
            return new ResponseEntity<>(courseService.createCourse(course), HttpStatus.OK);
        } catch (FailedOperationException e) {
            return  new ResponseEntity<>(new ResponseMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    // HTTP GET.

    @RequestMapping(path = "", method = RequestMethod.GET)
    List<Course> getCourses(@RequestParam(value="courseName", required=false) String courseName) {
        // If no query params, list all courses, otherwise list a by course name.
        if (courseName == null) {
            return courseService.listAllCourses();
        } else {
            return courseService.getCoursesByCourseName(courseName);
        }
    }

    @RequestMapping(path = "/{courseCode}", method = RequestMethod.GET)
    ResponseEntity getCourseByCourseCode(@PathVariable(value="courseCode") String courseCode) {
         Course course = courseService.getCourseByCourseCode(courseCode);
         if (course == null) {
             return new ResponseEntity<>(new ResponseMessage(
                     String.format("Course code %s not found.", courseCode)),
                     HttpStatus.NOT_FOUND);
         }
         return new ResponseEntity<>(course, HttpStatus.OK);
    }

    // HTTP UPDATE.

    @RequestMapping(path = "/{courseId}", method = RequestMethod.PUT)
    ResponseEntity updateCourseByCourseId(@PathVariable(value="courseId") String courseId, @RequestBody Course course) {
        try {
            course.setId(courseId);
            Course updatedCourse = courseService.updateCourseByCourseId(course);
            return new ResponseEntity<>(updatedCourse, HttpStatus.OK);
        } catch (InsufficientResourcesException e) {
            // This should never happen, but it might.
            return new ResponseEntity<>(new ResponseMessage(e.getMessage()), HttpStatus.PRECONDITION_FAILED);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(new ResponseMessage(e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }


    // HTTP DELETE.

    @Async
    @RequestMapping(path = "/{courseCode}", method = RequestMethod.DELETE)
    ResponseEntity<ResponseMessage>  deleteByCourseCode(@PathVariable(value="courseCode") String courseCode){
        try {
            courseService.delete(courseCode);
            return new ResponseEntity<>(new ResponseMessage("Delete operation success"), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(new ResponseMessage("Course code not found"), HttpStatus.NOT_FOUND);
        } catch (FailedOperationException e) {
            return new ResponseEntity<>(new ResponseMessage(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
