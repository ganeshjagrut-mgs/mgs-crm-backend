import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import TaskAudit from './task-audit';
import TaskAuditDetail from './task-audit-detail';
import TaskAuditUpdate from './task-audit-update';
import TaskAuditDeleteDialog from './task-audit-delete-dialog';

const TaskAuditRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<TaskAudit />} />
    <Route path="new" element={<TaskAuditUpdate />} />
    <Route path=":id">
      <Route index element={<TaskAuditDetail />} />
      <Route path="edit" element={<TaskAuditUpdate />} />
      <Route path="delete" element={<TaskAuditDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TaskAuditRoutes;
