# 🎯 End-to-End DevOps & Java Backend Interview Preparation Guide

This guide summarizes all architectural decisions, code quality enhancements, CI/CD pipeline troubleshooting, and scenario-based questions encountered while building and deploying the **VendorService** application.

---

## 📌 1. Project & CI/CD Architecture Overview

```
 [Developer] 
      │ (git push develop)
      ▼
 [GitHub Repository] ─── (Webhook / SCM Polling) ───► [Jenkins Pipeline (Port 9090)]
                                                               │
    ┌───────────────────────┬────────────────────────┬─────────┴─────────┐
    ▼                       ▼                        ▼                   ▼
 [1. SCM Checkout]     [2. Maven Build]     [3. JUnit 5 Tests]    [4. SonarQube (Port 9000)]
   - Branch: develop     - Clean package      - 12/12 Tests Pass    - Static Analysis
   - github-cred         - -DskipTests        - ~100% Coverage      - sonar-token
                                                                         │
                                                                         ▼
                                                                [5. Apache Tomcat (Port 8081)]
                                                                   - Deploy vendor.war
                                                                   - tomcat-cred (Manager API)
```

---

## 🛠️ 2. Key Code Quality & Refactoring Changes (SonarQube Fixes)

During static analysis, SonarQube flagged issues across Reliability, Security, and Maintainability. Here is what was refactored and **why it matters in technical interviews**:

### 🔹 A. Field Injection vs. Constructor Injection
- **Issue:** `@Autowired private VendorService vendorService;`
- **Refactoring:** Replaced with constructor-based dependency injection and `final` fields:
  ```java
  private final VendorService vendorService;

  public VendorController(VendorService vendorService) {
      this.vendorService = vendorService;
  }
  ```
- **Interview Question:** *Why is Constructor Injection preferred over Field Injection in Spring?*
  - **Immutability:** Dependencies can be marked `final`.
  - **Testability:** Classes can be instantiated in plain unit tests without starting a full Spring container or using reflection.
  - **Prevents NullPointerExceptions:** The object cannot be created in an incomplete/invalid state.
  - **Detects Circular Dependencies:** Spring throws `BeanCurrentlyInCreationException` at startup rather than failing silently at runtime.

---

### 🔹 B. Programming to Interfaces (Dependency Inversion Principle)
- **Issue:** `VendorController` was originally injected with concrete class `VendorServiceImpl` instead of interface `VendorService`.
- **Why it failed in Unit Tests:** `@WebMvcTest` created a `@MockBean private VendorService vendorServiceMock;`. Because Spring's dynamic mock proxy implements the interface and not the concrete class, Spring failed to inject the mock bean.
- **Refactoring:** Changed injection type to interface `VendorService`.
- **Takeaway:** Always code to interfaces to support loose coupling, mocking in test environments, and adhering to SOLID (Dependency Inversion).

---

### 🔹 C. Boosting Unit Test Coverage to ~100%
- **Issue:** Code coverage was originally 55.6% because controller endpoints, bean/entity constructors, and empty branch lists were untested.
- **Refactoring:**
  - Added WebMvc mock tests in `VendorControllerTest` for `GET /vendor/controller/getVendors`.
  - Added comprehensive constructor, getter, setter, and `toString` tests in `VendorBeanAndEntityTest`.
  - Added branch coverage tests in `VendorServiceTest` for empty lists.
- **Takeaway:** Writing focused unit tests for edge cases and DTOs ensures high confidence during refactoring and satisfies strict Quality Gates.

---

### 🔹 D. Managing False Positives & Tooling Code with `sonar.exclusions`
- **Issue:** Maven wrapper helper files (`.mvn/wrapper/MavenWrapperDownloader.java`) flagged 17 legacy wrapper issues.
- **Refactoring:** Added `<sonar.exclusions>.mvn/**</sonar.exclusions>` in `pom.xml`.
- **Takeaway:** Distinguish application source code from third-party/generated scaffolding to keep quality reports actionable.

---

### 🔹 E. Web Accessibility & HTML Standards
- **Issue:** `index.html` lacked `lang="en"` and `<img alt="...">`.
- **Refactoring:** Added required attributes adhering to WCAG and Sonar Web standards.

---

## 💼 3. Real-World Scenario-Based Interview Questions

### 🟢 Scenario 1: SonarQube Quality Gate Fails in CI/CD Pipeline
**Question:** *If SonarQube Quality Gate fails during a Jenkins pipeline execution, what steps do you take?*
- **Answer:**
  1. Inspect the Jenkins stage log to check which metric breached the threshold (e.g., code coverage dropped below target, new critical bugs, security hotspots, or duplication).
  2. Open the SonarQube dashboard for the specific project (`http://localhost:9000/dashboard?id=vendorservice`).
  3. Filter by **New Code** issues (bugs, vulnerabilities, code smells).
  4. Fix root causes locally (e.g., add missing unit tests for uncovered branches, resolve null pointer risks, eliminate duplicate blocks).
  5. Run `mvn clean test sonar:sonar` locally to verify before pushing to the remote branch.

---

### 🟢 Scenario 2: Sensitive Credentials in Declarative Pipelines
**Question:** *How do you securely handle API tokens and credentials in Jenkins declarative pipelines?*
- **Answer:**
  - Never hardcode credentials in `Jenkinsfile` or Git commits.
  - Store tokens and passwords in **Jenkins Credentials Store** (Global or Folder level).
  - Bind credentials dynamically inside pipeline stages using `withCredentials`:
    ```groovy
    withCredentials([string(credentialsId: 'sonar-token', variable: 'SONAR_TOKEN')]) {
        sh 'mvn sonar:sonar -Dsonar.token=${SONAR_TOKEN} ...'
    }
    ```
  - Use shell variable interpolation (`${SONAR_TOKEN}`) rather than Groovy string interpolation to prevent credential exposure in pipeline logs.

---

### 🟢 Scenario 3: Deploying WAR Application to External Tomcat via CI/CD
**Question:** *Explain how your Jenkins pipeline deploys the packaged artifact to Apache Tomcat.*
- **Answer:**
  - Build stage packages the Spring Boot WAR (`mvn clean package -DskipTests`).
  - Tomcat is configured with a user having `manager-script` role in `tomcat-users.xml`.
  - In Jenkins, the `Deploy to Tomcat` stage uses `curl` with PUT/POST request to Tomcat Manager text API:
    ```bash
    curl -v -u "${TC_USER}:${TC_PASS}" \
      -T "target/vendor.war" \
      "http://localhost:8081/manager/text/deploy?path=/vendor&update=true"
    ```
  - `update=true` ensures existing deployments are cleanly undeployed and replaced without needing a manual server restart.

---

### 🟢 Scenario 4: Spring Boot Test Execution in CI Environment Without External DB
**Question:** *Your Spring Boot tests fail in CI with Database Connection Refused. How do you design tests to avoid this?*
- **Answer:**
  - **Unit Testing (Mocking):** Use `@ExtendWith(MockitoExtension.class)` or `@WebMvcTest` + `@MockBean` for controller tests so no database connection is attempted.
  - **Integration Testing:** Use an in-memory database (such as **H2**) via a dedicated `application-test.properties` with `@ActiveProfiles("test")`, or utilize **Testcontainers** to spin up lightweight Docker MySQL containers dynamically during the test phase.

---

## 📊 4. Metrics & Achievements Summary

| Metric | Initial State | Final State |
| :--- | :--- | :--- |
| **Pipeline Build Status** | Failed | **All 5 Stages Passed (Green)** |
| **Reliability Rating** | C (3 Open Issues) | **A (0 Issues)** |
| **Security Rating** | A | **A (0 Issues)** |
| **Maintainability Rating** | A (10 Open Issues) | **A (0 Issues)** |
| **Code Coverage** | 55.6% | **~98% - 100%** |
| **Unit Test Pass Count** | 7 Passing / 1 Failing | **12 / 12 Passing (100%)** |
| **Live Deployed URL** | N/A | `http://localhost:8081/vendor/` |

---
*Created for interview preparation and codebase onboarding.*
