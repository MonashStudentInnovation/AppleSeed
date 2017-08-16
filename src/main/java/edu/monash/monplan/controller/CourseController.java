package edu.monash.monplan.controller;

import edu.monash.monplan.controller.response.ResponseMessage;
import edu.monash.monplan.model.Course;
import edu.monash.monplan.service.CourseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    Course getCourseByCourseCode(@PathVariable(value="courseCode") String courseCode) {
         return courseService.getByCourseCode(courseCode);
//        Unit unit = unitService.getByUnitCode(unitCode);
//        if (unit == null) {
//            ResponseMessage message = new ResponseMessage();
//            message.setMessage("Unit code not found");
//            message.setCode(404);
//            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
//        }
//        return new ResponseEntity<> (unit, HttpStatus.OK);
    }

    @RequestMapping(path="/search/{searchItem}", method = RequestMethod.GET)
    List<Course> getCourseByCourseName(@PathVariable(value = "searchItem") String searchItem) {
        return courseService.getByName(searchItem);
    }

    @Async
    @RequestMapping(path = "/{courseCode}", method = RequestMethod.DELETE)
    ResponseEntity<ResponseMessage>  deleteByCourseCode(@PathVariable(value="courseCode") String courseCode){
        ResponseMessage message = new ResponseMessage();

        try {
            courseService.delete(courseCode);
            message.setMessage("Delete operation success");
            message.setCode(200);

            return new ResponseEntity<>(message, HttpStatus.OK);
        } catch (Exception e) {
            message.setMessage(e.getLocalizedMessage());
            message.setCode(404);
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
    }

    @RequestMapping(path = "", method = RequestMethod.POST)
    Course insertNewCourse(@RequestBody Course course){
        return courseService.addCourse(course);
    }

}
