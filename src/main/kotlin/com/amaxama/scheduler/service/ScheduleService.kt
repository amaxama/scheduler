package com.amaxama.scheduler.service

import com.amaxama.scheduler.domain.Organization
import com.amaxama.scheduler.domain.Student

import com.amaxama.scheduler.constants.*
import com.amaxama.scheduler.domain.Schedule
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.stereotype.Service

@Service
class ScheduleService {

    fun processSchedule(json: String): Schedule {
        val mapper = jacksonObjectMapper()
        val schedule = mapper.readValue<Schedule>(json)

        val totalTimeslotsAvailable = findTotalTimeslots(schedule.orgs)

        val schedule2 = filterOrgStudentInterest(schedule.students, buildOrgStudentList(schedule.students, schedule.orgs))
        val schedule3 = filterStudentOrgInterest(schedule2.students, schedule2.orgs)

        return schedule3
//        return mapper.writeValueAsString(schedule3)

    }

    fun findTotalStudentOrgInterest(students: MutableList<Student>) : Int {
        return students.sumBy { it.orgInterest }
    }

    fun findTotalOrgTimesAvailable(orgs: MutableList<Organization>) : Int {
        return orgs.sumBy { it.studentsInterested }
    }

    fun confirmTimeslotsOutweighInterest(studentOrgInterest: Int, orgTimesAvailable: Int) : Boolean {
        return orgTimesAvailable >= studentOrgInterest
    }

    fun checkOrgTimeslotAndInterestMatch(orgs: MutableList<Organization>) : Boolean {
        return orgs.all { org -> org.timeslotsAvailable == org.studentsInterested }
    }

    fun checkStudentTimeslotsAndOrgInterestMatch(students: MutableList<Student>) : Boolean {
        return students.all  { student -> student.timeslotsAvailable == student.orgInterest}
    }

    private fun findTotalTimeslots(orgs: List<Organization>): Int {
        return orgs.sumBy { it.timeslotsAvailable }
    }

    private fun buildOrgStudentList(students: MutableList<Student>, orgs: MutableList<Organization>): MutableList<Organization> {
        for (org in orgs) {
            for (student in students) {
                if (student.organizations.contains(org.id)) {
                    org.students.add(student)
                }
            }
        }
        return sortOrgsByStudentInterest(orgs)
    }

    fun sortStudentsByOrgInterest(students: MutableList<Student>): MutableList<Student> {
        students.sortByDescending { it.orgInterest }
        return students
    }

    fun sortOrgsByStudentInterest(orgs: MutableList<Organization>): MutableList<Organization> {
        orgs.sortByDescending { it.studentsInterested }
        return orgs
    }

    fun filterStudentOrgInterest(students: MutableList<Student>, orgs: MutableList<Organization>): Schedule {
        for (student in students) {
//            println(student)
// TODO: Do not rebuild orgs every time
            val rankedOrgs = sortOrgsByStudentInterest(orgs)
//            rankedOrgs.forEach{ org -> println("ID: ${org.id} Students: ${org.students.forEach { print(it.name) }}")}
            var index = 0
// TODO: Come back to see whether reorg needs to happen between student org removal
            while (student.orgInterest > student.timeslotsAvailable) {
                val orgToRemove = getOrg(rankedOrgs, index)
                val hasRemovedItem = student.organizations.removeAll { orgId ->
                    orgId == orgToRemove.id
                }
                if (hasRemovedItem) {
                    orgToRemove.students.remove(student)
                }
//                println("removing $index")
                index ++
            }
//            println(student)
        }

        return Schedule(sortStudentsByOrgInterest(students), sortOrgsByStudentInterest(orgs))  // TODO: Figure out if need to return orgs here too
    }

    fun filterOrgStudentInterest(students: MutableList<Student>, orgs: MutableList<Organization>) : Schedule {
        for (org in orgs) {
//            println(org)
// TODO: Do not rebuild orgs every time
            val rankedStudents = sortStudentsByOrgInterest(students)
//            rankedStudents.forEach{ student -> println("NAME: ${student.name} Orgs: ${student.organizations.joinToString { it.toString() }}")}
// TODO: Stop doing this by index
            var index = 0
// TODO: Come back to see whether reorg needs to happen between student org removal
            while (org.studentsInterested > org.timeslotsAvailable) {
                val studentToRemove = getStudent(rankedStudents, index)
                val hasRemovedItem = org.students.removeAll { student ->
                    student == studentToRemove
                }
                if (hasRemovedItem) {
                    studentToRemove.organizations.remove(org.id)
                }
//                println("removing $index")
                index ++
            }
//            println(org)
        }

        return Schedule(sortStudentsByOrgInterest(students), sortOrgsByStudentInterest(orgs))  // TODO: Figure out if need to return orgs here too
    }

    fun getStudent(students: MutableList<Student>, index: Int) : Student {
        return students.elementAt(index)
    }

    fun getOrg(orgs: MutableList<Organization>, index: Int) : Organization {
        return orgs.elementAt(index)
    }






}