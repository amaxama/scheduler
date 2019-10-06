package com.amaxama.scheduler

import com.amaxama.scheduler.domain.Organization
import com.amaxama.scheduler.domain.Schedule
import com.amaxama.scheduler.domain.Student
import com.amaxama.scheduler.service.ScheduleService
import spock.lang.Specification

class ScheduleServiceSpec extends Specification {

	ScheduleService scheduleService = new ScheduleService()


	def "find total timeslots returns total available amongst all orgs"() {
		given:
			def org1 = new Organization(1, "org1", 2, [])
			def org2 = new Organization(2, "org2", 1, [])
			def org3 = new Organization(3, "org3", 3, [])
			def orgs = [org1, org2, org3]

		when:
			def timeslots = scheduleService.findTotalTimeslots(orgs)

		then:
			timeslots == 6

	}

	def "sortStudentsByOrgInterest returns sorted students based on number of orgs they want to see"() {
		given:
			def student1 = new Student("anna", [1, 2, 3, 4])
			def student2 = new Student("maxam", [1, 2])
			def student3 = new Student("student3", [3, 2, 4])
			def students = [student1, student2, student3]
			def sortedStudents = [student1, student3, student2]

		when:
			def result = scheduleService.sortStudentsByOrgInterest(students)

		then:
			result == sortedStudents

	}

	def "sortOrgsByStudentInterest returns sorted orgs based on number of students interested"() {
		given:
			def student1 = new Student("anna", [1, 2, 3, 4])
			def student2 = new Student("maxam", [1, 2])
			def student3 = new Student("student3", [3, 2, 4])
			def org3students = [student1, student3]
            def org2students = [student1, student2, student3]
            def org1students = [student1, student2]
            def org1 = new Organization(1, "org1", 2, org1students)
            def org2 = new Organization(2, "org2", 1, org2students)
            def org3 = new Organization(3, "org3", 3, org3students)
            def orgs = [org1, org2, org3]
            def sortedOrgs = [org2, org1, org3]

        when:
            def result = scheduleService.sortOrgsByStudentInterest(orgs)

        then:
            println result
            result == sortedOrgs

	}

    def "buildOrgStudentList"() {
        given:
            def student1 = new Student("anna", [1, 2, 3, 4])
            def student2 = new Student("maxam", [1, 2])
            def student3 = new Student("student3", [3, 2, 4])
            def org3students = [student1, student3]
            def org2students = [student1, student2, student3]
            def org1students = [student1, student2]
            def org1 = new Organization(1, "org1", 2, org1students)
            def org2 = new Organization(2, "org2", 1, org2students)
            def org3 = new Organization(3, "org3", 3, org3students)
            def orgs = [org1, org2, org3]
            def students = [student1, student2, student3]
            def sortedOrgs = [org2, org1, org3]

        when:
            def result = scheduleService.buildOrgStudentList(students, orgs)

        then:
            println result
            result == sortedOrgs

    }

	def "test filter studentOrgs"() {
		given:
            def student1 = new Student("Harry",1, [1, 2])
            def student1Final = new Student("Harry",1, [2])
            def student2 = new Student("Hermione", 1, [1])
            def student2Final = new Student("Hermione", 1, [1])
            def student3 = new Student("Ron", 1, [1, 2, 3])
            def student3Final = new Student("Ron", 1, [3])
            def student4 = new Student("Neville", 1, [1, 2, 3])
            def student4Final = new Student("Neville", 1, [1])
            def student5 = new Student("Ginny", 1, [2, 3])
            def student5Final = new Student("Ginny", 1, [3])
            def students = [student1, student2, student3, student4, student5]
            def org3students = [student3, student4, student5]
            def org2students = [student1, student3, student4, student5]
            def org1students = [student1, student2, student3, student4]
            def org1 = new Organization(1, "org1", 2, org1students)
            def org2 = new Organization(2, "org2", 1, org2students)
            def org3 = new Organization(3, "org3", 3, org3students)
            def orgs = [org1, org2, org3]
            def sortedOrgs = [org3, org1, org2]
			def finalStudents = [student1Final, student2Final, student3Final, student4Final, student5Final]
            def filteredPair = new Schedule(finalStudents, sortedOrgs)

		when:
			println students.toString()
			def result = scheduleService.filterStudentOrgInterest(students, orgs)

		then:
            println result
			result == filteredPair
	}

	def "filter orgStudents"() {
		given:
			def student1 = new Student("Harry",1, [1, 2])
			def student1Final = new Student("Harry",1, [1, 2])
			def student2 = new Student("Hermione", 1, [1])
			def student2Final = new Student("Hermione", 1, [1])
			def student3 = new Student("Ron", 1, [1, 2, 3])
			def student3Final = new Student("Ron", 1, [3])
			def student4 = new Student("Neville", 1, [1, 2, 3])
			def student4Final = new Student("Neville", 1, [1])
			def student5 = new Student("Ginny", 1, [2, 3])
			def student5Final = new Student("Ginny", 1, [2])
			def students = [student5, student2, student3, student4, student1]
			def org3students = [student3, student4, student5]
			def org2students = [student1, student3, student4, student5]
			def org1students = [student1, student2, student3, student4]
			def org1 = new Organization(1, "org1", 3, org1students)
			def org1Final = new Organization(1, "org1", 3, [student1, student2, student4])
			def org2 = new Organization(2, "org2", 2, org2students)
			def org2Final = new Organization(2, "org2", 2, [student1, student5])
			def org3 = new Organization(3, "org3", 1, org3students)
			def org3Final = new Organization(3, "org3", 1, [student3])
			def orgs = [org1, org2, org3]
			def sortedOrgs = [org1Final, org2Final, org3Final]
			def sortedStudents = [student1Final, student4Final, student5Final, student3Final, student2Final]
			def filteredPair = new Schedule(sortedStudents, sortedOrgs)

		when:
			println students.toString()
			def result = scheduleService.filterOrgStudentInterest(students, orgs)

		then:
			println result
			result == filteredPair
	}

}
