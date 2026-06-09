
-- 1) Allow deleting non-finished tournaments even if results exist; auto-clean dependents
CREATE OR REPLACE FUNCTION public.prevent_finished_tournament_delete()
RETURNS trigger
LANGUAGE plpgsql
SECURITY DEFINER
SET search_path TO 'public'
AS $$
BEGIN
  IF OLD.status = 'finished' THEN
    RAISE EXCEPTION 'cannot_delete_finished_tournament';
  END IF;
  -- Clean dependents so a cancel always succeeds for non-finished tournaments
  DELETE FROM public.tournament_results WHERE tournament_id = OLD.id;
  DELETE FROM public.tournament_team_invites WHERE tournament_id = OLD.id;
  DELETE FROM public.tournament_registrations WHERE tournament_id = OLD.id;
  RETURN OLD;
END $$;

-- 2) Ensure realtime broadcasts full row on UPDATE so live_stream_active changes propagate to all clients
ALTER TABLE public.tournaments REPLICA IDENTITY FULL;
