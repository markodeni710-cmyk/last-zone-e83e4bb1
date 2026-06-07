import { createFileRoute, redirect } from "@tanstack/react-router";

export const Route = createFileRoute("/feed")({
  beforeLoad: () => {
    throw redirect({ to: "/app/feed", replace: true });
  },
});