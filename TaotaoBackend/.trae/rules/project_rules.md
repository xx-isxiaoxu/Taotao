##电商平台项目规则
一、项目概述
本项目是一个基于Vue3的电商平台，采用现代化前端技术栈开发，注重模块化、组件化和良好的用户体验。

二、技术栈
    构建工具: Vite
    前端框架: Vue 3 (Composition API)
    UI框架: Element Plus
    状态管理: Pinia
    HTTP请求: Axios
    路由: Vue Router
    Mock数据: 使用Pinia模拟后端实现数据持久化

三、项目结构
/src
├── api                  # API请求封装
├── assets               # 静态资源
├── components           # 公共组件
│   ├── common           # 全局通用组件
│   ├── layout           # 布局组件
│   └── business         # 业务组件
├── composables          # 组合式函数
├── mock                 # Mock数据
├── router               # 路由配置
├── stores               # Pinia状态管理
├── styles               # 全局样式
├── utils                # 工具函数
└── views                # 页面组件
    ├── auth             # 认证相关页面
    ├── cart             # 购物车相关
    ├── home             # 首页
    ├── order            # 订单相关
    ├── product          # 商品相关
    └── user             # 用户中心

四、开发规范
1. 组件开发
    使用组合式API编写组件
    组件命名采用PascalCase
    一个目录一个组件，目录名与组件名一致
    组件props需定义类型和默认值
    使用defineProps和defineEmits进行类型声明
2. 状态管理
    每个业务模块创建独立的store
    使用storeToRefs解构store保持响应式
    复杂业务逻辑封装到actions中
3. API请求
    所有API请求统一在api目录管理
    使用Axios实例封装基础请求
    接口按模块划分文件
4. Mock数据
    使用Pinia模拟后端数据持久化
    创建mock store存储模拟数据
    实现CRUD操作模拟接口
5. 路由规范
    路由按需加载组件
    路由守卫统一管理
    路由meta定义页面权限等信息

五、页面设计要求
1.核心页面功能
首页
    轮播广告区
    商品分类导航
    热门商品推荐
    新品上架
    促销活动专区

商品详情页
    商品图片展示(可缩放)
    商品基本信息
    规格选择
    购买数量选择
    加入购物车/立即购买按钮
    商品详情(富文本)
    用户评价

购物车
    商品列表(图片、名称、价格、数量)
    全选/反选
    数量修改
    删除商品
    价格汇总
    结算按钮

订单流程
    订单确认页(收货地址选择、商品清单、优惠券使用)
    支付页(多种支付方式选择)
    支付结果页

用户中心
    个人信息管理
    收货地址管理
    订单列表(按状态筛选)
    收藏商品


