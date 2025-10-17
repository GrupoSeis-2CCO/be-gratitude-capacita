import React from 'react';
import { Navigate } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth.js';

export default function ProtectedRoute({ allowedUserTypes = [1,2], children }) {
  const { isLoggedIn, getCurrentUserType } = useAuth();
  const logged = isLoggedIn();
  const tipo = getCurrentUserType();

  if (!logged) return <Navigate to="/login" replace />;
  if (Array.isArray(allowedUserTypes) && allowedUserTypes.length > 0) {
    if (typeof tipo === 'number' && !allowedUserTypes.includes(tipo)) {
      return <Navigate to="/login" replace />;
    }
  }

  return children;
}
