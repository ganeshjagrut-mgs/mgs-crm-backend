import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import MasterCategory from './master-category';
import MasterCategoryDetail from './master-category-detail';
import MasterCategoryUpdate from './master-category-update';
import MasterCategoryDeleteDialog from './master-category-delete-dialog';

const MasterCategoryRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<MasterCategory />} />
    <Route path="new" element={<MasterCategoryUpdate />} />
    <Route path=":id">
      <Route index element={<MasterCategoryDetail />} />
      <Route path="edit" element={<MasterCategoryUpdate />} />
      <Route path="delete" element={<MasterCategoryDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default MasterCategoryRoutes;
