drop table if exists user cascade;
drop table if exists channel cascade;
drop table if exists product_post cascade;
drop table if exists channel_message cascade;
drop table if exists message_sequence cascade;
drop table if exists product_img cascade;
drop table if exists wish_list cascade;
drop table if exists trade_review cascade;
drop table if exists hide_post cascade;
drop table if exists community cascade;
drop table if exists community_comment cascade;
drop table if exists community_img cascade;
drop table if exists community_save cascade;
drop table if exists community_like cascade;
drop table if exists post_category cascade;
drop table if exists active_area cascade;
drop table if exists tender cascade;
create table user (
	id bigint auto_increment,
	provider int,
	email varchar(255),
	password varchar(255),
	role int,
	profile_img varchar(255),
	nickname varchar(255),
	sub varchar(255),
	manner_temperature decimal(3,1),
	created_at datetime,
	rep_badge_id bigint,
	primary key (id)
);
create table channel (
	id bigint auto_increment,
	post_id bigint,
	last_msg varchar(500),
	msg_updated_at datetime,
	created_at datetime,
	primary key (id)
);
create table product_post (
	id bigint auto_increment,
	author_id bigint not null,
	buyer_id bigint not null,
	selling_area_id bigint,
	category_id bigint,
	type int,
	status int,
	title varchar(255),
	rep_img varchar(255),
	description text,
	sell_price int,
	trading_location point,
	view_cnt int,
	refresh_cnt int,
	refreshed_at datetime,
	created_at datetime,
	deadline datetime,
	hidden_yn boolean,
	offer_yn boolean,
	wish_cnt int,
	chat_cnt int,
	primary key (id)
);
create table channel_message (
	id bigint auto_increment,
	channel_id bigint,
	sender_id bigint,
	message varchar(500),
	read_yn boolean,
	created_at datetime,
	msg_no bigint,
	primary key (id)
);
create table message_sequence (
	id bigint,
	msg_no bigint
);
create table product_img (
	id bigint auto_increment,
	post_id bigint,
	url varchar(255),
	created_at datetime,
	primary key (id)
);
create table wish_list (
	id bigint auto_increment,
	user_id bigint,
	post_id bigint,
	created_at datetime,
	primary key (id)
);
create table trade_review (
	id bigint auto_increment,
	post_id bigint,
	type int,
	description varchar(255),
	hide_yn boolean,
	created_at datetime,
	primary key (id)
);
create table hide_post (
	id bigint auto_increment,
	user_id bigint,
	post_id bigint,
	created_at datetime,
	primary key (id)
);
create table community (
	id bigint auto_increment,
	author_id bigint,
	area_id int,
	created_at datetime,
	title varchar(255),
	description text,
	view_cnt int,
	like_cnt int,
	rep_img varchar(255),
    primary key (id)
);
create table community_save (
	id bigint auto_increment,
	user_id bigint,
	community_id bigint,
	created_at datetime,
	primary key (id)
);
create table community_like (
	id bigint auto_increment,
	user_id bigint,
	community_id bigint,
	created_at datetime,
	primary key (id)
);
create table community_img (
	id bigint auto_increment,
	community_id bigint,
	url varchar(255),
	created_at datetime,
	primary key (id)
);
create table community_comment (
	id bigint auto_increment,
	author_id bigint not null,
	community_id bigint not null,
	comment text,
	parent_id bigint,
	img_url varchar(255),
	like_cnt int,
	created_at datetime,
	updated_at datetime,
	primary key (id)
);
create table post_category (
	id bigint,
	name varchar(20),
	primary key (id)
);
create table active_area (
	user_id bigint,
	reference_area_id int,
	authenticated_at datetime,
	count int
);
create table tender (
	id bigint auto_increment,
	user_id bigint,
	post_id bigint,
	price int,
	tender_at datetime,
	primary key (id)
)
