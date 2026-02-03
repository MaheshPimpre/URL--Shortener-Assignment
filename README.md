# URL Shortener – Spring Boot Application

## 📌 Overview
This project is a simple in-memory **URL Shortener Service** built using **Java Spring Boot**.  
It supports generating short URLs, redirecting users to the original URL, tracking domain metrics,  
and includes full REST APIs with in-memory storage.

---

## 🚀 Features

### ✔ 1. Shorten URL
- Accepts a long URL and returns a shortened version.
- If the same URL is provided again → returns the **same** short code (no duplicates).

### ✔ 2. Redirection API
- Visiting the short URL automatically redirects (HTTP 302) to the original URL.

### ✔ 3. In-Memory Storage
- No database is used.
- All data is stored using thread-safe `ConcurrentHashMap`.

### ✔ 4. Domain Metrics API
Returns **Top 3 most frequently shortened domains**, e.g.:

```
udemy.com: 6  
youtube.com: 4  
wikipedia.org: 2  
```

---

## 🏗 Application Architecture

```
┌──────────────────────────────┐
│        URL Controller         │
└───────────────┬──────────────┘
                │
                ▼
┌──────────────────────────────┐
│    URL Shortener Service     │
└───────────────┬──────────────┘
                │
                ▼
┌──────────────────────────────┐
│       In-Memory Store        │
│  (short ↔ original URLs)     │
│  (domain metrics counter)    │
└──────────────────────────────┘
```

---

## 📡 API Endpoints

### **1️⃣ POST /api/shorten**
Shortens the given URL.

**Request Body:**
```json
{
  "url": "https://youtube.com/video123"
}
```

**Response:**
```json
{
  "shortUrl": "http://localhost:8080/r/AbC123"
}
```

---

### **2️⃣ GET /api/redirect/{shortCode}**
Redirects the browser to the original URL.

Example:
```
GET http://localhost:8080/api/redirect/AbC123
```

Returns:
```
302 REDIRECT → https://youtube.com/video123
```

---

### **3️⃣ GET /api/original/{shortCode}**
Returns the original URL as text.

---

### **4️⃣ GET /api/metrics/top-domains**
Returns top 3 most shortened domains.

---

## 🐳 Docker Support

### **Build JAR**
```
mvn clean package -DskipTests
```

### **Build Docker Image**
```
docker build -t url-shortener .
```

### **Run Container**
```
docker run -p 8080:8080 url-shortener
```

---

## 🧪 Test Coverage
The application contains JUnit tests for:

- URL Shorten API
- Redirect API
- Original URL Fetch API
- Metrics API

---

## 🔗 GitHub Repository
https://github.com/MaheshPimpre/URL--Shortener-Assignment

---

## 👤 Author
**Mahesh Pimpre**
