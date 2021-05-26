package com.example.teachomatic3000

import androidx.test.platform.app.InstrumentationRegistry
import com.example.teachomatic3000.database.DataBaseHelper
import com.example.teachomatic3000.models.ClassModel
import com.example.teachomatic3000.models.LehrstoffModel
import com.example.teachomatic3000.models.StudentModel

import org.junit.Test
import org.junit.Assert.*

class DatabaseTests {

    @Test
    fun testDatabaseExisting() {
        val db = DataBaseHelper(InstrumentationRegistry.getInstrumentation().targetContext)
        assertEquals(db::class, DataBaseHelper::class)
    }

    @Test
    fun testAddStudent() {
        val db = DataBaseHelper(InstrumentationRegistry.getInstrumentation().targetContext)
        val student = StudentModel(0,"Peter","Hans")
        val success = db.addStudent(student)
        assertEquals(true, success)
    }

    @Test
    fun testStudentsTableSize() {
        val db = DataBaseHelper(InstrumentationRegistry.getInstrumentation().targetContext)
        // check current students
        val current_students = db.getStudents().size
        // add one more student to db
        val student1 = StudentModel(0,"Peter","Hans")
        db.addStudent(student1)
        val students_plus_one = db.getStudents()
        assertEquals(current_students+1, students_plus_one.size)
    }


    @Test
    fun addClassSuccess() {
        val db = DataBaseHelper(InstrumentationRegistry.getInstrumentation().targetContext)
        val new_class = ClassModel(0, "4a")
        val success = db.addClass(new_class)
        assertEquals(true, success)
    }

    @Test
    fun testClassTableSize() {
        val db = DataBaseHelper(InstrumentationRegistry.getInstrumentation().targetContext)
        // check current classes
        val current_classes = db.getClasses().size
        // add one more classes to db
        val class1 = ClassModel(0,"4b")
        db.addClass(class1)
        val classes_plus_one = db.getClasses()
        assertEquals(current_classes+1, classes_plus_one.size)
    }

    @Test
    fun testAddLehrstoffElements() {
        val db = DataBaseHelper(InstrumentationRegistry.getInstrumentation().targetContext)
        val Lehrstoff = LehrstoffModel(0,"Programmieren", "Small Basic Grundlagen werden erarbeitet",
            "2021-04-28", "2021-04-28", "2021-04-29", 9)
        val success = db.addLehrstoff(Lehrstoff)
        assertEquals(true, success) }

    @Test
    fun testAnonymization() {
        val db = DataBaseHelper(InstrumentationRegistry.getInstrumentation().targetContext)
        val student = StudentModel(0,"Markus","Müller")
        db.addStudent(student)
        val student_before = db.getStudents()[db.getStudents().size-1]

        assertEquals(true, student_before.contains("Markus Müller"))
        db.anonymizeCurrentStudents()
        val normalstudent = db.getStudents()[db.getStudents().size-1]
        assertEquals(false, normalstudent.contains("Markus Müller"))
    }

    @Test
    fun testAnonymizationClass() {
        val db = DataBaseHelper(InstrumentationRegistry.getInstrumentation().targetContext)

        //Erstelle Student und Class und ordne ihn der Class zu
        val student = StudentModel(0,"Marvin","lol")
        db.addStudent(student)
        val class1 = ClassModel(0,"4a")
        db.addClass(class1)
        db.addStudentToClass(student,class1)

        val student_of_class1 = db.getStudentsOfClass(class1)[0]
        assertEquals(true, student_of_class1.contains("Marvin lol"))

        db.anonymizeClass(class1.classId)
        val student_anonymized = db.getStudentsOfClass(class1)[0]
        assertEquals(false, student_anonymized.contains("Marvin lol"))
    }

    @Test
    fun testSCTableSize() {
        val db = DataBaseHelper(InstrumentationRegistry.getInstrumentation().targetContext)
        val student = StudentModel(0,"Markus","Müller")
        val classModel = ClassModel(0, "2a")
        db.addStudent(student)
        db.addClass(classModel)

        val current_students = db.getStudentsOfClass(classModel).size
        db.addStudentToClass(student, classModel)

        val students_plus_one = db.getStudentsOfClass(classModel).size
        assertEquals(current_students + 1, students_plus_one)
    }

    @Test
    fun testSCTableContent() {
        val db = DataBaseHelper(InstrumentationRegistry.getInstrumentation().targetContext)
        val student1 = StudentModel(0,"Max","Müller")
        val student2 = StudentModel(0,"Peter","Heinz")
        val student3 = StudentModel(0,"Susanne","Peters")
        val classModel = ClassModel(0, "3a")

        db.addStudent(student1)
        db.addStudent(student2)
        db.addStudent(student3)
        db.addClass(classModel)

        val current_students = db.getStudentsOfClass(classModel).size
        db.addStudentToClass(student1, classModel)
        db.addStudentToClass(student2, classModel)
        db.addStudentToClass(student3, classModel)

        val students_plus_three = db.getStudentsOfClass(classModel)
        assertEquals(current_students + 3, students_plus_three.size)

        assertEquals("Max", students_plus_three.get(students_plus_three.size-3).split(" ").get(1))

        assertEquals("Peter", students_plus_three.get(students_plus_three.size-2).split(" ").get(1))
        assertEquals("Susanne", students_plus_three.get(students_plus_three.size-1).split(" ").get(1))

    }

    @Test
    fun testaddMitarbeitsplus() {
        val db = DataBaseHelper(InstrumentationRegistry.getInstrumentation().targetContext)
        val student1 = StudentModel(0,"Markus","Müller")
        val student2 = StudentModel (0,"Julia", "Muster")
        val classModel = ClassModel(0, "3a")
        db.addStudent(student1)
        db.addStudent(student2)
        db.addClass(classModel)

        val current_students = db.getStudentsOfClass(classModel).size
        db.addStudentToClass(student1, classModel)
        db.addStudentToClass(student2, classModel)

        db.updateMitarbeitsPlus(student1, classModel)
        db.updateMitarbeitsPlus(student1, classModel)

        val afteraddMitarbeitsplus = db.getStudentsOfClass(classModel).size
        assertEquals(current_students + 2, afteraddMitarbeitsplus)

        val student1_after = db.getStudentsOfClass(classModel)[db.getStudentsOfClass(classModel).size -1]
        val student1_plus = student1_after.split(" ")[3].toInt()

        val student2_after = db.getStudentsOfClass(classModel)[db.getStudentsOfClass(classModel).size -2]
        val student2_plus = student2_after.split(" ")[3].toInt()

        assertEquals(0, student1_plus)
        assertEquals(2, student2_plus)
    }
}