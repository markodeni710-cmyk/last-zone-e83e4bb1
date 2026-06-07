import { createFileRoute, redirect } from "@tanstack/react-router";

export const Route = createFileRoute("/friends")({
  beforeLoad: () => {
    throw redirect({ to: "/app/friends", replace: true });
  },
});