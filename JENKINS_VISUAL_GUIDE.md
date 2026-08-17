# Jenkins Execution Flow - Visual Guide

## 🔄 Complete Test Execution Workflow

```
┌─────────────────────────────────────────────────────────────────┐
│                    JENKINS WORKFLOW                             │
└─────────────────────────────────────────────────────────────────┘

1. TRIGGER
   ┌─────────────────────────┐
   │ Manual Trigger          │  (You click "Build Now")
   │ Or Scheduled            │  (Runs at specific time)
   │ Or GitHub Webhook       │  (Auto on code push)
   └────────┬────────────────┘
            │
            ▼
2. CHECKOUT CODE
   ┌─────────────────────────┐
   │ Git Repository          │
   │ - Jenkinsfile           │
   │ - Source Code           │
   │ - Test Resources        │
   │ - pom.xml               │
   └────────┬────────────────┘
            │
            ▼
3. BUILD PROJECT
   ┌─────────────────────────┐
   │ Maven Compile           │
   │ mvn clean compile       │
   │ -DskipTests             │
   └────────┬────────────────┘
            │
            ▼
4. RUN TESTS
   ┌─────────────────────────┐
   │ TestNG Execution        │
   │ mvn test                │
   │ -Dsuite=login           │
   │ -Dbrowser=chrome        │
   └────────┬────────────────┘
            │
            ▼
5. GENERATE REPORTS
   ┌─────────────────────────┐
   │ Extent Report (HTML)    │
   │ TestNG Report (XML)     │
   │ Screenshots (PNG)       │
   │ Build Logs (TXT)        │
   └────────┬────────────────┘
            │
            ▼
6. PUBLISH RESULTS
   ┌─────────────────────────┐
   │ Archive Artifacts       │
   │ Publish HTML Reports    │
   │ Send Notifications      │
   │ Update Dashboard        │
   └────────┬────────────────┘
            │
            ▼
7. COMPLETE
   ┌─────────────────────────┐
   │ ✓ Build Successful      │
   │ View Reports            │
   │ Review Logs             │
   └─────────────────────────┘
```

---

## 📋 Step-by-Step Execution Details

### 1️⃣ STAGE: Checkout
```
Jenkins pulls latest code from Git repository
  ├── Jenkinsfile (pipeline configuration)
  ├── pom.xml (Maven configuration)
  ├── src/test/java/ (test code)
  ├── src/test/resources/ (test resources)
  └── Logs & Reports folders
  
Output: ✓ Code checked out successfully
Time: ~5 seconds
```

### 2️⃣ STAGE: Environment Check (Advanced Pipeline Only)
```
Jenkins verifies system requirements
  ├── Java version check
  ├── Maven version check
  ├── Suite parameter display
  └── Browser parameter display
  
Output: ✓ Environment verified
Time: ~3 seconds
```

### 3️⃣ STAGE: Clean & Build
```
Maven compiles the project WITHOUT running tests
  ├── Clean previous builds: mvn clean
  ├── Compile source: mvn compile
  ├── Compile test code: mvn test-compile
  └── Copy test resources
  
Output: ✓ Build completed successfully
Time: ~15-20 seconds
```

### 4️⃣ STAGE: Run Tests
```
TestNG framework executes test cases
  ├── Read suite file: src/test/resources/suites/login.xml
  ├── Initialize WebDriver (Chrome/Firefox/Edge)
  ├── Launch application URL
  ├── Execute each test method:
  │   ├── @Test verifyLoginTest()
  │   │   ├── Launch URL
  │   │   ├── Enter username
  │   │   ├── Enter password
  │   │   ├── Click login
  │   │   ├── Verify dashboard
  │   │   ├── Click logout
  │   │   └── Verify login page
  │   └── @Test verifyLoginTest1(data)
  ├── Generate Extent Report
  ├── Capture screenshots
  └── Generate TestNG Report
  
Output: ✓ Tests executed successfully (or ⚠ Some failed)
Time: ~60-90 seconds (depends on test count)
```

### 5️⃣ STAGE: Generate Reports
```
Create various report formats
  ├── Extent HTML Report: reports/*.html
  ├── TestNG XML Report: target/surefire-reports/*.xml
  ├── Screenshots: reports/screenshots/*.png
  └── Logs: logs/*.log
  
Output: ✓ Reports generated
Time: ~5 seconds
```

### 6️⃣ STAGE: Publish Results
```
Jenkins publishes artifacts and results
  ├── Archive test reports to Jenkins
  ├── Publish HTML reports in Jenkins UI
  ├── Parse TestNG XML results
  ├── Upload screenshots
  ├── Archive logs
  └── Send email notification (if configured)
  
Output: ✓ Reports archived
Time: ~10 seconds
```

---

## 🎯 Test Execution Timeline

```
Start
  │
  ├─ [~5s]   Checkout
  ├─ [~3s]   Environment Check
  ├─ [~20s]  Build (compile)
  ├─ [~75s]  Run Tests
  │           ├─ Initialize browser
  │           ├─ Test 1: verifyLoginTest
  │           ├─ Test 2: verifyLoginTest1
  │           └─ Generate reports
  ├─ [~5s]   Generate Reports
  ├─ [~10s]  Publish Results
  │
End (~120 seconds = ~2 minutes total)
```

---

## 📊 Reports Generated

### After Each Build, You Get:

```
Jenkins Dashboard → Build #X
│
├─ CONSOLE OUTPUT
│  └─ Complete Maven & test execution logs
│
├─ TEST RESULT
│  └─ Summary: X passed, Y failed, Z skipped
│
├─ EXTENT TEST REPORT (HTML)
│  ├─ Overview & Summary
│  ├─ Each test with steps
│  ├─ Screenshots for failed tests
│  ├─ Logs for each test
│  └─ Timeline & performance metrics
│
├─ SCREENSHOTS
│  └─ Only captured on test failure
│
└─ BUILD ARTIFACTS
   ├─ logs/selenium-automation.log
   ├─ target/surefire-reports/*.xml
   └─ reports/*.html
```

---

## 🔍 Example Test Execution Output

```
[INFO] -----------------------------------------------
[INFO]  T E S T S
[INFO] -----------------------------------------------
[INFO] Running tests.LoginTest
[INFO] Starting ChromeDriver
[INFO] Navigating to: https://hrm.example.com
[INFO] Entering username: admin
[INFO] Entering password: ****
[INFO] Clicking login button
[INFO] Verifying dashboard
[INFO] Dashboard displayed successfully ✓
[INFO] Clicking logout
[INFO] Logout successful ✓
[INFO] Test completed
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0

[INFO] -----------------------------------------------
[INFO] Total time: 1:45 minutes
[INFO] Finished at: 2026-08-17T14:45:30+05:30
[INFO] -----------------------------------------------
[INFO] BUILD SUCCESS
```

---

## 📈 Build Status Indicators

### Success Build (Green)
```
✓ Build #125
├─ Status: SUCCESS
├─ Duration: 1 min 45 sec
├─ Tests: 2 passed
├─ Reports: Available
└─ Action: View Latest Report
```

### Failed Build (Red)
```
✗ Build #124
├─ Status: FAILURE
├─ Duration: 45 sec (failed during compilation)
├─ Tests: Not run
├─ Error: See Console Output
└─ Action: Fix and retry
```

### Unstable Build (Yellow)
```
⚠ Build #123
├─ Status: UNSTABLE
├─ Duration: 2 min 30 sec
├─ Tests: 1 failed, 1 passed
├─ Reports: Available
└─ Action: Review Failed Tests
```

---

## 🎬 Real-World Example

### Your First Jenkins Run

```
1. Click "Build Now" on Jenkins job
   
   Console Output shows:
   ════════════════════════════════════════
   Started by user
   Running as SYSTEM
   Building in workspace /var/jenkins_home/workspace/Selenium-Tests
   ════════════════════════════════════════
   
2. Checkout Stage (5s)
   [INFO] Checking out code from repository...
   ✓ Code checked out successfully
   
3. Build Stage (20s)
   [INFO] Compiling 5 source files with javac
   [INFO] Building jar: target/pageobjectmodel-0.0.1-SNAPSHOT.jar
   ✓ Build completed successfully
   
4. Test Stage (75s)
   [INFO] Running tests.LoginTest
   [INFO] Starting test: verifyLoginTest
   [INFO] Launch ChromeDriver
   [INFO] Navigating to application...
   [INFO] Enter username and password
   [INFO] Clicked on Login button
   [INFO] Dashboard displayed ✓
   [INFO] Logout successful ✓
   [INFO] Test completed successfully
   
5. Report Stage (5s)
   ✓ Reports generated
   
6. Publish Stage (10s)
   ✓ Reports archived and published
   
   FINAL STATUS: SUCCESS
   Total time: 2 minutes 5 seconds
   
   Available Reports:
   • Extent Report
   • Test Results
   • Console Output
```

---

## 🔗 Jenkins UI Navigation

```
Jenkins Dashboard
│
├─ YOUR JOB: "Selenium-Tests"
│  │
│  ├─ Build History (left sidebar)
│  │  └─ Build #125 → Click to view
│  │
│  ├─ Build #125
│  │  ├─ Console Output → Full logs
│  │  ├─ Test Result → Pass/Fail summary
│  │  ├─ Extent Test Report → Detailed HTML
│  │  ├─ Screenshots → Failed test images
│  │  └─ Artifacts → All build files
│  │
│  └─ Configure Job
│     ├─ Pipeline → Script configuration
│     ├─ Build Triggers → Schedule
│     ├─ Build Parameters → Custom inputs
│     └─ Post-build Actions → Notifications
```

---

## ⚡ Performance Tips

### Faster Test Execution
```
1. Use parallel execution
   parallel="tests" thread-count="3"
   
2. Use headless browser
   -Dheadless=true
   
3. Skip unnecessary steps
   -DskipTests
   
4. Use cloud-based browsers
   -Dcloud=true
```

### Faster Jenkins Builds
```
1. Use agent with SSD
2. Configure Maven caching
3. Use build cache
4. Skip artifact archival for large files
```

---

## 🛡️ Error Handling

### Test Fails
```
Build Status: UNSTABLE (Yellow)

Steps to Debug:
1. Click "Console Output"
2. Find [ERROR] message
3. Check "Extent Test Report" for screenshots
4. Review test code and logs
5. Fix issue and commit
6. Jenkins rebuilds automatically
```

### Build Fails
```
Build Status: FAILURE (Red)

Common Causes:
1. Compilation error → Check Console Output
2. Missing dependency → Check pom.xml
3. Wrong Java version → Check Jenkins tools
4. Git connection issue → Check credentials

Fix and rebuild:
Click "Build Now" again
```

---

## 🎓 Understanding Reports

### Extent Report
```
Shows:
• Test name and status
• Timestamp
• Duration
• Steps executed with logs
• Screenshots on failure
• Performance metrics
```

### TestNG Report
```
Shows:
• Test class and method name
• Pass/Fail status
• Execution time
• Stack trace on failure
• Grouped by class/suite
```

### Console Output
```
Shows:
• Maven compilation output
• Test execution logs
• Test framework messages
• Build success/failure status
• Full error messages
```

---

## 📞 What to Do When...

### Test Passes ✓
```
Congratulations! 🎉
→ Build status: SUCCESS
→ All reports generated
→ Next step: Review report for coverage
```

### Test Fails ✗
```
→ Build status: UNSTABLE
→ Check "Extent Report" for failure reason
→ View screenshot of failure
→ Fix test code or application code
→ Commit and push
→ Jenkins rebuilds automatically
```

### Build Doesn't Start
```
→ Check Jenkins job configuration
→ Verify repository URL
→ Check Jenkins credentials
→ Review console output for errors
→ Check agent has Java and Maven
```

### Reports Not Generated
```
→ Check if tests actually ran
→ Verify HTML Publisher plugin installed
→ Check Jenkins workspace permissions
→ Review post-build actions in Jenkinsfile
```

---

**Status: ✅ Complete and Ready for Use**

All files are in place. Time to create your first Jenkins job! 🚀
