import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IPipelineAudit } from 'app/shared/model/pipeline-audit.model';
import { getEntity, updateEntity, createEntity, reset } from './pipeline-audit.reducer';

export const PipelineAuditUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const pipelineAuditEntity = useAppSelector(state => state.pipelineAudit.entity);
  const loading = useAppSelector(state => state.pipelineAudit.loading);
  const updating = useAppSelector(state => state.pipelineAudit.updating);
  const updateSuccess = useAppSelector(state => state.pipelineAudit.updateSuccess);

  const handleClose = () => {
    navigate('/pipeline-audit' + location.search);
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
      ...pipelineAuditEntity,
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
          ...pipelineAuditEntity,
          eventTimestamp: convertDateTimeFromServer(pipelineAuditEntity.eventTimestamp),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="crmApp.pipelineAudit.home.createOrEditLabel" data-cy="PipelineAuditCreateUpdateHeading">
            <Translate contentKey="crmApp.pipelineAudit.home.createOrEditLabel">Create or edit a PipelineAudit</Translate>
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
                  id="pipeline-audit-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('crmApp.pipelineAudit.eventTimestamp')}
                id="pipeline-audit-eventTimestamp"
                name="eventTimestamp"
                data-cy="eventTimestamp"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('crmApp.pipelineAudit.action')}
                id="pipeline-audit-action"
                name="action"
                data-cy="action"
                type="text"
              />
              <ValidatedField
                label={translate('crmApp.pipelineAudit.rowId')}
                id="pipeline-audit-rowId"
                name="rowId"
                data-cy="rowId"
                type="text"
              />
              <ValidatedField
                label={translate('crmApp.pipelineAudit.changes')}
                id="pipeline-audit-changes"
                name="changes"
                data-cy="changes"
                type="textarea"
              />
              <ValidatedField
                label={translate('crmApp.pipelineAudit.correlationId')}
                id="pipeline-audit-correlationId"
                name="correlationId"
                data-cy="correlationId"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/pipeline-audit" replace color="info">
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

export default PipelineAuditUpdate;
