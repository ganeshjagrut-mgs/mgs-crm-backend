import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ITaskAudit } from 'app/shared/model/task-audit.model';
import { getEntity, updateEntity, createEntity, reset } from './task-audit.reducer';

export const TaskAuditUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const taskAuditEntity = useAppSelector(state => state.taskAudit.entity);
  const loading = useAppSelector(state => state.taskAudit.loading);
  const updating = useAppSelector(state => state.taskAudit.updating);
  const updateSuccess = useAppSelector(state => state.taskAudit.updateSuccess);

  const handleClose = () => {
    navigate('/task-audit' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    values.eventTimestamp = convertDateTimeToServer(values.eventTimestamp);

    const entity = {
      ...taskAuditEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          eventTimestamp: displayDefaultDateTime(),
        }
      : {
          ...taskAuditEntity,
          eventTimestamp: convertDateTimeFromServer(taskAuditEntity.eventTimestamp),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="crmApp.taskAudit.home.createOrEditLabel" data-cy="TaskAuditCreateUpdateHeading">
            <Translate contentKey="crmApp.taskAudit.home.createOrEditLabel">Create or edit a TaskAudit</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="task-audit-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('crmApp.taskAudit.eventTimestamp')}
                id="task-audit-eventTimestamp"
                name="eventTimestamp"
                data-cy="eventTimestamp"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('crmApp.taskAudit.action')}
                id="task-audit-action"
                name="action"
                data-cy="action"
                type="text"
              />
              <ValidatedField label={translate('crmApp.taskAudit.rowId')} id="task-audit-rowId" name="rowId" data-cy="rowId" type="text" />
              <ValidatedField
                label={translate('crmApp.taskAudit.changes')}
                id="task-audit-changes"
                name="changes"
                data-cy="changes"
                type="textarea"
              />
              <ValidatedField
                label={translate('crmApp.taskAudit.correlationId')}
                id="task-audit-correlationId"
                name="correlationId"
                data-cy="correlationId"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/task-audit" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default TaskAuditUpdate;
