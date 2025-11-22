import entityType from 'app/entities/entity-type/entity-type.reducer';
import masterStaticGroup from 'app/entities/master-static-group/master-static-group.reducer';
import masterStaticType from 'app/entities/master-static-type/master-static-type.reducer';
import masterCategory from 'app/entities/master-category/master-category.reducer';
import tenant from 'app/entities/tenant/tenant.reducer';
import encryption from 'app/entities/encryption/encryption.reducer';
import customerCompany from 'app/entities/customer-company/customer-company.reducer';
import department from 'app/entities/department/department.reducer';
import customer from 'app/entities/customer/customer.reducer';
import lead from 'app/entities/lead/lead.reducer';
import subPipeline from 'app/entities/sub-pipeline/sub-pipeline.reducer';
import subPipelineOpenStage from 'app/entities/sub-pipeline-open-stage/sub-pipeline-open-stage.reducer';
import subPipelineCloseStage from 'app/entities/sub-pipeline-close-stage/sub-pipeline-close-stage.reducer';
import pipeline from 'app/entities/pipeline/pipeline.reducer';
import pipelineTag from 'app/entities/pipeline-tag/pipeline-tag.reducer';
import task from 'app/entities/task/task.reducer';
import quotation from 'app/entities/quotation/quotation.reducer';
import complaint from 'app/entities/complaint/complaint.reducer';
import address from 'app/entities/address/address.reducer';
import country from 'app/entities/country/country.reducer';
import state from 'app/entities/state/state.reducer';
import city from 'app/entities/city/city.reducer';
import pipelineAudit from 'app/entities/pipeline-audit/pipeline-audit.reducer';
import taskAudit from 'app/entities/task-audit/task-audit.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  entityType,
  masterStaticGroup,
  masterStaticType,
  masterCategory,
  tenant,
  encryption,
  customerCompany,
  department,
  customer,
  lead,
  subPipeline,
  subPipelineOpenStage,
  subPipelineCloseStage,
  pipeline,
  pipelineTag,
  task,
  quotation,
  complaint,
  address,
  country,
  state,
  city,
  pipelineAudit,
  taskAudit,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
