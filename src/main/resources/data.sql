INSERT INTO product_post (id, author_id, buyer_id, selling_area_id, category_id, type, status, title, rep_img, description, sell_price, trading_location, view_cnt, refresh_cnt, refreshed_at, created_at, deadline, hidden_yn, offer_yn, wish_cnt, chat_cnt) VALUES
(1077, 24234, 342, -1, -1,1,1,'testpost','','desc!',10000,null,134,1,'2024-01-01 00:00:01','2024-01-01 00:00:01','2024-01-01 00:00:01',0,0,0,0),
(1078, 24234, 342, -1, -1,1,1,'testpost','','desc!',10000,null,134,1,'2024-01-01 00:00:01','2024-01-01 00:00:01','2024-01-01 00:00:01',0,0,0,0);
INSERT INTO user (email,password,nickname,created_at) VALUES
('t1@t1.kr','1234','testuser1','2023-12-25 00:00:01'),
('t2@t2.com','1234','testuser2','2023-11-30 05:10:01'),
('t3@t3.com','1234','testuser3','2023-11-30 05:16:01');

INSERT INTO channel (post_id) VALUES (1077), (1078);
INSERT INTO channel_user (user_id, channel_id) VALUES (1, 1), (2, 1), (1, 2), (3, 2);
