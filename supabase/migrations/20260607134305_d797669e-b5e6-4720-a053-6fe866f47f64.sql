-- Restrict Realtime CDC payloads to non-sensitive columns
ALTER PUBLICATION supabase_realtime DROP TABLE public.tournaments;
ALTER PUBLICATION supabase_realtime ADD TABLE public.tournaments
  (id, organizer_id, name, description, prize_pool, mode, max_teams, starts_at,
   banner_url, status, created_at, expires_at, map_mode, region, system, min_rank,
   rules, trophies_count, team_size, live_stream_active, live_stream_started_at);

ALTER PUBLICATION supabase_realtime DROP TABLE public.profiles;
ALTER PUBLICATION supabase_realtime ADD TABLE public.profiles
  (id, username, display_name, avatar_url, bio, rank, preferred_server, sensitivity,
   created_at, updated_at, role, kd, language, mic_available, country, availability,
   username_changed_at, last_seen_at);