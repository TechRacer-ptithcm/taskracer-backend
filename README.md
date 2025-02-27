# 📌 Tasks Racer

## 📖 Giới Thiệu

Taks Racer là một nền tảng **tối ưu hóa học tập cho sinh viên**, tích hợp **Pomodoro, quản lý lịch học, quản lý tài liệu, học nhóm, điểm kinh nghiệm, đánh giá xếp hạng**, giúp sinh viên học tập hiệu quả hơn.

## 🚀 Công Nghệ Sử Dụng

- **Ngôn ngữ**: Java 21
- **Framework**: Spring Boot
- **Cơ sở dữ liệu**: PostgreSQL, Redis (caching)
- **Giao tiếp Message Queue**: Apache Kafka
- **API**: RESTful API, WebSocket
- **Authentication & Authorization**: Spring Security, JWT
- **Containerization**: Docker, Kubernetes
- **CI/CD**: GitHub Actions, Jenkins
- **Logging & Monitoring**: ELK Stack, Prometheus, Grafana

## 📌 Các Tính Năng Backend Chính

### 1️⃣ **Quản lý thời gian học tập & Pomodoro**

✔ Hệ thống **Pomodoro thông minh**: Học tập với các phiên **25 phút + 5 phút nghỉ**.  
✔ **Lưu lịch sử phiên học**, hỗ trợ báo cáo thống kê.  
✔ **Chế độ tập trung**: Chặn thông báo, ngăn chặn xao nhãng.

### 2️⃣ **Quản lý lịch học & nhắc nhở deadline**

✔ API **quản lý lịch học** theo ngày, tuần, tháng.  
✔ Tự động **đồng bộ với Google Calendar**.  
✔ **Nhắc nhở deadline**: Gửi thông báo khi sắp đến hạn nộp bài.

### 3️⃣ **Quản lý tài liệu học tập**

✔ **Tải lên & lưu trữ tài liệu** theo từng môn học (AWS S3, Firebase Storage).  
✔ **Tìm kiếm thông minh**: Lọc theo môn học, tag.  
✔ **Ghi chú & highlight** trực tiếp trên tài liệu.

### 4️⃣ **Học nhóm & Thảo luận**

✔ API **tạo phòng học nhóm**, hỗ trợ **chat real-time**.  
✔ **Tích hợp WebSocket** để đồng bộ trạng thái nhóm.  
✔ **Theo dõi tiến độ học tập** trong nhóm.

### 5️⃣ **Game hóa học tập**

✔ **Hệ thống điểm XP, Huy hiệu** khi hoàn thành nhiệm vụ.  
✔ **Thử thách học tập**: Hoàn thành 10 phiên Pomodoro, học liên tục 5 ngày.  
✔ **Bảng xếp hạng**: Ai có nhiều XP nhất trong tuần?

## 📜 Cài Đặt & Chạy Dự Án

### 📌 Yêu cầu hệ thống

- Java 21
- Docker & Docker Compose
- PostgreSQL, Redis, Kafka