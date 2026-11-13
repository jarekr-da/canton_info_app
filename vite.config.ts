import react from '@vitejs/plugin-react'
import { defineConfig } from 'vite'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  // Capacitor loads dist/ from the app bundle, so asset URLs must be relative.
  base: './',
  build: {
    // Android 11 shipped with a Chromium 83-era System WebView, and the LED43 may never update it.
    // Raise this once the device's real WebView version is known.
    target: 'chrome83',
    cssTarget: 'chrome83',
  },
})
