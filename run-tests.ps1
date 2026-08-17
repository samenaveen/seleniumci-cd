# Quick Test Runner Script for Windows
# Usage: .\run-tests.ps1 -Suite login -Browser chrome

param(
    [string]$Suite = "login",
    [string]$Browser = "chrome",
    [bool]$SkipClean = $false
)

# Colors for output
$Green = 'Green'
$Blue = 'Cyan'
$Yellow = 'Yellow'
$Red = 'Red'

Write-Host "========================================" -ForegroundColor $Blue
Write-Host "Selenium Automation Test Runner" -ForegroundColor $Blue
Write-Host "========================================" -ForegroundColor $Blue
Write-Host ""

# Display configuration
Write-Host "Configuration:" -ForegroundColor $Yellow
Write-Host "  Suite: $Suite"
Write-Host "  Browser: $Browser"
Write-Host "  Skip Clean: $SkipClean"
Write-Host ""

# Check prerequisites
Write-Host "Checking Prerequisites..." -ForegroundColor $Yellow

# Check Maven
$mavenExists = $null -ne (Get-Command mvn -ErrorAction SilentlyContinue)
if (-not $mavenExists) {
    Write-Host "✗ Maven not found. Please install Maven." -ForegroundColor $Red
    exit 1
}
Write-Host "✓ Maven found" -ForegroundColor $Green

# Check Java
$javaExists = $null -ne (Get-Command java -ErrorAction SilentlyContinue)
if (-not $javaExists) {
    Write-Host "✗ Java not found. Please install Java 21+." -ForegroundColor $Red
    exit 1
}
Write-Host "✓ Java found" -ForegroundColor $Green

# Get Java version
$javaVersion = java -version 2>&1 | Select-Object -First 1
Write-Host "  $javaVersion"
Write-Host ""

# Clean Maven cache (optional)
if (-not $SkipClean) {
    Write-Host "Cleaning previous builds..." -ForegroundColor $Yellow
    mvn clean -q
    Write-Host "✓ Cleaned" -ForegroundColor $Green
    Write-Host ""
}

# Compile
Write-Host "Compiling project..." -ForegroundColor $Yellow
$compileResult = mvn compile -q -DskipTests
if ($LASTEXITCODE -ne 0) {
    Write-Host "✗ Compilation failed" -ForegroundColor $Red
    exit 1
}
Write-Host "✓ Compilation successful" -ForegroundColor $Green
Write-Host ""

# Run tests
Write-Host "Running tests..." -ForegroundColor $Yellow
Write-Host "  Suite: $Suite"
Write-Host "  Browser: $Browser"
Write-Host ""

$testResult = mvn test -Dsuite=$Suite -Dbrowser=$Browser
$exitCode = $LASTEXITCODE

if ($exitCode -eq 0) {
    Write-Host ""
    Write-Host "========================================" -ForegroundColor $Green
    Write-Host "✓ All tests passed successfully!" -ForegroundColor $Green
    Write-Host "========================================" -ForegroundColor $Green
    Write-Host ""
    
    # Display report paths
    Write-Host "Test Reports:" -ForegroundColor $Yellow
    if (Test-Path "reports") {
        $htmlReport = Get-ChildItem -Path "reports" -Filter "*.html" -Recurse | Select-Object -First 1
        if ($htmlReport) {
            Write-Host "  HTML Report: $($htmlReport.FullName)"
        }
    }
    if (Test-Path "target\surefire-reports") {
        Write-Host "  TestNG Report: target\surefire-reports\"
    }
    if (Test-Path "logs") {
        Write-Host "  Logs: logs\"
    }
    Write-Host ""
    
    exit 0
} else {
    Write-Host ""
    Write-Host "========================================" -ForegroundColor $Red
    Write-Host "✗ Some tests failed!" -ForegroundColor $Red
    Write-Host "========================================" -ForegroundColor $Red
    Write-Host ""
    Write-Host "Failed Test Reports:" -ForegroundColor $Yellow
    if (Test-Path "reports") {
        $htmlReport = Get-ChildItem -Path "reports" -Filter "*.html" -Recurse | Select-Object -First 1
        if ($htmlReport) {
            Write-Host "  HTML Report: $($htmlReport.FullName)"
        }
    }
    Write-Host "  TestNG Report: target\surefire-reports\"
    Write-Host ""
    
    exit 1
}
