# Token 实现机制
## 采用 JWT (JSON Web Token)
- 生成：用户登录成功后，后端验证账号密码，调用 JWT 工具类生成包含用户账号的加密 Token，并返回给前端。
- 存储：前端接收到 Token 后，存储在浏览器的 localStorage 中，持久化保存。
- 传输：前端调用需要鉴权的接口（如 /repair/my-order）时，通过 auth.js 拦截器自动将 Token 放入 HTTP 请求头（Header）的 Authorization 字段中。
- 校验：后端配置 AuthInterceptor 拦截器，拦截所有需要鉴权的接口，校验请求头中 Token 的合法性及有效性，校验不通过则拒绝访问。