#!/bin/bash
# Quick Test Runner Script
# Usage: ./run-tests.sh [suite] [browser]

set -e

# Colors for output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Default values
SUITE="${1:-login}"
BROWSER="${2:-chrome}"
SKIP_CLEAN="${3:-false}"

echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}Selenium Automation Test Runner${NC}"
echo -e "${BLUE}========================================${NC}"
echo ""

# Display configuration
echo -e "${YELLOW}Configuration:${NC}"
echo "  Suite: $SUITE"
echo "  Browser: $BROWSER"
echo "  Skip Clean: $SKIP_CLEAN"
echo ""

# Check prerequisites
echo -e "${YELLOW}Checking Prerequisites...${NC}"

if ! command -v mvn &> /dev/null; then
    echo -e "${RED}✗ Maven not found. Please install Maven.${NC}"
    exit 1
fi
echo -e "${GREEN}✓ Maven found${NC}"

if ! command -v java &> /dev/null; then
    echo -e "${RED}✗ Java not found. Please install Java 21+.${NC}"
    exit 1
fi
echo -e "${GREEN}✓ Java found${NC}"

JAVA_VERSION=$(java -version 2>&1 | head -n1)
echo "  $JAVA_VERSION"
echo ""

# Clean Maven cache (optional)
if [ "$SKIP_CLEAN" = "false" ]; then
    echo -e "${YELLOW}Cleaning previous builds...${NC}"
    mvn clean -q
    echo -e "${GREEN}✓ Cleaned${NC}"
    echo ""
fi

# Compile
echo -e "${YELLOW}Compiling project...${NC}"
mvn compile -q -DskipTests
echo -e "${GREEN}✓ Compilation successful${NC}"
echo ""

# Run tests
echo -e "${YELLOW}Running tests...${NC}"
echo "  Suite: $SUITE"
echo "  Browser: $BROWSER"
echo ""

if mvn test -Dsuite=$SUITE -Dbrowser=$BROWSER; then
    echo ""
    echo -e "${GREEN}========================================${NC}"
    echo -e "${GREEN}✓ All tests passed successfully!${NC}"
    echo -e "${GREEN}========================================${NC}"
    echo ""
    
    # Display report paths
    echo -e "${YELLOW}Test Reports:${NC}"
    if [ -d "reports" ]; then
        echo "  HTML Report: $(find reports -name '*.html' -type f | head -1)"
    fi
    if [ -d "target/surefire-reports" ]; then
        echo "  TestNG Report: target/surefire-reports/"
    fi
    if [ -d "logs" ]; then
        echo "  Logs: logs/"
    fi
    echo ""
    
    exit 0
else
    echo ""
    echo -e "${RED}========================================${NC}"
    echo -e "${RED}✗ Some tests failed!${NC}"
    echo -e "${RED}========================================${NC}"
    echo ""
    echo -e "${YELLOW}Failed Test Reports:${NC}"
    if [ -d "reports" ]; then
        echo "  HTML Report: $(find reports -name '*.html' -type f | head -1)"
    fi
    echo "  TestNG Report: target/surefire-reports/"
    echo ""
    exit 1
fi
