package kr.ac.ks.app.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Course {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Student student;

    @OneToMany(mappedBy = "course")
    private List<Lesson> lessons = new ArrayList<>();

    public void setStudent(Student student) {
        this.student = student;
        this.student.getCourses().add(this);
    }

    public void addLesson(Lesson lesson) {
        this.lessons.add(lesson);
        lesson.setCourse(this);
    }

    public static Course createCourse(Student student, Lesson lesson) {
        Course course = new Course();
        course.setStudent(student);
        course.addLesson(lesson);
        return course;
    }
}
