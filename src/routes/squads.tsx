import { createFileRoute, redirect } from "@tanstack/react-router";

export const Route = createFileRoute("/squads")({
  beforeLoad: () => {
    throw redirect({ to: "/app/squads", replace: true });
  },
});