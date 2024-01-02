drop table if exists user cascade;
drop table if exists channel cascade;
drop table if exists product_post cascade;
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
