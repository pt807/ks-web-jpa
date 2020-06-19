package kr.ac.ks.app.controller;

import kr.ac.ks.app.domain.Course;
import kr.ac.ks.app.domain.Lesson;
import kr.ac.ks.app.domain.Student;
import kr.ac.ks.app.repository.CourseRepository;
import kr.ac.ks.app.repository.LessonRepository;
import kr.ac.ks.app.repository.StudentRepository;
import lombok.Getter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;

@Controller
public class CourseController {
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final LessonRepository lessonRepository;


    public CourseController(StudentRepository studentRepository, CourseRepository courseRepository, LessonRepository lessonRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.lessonRepository = lessonRepository;
    }

    @GetMapping("/course")
    public String showCourseForm(Model model) {
        List<Student> students = studentRepository.findAll();
        List<Lesson> lessons = lessonRepository.findAll();
        model.addAttribute("students", students);
        model.addAttribute("lessons", lessons);
        return "courses/courseForm";
    }

    @PostMapping("/course")
    public String createCourse(@RequestParam("studentId") Long studentId,
                               @RequestParam("lessonId") Long lessonId
                               ) {
        Student student = studentRepository.findById(studentId).get();
        Lesson lesson = lessonRepository.findById(lessonId).get();
        Course course = Course.createCourse(student,lesson);
        Course savedCourse = courseRepository.save(course);
        return "redirect:/courses";
    }

    @GetMapping("/courses")
    public String courseList(Model model) {
        List<Course> courses = courseRepository.findAll();
        model.addAttribute("courses", courses);
        return "courses/courseList";
    }

    @GetMapping("/courses/update/{id}")
    public String updateform(Model model, @PathVariable Long id) {
        Course course = courseRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Invalid student Id:" + id));
        List<Lesson> lessons = lessonRepository.findAll();
        course.setLessons(lessons);
        model.addAttribute("course", course);
        return "courses/courseupdateForm";
    }

    @PostMapping("/courses/update/{id}")
    public String updatecourse(@PathVariable Long id, @RequestParam("lessonId") Long lessonId ) {
        Course course = courseRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Invalid student Id:" + id));
        Lesson lesson = course.getLessons().get(0);
        lesson.setCourse(null);
        Lesson lessons2 = lessonRepository.findById(lessonId).orElseThrow(() ->
                new IllegalArgumentException("Invalid student Id:" + id));
        course.addLesson(lessons2);
        courseRepository.save(course);
        return "redirect:/courses";
    }

    @GetMapping("/courses/delete/{id}")
    public String delete(@PathVariable Long id) {
        Course course = courseRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Invalid student Id:" + id));
        Lesson lesson = course.getLessons().get(0);
        lesson.setCourse(null);
        courseRepository.delete(course);
        return "redirect:/courses";
    }
}
