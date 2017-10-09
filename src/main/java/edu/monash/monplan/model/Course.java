package edu.monash.monplan.model;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import org.monplan.abstraction_layer.DataModel;

@Entity
public class Course extends DataModel {

    public static String codeField = "courseCode";
    public static String nameField = "courseName";

    @Id
    private String id;

    @Index
    private String courseCode;

    @Index
    private String courseName;

    @Index
    private String managingFaculty;

    @Override
    public String fetchCode() {
        return courseCode;
    }

    @Override
    public String fetchName() {
        return courseName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getManagingFaculty() {
        return managingFaculty;
    }

    public void setManagingFaculty(String managingFaculty) {
        this.managingFaculty = managingFaculty;
    }

}
