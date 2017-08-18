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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    List<Course> getCourses(@RequestParam(value="courseCode", required=false) String[] courseCodes,
                            @RequestParam(value="courseName", required=false) String[] courseNames) {
        // If no query params, list all courses, otherwise list a by course name.
        if (courseCodes == null && courseNames == null) {
            return courseService.listAllCourses();
        }

        // initialize the results to a set, because we only want unique courses
        Set<String> seenCourseCodes = new HashSet<>();
        List<Course> results = new ArrayList<>();
        if (courseCodes != null) {
            // for each given courseCode, find the matches for that
            for (String courseCode : courseCodes) {
                Course course = courseService.getCourseByCourseCode(courseCode);
                // only add to results if course is not null and if we have not seen this course code
                if (course != null && !seenCourseCodes.contains(course.getCourseCode())) {
                    results.add(course);
                    seenCourseCodes.add(course.getCourseCode());
                }
            }
        }
        if (courseNames != null) {
            // for each given courseName, find the matches for that
            for (String courseName : courseNames) {
                List<Course> matches = courseService.getCoursesByCourseName(courseName);
                for (Course course: matches) {
                    // only add to results if we have not seen this course code
                    if (!seenCourseCodes.contains(course.getCourseCode())) {
                        results.add(course);
                        seenCourseCodes.add(course.getCourseCode());
                    }
                }
            }
        }
        return results;
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
