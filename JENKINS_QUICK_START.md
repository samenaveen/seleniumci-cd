# Quick Start: Execute Tests Through Jenkins

## Prerequisites
✓ Jenkins server running (local or remote)
✓ Maven installed on Jenkins agent
✓ Java 21+ installed on Jenkins agent
✓ Project repository pushed to Git (GitHub, GitLab, or Bitbucket)

## Quick Setup (5 Minutes)

### 1. Install Jenkins Plugins
```
Manage Jenkins → Manage Plugins → Search and install:
- Pipeline
- Git
- HTML Publisher
- JUnit
```

### 2. Create Jenkins Job
```
New Item → Enter Name: "Selenium-Tests" → Select "Pipeline" → OK
```

### 3. Configure Pipeline
```
Pipeline Section → Definition: "Pipeline script from SCM"
SCM: Git
Repository URL: <Your Git Repo URL>
Branch: */main
Script Path: Jenkinsfile
```

### 4. Click "Build Now"

## Build Status & Results

After the build completes, you'll see:

| Item | Location |
|------|----------|
| Test Results | Build → Test Result |
| Extent Report | Build → Extent Report |
| Screenshots | Build → Screenshots |
| Build Log | Build → Console Output |

## Commands in Jenkins (via Jenkinsfile)

### Run All Tests
```groovy
sh 'mvn clean test'
```

### Run Specific Suite
```groovy
sh 'mvn test -Dsuite=login'
```

### Run with Parameters
```groovy
sh 'mvn test -Dsuite=${suite}'
```

### Run with Custom Browser
```groovy
sh 'mvn test -Dbrowser=firefox'
```

## Viewing Test Reports

1. **After Build Completes** → Click on build number
2. **Click on Report Links**:
   - `Test Result` - JUnit summary
   - `Extent Report` - Detailed HTML report
   - `Screenshots` - Failed test screenshots
   - `Console Output` - Full build logs

## Scheduling Tests

### Every Hour
```
Pipeline → Triggers → Poll SCM
Schedule: H * * * *
```

### Every Day at 2 AM
```
Schedule: H 2 * * *
```

### Every Weekday at 9 AM
```
Schedule: H 9 * * 1-5
```

## Jenkins Pipeline Overview

The Jenkinsfile provides:

1. **Checkout** - Pulls code from Git
2. **Build** - Compiles project
3. **Test** - Runs test cases
4. **Post Actions** - Archives reports and logs

## Build Status Indicators

| Status | Meaning |
|--------|---------|
| 🔵 Blue | Build successful |
| 🔴 Red | Build failed |
| ⚪ Gray | No builds yet |
| 🟡 Yellow | Build unstable (tests failed) |

## Troubleshooting

### Build Fails: "No suite found"
**Check**: Ensure `login.xml` exists in `src/test/resources/suites/`

### No WebDriver Found
**Fix**: Install ChromeDriver/GeckoDriver on Jenkins agent
**Or**: Use headless mode in test configuration

### Timeout During Tests
**Increase**: Timeout value in Jenkinsfile (change `30` to higher value)

### Reports Not Generated
**Check**: Test execution succeeded - review console output

## Environment Variables Available in Jenkinsfile

```groovy
BUILD_ID          - Build number
BUILD_URL         - URL to build page
WORKSPACE         - Jenkins workspace directory
JOB_NAME          - Name of the job
NODE_NAME         - Agent node name
```

## Example: Custom Build Parameters

1. Job Configuration → Check "This project is parameterized"
2. Add Parameter → String Parameter:
   - Name: `suite`
   - Default: `login`
3. In Jenkinsfile: `sh 'mvn test -Dsuite=${suite}'`
4. When building, select parameter value

## Integration Options

### GitHub Webhook (Auto-trigger on Push)
```
GitHub Repo Settings → Webhooks → Add:
- Payload URL: http://jenkins/github-webhook/
- Content type: application/json
- Trigger: Push events
```

### Email Notifications
Add to Jenkinsfile:
```groovy
post {
    success {
        emailext(
            to: 'team@example.com',
            subject: 'Tests Passed',
            body: 'All tests passed successfully!'
        )
    }
}
```

## Monitor Builds

### Real-time Monitoring
- Jenkins Dashboard → Job Name → Build History
- Click on build → Console Output (streams in real-time)

### Build Metrics
- Manage Jenkins → Manage Plugins → Install "CloudBees Build Name Updater"
- Adds build trending and metrics

## Next Steps

1. **Run First Build**: Click "Build Now"
2. **Monitor Execution**: Click build number → Console Output
3. **View Reports**: After completion → Click "Extent Report"
4. **Schedule Builds**: Configure triggers for automated execution
5. **Add Notifications**: Setup email/Slack alerts

---

**For detailed setup**, see: `JENKINS_SETUP.md`
