
DROP POLICY IF EXISTS "shop_self_grants_insert" ON public.admin_trophy_grants;

DROP POLICY IF EXISTS "tournaments_select_all" ON public.tournaments;
CREATE POLICY "tournaments_select_authenticated"
  ON public.tournaments
  FOR SELECT
  TO authenticated
  USING (true);

REVOKE SELECT (room_id, room_password) ON public.tournaments FROM anon, authenticated;

REVOKE SELECT (pubg_id, suspended_at, suspended_until, suspension_reason)
  ON public.profiles FROM anon;

CREATE POLICY "users_read_own_sessions"
  ON public.account_sessions
  FOR SELECT
  TO authenticated
  USING (user_id = auth.uid());

REVOKE EXECUTE ON FUNCTION public.block_admin_voice_calls() FROM anon, public;
REVOKE EXECUTE ON FUNCTION public.ensure_admin_friendship(uuid) FROM anon, public;
REVOKE EXECUTE ON FUNCTION public.get_admin_user_id() FROM anon, public;
REVOKE EXECUTE ON FUNCTION public.get_suspicious_accounts() FROM anon, public;
REVOKE EXECUTE ON FUNCTION public.is_admin_user(uuid) FROM anon, public;
REVOKE EXECUTE ON FUNCTION public.is_server_admin(uuid) FROM anon, public;
REVOKE EXECUTE ON FUNCTION public.profile_ensure_admin_friend() FROM anon, public;
REVOKE EXECUTE ON FUNCTION public.protect_admin_dm_thread_delete() FROM anon, public;
REVOKE EXECUTE ON FUNCTION public.protect_admin_friendship_delete() FROM anon, public;
REVOKE EXECUTE ON FUNCTION public.protect_admin_from_block() FROM anon, public;
REVOKE EXECUTE ON FUNCTION public.protect_admin_from_dm() FROM anon, public;
REVOKE EXECUTE ON FUNCTION public.protect_admin_from_friend_request() FROM anon, public;
REVOKE EXECUTE ON FUNCTION public.record_account_session(text, text, text, text, text, text) FROM anon, public;
REVOKE EXECUTE ON FUNCTION public.validate_username_reserved() FROM anon, public;
