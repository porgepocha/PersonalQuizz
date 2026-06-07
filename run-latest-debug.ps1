$ErrorActionPreference = "Stop"

$root = Split-Path -Parent $MyInvocation.MyCommand.Path
$distDir = Join-Path $root "dist"

$debugFolder = Get-ChildItem -Path $distDir -Directory |
    Where-Object { $_.Name -like "GiftAuraQuizDebug*" } |
    Sort-Object LastWriteTime -Descending |
    Select-Object -First 1

if (-not $debugFolder) {
    throw "Nao encontrei nenhuma pasta GiftAuraQuizDebug dentro de dist."
}

$exePath = Join-Path $debugFolder.FullName "GiftAuraQuizDebug.exe"
$logPath = Join-Path $debugFolder.FullName "debug-output.txt"

if (!(Test-Path $exePath)) {
    throw "Nao encontrei o executavel em $exePath"
}

Write-Host "A correr: $exePath"
Write-Host "O output vai para: $logPath"

cmd /c "`"$exePath`" > `"$logPath`" 2>&1"

Write-Host ""
Write-Host "Conteudo do log:"
Get-Content -Path $logPath
