package com.amaxama.scheduler.domain

class Student (
        val name: String = "",
        val timeslotsAvailable: Int = 3,
        val organizations: MutableList<Int> = mutableListOf()) {


    val orgInterest get() = organizations.size

    constructor(name: String, organizations: MutableList<Int>) : this(name, 3, organizations)


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Student

        if (name != other.name) return false
        if (organizations != other.organizations) return false
        if (timeslotsAvailable != other.timeslotsAvailable) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + organizations.hashCode()
        result = 31 * result + timeslotsAvailable
        return result
    }

    override fun toString(): String {
        return "Student(name='$name', organizations=$organizations, timeslotsAvailable=$timeslotsAvailable, orgInterest=$orgInterest)"
    }
}



