// @lovable.dev/vite-tanstack-config already includes the following — do NOT add them manually
// or the app will break with duplicate plugins:
//   - tanstackStart, viteReact, tailwindcss, tsConfigPaths, nitro (build-only using cloudflare as a default target),
//     componentTagger (dev-only), VITE_* env injection, @ path alias, React/TanStack dedupe,
//     error logger plugins, and sandbox detection (port/host/strictPort).
// You can pass additional config via defineConfig({ vite: { ... }, etc... }) if needed.
import { defineConfig } from "@lovable.dev/vite-tanstack-config";

const backendUrl = process.env.VITE_SUPABASE_URL || process.env.SUPABASE_URL || "https://zqjlelgitjhunpoapmgb.supabase.co";
const backendPublishableKey =
  process.env.VITE_SUPABASE_PUBLISHABLE_KEY ||
  process.env.SUPABASE_PUBLISHABLE_KEY ||
  "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InpxamxlbGdpdGpodW5wb2FwbWdiIiwicm9sZSI6ImFub24iLCJpYXQiOjE3Nzk5ODYyOTgsImV4cCI6MjA5NTU2MjI5OH0.zRY8jJcy6DmIqKt1XbKGAZTXJ9f55570NnhFJ9Wj6wU";

export default defineConfig({
  tanstackStart: {
    // Redirect TanStack Start's bundled server entry to src/server.ts (our SSR error wrapper).
    // nitro/vite builds from this
    server: { entry: "server" },
  },
  vite: {
    define: {
      "process.env.SUPABASE_URL": JSON.stringify(backendUrl),
      "process.env.SUPABASE_PUBLISHABLE_KEY": JSON.stringify(backendPublishableKey),
      "import.meta.env.VITE_SUPABASE_URL": JSON.stringify(backendUrl),
      "import.meta.env.VITE_SUPABASE_PUBLISHABLE_KEY": JSON.stringify(backendPublishableKey),
    },
  },
});
