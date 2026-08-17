# Jenkins Setup Guide for Selenium Automation Framework

## Prerequisites
- Jenkins server installed and running
- Java 21 or higher installed on Jenkins agent
- Maven installed on Jenkins agent
- Git installed on Jenkins agent
- WebDriver installed (for Selenium tests)

## Step 1: Create a New Jenkins Job

1. **Open Jenkins Dashboard** → Click "New Item"
2. **Enter Job Name**: `Selenium-Automation-Tests`
3. **Select Job Type**: `Pipeline`
4. **Click OK**

## Step 2: Configure Pipeline

### Option A: Pipeline from SCM (Recommended)
1. Go to **Pipeline** section
2. **Definition**: Select "Pipeline script from SCM"
3. **SCM**: Select "Git"
4. **Repository URL**: Enter your GitHub/GitLab repository URL
5. **Branch Specifier**: `*/main` or `*/master`
6. **Script Path**: `Jenkinsfile` (this is the file created in the project root)
7. **Save**

### Option B: Direct Pipeline Script
1. Go to **Pipeline** section
2. **Definition**: Select "Pipeline script"
3. Copy and paste the Jenkinsfile content
4. **Save**

## Step 3: Configure Build Triggers (Optional)

Choose how frequently you want tests to run:

### Poll SCM
- **Schedule**: `H * * * *` (runs every hour)
- Alternative: `H H * * *` (runs once a day)
- Alternative: `H H * * 1-5` (runs weekdays only)

### GitHub Webhook (if using GitHub)
1. Go to your GitHub repository → Settings → Webhooks
2. Add webhook with Jenkins payload URL: `http://jenkins-server/github-webhook/`
3. Set trigger on "Push events"

## Step 4: Configure Build Parameters (Optional)

Add parameters for different test suites:

1. Go to Job Configuration → General
2. **Check**: "This project is parameterized"
3. **Add Parameter** → **String Parameter**:
   - **Name**: `suite`
   - **Default Value**: `login`
   - **Description**: `Test suite to run (login or user)`

Then in your Jenkinsfile, run: `mvn test -Dsuite=${suite}`

## Step 5: Install Required Jenkins Plugins

1. Go to **Manage Jenkins** → **Manage Plugins**
2. Install the following plugins:
   - **Pipeline**: Pipeline
   - **Git**: Git plugin
   - **JUnit Plugin**: For test results
   - **HTML Publisher Plugin**: For Extent Reports
   - **Email Extension Plugin**: For email notifications
   - **Log Parser Plugin**: For log analysis

## Step 6: Configure Email Notifications (Optional)

1. Go to **Manage Jenkins** → **Configure System**
2. Find **Email Notification** section
3. Configure SMTP settings
4. Save

## Step 7: Run the Job

1. **Click "Build Now"** on the job dashboard
2. **Monitor build progress** in "Build History"
3. **View Console Output** for detailed logs
4. **Check Reports**:
   - Test Results (JUnit)
   - Extent Reports
   - Screenshots
   - Build Logs

## Test Reports Location

After build completion, Jenkins will archive:
- **TestNG Reports**: `target/surefire-reports/`
- **Extent Reports**: `reports/*.html`
- **Screenshots**: `reports/screenshots/`
- **Logs**: `logs/`

## Troubleshooting

### Issue: "mvn command not found"
**Solution**: Ensure Maven is installed and added to PATH on Jenkins agent, or use Maven plugin in Jenkins

### Issue: "Chrome/Firefox driver not found"
**Solution**: Install WebDriver on Jenkins agent or use headless mode in your test configuration

### Issue: Tests timing out
**Solution**: Increase timeout in Jenkinsfile or adjust WebDriver wait times

### Issue: Reports not generated
**Solution**: Check if test execution failed - fix test errors and re-run

## Advanced Configuration

### Running Tests in Docker Container
Modify Jenkinsfile to use Docker agent:
```groovy
agent {
    docker {
        image 'maven:3.9-eclipse-temurin-21'
        args '-v /root/.m2:/root/.m2'
    }
}
```

### Parallel Execution
Run multiple test suites in parallel:
```groovy
parallel {
    stage('Login Tests') {
        steps {
            sh 'mvn test -Dsuite=login'
        }
    }
    stage('User Tests') {
        steps {
            sh 'mvn test -Dsuite=user'
        }
    }
}
```

### Scheduled Nightly Builds
Add to Jenkinsfile:
```groovy
triggers {
    cron('H 2 * * *')  // Run at 2 AM daily
}
```

## Commands Reference

### Build and Test
```bash
mvn clean test
```

### Run Specific Suite
```bash
mvn test -Dsuite=login
```

### Skip Tests During Build
```bash
mvn clean install -DskipTests
```

### Run with Specific Log Level
```bash
mvn test -X  # Debug mode
```

## Cleanup Configuration

To prevent disk space issues from old builds:
1. Job Configuration → **Discard old builds**
2. Set **Max # of builds to keep**: 10
3. Set **Max # of build logs to keep**: 30
4. Save

---

**For more information**, refer to:
- [Jenkins Pipeline Documentation](https://www.jenkins.io/doc/book/pipeline/)
- [Maven Plugin for Jenkins](https://plugins.jenkins.io/maven-plugin/)
- [TestNG with Jenkins](https://testng.org/doc/documentation-main.html)
