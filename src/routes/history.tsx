import { createFileRoute, redirect } from "@tanstack/react-router";

export const Route = createFileRoute("/history")({
  beforeLoad: () => {
    throw redirect({ to: "/app/history", replace: true });
  },
});