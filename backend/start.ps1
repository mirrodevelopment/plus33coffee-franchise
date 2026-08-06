$env:JAVA_HOME = "C:\Program Files\Microsoft\jdk-21.0.12.8-hotspot"
$env:MAVEN_HOME = "C:\tools\apache-maven-3.9.6"
$env:PATH = "$env:JAVA_HOME\bin;$env:MAVEN_HOME\bin;$env:PATH"

Write-Host ""
Write-Host "  =====================================================" -ForegroundColor DarkYellow
Write-Host "   PLUS33 CAFE FRANCAIS - Franchise Backend Server" -ForegroundColor Yellow
Write-Host "  =====================================================" -ForegroundColor DarkYellow
Write-Host ""
Write-Host "  Java:   $env:JAVA_HOME" -ForegroundColor Cyan
Write-Host "  Maven:  $env:MAVEN_HOME" -ForegroundColor Cyan
Write-Host "  URL:    http://localhost:8080" -ForegroundColor Green
Write-Host "  H2 DB:  http://localhost:8080/h2-console" -ForegroundColor Green
Write-Host ""

Set-Location "$PSScriptRoot"
& mvn spring-boot:run
