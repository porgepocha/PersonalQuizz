$ErrorActionPreference = "Stop"

$root = Split-Path -Parent $MyInvocation.MyCommand.Path
$clientDir = Join-Path $root "gift-quiz-client"
$distDir = Join-Path $root "dist"
$inputDir = Join-Path $clientDir "target"
$dependencyDir = Join-Path $inputDir "dependency"
$appInputDir = Join-Path $inputDir "jpackage-input-debug"
$jarName = "gift-quiz-client-1.0.0-SNAPSHOT.jar"
$mainJar = Join-Path $inputDir $jarName
$runtimeImageName = "GiftAuraQuizDebug"
$jpackageCommand = $null

function Resolve-FirstExistingPath {
    param (
        [string[]]$Candidates
    )

    foreach ($candidate in $Candidates) {
        if (Test-Path $candidate) {
            return $candidate
        }
    }

    return $null
}

$candidateCommands = @()
if ($env:JAVA_HOME) {
    $candidateCommands += (Join-Path $env:JAVA_HOME "bin\jpackage.exe")
}
$candidateCommands += "C:\Program Files\Java\jdk-21.0.11\bin\jpackage.exe"

foreach ($candidate in $candidateCommands) {
    if (Test-Path $candidate) {
        $jpackageCommand = $candidate
        break
    }
}

if (-not $jpackageCommand) {
    throw "Could not find jpackage.exe. Set JAVA_HOME to a JDK 21 installation or update package-client-debug.ps1."
}

Write-Host "Building client jar..."
Push-Location $root
mvn -pl gift-quiz-model install -DskipTests -q
if ($LASTEXITCODE -ne 0) {
    throw "Could not install gift-quiz-model locally."
}

mvn -pl gift-quiz-client package -DskipTests -q
if ($LASTEXITCODE -ne 0) {
    throw "Could not build gift-quiz-client."
}

mvn -pl gift-quiz-client dependency:copy-dependencies -DincludeScope=runtime -DoutputDirectory=target\dependency -q
if ($LASTEXITCODE -ne 0) {
    throw "Could not copy gift-quiz-client runtime dependencies."
}
Pop-Location

if (!(Test-Path $mainJar)) {
    throw "Could not find built jar at $mainJar"
}

if (Test-Path $appInputDir) {
    Remove-Item -LiteralPath $appInputDir -Recurse -Force
}
New-Item -ItemType Directory -Path $appInputDir | Out-Null
Copy-Item -LiteralPath $mainJar -Destination $appInputDir -Force
Get-ChildItem -Path $dependencyDir -Filter *.jar | ForEach-Object {
    Copy-Item -LiteralPath $_.FullName -Destination $appInputDir -Force
}

if (!(Test-Path $distDir)) {
    New-Item -ItemType Directory -Path $distDir | Out-Null
}

$appImageDir = Join-Path $distDir $runtimeImageName
if (Test-Path $appImageDir) {
    Write-Host "Trying to remove old debug app image..."
    try {
        Remove-Item -LiteralPath $appImageDir -Recurse -Force
    } catch {
        $runtimeImageName = "GiftAuraQuizDebug-" + (Get-Date -Format "yyyyMMdd-HHmmss")
        $appImageDir = Join-Path $distDir $runtimeImageName
        Write-Host "Old debug folder is locked. Creating a new one: $runtimeImageName"
    }
}

Write-Host "Packaging debug desktop app with jpackage..."
& $jpackageCommand `
  --type app-image `
  --name $runtimeImageName `
  --win-console `
  --module-path $dependencyDir `
  --add-modules "javafx.controls,javafx.media,jdk.httpserver,jdk.crypto.ec" `
  --input $appInputDir `
  --main-jar $jarName `
  --main-class "com.jorge.constanca.client.GiftQuizClientApp" `
  --dest $distDir

$spotifyExample = Resolve-FirstExistingPath @(
    (Join-Path $root "spotify.properties.example"),
    (Join-Path $root "spotify.properties.example.txt")
)

if ($spotifyExample) {
    Copy-Item -LiteralPath $spotifyExample `
      -Destination (Join-Path $appImageDir "spotify.properties.example") `
      -Force
}

$spotifyConfig = Resolve-FirstExistingPath @(
    (Join-Path $root "spotify.properties"),
    (Join-Path $root "spotify.properties.properties")
)
if (Test-Path $spotifyConfig) {
    Copy-Item -LiteralPath $spotifyConfig -Destination (Join-Path $appImageDir "spotify.properties") -Force
}

$sourceMusicDir = Join-Path $root "music"
$appMusicDir = Join-Path $appImageDir "music"
if (!(Test-Path $appMusicDir)) {
    New-Item -ItemType Directory -Path $appMusicDir | Out-Null
}
if (Test-Path $sourceMusicDir) {
    Get-ChildItem -Path $sourceMusicDir -Filter *.mp3 -File | ForEach-Object {
        Copy-Item -LiteralPath $_.FullName -Destination $appMusicDir -Force
    }
}

Write-Host "Done. Debug app image created in $distDir"
