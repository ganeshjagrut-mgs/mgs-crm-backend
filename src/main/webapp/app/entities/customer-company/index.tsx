import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import CustomerCompany from './customer-company';
import CustomerCompanyDetail from './customer-company-detail';
import CustomerCompanyUpdate from './customer-company-update';
import CustomerCompanyDeleteDialog from './customer-company-delete-dialog';

const CustomerCompanyRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<CustomerCompany />} />
    <Route path="new" element={<CustomerCompanyUpdate />} />
    <Route path=":id">
      <Route index element={<CustomerCompanyDetail />} />
      <Route path="edit" element={<CustomerCompanyUpdate />} />
      <Route path="delete" element={<CustomerCompanyDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CustomerCompanyRoutes;
