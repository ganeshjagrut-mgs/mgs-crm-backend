import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Encryption from './encryption';
import EncryptionDetail from './encryption-detail';
import EncryptionUpdate from './encryption-update';
import EncryptionDeleteDialog from './encryption-delete-dialog';

const EncryptionRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Encryption />} />
    <Route path="new" element={<EncryptionUpdate />} />
    <Route path=":id">
      <Route index element={<EncryptionDetail />} />
      <Route path="edit" element={<EncryptionUpdate />} />
      <Route path="delete" element={<EncryptionDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default EncryptionRoutes;
