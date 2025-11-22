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
import { IPipeline } from 'app/shared/model/pipeline.model';
import { getEntities as getPipelines } from 'app/entities/pipeline/pipeline.reducer';
import { ITask } from 'app/shared/model/task.model';
import { TaskType } from 'app/shared/model/enumerations/task-type.model';
import { Status } from 'app/shared/model/enumerations/status.model';
import { getEntity, updateEntity, createEntity, reset } from './task.reducer';

export const TaskUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const customers = useAppSelector(state => state.customer.entities);
  const masterStaticTypes = useAppSelector(state => state.masterStaticType.entities);
  const pipelines = useAppSelector(state => state.pipeline.entities);
  const taskEntity = useAppSelector(state => state.task.entity);
  const loading = useAppSelector(state => state.task.loading);
  const updating = useAppSelector(state => state.task.updating);
  const updateSuccess = useAppSelector(state => state.task.updateSuccess);
  const taskTypeValues = Object.keys(TaskType);
  const statusValues = Object.keys(Status);

  const handleClose = () => {
    navigate('/task' + location.search);
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
    dispatch(getPipelines({}));
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

    const entity = {
      ...taskEntity,
      ...values,
      taskOwner: users.find(it => it.id.toString() === values.taskOwner.toString()),
      customer: customers.find(it => it.id.toString() === values.customer.toString()),
      relatedTo: masterStaticTypes.find(it => it.id.toString() === values.relatedTo.toString()),
      pipeline: pipelines.find(it => it.id.toString() === values.pipeline.toString()),
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
          taskType: 'CALL',
          status: 'PENDING',
          ...taskEntity,
          taskOwner: taskEntity?.taskOwner?.id,
          customer: taskEntity?.customer?.id,
          relatedTo: taskEntity?.relatedTo?.id,
          pipeline: taskEntity?.pipeline?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="crmApp.task.home.createOrEditLabel" data-cy="TaskCreateUpdateHeading">
            <Translate contentKey="crmApp.task.home.createOrEditLabel">Create or edit a Task</Translate>
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
                  id="task-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField label={translate('crmApp.task.taskType')} id="task-taskType" name="taskType" data-cy="taskType" type="select">
                {taskTypeValues.map(taskType => (
                  <option value={taskType} key={taskType}>
                    {translate('crmApp.TaskType.' + taskType)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('crmApp.task.dueDate')}
                id="task-dueDate"
                name="dueDate"
                data-cy="dueDate"
                type="date"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('crmApp.task.taskName')}
                id="task-taskName"
                name="taskName"
                data-cy="taskName"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField label={translate('crmApp.task.status')} id="task-status" name="status" data-cy="status" type="select">
                {statusValues.map(status => (
                  <option value={status} key={status}>
                    {translate('crmApp.Status.' + status)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('crmApp.task.taskCompletionDate')}
                id="task-taskCompletionDate"
                name="taskCompletionDate"
                data-cy="taskCompletionDate"
                type="date"
              />
              <ValidatedField
                label={translate('crmApp.task.correlationId')}
                id="task-correlationId"
                name="correlationId"
                data-cy="correlationId"
                type="text"
              />
              <ValidatedField
                id="task-taskOwner"
                name="taskOwner"
                data-cy="taskOwner"
                label={translate('crmApp.task.taskOwner')}
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
              <ValidatedField id="task-customer" name="customer" data-cy="customer" label={translate('crmApp.task.customer')} type="select">
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
                id="task-relatedTo"
                name="relatedTo"
                data-cy="relatedTo"
                label={translate('crmApp.task.relatedTo')}
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
              <ValidatedField id="task-pipeline" name="pipeline" data-cy="pipeline" label={translate('crmApp.task.pipeline')} type="select">
                <option value="" key="0" />
                {pipelines
                  ? pipelines.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/task" replace color="info">
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

export default TaskUpdate;
