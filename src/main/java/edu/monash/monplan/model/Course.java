package edu.monash.monplan.model;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.threewks.gaetools.search.SearchIndex;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.monplan.utils.TextSearch;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
public class Course {

    @Id
    private String id;

    @Index
    @SearchIndex
    private String courseCode;

    @Index
    private String courseName;

    @Index
    private String managingFaculty;


    public static class SearchFields {
        public static final String CourseName = "courseNameSearchableText";
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


    @Override
    public String toString() {

        return new ToStringBuilder(this)
                .append("courseCode", courseCode)
                .append("courseName", courseName)
                .append("managingFaculty", managingFaculty)
                .toString();
    }

    public void init() {
        // Protects us from accidentally re-initialising an object that's retrieved from db
        this.setId(UUID.randomUUID().toString());
    }

    @SearchIndex
    public String getCourseNameSearchableText() {
        return TextSearch.getSearchableText(courseName);
    }

}
