import { useState, useEffect } from 'react';

export function useAuth() {
  const [user, setUser] = useState(null);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    checkAuthStatus();
  }, []);

  const checkAuthStatus = async () => {
    try {
      const response = await fetch('/__replauthuser');
      const userData = await response.json();
      setUser(userData);
    } catch (error) {
      console.error('Error checking auth status:', error);
      setUser(null);
    } finally {
      setIsLoading(false);
    }
  };

  const signInWithReplit = () => {
    const h = 500;
    const w = 350;
    const left = screen.width / 2 - w / 2;
    const top = screen.height / 2 - h / 2;

    const authWindow = window.open(
      `https://replit.com/auth_with_repl_site?domain=${location.host}`,
      '_blank',
      `modal=yes,toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=no,resizable=no,copyhistory=no,width=${w},height=${h},top=${top},left=${left}`
    );

    const authCheckInterval = setInterval(() => {
      if (authWindow?.closed) {
        clearInterval(authCheckInterval);
        checkAuthStatus();
      }
    }, 500);
  };

  const signOut = async () => {
    try {
      setIsLoading(true);
      await fetch('/__replauthlogout');
      setUser(null);
    } catch (error) {
      console.error('Error signing out:', error);
    } finally {
      setIsLoading(false);
    }
  };

  return {
    user,
    isLoading,
    signInWithReplit,
    signOut
  };
}