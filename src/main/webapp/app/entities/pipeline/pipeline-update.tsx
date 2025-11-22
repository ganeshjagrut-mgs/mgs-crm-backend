import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { ICustomer } from 'app/shared/model/customer.model';
import { getEntities as getCustomers } from 'app/entities/customer/customer.reducer';
import { IMasterStaticType } from 'app/shared/model/master-static-type.model';
import { getEntities as getMasterStaticTypes } from 'app/entities/master-static-type/master-static-type.reducer';
import { ISubPipeline } from 'app/shared/model/sub-pipeline.model';
import { getEntities as getSubPipelines } from 'app/entities/sub-pipeline/sub-pipeline.reducer';
import { IPipeline } from 'app/shared/model/pipeline.model';
import { getEntity, updateEntity, createEntity, reset } from './pipeline.reducer';

export const PipelineUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const customers = useAppSelector(state => state.customer.entities);
  const masterStaticTypes = useAppSelector(state => state.masterStaticType.entities);
  const subPipelines = useAppSelector(state => state.subPipeline.entities);
  const pipelineEntity = useAppSelector(state => state.pipeline.entity);
  const loading = useAppSelector(state => state.pipeline.loading);
  const updating = useAppSelector(state => state.pipeline.updating);
  const updateSuccess = useAppSelector(state => state.pipeline.updateSuccess);

  const handleClose = () => {
    navigate('/pipeline' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUsers({}));
    dispatch(getCustomers({}));
    dispatch(getMasterStaticTypes({}));
    dispatch(getSubPipelines({}));
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
    if (values.totalAmount !== undefined && typeof values.totalAmount !== 'number') {
      values.totalAmount = Number(values.totalAmount);
    }
    if (values.noOfSamples !== undefined && typeof values.noOfSamples !== 'number') {
      values.noOfSamples = Number(values.noOfSamples);
    }

    const entity = {
      ...pipelineEntity,
      ...values,
      pipelineOwner: users.find(it => it.id.toString() === values.pipelineOwner.toString()),
      customer: customers.find(it => it.id.toString() === values.customer.toString()),
      stageOfPipeline: masterStaticTypes.find(it => it.id.toString() === values.stageOfPipeline.toString()),
      subPipeline: subPipelines.find(it => it.id.toString() === values.subPipeline.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...pipelineEntity,
          pipelineOwner: pipelineEntity?.pipelineOwner?.id,
          customer: pipelineEntity?.customer?.id,
          stageOfPipeline: pipelineEntity?.stageOfPipeline?.id,
          subPipeline: pipelineEntity?.subPipeline?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="crmApp.pipeline.home.createOrEditLabel" data-cy="PipelineCreateUpdateHeading">
            <Translate contentKey="crmApp.pipeline.home.createOrEditLabel">Create or edit a Pipeline</Translate>
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
                  id="pipeline-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('crmApp.pipeline.pipelineName')}
                id="pipeline-pipelineName"
                name="pipelineName"
                data-cy="pipelineName"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('crmApp.pipeline.totalAmount')}
                id="pipeline-totalAmount"
                name="totalAmount"
                data-cy="totalAmount"
                type="text"
              />
              <ValidatedField
                label={translate('crmApp.pipeline.noOfSamples')}
                id="pipeline-noOfSamples"
                name="noOfSamples"
                data-cy="noOfSamples"
                type="text"
              />
              <ValidatedField
                label={translate('crmApp.pipeline.correlationId')}
                id="pipeline-correlationId"
                name="correlationId"
                data-cy="correlationId"
                type="text"
              />
              <ValidatedField
                id="pipeline-pipelineOwner"
                name="pipelineOwner"
                data-cy="pipelineOwner"
                label={translate('crmApp.pipeline.pipelineOwner')}
                type="select"
              >
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="pipeline-customer"
                name="customer"
                data-cy="customer"
                label={translate('crmApp.pipeline.customer')}
                type="select"
              >
                <option value="" key="0" />
                {customers
                  ? customers.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="pipeline-stageOfPipeline"
                name="stageOfPipeline"
                data-cy="stageOfPipeline"
                label={translate('crmApp.pipeline.stageOfPipeline')}
                type="select"
              >
                <option value="" key="0" />
                {masterStaticTypes
                  ? masterStaticTypes.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="pipeline-subPipeline"
                name="subPipeline"
                data-cy="subPipeline"
                label={translate('crmApp.pipeline.subPipeline')}
                type="select"
              >
                <option value="" key="0" />
                {subPipelines
                  ? subPipelines.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/pipeline" replace color="info">
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

export default PipelineUpdate;
