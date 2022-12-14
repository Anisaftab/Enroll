package com.example.enroll.ui.login;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.sql.*;
import java.util.*;


public class MyDBHandler extends SQLiteOpenHelper {
    private static final String COURSE_TABLE_NAME = "courses";
    private static final String COLUMN_COURSE_CODE = "course_code";
    private static final String COLUMN_COURSE_NAME = "course_name";
    private static final String COLUMN_COURSE_INSTRUCTOR_USERNAME = "course_instructor_username";
    private static final String COLUMN_COURSE_INSTRUCTOR_NAME = "course_instructor_name";
    private static final String COLUMN_COURSE_DAY1 = "course_day1";
    private static final String COLUMN_COURSE_DAY2 = "course_day2";
    private static final String COLUMN_COURSE_TIME1 = "course_time1";
    private static final String COLUMN_COURSE_TIME2 = "course_time2";
    private static final String COLUMN_COURSE_DESCRIPTION = "course_description";
    private static final String COLUMN_COURSE_CAPACITY = "course_capacity";
    private static final String COLUMN_COURSE_CURRENT_CAPACITY = "course_current_capacity";

    private static final String ACCOUNT_TABLE_NAME = "accounts";
    private static final String COLUMN_ACCOUNT_TYPE = "account_type";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_NAME = "name";



    private static final String DATABASE_NAME = "enroll.db";
    private static final int DATABASE_VERSION = 1;

    public MyDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override

    public void onCreate(SQLiteDatabase db) {
        String create_course_table_cmd = "CREATE TABLE " + COURSE_TABLE_NAME +
                "(" + COLUMN_COURSE_CODE + " TEXT, " +
                COLUMN_COURSE_NAME + " TEXT, " +
                COLUMN_COURSE_INSTRUCTOR_USERNAME + " TEXT, " +
                COLUMN_COURSE_INSTRUCTOR_NAME + " TEXT, " +
                COLUMN_COURSE_DAY1 + " TEXT, " +
                COLUMN_COURSE_DAY2 + " TEXT, " +
                COLUMN_COURSE_TIME1 + " TEXT, " +
                COLUMN_COURSE_TIME2 + " TEXT, " +
                COLUMN_COURSE_DESCRIPTION + " TEXT, " +
                COLUMN_COURSE_CAPACITY + " TEXT, " +
                COLUMN_COURSE_CURRENT_CAPACITY + " TEXT " +
                ")";

        String create_account_table_cmd = "CREATE TABLE " + ACCOUNT_TABLE_NAME +
                "(" + COLUMN_ACCOUNT_TYPE + " TEXT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_USERNAME + " TEXT, " +
                COLUMN_PASSWORD + " TEXT " + ")";

        db.execSQL(create_course_table_cmd);
        db.execSQL(create_account_table_cmd);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + COURSE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ACCOUNT_TABLE_NAME);
        onCreate(db);
    }

    public Cursor getCourseData() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + COURSE_TABLE_NAME;
        return db.rawQuery(query, null);
    }

    public boolean createCourse(String course_code, String course_name){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COLUMN_COURSE_CODE, course_code);
        values.put(COLUMN_COURSE_NAME, course_name);
        values.put(COLUMN_COURSE_CURRENT_CAPACITY, 0);

        long result = db.insert(COURSE_TABLE_NAME, null, values);

        if(result != -1){
            String create_course_student_list = "CREATE TABLE " + course_code +
                    "(" + "student" + " TEXT " + ")";

            db.execSQL(create_course_student_list);
        }

        db.close();
        return result != -1;
    }

    public boolean editCourse(String original_course_code, String original_course_name,
                           String new_course_code, String new_course_name){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_COURSE_CODE, new_course_code);
        values.put(COLUMN_COURSE_NAME, new_course_name);

        long result = db.update(COURSE_TABLE_NAME, values, COLUMN_COURSE_CODE+"=?", new String[]{original_course_code});
        if(result == -1){
            return false;
        }
        result = db.update(COURSE_TABLE_NAME, values, COLUMN_COURSE_NAME+"=?", new String[]{original_course_name});
        db.close();

        return result != -1;
    }

    public Cursor findCourse(String course_code, String course_name){
        SQLiteDatabase db = this.getWritableDatabase();

        String search = "";

        if((!Objects.equals(course_code, "")) && (!Objects.equals(course_name, ""))){
            search = "SELECT * FROM " + COURSE_TABLE_NAME + " WHERE " + COLUMN_COURSE_CODE + " = \"" + course_code + "\"" + " AND "
                    + COLUMN_COURSE_NAME + " = \"" + course_name + "\"";
        } else if(Objects.equals(course_code, "") && (!Objects.equals(course_name, ""))){
            search = "SELECT * FROM " + COURSE_TABLE_NAME + " WHERE " + COLUMN_COURSE_NAME + " = \"" + course_name  + "\"";
        } else if(!Objects.equals(course_code, "") && (Objects.equals(course_name, ""))){
            search = "SELECT * FROM " + COURSE_TABLE_NAME + " WHERE " + COLUMN_COURSE_CODE + " = \"" + course_code  + "\"";
        } else{
            search = "SELECT * FROM " + COURSE_TABLE_NAME;
        }

        return db.rawQuery(search, null);
    }

    public Cursor findStudentCourse(String course_code, String course_name, String day){
        SQLiteDatabase db = this.getWritableDatabase();

        String search = "";

        if((!Objects.equals(course_code, "")) && (!Objects.equals(course_name, "")) && (!Objects.equals(day, ""))){
            search = "SELECT * FROM " + COURSE_TABLE_NAME + " WHERE " + COLUMN_COURSE_CODE + " = \"" + course_code + "\"" + " AND "
                    + COLUMN_COURSE_NAME + " = \"" + course_name + "\"" + " AND " + COLUMN_COURSE_DAY1 + " = \"" + day + "\"" + " OR " + COLUMN_COURSE_DAY2 + " = \"" + day + "\"";
        } else if((!Objects.equals(course_code, "")) && (!Objects.equals(course_name, "")) && (Objects.equals(day, ""))){
            search = "SELECT * FROM " + COURSE_TABLE_NAME + " WHERE " + COLUMN_COURSE_CODE + " = \"" + course_code + "\"" + " AND "
                    + COLUMN_COURSE_NAME + " = \"" + course_name + "\"";
        } else if(Objects.equals(course_code, "") && (!Objects.equals(course_name, "")) && (!Objects.equals(day, ""))){
            search = "SELECT * FROM " + COURSE_TABLE_NAME + " WHERE " + COLUMN_COURSE_NAME + " = \"" + course_name  + "\""
                    + " AND " + COLUMN_COURSE_DAY1 + " = \"" + day + "\"" + " OR " + COLUMN_COURSE_DAY2 + " = \"" + day + "\"";
        } else if(Objects.equals(course_code, "") && (!Objects.equals(course_name, "")) && (Objects.equals(day, ""))){
            search = "SELECT * FROM " + COURSE_TABLE_NAME + " WHERE " + COLUMN_COURSE_NAME + " = \"" + course_name  + "\"";
        } else if(!Objects.equals(course_code, "") && (Objects.equals(course_name, "")) && (!Objects.equals(day, ""))){
            search = "SELECT * FROM " + COURSE_TABLE_NAME + " WHERE " + COLUMN_COURSE_CODE + " = \"" + course_code
                    + "\"" + " AND " + COLUMN_COURSE_DAY1 + " = \"" + day + "\"" + " OR " + COLUMN_COURSE_DAY2 + " = \"" + day + "\"";
        } else if(!Objects.equals(course_code, "") && (Objects.equals(course_name, "")) && (Objects.equals(day, ""))){
            search = "SELECT * FROM " + COURSE_TABLE_NAME + " WHERE " + COLUMN_COURSE_CODE + " = \"" + course_code + "\"";
        } else if(Objects.equals(course_code, "") && (Objects.equals(course_name, "")) && (!Objects.equals(day, ""))){
            search = "SELECT * FROM " + COURSE_TABLE_NAME + " WHERE " + COLUMN_COURSE_DAY1 + " =\"" + day + "\"" + " OR " + COLUMN_COURSE_DAY2 + " =\"" + day + "\"";
        } else{
            search = "SELECT * FROM " + COURSE_TABLE_NAME;
        }

        return db.rawQuery(search,null);
    }

    public boolean registerStudentForCourse(String course_code, String course_name, String student_username){

        if(Objects.equals(course_code, "") || Objects.equals(course_name, "") || Objects.equals(student_username, "")){
            return false;
        }

        if(student_username == null){
            System.out.println("user name is null");
            return false;
        }

        SQLiteDatabase db = this.getWritableDatabase();

        // Check if the course has an instructor and full information
        Cursor cursor = db.rawQuery("SELECT * FROM " + COURSE_TABLE_NAME + " WHERE " + COLUMN_COURSE_CODE + "=?", new String[]{course_code});

        cursor.moveToFirst();

        for(int i = 0; i < 11; i++){
            if(cursor.isNull(i)){
                return false;
            }
            System.out.println(cursor.getString(i) + "    like what.");
        }

        int curr_capacity = getCurrentCourseCapacity(course_code, course_name);
        int capacity = getCourseCapacity(course_code, course_name);

        System.out.println("curr cap: " + curr_capacity);

        System.out.println("capacity: " + capacity);

        // Check if there's space in the course
        if(curr_capacity == -1 || capacity == -1){
            return false;
        } else if(curr_capacity >= capacity){
            return false;
        }

        // Check if the student has a conflict in their schedule
        CourseObj studentNewCourse = getCourseObject(course_code, course_name);
        ArrayList<CourseObj> enrolled_courses = getCoursesStudentIsEnrolledIn(student_username);

        int size_enrolled_courses = enrolled_courses.size();

        for(int i = 0; i < size_enrolled_courses; i++){
            CourseObj dummy = enrolled_courses.get(i);
            if(Objects.equals(studentNewCourse.getCourse_code(), dummy.getCourse_code())){
                return false;
            }

            if(Objects.equals(studentNewCourse.getDay1(), dummy.getDay1()) && Objects.equals(studentNewCourse.getTime1(), dummy.getTime1())){
                return false;
            }

            if(Objects.equals(studentNewCourse.getDay2(), dummy.getDay2()) && Objects.equals(studentNewCourse.getTime2(), dummy.getTime2())){
                return false;
            }
        }

        // if all those cases fail (aka everything is okay) then add the student to the course table
        ContentValues values = new ContentValues();

        values.put("student", student_username);

        long result = db.insert(course_code, null, values);


        if(result != -1){
            updateCurrentCourseCapacity(course_code, course_name, 1);
        }

        return result != -1;
    }

    public boolean unenrollStudent(String course_code, String course_name, String student_username){
        if(Objects.equals(course_code, "") || Objects.equals(course_name, "") || Objects.equals(student_username, "")){
            return false;
        }

        SQLiteDatabase db = this.getWritableDatabase();

        ArrayList<CourseObj> enrolled_courses = getCoursesStudentIsEnrolledIn(student_username);

        boolean studentInCourse = false;

        for(int i = 0; i < enrolled_courses.size(); i++){
            if(Objects.equals(enrolled_courses.get(i).getCourse_code(), course_code)){
                studentInCourse = true;
            }
        }
        if(studentInCourse){
            long result = db.delete(course_code, "student=?", new String[]{student_username});

            if(result != -1){
                updateCurrentCourseCapacity(course_code, course_name, -1);

                return true;
            } else{
                return false;
            }
        } else{
            return false;
        }
    }

    public CourseObj getCourseObject(String course_code, String course_name){
        SQLiteDatabase db = this.getWritableDatabase();

        if(!Objects.equals(course_code, "")){
            String search = "SELECT course_name, course_instructor_name, course_day1, course_day2, course_time1, course_time2, course_description, course_capacity, course_current_capacity FROM " + COURSE_TABLE_NAME + " WHERE " + COLUMN_COURSE_CODE + " = \"" + course_code + "\"";
            Cursor cursor = db.rawQuery(search, null);

            cursor.moveToFirst();

            return new CourseObj(course_code, cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8));
        }

        return new CourseObj("null", "null", "null", "null", "null", "null", "null", "null", "null", "null");
    }

    public ArrayList<String> getStudentsInCourse(String course_code){
        ArrayList<String> students = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();

        String search = "SELECT student FROM " + course_code;
        Cursor cursor = db.rawQuery(search, null);

        cursor.moveToFirst();
        int cursor_size = cursor.getCount();

        for(int i = 0; i < cursor_size; i++){
            students.add(cursor.getString(0));
            cursor.moveToNext();
        }

        return students;
    }

    public ArrayList<CourseObj> getCoursesStudentIsEnrolledIn(String student_username){
        ArrayList<String> course_list = getListOfCourses();

        ArrayList<CourseObj> student_course_list = new ArrayList<>();

        int numberOfCourses = course_list.size();

        SQLiteDatabase db = this.getWritableDatabase();

        for(int i = 0; i < numberOfCourses; i++){
            String search = "SELECT student FROM " + course_list.get(i);
            Cursor cursor = db.rawQuery(search, null);

            cursor.moveToFirst();
            int cursor_size = cursor.getCount();

            for(int j = 0 ; j < cursor_size; j++){
                if(Objects.equals(cursor.getString(0), student_username)){
                    student_course_list.add(getCourseObject(course_list.get(i), ""));
                    break;
                }
                cursor.moveToNext();
            }
        }

        return student_course_list;
    }

    public String[] getCourseTimes(String course_code, String course_name){
        SQLiteDatabase db = this.getWritableDatabase();

        String[] course_times = new String[3];

        if(!Objects.equals(course_code, "")){
            String search = "SELECT course_day1, course_time1, course_day2, course_time2 FROM " + COURSE_TABLE_NAME + " WHERE " + COLUMN_COURSE_CODE + " = \"" + course_code + "\"";
            Cursor cursor = db.rawQuery(search, null);

            cursor.moveToFirst();

            for(int i = 0; i < 4; i++){
                course_times[i] = cursor.getString(i);
            }
        }

        return course_times;
    }

    public ArrayList<String> getListOfCourses(){
        SQLiteDatabase db = this.getWritableDatabase();

        String search = "SELECT course_code FROM " + COURSE_TABLE_NAME;
        ArrayList<String> course_list = new ArrayList<>();

        Cursor cursor = db.rawQuery(search, null);

        int size = cursor.getCount();

        cursor.moveToFirst();

        for(int i = 0; i < cursor.getCount(); i++){
            course_list.add(cursor.getString(0));
            cursor.moveToNext();
        }

        return course_list;
    }

    public int getCurrentCourseCapacity(String course_code, String course_name){
        SQLiteDatabase db = this.getWritableDatabase();

        String search = "";

        if(!Objects.equals(course_code, "")){
            search = "SELECT course_current_capacity FROM " + COURSE_TABLE_NAME + " WHERE " + COLUMN_COURSE_CODE + " = \"" + course_code + "\"";
        } else{
            return -1;
        }

        Cursor cursor = db.rawQuery(search, null);

        cursor.moveToFirst();

        return cursor.getInt(0);
    }

    public int getCourseCapacity(String course_code, String course_name){
        SQLiteDatabase db = this.getWritableDatabase();

        String search = "";

        if(!Objects.equals(course_code, "")){
            search = "SELECT course_capacity FROM " + COURSE_TABLE_NAME + " WHERE " + COLUMN_COURSE_CODE + " = \"" + course_code + "\"";
        } else{
            return -1;
        }

        Cursor cursor = db.rawQuery(search, null);

        cursor.moveToFirst();

        return cursor.getInt(0);
    }

    public boolean updateCurrentCourseCapacity(String course_code, String course_name, int value){
        SQLiteDatabase db = this.getWritableDatabase();

        int curr_capacity = getCurrentCourseCapacity(course_code, course_name);
        int capacity = getCourseCapacity(course_code, course_name);

        if(curr_capacity == -1 || capacity == -1){
            return false;
        } else {
            curr_capacity += value;
            ContentValues values = new ContentValues();

            values.put(COLUMN_COURSE_CURRENT_CAPACITY, curr_capacity);

            long result = db.update(COURSE_TABLE_NAME, values, COLUMN_COURSE_CODE+"=?", new String[]{course_code});

            return result != -1;
        }
    }

    public boolean updateCourseTimes(String course_code, String course_name,
                                     String day1, String day2, String hours1, String hours2){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_COURSE_DAY1, day1);
        values.put(COLUMN_COURSE_DAY2, day2);
        values.put(COLUMN_COURSE_TIME1, hours1);
        values.put(COLUMN_COURSE_TIME2, hours2);

        long result = db.update(COURSE_TABLE_NAME, values, COLUMN_COURSE_CODE+"=?", new String[]{course_code});
        if(result == -1){
            return false;
        }
        result = db.update(COURSE_TABLE_NAME, values, COLUMN_COURSE_NAME+"=?", new String[]{course_name});

        return result != -1;
    }

    public boolean updateCourseCapacity(String course_code, String course_name, int capacity){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_COURSE_CAPACITY, capacity);

        long result = db.update(COURSE_TABLE_NAME, values, COLUMN_COURSE_CODE+"=?", new String[]{course_code});
        if(result == -1){
            return false;
        }
        result = db.update(COURSE_TABLE_NAME, values, COLUMN_COURSE_NAME+"=?", new String[]{course_name});

        return result != -1;
    }

    public boolean updateCourseDescription(String course_code, String course_name, String description){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_COURSE_DESCRIPTION, description);

        long result = db.update(COURSE_TABLE_NAME, values, COLUMN_COURSE_CODE+"=?", new String[]{course_code});
        if(result == -1){
            return false;
        }
        result = db.update(COURSE_TABLE_NAME, values, COLUMN_COURSE_NAME+"=?", new String[]{course_name});

        return result != -1;
    }

    public boolean isInstructor(String course_code, String course_name){

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + COURSE_TABLE_NAME + " WHERE " + COLUMN_COURSE_CODE + " = \"" + course_code  + "\"", null);

        cursor.moveToFirst();

        return (cursor.isNull(2));
    }

    public boolean assignInstructor(String course_code, String course_name,
                                    String instructorUsername, String instructorName){

        boolean availableSpot = isInstructor(course_code, course_name);

        if(!availableSpot){
            return false;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_COURSE_INSTRUCTOR_USERNAME, instructorUsername);
        values.put(COLUMN_COURSE_INSTRUCTOR_NAME, instructorName);

        long result = db.update(COURSE_TABLE_NAME, values, COLUMN_COURSE_CODE+"=?", new String[]{course_code});

        return result != -1;
    }

    public boolean removeInstructor(String course_code, String course_name,
                                    String instructorUsername, String instructorName){

        SQLiteDatabase db = this.getWritableDatabase();

        if(getInstructorUsername(course_code, course_name) == null){
            return false;
        }

        if(getInstructorUsername(course_code, course_name).equals(instructorUsername)){
            ContentValues values = new ContentValues();
            values.putNull(COLUMN_COURSE_INSTRUCTOR_USERNAME);
            values.putNull(COLUMN_COURSE_INSTRUCTOR_NAME);
            values.putNull(COLUMN_COURSE_DAY1);
            values.putNull(COLUMN_COURSE_DAY2);
            values.putNull(COLUMN_COURSE_TIME1);
            values.putNull(COLUMN_COURSE_TIME2);
            values.putNull(COLUMN_COURSE_DESCRIPTION);
            values.putNull(COLUMN_COURSE_CAPACITY);

            long result = db.update(COURSE_TABLE_NAME, values, COLUMN_COURSE_CODE + "=?", new String[]{course_code});

            return result != -1;
        } else{
            return false;
        }
    }

    public boolean deleteCourse(String course_code, String course_name){
        SQLiteDatabase db = this.getWritableDatabase();

        long result = db.delete(COURSE_TABLE_NAME, COLUMN_COURSE_CODE+"=?", new String[]{course_code});
        if(result == -1){
            return false;
        }

        result = db.delete(COURSE_TABLE_NAME, COLUMN_COURSE_NAME+"=?", new String[]{course_name});

        if(result != -1){
            db.execSQL("DROP TABLE IF EXISTS "+ course_code);
        }

        return result != -1;
    }

    public Boolean createAccount(String account_type, String name, String username, String password){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COLUMN_ACCOUNT_TYPE, account_type);
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);

        long result = db.insert(ACCOUNT_TABLE_NAME, null, values);
        return result != -1;
    }

    public boolean deleteAccount(String username){
        SQLiteDatabase db = this.getWritableDatabase();

        long result = db.delete(ACCOUNT_TABLE_NAME, COLUMN_USERNAME+"=?", new String[]{username});

        return result != -1;
    }

    public boolean checkUsername(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + ACCOUNT_TABLE_NAME + " WHERE " + COLUMN_USERNAME + "=?", new String[]{username
        });

        if (cursor.getCount() > 0){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean checkUsernamePassword(String username , String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        String search = ("SELECT * FROM " + ACCOUNT_TABLE_NAME +" WHERE " + COLUMN_USERNAME + " = \"" + username + "\"" + " AND " + COLUMN_PASSWORD +
                " = \"" + password + "\"");

        Cursor cursor = db.rawQuery(search, null);

        if (cursor.getCount() > 0){
            return true;
        }
        else{
            return false;
        }
    }

    public String getInstructorUsername(String course_code, String course_name){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + COURSE_TABLE_NAME + " WHERE " + COLUMN_COURSE_CODE + "=?", new String[]{course_code});

        cursor.moveToFirst();

        return cursor.getString(2);
    }

    public String checkAccountType(String account_type){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + ACCOUNT_TABLE_NAME + " WHERE " + COLUMN_USERNAME + "=?", new String[]{account_type});

        cursor.moveToFirst();

        return cursor.getString(0);
    }
    public String checkType(String account_type){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + ACCOUNT_TABLE_NAME + " WHERE " + COLUMN_USERNAME + "=?", new String[]{account_type});

        cursor.moveToFirst();

        return cursor.getString(0);
    }

    public String getName(String username){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + ACCOUNT_TABLE_NAME + " WHERE " + COLUMN_USERNAME + "=?", new String[]{username});

        cursor.moveToFirst();

        return cursor.getString(1);
    }

}
