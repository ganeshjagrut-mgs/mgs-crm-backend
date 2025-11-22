import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import MasterStaticGroup from './master-static-group';
import MasterStaticGroupDetail from './master-static-group-detail';
import MasterStaticGroupUpdate from './master-static-group-update';
import MasterStaticGroupDeleteDialog from './master-static-group-delete-dialog';

const MasterStaticGroupRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<MasterStaticGroup />} />
    <Route path="new" element={<MasterStaticGroupUpdate />} />
    <Route path=":id">
      <Route index element={<MasterStaticGroupDetail />} />
      <Route path="edit" element={<MasterStaticGroupUpdate />} />
      <Route path="delete" element={<MasterStaticGroupDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default MasterStaticGroupRoutes;
