# Asset Checklist

To finish the app visuals and audio, prepare these files locally:

## Photos

Save the five photos with these exact names:

- `photo-1.jpg`
- `photo-2.jpg`
- `photo-3.jpg`
- `photo-4.jpg`
- `photo-5.jpg`

Put them together in one folder anywhere on your computer.

## Audio

Pick one background song file, ideally:

- calm
- dreamy
- soft
- instrumental if possible

Supported best here:

- `.mp3`

## Import command

From the project root:

```powershell
.\import-assets.ps1 -PhotosDirectory "C:\path\to\your\photos" -AudioFile "C:\path\to\your\song.mp3"
```

If you want to skip audio for now:

```powershell
.\import-assets.ps1 -PhotosDirectory "C:\path\to\your\photos"
```
