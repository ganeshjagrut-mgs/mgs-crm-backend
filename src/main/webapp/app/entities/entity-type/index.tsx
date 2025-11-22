import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import EntityType from './entity-type';
import EntityTypeDetail from './entity-type-detail';
import EntityTypeUpdate from './entity-type-update';
import EntityTypeDeleteDialog from './entity-type-delete-dialog';

const EntityTypeRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<EntityType />} />
    <Route path="new" element={<EntityTypeUpdate />} />
    <Route path=":id">
      <Route index element={<EntityTypeDetail />} />
      <Route path="edit" element={<EntityTypeUpdate />} />
      <Route path="delete" element={<EntityTypeDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default EntityTypeRoutes;
