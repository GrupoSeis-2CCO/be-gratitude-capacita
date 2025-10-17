import React from "react";
import Layout from "../Layout";
import { useAuth } from "../hooks/useAuth.js";
import StudentMaterialPage from "./StudentMaterialPage.jsx";
import MaterialPage from "./MaterialPage.jsx";

export default function MaterialRoutePage() {
  const { getCurrentUserType } = useAuth();
  const userType = getCurrentUserType?.();
  if (userType === 2) {
    return (
      <Layout footerType="mini" headerType="student">
        <StudentMaterialPage />
      </Layout>
    );
  }
  return (
    <Layout footerType="mini">
      <MaterialPage />
    </Layout>
  );
}
