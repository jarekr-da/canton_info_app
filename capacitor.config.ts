import type { CapacitorConfig } from '@capacitor/cli';

const config: CapacitorConfig = {
  appId: 'com.example.cantoninfo',
  appName: 'Canton Info',
  webDir: 'dist',
  plugins: {
    // Kiosk: start with status/navigation bars hidden. MainActivity also re-hides them on focus.
    SystemBars: {
      hidden: true,
      initialViewportFitValueHint: 'cover',
    },
  },
};

export default config;
