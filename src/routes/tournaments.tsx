import { createFileRoute, redirect } from "@tanstack/react-router";

export const Route = createFileRoute("/tournaments")({
  beforeLoad: () => {
    throw redirect({ to: "/app/tournaments", replace: true });
  },
});