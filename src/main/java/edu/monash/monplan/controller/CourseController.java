package edu.monash.monplan.controller;

import edu.monash.monplan.controller.response.ResponseMessage;
import edu.monash.monplan.model.Course;
import edu.monash.monplan.service.CourseService;
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

    @RequestMapping(path = "", method = RequestMethod.GET)
    List<Course> getAllCourses(){
        return courseService.listAllUnits();
    }

    @RequestMapping(path = "/{courseCode}", method = RequestMethod.GET)
    ResponseEntity getCourseByCourseCode(@PathVariable(value="courseCode") String courseCode) {
         Course course =  courseService.getByCourseCode(courseCode);

         if (course == null) {
             return new ResponseEntity<>(new ResponseMessage("Course code not found"), HttpStatus.NOT_FOUND);
         }
         return new ResponseEntity<>(course, HttpStatus.OK);
    }

    @RequestMapping(path="/search/{searchItem}", method = RequestMethod.GET)
    List<Course> getCourseByCourseName(@PathVariable(value = "searchItem") String searchItem) {
        return courseService.getByName(searchItem);
    }

    @Async
    @RequestMapping(path = "/{courseCode}", method = RequestMethod.DELETE)
    ResponseEntity<ResponseMessage>  deleteByCourseCode(@PathVariable(value="courseCode") String courseCode){
        try {
            courseService.delete(courseCode);
            return new ResponseEntity<>(new ResponseMessage("Delete operation success"), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(new ResponseMessage("Course code not found"), HttpStatus.NOT_FOUND);
        } catch (FailedOperationException e) {
            return new ResponseEntity<>(new ResponseMessage("Delete operation failed"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(path = "", method = RequestMethod.POST)
    Course insertNewCourse(@RequestBody Course course){
        return courseService.addCourse(course);
    }

}
