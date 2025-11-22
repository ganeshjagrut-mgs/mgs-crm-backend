import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './pipeline-audit.reducer';

export const PipelineAuditDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const pipelineAuditEntity = useAppSelector(state => state.pipelineAudit.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="pipelineAuditDetailsHeading">
          <Translate contentKey="crmApp.pipelineAudit.detail.title">PipelineAudit</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{pipelineAuditEntity.id}</dd>
          <dt>
            <span id="eventTimestamp">
              <Translate contentKey="crmApp.pipelineAudit.eventTimestamp">Event Timestamp</Translate>
            </span>
          </dt>
          <dd>
            {pipelineAuditEntity.eventTimestamp ? (
              <TextFormat value={pipelineAuditEntity.eventTimestamp} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="action">
              <Translate contentKey="crmApp.pipelineAudit.action">Action</Translate>
            </span>
          </dt>
          <dd>{pipelineAuditEntity.action}</dd>
          <dt>
            <span id="rowId">
              <Translate contentKey="crmApp.pipelineAudit.rowId">Row Id</Translate>
            </span>
          </dt>
          <dd>{pipelineAuditEntity.rowId}</dd>
          <dt>
            <span id="changes">
              <Translate contentKey="crmApp.pipelineAudit.changes">Changes</Translate>
            </span>
          </dt>
          <dd>{pipelineAuditEntity.changes}</dd>
          <dt>
            <span id="correlationId">
              <Translate contentKey="crmApp.pipelineAudit.correlationId">Correlation Id</Translate>
            </span>
          </dt>
          <dd>{pipelineAuditEntity.correlationId}</dd>
        </dl>
        <Button tag={Link} to="/pipeline-audit" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/pipeline-audit/${pipelineAuditEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PipelineAuditDetail;
