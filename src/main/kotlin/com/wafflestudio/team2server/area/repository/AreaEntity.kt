package com.wafflestudio.team2server.area.repository

import jakarta.persistence.*

@Entity(name = "area")
class AreaEntity(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Int = 0,
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
