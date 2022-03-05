# Use by executing:
# $ Import-Module Dockerize
function Dockerize
{
    Param ([string] $artifact)

    if ($artifact -and (Test-Path -PathType Container $artifact)) {
        Set-Location ${artifact}
        mvn clean package -DskipTests
        docker build -t ostock/${artifact}:0.0.1-SNAPSHOT --build-arg JAR_FILE=target/${artifact}-0.0.1-SNAPSHOT.jar .
        Set-Location -
    } else {
        Write-Output "Nothing to dockerize..."
    }
}

