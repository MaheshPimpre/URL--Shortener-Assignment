# URL Shortener – Spring Boot Application

## Overview
This is a Spring Boot–based **URL Shortener Application** that generates unique short URLs,  
redirects users to original URLs, and keeps in-memory domain statistics.  
Everything is stored using `ConcurrentHashMap` (no database required).

This application was built for the assignment and includes:

- URL Shortening
- Redirection API
- Domain Metrics API
- Duplicate URL handling
- In-Memory storage
- Unit tests
- Docker support
- JAR build instructions
---

## Features

### 1. Shorten URL
- Accepts a long URL and returns a shortened version.
- If the same URL is provided again → returns the **same** short code (no duplicates).

### 2. Redirection
- Visiting `/{shortCode}` redirects to the original URL
- Implemented using Spring’s `RedirectView`

### 3. In-Memory Storage
- Uses `ConcurrentHashMap` for efficient, thread-safe storage
- Maps stored:
    - **shortCode → originalURL**
    - **originalURL → shortCode**
    - **domainName → count**

### 4. Domain Metrics API
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

## 📁 Project Structure

```
src/main/java
 └── controller
 └── service
 └── store
 └── exception
 └── UrlShortenerApplication.java

src/test/java
 └── UrlControllerTest.java

Dockerfile
README.md
pom.xml
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

## 🏃‍♂️ Running the Application

### ▶ 1. Run using JAR

Build JAR:
```
mvn clean package -DskipTests
```

Run JAR:
```
java -jar target/URL-Shortener-Assignment-1.0.0.jar
```

Application starts at:
```
http://localhost:8080
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

## Docker Support

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

## Test Coverage
The application contains JUnit tests for:

- URL Shorten API
- Redirect API
- Original URL Fetch API
- Metrics API

---

## GitHub Repository
https://github.com/MaheshPimpre/URL--Shortener-Assignment

---

## Author
**Mahesh Pimpre**
