import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Pipeline from './pipeline';
import PipelineDetail from './pipeline-detail';
import PipelineUpdate from './pipeline-update';
import PipelineDeleteDialog from './pipeline-delete-dialog';

const PipelineRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Pipeline />} />
    <Route path="new" element={<PipelineUpdate />} />
    <Route path=":id">
      <Route index element={<PipelineDetail />} />
      <Route path="edit" element={<PipelineUpdate />} />
      <Route path="delete" element={<PipelineDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PipelineRoutes;
