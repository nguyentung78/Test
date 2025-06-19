# REST API Quản lý User

Dự án này là một **REST API** quản lý thông tin người dùng, hỗ trợ **đăng ký**, **đăng nhập**, **cập nhật hồ sơ**, **xóa tài khoản**, và **quản trị user**. Công nghệ sử dụng:

- **Spring Boot**: Framework chính để xây dựng API.
- **MySQL**: Cơ sở dữ liệu lưu trữ thông tin user.
- **JWT**: Xác thực và phân quyền.
- **Cloudinary**: Upload và lưu trữ hình ảnh avatar.

## Yêu cầu hệ thống

- **Java**: 17
- **Maven**: 3.8.0 trở lên
- **MySQL**: 8.0 trở lên
- **Cloudinary**: Tài khoản Cloudinary (thông tin đã được cung cấp sẵn trong `application.properties`)
- **IDE**: IntelliJ IDEA (đề xuất, đã được sử dụng để phát triển dự án)
- **Công cụ test API**: Postman (để kiểm tra các endpoint)
- **Hệ điều hành**: Windows, macOS, hoặc Linux

## Hướng dẫn cài đặt môi trường

### Cài đặt Java và Maven

- **Java 17**:
  - Tải và cài đặt JDK 17 từ [Oracle](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) hoặc [OpenJDK](https://adoptopenjdk.net/).
  - Kiểm tra phiên bản:
    ```bash
    java -version
    ```
    - Đảm bảo đầu ra hiển thị phiên bản 17.x.x.
- **Maven**:
  - Tải và cài đặt Maven từ [trang chính thức](https://maven.apache.org/download.cgi).
  - Kiểm tra phiên bản:
    ```bash
    mvn -version
    ```
    - Đảm bảo đầu ra hiển thị phiên bản 3.8.0 trở lên.

### Cài đặt MySQL

- Tải và cài đặt MySQL từ [trang chính thức](https://dev.mysql.com/downloads/mysql/).
- Tạo cơ sở dữ liệu:
  ```sql
  CREATE DATABASE test1706;
  ```
- Mở file `src/main/resources/application.properties` và kiểm tra thông tin kết nối database:
  ```properties
  spring.datasource.url=jdbc:mysql://localhost:3306/test1706?createDatabaseIfNotExist=true
  ```
  - nhập thông tin username và password cá nhân vào

### Cấu hình Cloudinary và JWT

- Dự án đã cung cấp sẵn thông tin Cloudinary và khóa JWT trong file `application.properties`:
  ```properties
  cloudinary.cloud-name=dw4t9wg6k
  cloudinary.api-key=187735852455942
  cloudinary.api-secret=QpBRw1QtrMLmExqc99h2U5CbUEE
  SECRET_KEY=nbt7803@
  EXPIRED=86400000
  ```
- Bạn có thể sử dụng các giá trị này để tiết kiệm thời gian, không cần đăng ký tài khoản Cloudinary mới.

### Thiết lập dự án trong IntelliJ IDEA

- **Clone dự án từ Git**:
  ```bash
  git clone <repository_url>
  cd demo
  ```
- **Mở dự án**:
  - Mở IntelliJ IDEA, chọn `Open` và trỏ đến thư mục dự án vừa clone.
  - IntelliJ sẽ tự động nhận diện file `build.gradle`. Nhấn biểu tượng con voi (🐘) hoặc `Load Gradle Project` để tải các dependencies.
- **Dependencies**:
  - File `build.gradle` đã bao gồm tất cả dependencies cần thiết:
    - Spring Boot DevTools
    - Lombok
    - Spring Web
    - Spring Security
    - Spring Data JPA
    - MySQL Driver
    - Validation
    - Cloudinary: `com.cloudinary:cloudinary-http44:1.32.2`
    - JWT: `io.jsonwebtoken:jjwt:0.9.1`
    - JAXB: `javax.xml.bind:jaxb-api:2.3.1`
  - Nếu IntelliJ không tự động tải, nhấn `Sync Project with Gradle Files` trong tab Gradle (bên phải màn hình).

## Hướng dẫn bootstrap service

### Khởi tạo dữ liệu vai trò

- Trước khi chạy ứng dụng, cần tạo hai vai trò `ADMIN` và `USER` trong database.
- Vào database thêm ADMIN và USER vào bảng roles

### Chạy ứng dụng

- Trong IntelliJ:
  - Mở class `DemoApplication` và nhấn nút `Run` (hình tam giác xanh).
- Hoặc dùng lệnh:
  ```bash
  mvn spring-boot:run
  ```
- Ứng dụng sẽ chạy trên: **http://localhost:8080**.

### Kiểm tra ứng dụng

- Sử dụng **Postman** để test các endpoint (chi tiết ở phần dưới).
- Endpoint công khai đầu tiên để thử:
  ```http
  POST http://localhost:8080/api/v1/auth/register
  ```

## Cấu trúc endpoint

| Endpoint                          | Method | Quyền truy cập       | Mô tả                              |
|-----------------------------------|--------|---------------------|------------------------------------|
| `/api/v1/auth/register`           | POST   | Công khai           | Đăng ký người dùng mới (username, email, password, fullname, phone, avatar) |
| `/api/v1/auth/login`              | POST   | Công khai           | Đăng nhập và nhận JWT token        |
| `/api/v1/user/profile`            | GET    | USER, ADMIN         | Lấy thông tin hồ sơ người dùng     |
| `/api/v1/user/profile`            | PATCH  | USER, ADMIN         | Cập nhật thông tin hồ sơ (username, email, phone, fullname, avatar) |
| `/api/v1/user/account`            | DELETE | USER, ADMIN         | Xóa tài khoản (soft delete)        |
| `/api/v1/admin/users`             | GET    | ADMIN               | Lấy danh sách người dùng (phân trang) |

## Hướng dẫn kiểm thử trên Postman

### Đăng ký người dùng

- **Request**:
  ```http
  POST http://localhost:8080/api/v1/auth/register
  Content-Type: multipart/form-data
  ```
  - **Body** (form-data):
    - `username`: testuser
    - `email`: test@example.com
    - `password`: password123
    - `fullname`: Test User
    - `phone`: 0123456789
    - `avatar`: (chọn file hình ảnh, ví dụ: avatar.jpg)
- **Response** (200 OK):
  ```json
  "Đăng ký thành công"
  ```

### Đăng nhập

- **Request**:
  ```http
  POST http://localhost:8080/api/v1/auth/login
  Content-Type: application/json
  ```
  - **Body**:
    ```json
    {
        "username": "testuser",
        "password": "password123"
    }
    ```
- **Response** (200 OK):
  ```json
  {
      "username": "testuser",
      "accessToken": "<jwt_token>",
      "typeToken": "Bearer",
      "roles": ["USER"],
      "avatar": "<cloudinary_url>"
  }
  ```

### Lấy thông tin hồ sơ

- **Request**:
  ```http
  GET http://localhost:8080/api/v1/user/profile
  Authorization: Bearer <jwt_token>
  ```
- **Response** (200 OK):
  ```json
  {
      "id": 1,
      "username": "testuser",
      "email": "test@example.com",
      "fullname": "Test User",
      "phone": "0123456789",
      "avatar": "<cloudinary_url>",
      "status": true,
      "roles": ["USER"],
      "createdAt": "2025-06-19T22:30:00"
  }
  ```

### Cập nhật hồ sơ

- **Request**:
  ```http
  PATCH http://localhost:8080/api/v1/user/profile
  Content-Type: multipart/form-data
  Authorization: Bearer <jwt_token>
  ```
  - **Body** (form-data):
    - `username`: newusername
    - `email`: newemail@example.com
    - `phone`: 0987654321
    - `fullname`: New Name
    - `avatar`: (file hình ảnh mới)
- **Response** (200 OK):
  ```json
  "Cập nhật hồ sơ thành công"
  ```

### Xóa tài khoản

- **Request**:
  ```http
  DELETE http://localhost:8080/api/v1/user/account
  Authorization: Bearer <jwt_token>
  ```
- **Response** (200 OK):
  ```json
  "Xóa tài khoản thành công"
  ```