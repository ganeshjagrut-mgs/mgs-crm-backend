import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import SubPipelineCloseStage from './sub-pipeline-close-stage';
import SubPipelineCloseStageDetail from './sub-pipeline-close-stage-detail';
import SubPipelineCloseStageUpdate from './sub-pipeline-close-stage-update';
import SubPipelineCloseStageDeleteDialog from './sub-pipeline-close-stage-delete-dialog';

const SubPipelineCloseStageRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<SubPipelineCloseStage />} />
    <Route path="new" element={<SubPipelineCloseStageUpdate />} />
    <Route path=":id">
      <Route index element={<SubPipelineCloseStageDetail />} />
      <Route path="edit" element={<SubPipelineCloseStageUpdate />} />
      <Route path="delete" element={<SubPipelineCloseStageDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default SubPipelineCloseStageRoutes;
