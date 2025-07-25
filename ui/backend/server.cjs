require('dotenv').config();

// 使用相对路径引用 app.cjs
const app = require('./app.cjs');  // 确保路径是正确的，`app.cjs` 文件位于同一目录下

const port = process.env.PORT || 3000;
app.listen(port, () => {
    console.log(`Server is running on http://localhost:${port}`);
});
