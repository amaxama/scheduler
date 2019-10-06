package com.amaxama.scheduler.controller

import com.amaxama.scheduler.domain.Schedule
import com.amaxama.scheduler.service.BarrenLandService
import com.amaxama.scheduler.service.ScheduleService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ScheduleController(@Autowired val scheduleService: ScheduleService, @Autowired val barrenLandService: BarrenLandService
) {

    @PostMapping("/schedules/v1")
    fun processSchedule(@RequestBody body: String) : Schedule {
        return scheduleService.processSchedule(body)
    }

    @PostMapping("/barrenland")
    fun runBarrenLand(@RequestBody body: String) : String {
        return barrenLandService.getFertileLand(body)
    }

}