import { createContext, useContext, ReactNode } from 'react';
import { useAuth, UserAuth } from '@/hooks/use-auth';

// Create context
const AuthContext = createContext<UserAuth | undefined>(undefined);

// Create a provider component
export function AuthProvider({ children }: { children: ReactNode }) {
  const auth = useAuth();

  return <AuthContext.Provider value={auth}>{children}</AuthContext.Provider>;
}

// Custom hook to use the auth context
export function useAuthContext() {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuthContext must be used within an AuthProvider');
  }
  return context;
}