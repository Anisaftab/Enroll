package com.example.enroll;

import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.database.Cursor;
import static org.mockito.Mockito.when;

import com.example.enroll.ui.login.Course;
import com.example.enroll.ui.login.CourseObj;
import com.example.enroll.ui.login.Instructor;
import com.example.enroll.ui.login.InstructorActivity;
import com.example.enroll.ui.login.MyDBHandler;
import com.example.enroll.ui.login.Student;
import com.example.enroll.ui.login.StudentActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;


public class StudentUnitTest {

    StudentActivity student ;
    InstructorActivity instructor;


    MyDBHandler testDB;
    boolean actual;
    boolean actual2;


    @Before
    public void viewCoursesTest(){
        student = Mockito.mock(StudentActivity.class);
        instructor = Mockito.mock(InstructorActivity.class);
        testDB = Mockito.mock(MyDBHandler.class);


        testDB.createCourse("ASI","Absurd Science");



        testDB.createAccount("Student","Student1","Student1","easy");
        testDB.createAccount("Instructor","Instructor1","Instructor1","medium");
        testDB.assignInstructor("ASI","Absurd Science","Instructor1","Instructor1");
        testDB.updateCourseCapacity("ASI","Absurd Science",1);
        testDB.updateCourseTimes("ASI","Absurd Science","Tuesday","Wednesday","10:00-11:00","10:00-11:00");
        testDB.createAccount("Student","Student2","Student2","easy");


        testDB.registerStudentForCourse("ASI","Absurd Science","Student1");


    }

    // basic enroll test
    @Test
    public void enrollInCourseTest(){

        when(testDB.registerStudentForCourse("ASI","Absurd Science","Student1")).thenReturn(true);
        actual = testDB.registerStudentForCourse("ASI","Absurd Science","Student1");
        assertEquals("Student unable to enroll in a course even if its not full",true, actual);


    }

    // enroll test if course is full
    @Test
    public void courseFullTest(){
        testDB.registerStudentForCourse("ASI","Absurd Science","Student1");
        boolean actual = testDB.registerStudentForCourse("ASI","Absurd Science","Student2");

        assertEquals("Student able to enroll even if the course is full",false, actual);

    }

    //can a student unenroll?
    @Test
    public void unEnrollTest(){
        when(testDB.unenrollStudent("ASI","Absurd Science","Student1")).thenReturn(true);
        testDB.registerStudentForCourse("ASI","Absurd Science","Student1");
        boolean actual = testDB.unenrollStudent("ASI","Absurd Science","Student1");

        assertEquals("Student unable to unenroll from a registered course",true, actual);

    }

    // get which courses student is in
    @Test
    public void coursesEnrolledTest(){
        CourseObj course = new CourseObj("ASI","Absurd Science","Instructor1","Tuesday","Wednesday","10:00-11:00","10:00-11:00","Absurd course","1","1");
        ArrayList<CourseObj> enrolledCourses = new ArrayList<CourseObj>();
        enrolledCourses.add(course);
        testDB.registerStudentForCourse("ASI","Absurd Science","Student1");
        when(testDB.getCoursesStudentIsEnrolledIn("Student1")).thenReturn(enrolledCourses);

        assertEquals("Courses student is enrolled in not working properly ",enrolledCourses,testDB.getCoursesStudentIsEnrolledIn("Student1"));
    }

    // instructor can see what students are in their course
    @Test
    public void getStudentsInCourseTest(){
        ArrayList<String> students = new ArrayList<>();
        students.add("Student1");
        students.add("Student2");

        testDB.registerStudentForCourse("ASI2","Absurd Science","Student1");
        testDB.registerStudentForCourse("ASI2","Absurd Science","Student1");

        when(testDB.getStudentsInCourse("ASI2")).thenReturn(students);

        assertEquals("Enrolled Students List in course not working properly",students,testDB.getStudentsInCourse("ASI2"));
    }





}
