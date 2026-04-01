// auth.js  全局登录校验（所有页面自动生效）
function checkLogin() {
    // 1. 拿 Token
    const token = localStorage.getItem("token");

    // 2. 如果没有 Token → 拦截！跳登录
    if (!token) {
        alert("请先登录！");
        window.location.href = "login.html";
        return false;
    }

    // 3. 有 Token → 放行
    return true;
}

// 页面一加载就自动执行校验
window.onload = function () {
    checkLogin();
};

// Token 的请求工具
// 所有页面都用它发请求
function request(url, options = {}) {
    // 1. 拿到 Token
    const token = localStorage.getItem("token");

    // 2. 默认配置（自动带请求头）
    const defaultOptions = {
        headers: {
            "token": token, // ✅ 自动把 Token 塞进去！
            "Content-Type": "application/x-www-form-urlencoded"
        }
    };

    // 3. 合并用户传入的参数
    const finalOptions = { ...defaultOptions, ...options };

    // 4. 发送请求
    return fetch(url, finalOptions);
}