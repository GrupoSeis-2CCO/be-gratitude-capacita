import React from "react";
import Layout from "../Layout";
import { useAuth } from "../hooks/useAuth.js";
import StudentMaterialsListPage from "./StudentMaterialsListPage.jsx";
import MaterialsListPage from "./MaterialsListPage.jsx";

export default function MaterialsRoutePage() {
  const { getCurrentUserType } = useAuth();
  const userType = getCurrentUserType?.();

  if (userType === 2) {
    return (
      <Layout footerType="mini" headerType="student">
        <StudentMaterialsListPage />
      </Layout>
    );
  }
  return (
    <Layout footerType="mini">
      <MaterialsListPage />
    </Layout>
  );
}
