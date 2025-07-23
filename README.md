# CoWrite

## 🚀 Project Introduction

CoWrite is a real-time document collaboration platform for technical teams, content organizations and knowledge-based enterprises. It has the use experience of linguaque/Notion and supports:

- Multi-person online editing collaboration
- document structure tree management
- Template-label system
- Fine-grained access control
- Comments/Audit Log

---

## 🎯 Core highlights

- 🧩 Document collaboration model is complete : knowledge base, documents, comments and versions.
- 🔄 Real-time editing capability (WebSocket/OT) reservation
- 🔐 Permission control granularity to document level
- 📦 Template and tag system make documents reusable and organized
- 🔔 Notification Center+Webhook External Integration
- 📜 Operation log audit, ensuring that the content can be traced

---


health endpoint: http://localhost:8080/actuator/health

### Architecture diagram

![Architecture diagram](.doc/img.png)

### Starting method
```bash
scripts\start.bat
```




### NodeJS Backend

```
my-node-app/
├── backend/                         # 后端服务相关代码
│   ├── controllers/                 # 路由处理逻辑
│   │   ├── authController.js        # 认证相关逻辑
│   │   ├── userController.js        # 用户管理逻辑
│   │   ├── postController.js        # 帖子管理逻辑
│   │   └── commentController.js     # 评论管理逻辑
│   ├── routes/                      # 路由配置
│   │   ├── authRoutes.js            # 认证相关路由
│   │   ├── userRoutes.js            # 用户管理路由
│   │   ├── postRoutes.js            # 帖子管理路由
│   │   └── commentRoutes.js         # 评论管理路由
│   ├── services/                    # 数据库、业务逻辑服务层
│   │   ├── authService.js           # 认证服务
│   │   ├── userService.js           # 用户服务
│   │   └── postService.js           # 帖子服务
│   ├── middlewares/                 # 中间件
│   │   ├── authMiddleware.js        # 认证中间件
│   │   ├── errorHandler.js          # 错误处理中间件
│   │   └── validationMiddleware.js  # 数据验证中间件
│   ├── config/                      # 配置文件
│   │   ├── config.js                # 配置文件（如数据库、服务器等）
│   │   └── db.js                    # 数据库连接配置
│   ├── cronJobs/                    # 定时任务（后台程序）
│   │   ├── userStatsCronJob.js      # 用户统计定时任务
│   │   └── postStatsCronJob.js      # 帖子统计定时任务
│   ├── utils/                       # 辅助工具类
│   │   ├── logger.js                # 日志管理工具
│   │   └── validator.js             # 数据验证工具
│   ├── app.js                       # Express 应用入口文件
│   └── server.js                    # 启动服务器
├── database/                        # 存放 SQLite 或其他数据库文件
│   └── database.db                  # SQLite 数据库文件
├── public/                          # 公共文件（例如上传的图片、静态文件等）
│   └── uploads/
├── scripts/                         # 脚本
│   └── setupDatabase.js             # 设置数据库和初始化表脚本
├── .env                              # 环境变量配置文件
├── package.json                     # 项目依赖及配置
├── tsconfig.json                    # TypeScript 配置文件
└── README.md                        # 项目说明文档
```