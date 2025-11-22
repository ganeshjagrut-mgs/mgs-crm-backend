import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import MasterStaticType from './master-static-type';
import MasterStaticTypeDetail from './master-static-type-detail';
import MasterStaticTypeUpdate from './master-static-type-update';
import MasterStaticTypeDeleteDialog from './master-static-type-delete-dialog';

const MasterStaticTypeRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<MasterStaticType />} />
    <Route path="new" element={<MasterStaticTypeUpdate />} />
    <Route path=":id">
      <Route index element={<MasterStaticTypeDetail />} />
      <Route path="edit" element={<MasterStaticTypeUpdate />} />
      <Route path="delete" element={<MasterStaticTypeDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default MasterStaticTypeRoutes;
