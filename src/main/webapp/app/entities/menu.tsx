import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/entity-type">
        <Translate contentKey="global.menu.entities.entityType" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/master-static-group">
        <Translate contentKey="global.menu.entities.masterStaticGroup" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/master-static-type">
        <Translate contentKey="global.menu.entities.masterStaticType" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/master-category">
        <Translate contentKey="global.menu.entities.masterCategory" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/tenant">
        <Translate contentKey="global.menu.entities.tenant" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/encryption">
        <Translate contentKey="global.menu.entities.encryption" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/customer-company">
        <Translate contentKey="global.menu.entities.customerCompany" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/department">
        <Translate contentKey="global.menu.entities.department" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/customer">
        <Translate contentKey="global.menu.entities.customer" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/lead">
        <Translate contentKey="global.menu.entities.lead" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/sub-pipeline">
        <Translate contentKey="global.menu.entities.subPipeline" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/sub-pipeline-open-stage">
        <Translate contentKey="global.menu.entities.subPipelineOpenStage" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/sub-pipeline-close-stage">
        <Translate contentKey="global.menu.entities.subPipelineCloseStage" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/pipeline">
        <Translate contentKey="global.menu.entities.pipeline" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/pipeline-tag">
        <Translate contentKey="global.menu.entities.pipelineTag" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/task">
        <Translate contentKey="global.menu.entities.task" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/quotation">
        <Translate contentKey="global.menu.entities.quotation" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/complaint">
        <Translate contentKey="global.menu.entities.complaint" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/address">
        <Translate contentKey="global.menu.entities.address" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/country">
        <Translate contentKey="global.menu.entities.country" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/state">
        <Translate contentKey="global.menu.entities.state" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/city">
        <Translate contentKey="global.menu.entities.city" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/pipeline-audit">
        <Translate contentKey="global.menu.entities.pipelineAudit" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/task-audit">
        <Translate contentKey="global.menu.entities.taskAudit" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
