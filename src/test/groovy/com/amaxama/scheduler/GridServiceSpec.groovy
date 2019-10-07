package com.amaxama.scheduler

import com.amaxama.scheduler.service.GridService
import spock.lang.Specification

class GridServiceSpec extends Specification {

	GridService gridService = new GridService()

	def "test"() {
		when:
			gridService.test()

		then:
			"ok"
	}

}
