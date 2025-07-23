-- 插入初始用户数据
INSERT INTO hib_user (username, email, password, avatar_url, status, theme_dark, email_notifications, language, bio)
VALUES
    ('Cow-50482dbc41',
     '2148582258@qq.com',
     'default_password', -- 在这里设置一个默认的密码，假设这是一个初级用户数据
     'https://cetide-1325039295.cos.ap-chengdu.myqcloud.com/west/default_user.png',
     'ACTIVE',
     0,
     1,
     'zh-CN',
     '此用户很懒, 什么也没有留下.....');
