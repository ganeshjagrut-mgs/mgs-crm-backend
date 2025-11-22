import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Complaint from './complaint';
import ComplaintDetail from './complaint-detail';
import ComplaintUpdate from './complaint-update';
import ComplaintDeleteDialog from './complaint-delete-dialog';

const ComplaintRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Complaint />} />
    <Route path="new" element={<ComplaintUpdate />} />
    <Route path=":id">
      <Route index element={<ComplaintDetail />} />
      <Route path="edit" element={<ComplaintUpdate />} />
      <Route path="delete" element={<ComplaintDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ComplaintRoutes;
