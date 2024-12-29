# 助农电商平台

![助农电商平台](frontend/public/images/logo/logo.svg)

## 项目简介

助农电商平台是一个专注于帮助农民销售农产品的在线商城系统。平台致力于搭建农产品销售渠道，促进农村经济发展，实现农产品产销对接。

### 主要特点

- 🌾 专注农产品：为农民提供专业的农产品销售渠道
- 🚀 便捷交易：简单易用的购物流程，支持多种支付方式
- 💡 智能推荐：基于用户喜好的个性化商品推荐
- 📱 响应式设计：完美适配各种设备，随时随地购物
- 🛡️ 安全可靠：严格的商品质量把控，安全的支付环境

## 功能特性

### 用户端功能
- 用户注册与登录
- 商品浏览与搜索
- 购物车管理
- 订单管理
- 个人中心
- 收货地址管理
- 商品评价

### 商家端功能
- 商品管理
- 订单处理
- 库存管理
- 销售统计
- 评价管理

### 管理员功能
- 用户管理
- 商家管理
- 商品分类管理
- 系统设置
- 数据统计

## 技术栈

### 前端技术
- Vue 3
- Vue Router
- Pinia
- Element Plus
- Vite
- Axios

### 后端技术
- Node.js
- Express
- MongoDB
- Redis
- JWT

## 开发环境要求

- Node.js >= 16.0.0
- npm >= 8.0.0
- MongoDB >= 4.0.0
- Redis >= 6.0.0

## 安装步骤

1. 克隆项目
```bash
git clone https://github.com/yourusername/agrihelp.git
cd agrihelp
```

2. 安装前端依赖
```bash
cd frontend
npm install
```

3. 安装后端依赖
```bash
cd ../backend
npm install
```

4. 配置环境变量
```bash
cp .env.example .env
# 编辑 .env 文件，配置必要的环境变量
```

5. 启动开发服务器

前端开发服务器：
```bash
cd frontend
npm run dev
```

后端开发服务器：
```bash
cd backend
npm run dev
```

## 项目结构

```
agrihelp/
├── frontend/                # 前端项目目录
│   ├── public/             # 静态资源
│   ├── src/               # 源代码
│   │   ├── assets/       # 资源文件
│   │   ├── components/   # 公共组件
│   │   ├── views/        # 页面组件
│   │   ├── router/       # 路由配置
│   │   ├── stores/       # 状态管理
│   │   └── utils/        # 工具函数
│   └── package.json      # 项目依赖
│
└── backend/               # 后端项目目录
    ├── src/              # 源代码
    │   ├── controllers/  # 控制器
    │   ├── models/       # 数据模型
    │   ├── routes/       # 路由配置
    │   └── utils/        # 工具函数
    └── package.json      # 项目依赖
```

## 开发规范

### 代码规范
- 使用 ESLint 进行代码检查
- 使用 Prettier 进行代码格式化
- 遵循 Vue 3 组合式 API 的最佳实践
- 组件名使用 PascalCase 命名
- 文件名使用 kebab-case 命名

### Git 提交规范
- feat: 新功能
- fix: 修复问题
- docs: 文档修改
- style: 代码格式修改
- refactor: 代码重构
- test: 测试用例修改
- chore: 其他修改

## 部署说明

### 前端部署
1. 构建生产环境代码
```bash
cd frontend
npm run build
```

2. 将 dist 目录下的文件部署到 Web 服务器

### 后端部署
1. 安装 PM2
```bash
npm install -g pm2
```

2. 启动服务
```bash
cd backend
pm2 start ecosystem.config.js
```

## 常见问题

### Q: 如何处理跨域问题？
A: 开发环境下通过 Vite 的代理配置处理，生产环境需要在后端配置 CORS。

### Q: 如何更新依赖包？
A: 使用 `npm update` 更新到最新版本，注意检查更新后的兼容性。

## 贡献指南

1. Fork 项目
2. 创建功能分支
3. 提交代码
4. 创建 Pull Request

## 版本历史

- v1.0.0 (2024-01-01)
  - 初始版本发布
  - 实现基础购物功能
  - 支持用户注册登录

## 联系方式

- 项目负责人：[姓名]
- 邮箱：[邮箱地址]
- 微信：[微信号]

## 许可证

本项目采用 MIT 许可证，详情请参见 [LICENSE](LICENSE) 文件。
