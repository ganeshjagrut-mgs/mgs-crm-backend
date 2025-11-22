import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import PipelineAudit from './pipeline-audit';
import PipelineAuditDetail from './pipeline-audit-detail';
import PipelineAuditUpdate from './pipeline-audit-update';
import PipelineAuditDeleteDialog from './pipeline-audit-delete-dialog';

const PipelineAuditRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<PipelineAudit />} />
    <Route path="new" element={<PipelineAuditUpdate />} />
    <Route path=":id">
      <Route index element={<PipelineAuditDetail />} />
      <Route path="edit" element={<PipelineAuditUpdate />} />
      <Route path="delete" element={<PipelineAuditDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PipelineAuditRoutes;
