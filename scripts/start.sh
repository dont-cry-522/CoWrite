#!/bin/bash

# 启动 Spring Boot 后端服务
echo "启动 Spring Boot 后端服务..."
nohup mvn spring-boot:run &

# 启动 Vue 前端服务
echo "启动 Vue 前端服务..."

cd ui

# 检查 node_modules 文件夹是否存在，如果存在则跳过 npm install
if [ -d "node_modules" ]; then
  echo "node_modules 文件夹已存在，跳过安装依赖"
else
  echo "node_modules 文件夹不存在，正在安装依赖..."
  npm install
fi

# 启动 Vue 开发服务器
npm run dev

cd backend

node server.cjs

echo "所有服务已启动！"
