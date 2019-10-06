package com.amaxama.scheduler.domain

class Organization (
    val id: Int,
    val name: String,
    val timeslotsAvailable: Int,
    val students: MutableList<Student> = mutableListOf<Student>() ) {

    val studentsInterested get() = students.size



    override fun toString(): String {
        return "Organization(id=$id, name='$name', timeslotsAvailable=$timeslotsAvailable, students=${students.joinToString { it.name }}, studentInterest=$studentsInterested)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Organization

        if (id != other.id) return false
        if (name != other.name) return false
        if (timeslotsAvailable != other.timeslotsAvailable) return false
        if (students != other.students) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + name.hashCode()
        result = 31 * result + timeslotsAvailable
        result = 31 * result + students.hashCode()
        return result
    }


}