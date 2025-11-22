import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import EntityType from './entity-type';
import MasterStaticGroup from './master-static-group';
import MasterStaticType from './master-static-type';
import MasterCategory from './master-category';
import Tenant from './tenant';
import Encryption from './encryption';
import CustomerCompany from './customer-company';
import Department from './department';
import Customer from './customer';
import Lead from './lead';
import SubPipeline from './sub-pipeline';
import SubPipelineOpenStage from './sub-pipeline-open-stage';
import SubPipelineCloseStage from './sub-pipeline-close-stage';
import Pipeline from './pipeline';
import PipelineTag from './pipeline-tag';
import Task from './task';
import Quotation from './quotation';
import Complaint from './complaint';
import Address from './address';
import Country from './country';
import State from './state';
import City from './city';
import PipelineAudit from './pipeline-audit';
import TaskAudit from './task-audit';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="entity-type/*" element={<EntityType />} />
        <Route path="master-static-group/*" element={<MasterStaticGroup />} />
        <Route path="master-static-type/*" element={<MasterStaticType />} />
        <Route path="master-category/*" element={<MasterCategory />} />
        <Route path="tenant/*" element={<Tenant />} />
        <Route path="encryption/*" element={<Encryption />} />
        <Route path="customer-company/*" element={<CustomerCompany />} />
        <Route path="department/*" element={<Department />} />
        <Route path="customer/*" element={<Customer />} />
        <Route path="lead/*" element={<Lead />} />
        <Route path="sub-pipeline/*" element={<SubPipeline />} />
        <Route path="sub-pipeline-open-stage/*" element={<SubPipelineOpenStage />} />
        <Route path="sub-pipeline-close-stage/*" element={<SubPipelineCloseStage />} />
        <Route path="pipeline/*" element={<Pipeline />} />
        <Route path="pipeline-tag/*" element={<PipelineTag />} />
        <Route path="task/*" element={<Task />} />
        <Route path="quotation/*" element={<Quotation />} />
        <Route path="complaint/*" element={<Complaint />} />
        <Route path="address/*" element={<Address />} />
        <Route path="country/*" element={<Country />} />
        <Route path="state/*" element={<State />} />
        <Route path="city/*" element={<City />} />
        <Route path="pipeline-audit/*" element={<PipelineAudit />} />
        <Route path="task-audit/*" element={<TaskAudit />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
