# 🏥 Healthcare Platform - Microservices E-commerce System

A **production-ready Spring Boot microservices platform** for healthcare services with comprehensive **role-based access control**, **Docker deployment**, and **CI/CD integration**.

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.java.net/projects/jdk/21/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-green.svg)](https://spring.io/projects/spring-boot)
[![Docker](https://img.shields.io/badge/Docker-Ready-blue.svg)](https://www.docker.com/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue.svg)](https://www.postgresql.org/)
[![MongoDB](https://img.shields.io/badge/MongoDB-7-green.svg)](https://www.mongodb.com/)

---

## 🌟 **Key Features**

✅ **Complete E-commerce Platform** - Healthcare services marketplace with full booking system  
✅ **Role-Based Access Control** - Customer, Healthcare Provider, and Admin roles  
✅ **Comprehensive API Suite** - RESTful APIs with complete Swagger documentation  
✅ **Real-time Features** - WebSocket support for live booking updates  
✅ **Production-Ready Docker** - Individual service containers with orchestration  
✅ **CI/CD Integration** - GitHub Actions workflow with automated testing  
✅ **Database Integration** - PostgreSQL + MongoDB with Liquibase migrations  
✅ **Security Hardened** - JWT authentication, method-level security  
✅ **Scalable Architecture** - Microservices with service discovery and load balancing

---

## 🏗️ **System Architecture**

### **Microservices Overview**
| Service | Port | Database | Purpose | Key Features |
|---------|------|----------|---------|-------------|
| **Discovery Server** | 8761 | None | Service Registry | Eureka service discovery |
| **API Gateway** | 8080 | None | Traffic Router | Spring Cloud Gateway, Load balancing |
| **Auth Service** | 8081 | PostgreSQL | Authentication | JWT, Role-based auth, User management |
| **Profile Service** | 8082 | MongoDB | User Profiles | Customer & provider profiles |
| **Booking Service** | 8083 | MongoDB | Appointments | Real-time booking, WebSocket notifications |
| **Products Service** | 8084 | PostgreSQL | Healthcare Services | Service catalog, provider offerings |
| **Notification Service** | 8085 | None | Messaging | Email/SMS notifications |
| **Swagger Aggregator** | 8090 | None | API Documentation | Unified API docs |

### **Database Design**
- **PostgreSQL Database**: `health-care-parent` (shared by Auth and Products services)
- **MongoDB Collections**: Separate databases for profiles, bookings
- **Liquibase Integration**: Automated database migrations and schema management

---

## 👤 **Role-Based System**

The platform supports **3 distinct user roles** with specialized APIs:

### **🛒 CUSTOMER APIs** (`/v1/customer/*`)
- **Dashboard**: Personal health overview, appointment summaries  
- **Provider Discovery**: Search healthcare providers by specialization, location, ratings  
- **Appointment Management**: Book, cancel, reschedule appointments  
- **Profile Management**: Personal information, medical history

### **🏥 PROVIDER APIs** (`/v1/provider/*`)
- **Provider Dashboard**: Revenue analytics, appointment metrics, ratings overview  
- **Appointment Management**: View, update appointment status  
- **Availability Management**: Set working hours, available time slots  
- **Profile Management**: Professional credentials, services offered  
- **Document Management**: Upload and manage professional documents

### **⚙️ ADMIN APIs** (`/v1/admin/*`)
- **Platform Analytics**: User growth, revenue trends, system performance  
- **User Management**: Manage all platform users, update statuses  
- **Provider Verification**: Approve/reject healthcare provider applications  
- **System Configuration**: Platform settings, maintenance mode

---

## 🚀 **Quick Start Guide**

### **Prerequisites**
- Java 21+
- Maven 3.8+
- Docker & Docker Compose
- Git

### **1. Clone and Setup**
```bash
git clone <your-repo-url>
cd health-care-parent

# Verify Java version
java --version

# Verify Docker
docker --version && docker-compose --version
```

### **2. Build All Services**
```bash
# Build all microservices and Docker images
./script/build-all.sh

# Build without tests (faster for development)
./script/build-all.sh 1.0-SNAPSHOT true
```

### **3. Start the Platform**
```bash
# Step 1: Start infrastructure services first
docker-compose -f docker-compose-infra.yml up -d

# Step 2: Wait for infrastructure to be healthy (30-60 seconds)
docker-compose -f docker-compose-infra.yml ps

# Step 3: Start application services
docker-compose up -d

# Check service health
./script/check-services.sh
```

### **4. Access the Platform**
- **API Gateway**: http://localhost:8080
- **Unified Swagger Documentation**: http://localhost:8090/swagger-ui.html
- **Service Discovery**: http://localhost:8761
- **Email Testing (MailDev)**: http://localhost:1080
- **Grafana Dashboards**: http://localhost:3000 (admin/admin)
- **Prometheus Metrics**: http://localhost:9090
- **Individual Services**: http://localhost:808X (where X is 1-5)

---

## 🔧 **Development Workflow**

### **Local Development**
```bash
# Build all services
./script/build-all.sh

# Start infrastructure first
docker-compose -f docker-compose-infra.yml up -d

# Start application services
docker-compose up -d

# View application logs
docker-compose logs -f

# View specific service logs
docker-compose logs -f auth-service

# Stop all services
docker-compose down
docker-compose -f docker-compose-infra.yml down
```

### **Testing**
```bash
# Run all tests
mvn clean test

# Run tests for specific service
cd auth-service && mvn test

# Build without running tests
mvn clean package -DskipTests
```

### **Database Operations**
```bash
# Access PostgreSQL database
docker exec -it healthcare-postgres psql -U postgres -d health-care-parent

# Access MongoDB
docker exec -it healthcare-mongodb mongosh

# Check Liquibase migrations
docker logs healthcare-auth | grep -i liquibase
```

---

## 🐳 **Docker Deployment**

### **Development Environment**
```bash
# Quick start - infrastructure first
docker-compose -f docker-compose-infra.yml up -d

# Then start application services
docker-compose up -d

# Check status
docker-compose ps
docker-compose -f docker-compose-infra.yml ps

# Clean restart
docker-compose down
docker-compose -f docker-compose-infra.yml down
docker-compose -f docker-compose-infra.yml up -d
docker-compose up -d
```

### **Production Environment**
```bash
# Setup production environment
cp .env.example .env.prod
# Edit .env.prod with secure values

# Deploy to production
./script/deploy-docker.sh prod up

# Monitor production services
./script/deploy-docker.sh prod status
```

### **Container Management**
```bash
# Build all Docker images
docker-compose build

# Clean up Docker resources
docker system prune -f
docker volume prune -f

# View service logs in real-time
docker-compose logs -f

# View infrastructure logs
docker-compose -f docker-compose-infra.yml logs -f
```

---

## 📚 **API Documentation**

### **Swagger Documentation**
```bash
# Generate unified API documentation
./script/generate-unified-swagger.sh

# Test swagger endpoints
./script/test-swagger-script.sh
```

### **Access API Documentation**
- **Unified Documentation**: http://localhost:8090/swagger-ui.html
- **Individual Services**: http://localhost:<port>/swagger-ui.html
- **OpenAPI JSON**: http://localhost:<port>/v3/api-docs

### **Code Generation**
```bash
# Generate TypeScript client for Angular/React
npx @openapitools/openapi-generator-cli generate \
  -i http://localhost:8090/v3/api-docs \
  -g typescript-angular \
  -o ./src/app/api-client

# Generate Java client
npx @openapitools/openapi-generator-cli generate \
  -i http://localhost:8090/v3/api-docs \
  -g java \
  -o ./java-client
```

---

## 🔐 **Security Features**

### **Authentication & Authorization**
- **JWT Token-based authentication** with role-based claims
- **Spring Security integration** with method-level security (@PreAuthorize)
- **Role-based access control** (RBAC) for all endpoints
- **BCrypt password encryption** with secure password policies

### **Security Best Practices**
- **Non-root containers** - All Docker containers run as non-root users
- **Health checks** - Comprehensive service monitoring
- **Resource limits** - Container resource constraints
- **Network isolation** - Custom Docker networks

---

## 🧪 **Testing & Quality Assurance**

### **Automated Testing**
```bash
# Run unit tests
mvn test

# Run integration tests
mvn verify

# Run tests with coverage
mvn test jacoco:report

# Build test report
mvn surefire-report:report
```

### **Health Monitoring**
```bash
# Check all services
./script/check-services.sh

# Individual service health
curl http://localhost:8081/actuator/health

# Service metrics
curl http://localhost:8081/actuator/metrics
```

### **Database Testing**
```bash
# Test database connectivity
docker exec healthcare-postgres psql -U postgres -d health-care-parent -c "SELECT COUNT(*) FROM user_tbl;"

# Verify Liquibase tables
docker exec healthcare-postgres psql -U postgres -d health-care-parent -c "\\dt"
```

---

## 🔄 **CI/CD Integration**

### **GitHub Actions Workflow**
The platform includes a complete CI/CD pipeline:

- **Automated Testing** with PostgreSQL and MongoDB test containers
- **Multi-service Docker Builds** with efficient caching
- **Security Scanning** with vulnerability detection
- **Automated Deployment** with staging and production environments

### **Pipeline Triggers**
```bash
# Trigger full pipeline (main branch)
git push origin main

# Trigger testing only (feature branches)  
git push origin feature/new-feature

# Manual deployment
gh workflow run deploy-production
```

---

## 🛠️ **Technology Stack**

### **Backend Technologies**
- **Java 21** - Latest LTS version with modern features
- **Spring Boot 3.2.0** - Latest Spring Boot with native support
- **Spring Cloud 2023** - Gateway, Netflix Eureka, Config
- **Spring Security 6** - JWT authentication, RBAC
- **Spring Data JPA & MongoDB** - Multi-database support
- **Liquibase** - Database version control and migrations
- **WebSocket + STOMP** - Real-time communication

### **Infrastructure & DevOps**
- **Docker & Docker Compose** - Containerization and orchestration
- **PostgreSQL 15** - Reliable relational database
- **MongoDB 7** - Modern document database
- **GitHub Actions** - CI/CD automation
- **SpringDoc OpenAPI 3** - API documentation

### **Monitoring & Observability**
- **Spring Boot Actuator** - Health checks and metrics
- **Micrometer** - Application metrics
- **Structured Logging** - JSON-formatted logs
- **Health Check Endpoints** - Service monitoring

---

## 🔧 **Available Scripts**

| Script | Purpose | Usage |
|--------|---------|--------|
| `./script/build-all.sh` | Build all services and Docker images | `./script/build-all.sh [version] [skip-tests]` |
| `./script/deploy-docker.sh` | Deploy and manage Docker services | `./script/deploy-docker.sh [env] [operation]` |
| `./script/check-services.sh` | Health check all services | `./script/check-services.sh` |
| `./script/generate-unified-swagger.sh` | Generate API documentation | `./script/generate-unified-swagger.sh` |
| `./script/test-swagger-script.sh` | Test Swagger generation | `./script/test-swagger-script.sh` |
| `./script/generate-dockerfiles.sh` | Generate Dockerfiles | `./script/generate-dockerfiles.sh` |

---

## 🚀 **API Usage Examples**

### **1. Register a New User**
```bash
curl -X POST http://localhost:8081/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "provider@clinic.com",
    "password": "SecurePass123!",
    "confirmPassword": "SecurePass123!",
    "role": "PROVIDER"
  }'
```

### **2. User Login**
```bash
curl -X POST http://localhost:8081/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "provider@clinic.com", 
    "password": "SecurePass123!"
  }'
```

### **3. Access Protected Endpoint**
```bash
# Use JWT token from login response
curl -X GET http://localhost:8081/v1/provider/dashboard \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### **4. Healthcare Service Search**
```bash
curl -X GET "http://localhost:8084/v1/healthcare-services?category=cardiology&location=NYC" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

---

## 🗂️ **Project Structure**

```
health-care-parent/
├── api-gateway/              # Spring Cloud Gateway
├── auth-service/             # Authentication & Authorization
├── booking-service/          # Appointment Management
├── commons/                  # Shared Libraries
├── discovery-server/         # Eureka Service Registry
├── notification-service/     # Email/SMS Notifications
├── products/                 # Healthcare Services Catalog
├── profile-service/          # User Profile Management
├── swagger-aggregator/       # API Documentation Aggregation
├── script/                   # Deployment & Build Scripts
├── init-scripts/             # Database Initialization
├── docker-compose.yml        # Application Services
├── docker-compose-infra.yml  # Infrastructure Services (DB, Kafka, etc.)
├── docker-compose.dev.yml    # Development Environment
├── docker-compose.prod.yml   # Production Environment
├── .env.example             # Environment Variables Template
└── README.md                # This File
```

---

## 🔍 **Troubleshooting**

### **Common Issues**

**Services won't start:**
```bash
# Check Docker resources
docker system df
docker system prune -f

# Rebuild images
./script/deploy-docker.sh dev build
./script/deploy-docker.sh dev up
```

**Database connection issues:**
```bash
# Check database status
docker exec healthcare-postgres pg_isready -U postgres

# Reset database
docker-compose -f docker-compose-infra.yml down -v
docker-compose -f docker-compose-infra.yml up postgres -d
```

**Port conflicts:**
```bash
# Check what's using ports
lsof -i :8080
lsof -i :8081

# Kill conflicting processes
kill -9 <PID>
```

### **Health Check Commands**
```bash
# Full system health check
./script/check-services.sh

# Individual service health
curl http://localhost:8081/actuator/health

# Database connectivity test
docker exec healthcare-postgres psql -U postgres -c "SELECT 1;"
```

---

## 🤝 **Contributing**

1. **Fork the repository**
2. **Create feature branch**: `git checkout -b feature/amazing-feature`
3. **Build and test**: `./script/build-all.sh && mvn test`
4. **Commit changes**: `git commit -m 'Add amazing feature'`
5. **Push to branch**: `git push origin feature/amazing-feature`
6. **Create Pull Request**

### **Development Guidelines**
- Follow Spring Boot best practices
- Add comprehensive tests for new features
- Update Swagger documentation for API changes
- Ensure Docker images build successfully
- Test with all three user roles (Customer, Provider, Admin)

---

## 📄 **License**

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 📞 **Support & Contact**

- **Issues**: [GitHub Issues](https://github.com/your-repo/health-care-parent/issues)
- **Documentation**: Complete API documentation at `/swagger-ui.html`
- **Health Monitoring**: Use `./script/check-services.sh` for system status

---

*Built with ❤️ using Spring Boot, Docker, and modern microservices architecture*