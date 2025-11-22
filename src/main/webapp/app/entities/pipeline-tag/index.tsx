import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import PipelineTag from './pipeline-tag';
import PipelineTagDetail from './pipeline-tag-detail';
import PipelineTagUpdate from './pipeline-tag-update';
import PipelineTagDeleteDialog from './pipeline-tag-delete-dialog';

const PipelineTagRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<PipelineTag />} />
    <Route path="new" element={<PipelineTagUpdate />} />
    <Route path=":id">
      <Route index element={<PipelineTagDetail />} />
      <Route path="edit" element={<PipelineTagUpdate />} />
      <Route path="delete" element={<PipelineTagDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PipelineTagRoutes;
