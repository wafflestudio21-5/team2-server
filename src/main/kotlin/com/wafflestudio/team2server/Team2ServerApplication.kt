package com.wafflestudio.team2server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

@SpringBootApplication
@EnableCaching
class Team2ServerApplication

fun main(args: Array<String>) {
	runApplication<Team2ServerApplication>(*args)
}
