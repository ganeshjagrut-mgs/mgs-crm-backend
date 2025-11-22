import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import SubPipeline from './sub-pipeline';
import SubPipelineDetail from './sub-pipeline-detail';
import SubPipelineUpdate from './sub-pipeline-update';
import SubPipelineDeleteDialog from './sub-pipeline-delete-dialog';

const SubPipelineRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<SubPipeline />} />
    <Route path="new" element={<SubPipelineUpdate />} />
    <Route path=":id">
      <Route index element={<SubPipelineDetail />} />
      <Route path="edit" element={<SubPipelineUpdate />} />
      <Route path="delete" element={<SubPipelineDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default SubPipelineRoutes;
