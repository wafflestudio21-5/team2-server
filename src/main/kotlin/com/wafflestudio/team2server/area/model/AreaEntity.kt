package com.wafflestudio.team2server.area.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.OneToMany

@Entity(name = "area")
class AreaEntity(
	@Id
	val id: Int,
	val code: String,
	val fullName: String,
	val name: String,
	val sggName: String,
	val sdName: String,
	/*
	@OneToMany(mappedBy = "area_id")
	val adj1: List<AreaAdjEntity>,
	@OneToMany(mappedBy = "area_id")
	val adj2: List<AreaAdjEntity>,
	@OneToMany(mappedBy = "area_id")
	val adj3: List<AreaAdjEntity>,
	*/
	@OneToMany(mappedBy = "area")
	val areaUsers: List<AreaUserEntity> = mutableListOf(),
)
