import { createFileRoute, redirect } from "@tanstack/react-router";

export const Route = createFileRoute("/servers/$serverId")({
  beforeLoad: ({ params }) => {
    throw redirect({
      to: "/app/servers/$serverId",
      params: { serverId: params.serverId },
      replace: true,
    });
  },
});