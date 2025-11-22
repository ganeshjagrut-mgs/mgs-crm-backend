import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './task-audit.reducer';

export const TaskAuditDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const taskAuditEntity = useAppSelector(state => state.taskAudit.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="taskAuditDetailsHeading">
          <Translate contentKey="crmApp.taskAudit.detail.title">TaskAudit</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{taskAuditEntity.id}</dd>
          <dt>
            <span id="eventTimestamp">
              <Translate contentKey="crmApp.taskAudit.eventTimestamp">Event Timestamp</Translate>
            </span>
          </dt>
          <dd>
            {taskAuditEntity.eventTimestamp ? (
              <TextFormat value={taskAuditEntity.eventTimestamp} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="action">
              <Translate contentKey="crmApp.taskAudit.action">Action</Translate>
            </span>
          </dt>
          <dd>{taskAuditEntity.action}</dd>
          <dt>
            <span id="rowId">
              <Translate contentKey="crmApp.taskAudit.rowId">Row Id</Translate>
            </span>
          </dt>
          <dd>{taskAuditEntity.rowId}</dd>
          <dt>
            <span id="changes">
              <Translate contentKey="crmApp.taskAudit.changes">Changes</Translate>
            </span>
          </dt>
          <dd>{taskAuditEntity.changes}</dd>
          <dt>
            <span id="correlationId">
              <Translate contentKey="crmApp.taskAudit.correlationId">Correlation Id</Translate>
            </span>
          </dt>
          <dd>{taskAuditEntity.correlationId}</dd>
        </dl>
        <Button tag={Link} to="/task-audit" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/task-audit/${taskAuditEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default TaskAuditDetail;
