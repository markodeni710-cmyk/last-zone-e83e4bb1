import { createFileRoute, redirect } from "@tanstack/react-router";

export const Route = createFileRoute("/dm/$userId")({
  beforeLoad: ({ params }) => {
    throw redirect({
      to: "/app/dm/$userId",
      params: { userId: params.userId },
      replace: true,
    });
  },
});