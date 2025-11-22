import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import SubPipelineOpenStage from './sub-pipeline-open-stage';
import SubPipelineOpenStageDetail from './sub-pipeline-open-stage-detail';
import SubPipelineOpenStageUpdate from './sub-pipeline-open-stage-update';
import SubPipelineOpenStageDeleteDialog from './sub-pipeline-open-stage-delete-dialog';

const SubPipelineOpenStageRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<SubPipelineOpenStage />} />
    <Route path="new" element={<SubPipelineOpenStageUpdate />} />
    <Route path=":id">
      <Route index element={<SubPipelineOpenStageDetail />} />
      <Route path="edit" element={<SubPipelineOpenStageUpdate />} />
      <Route path="delete" element={<SubPipelineOpenStageDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default SubPipelineOpenStageRoutes;
