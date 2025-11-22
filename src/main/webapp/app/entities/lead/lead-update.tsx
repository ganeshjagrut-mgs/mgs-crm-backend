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
import { ILead } from 'app/shared/model/lead.model';
import { getEntity, updateEntity, createEntity, reset } from './lead.reducer';

export const LeadUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const customers = useAppSelector(state => state.customer.entities);
  const masterStaticTypes = useAppSelector(state => state.masterStaticType.entities);
  const leadEntity = useAppSelector(state => state.lead.entity);
  const loading = useAppSelector(state => state.lead.loading);
  const updating = useAppSelector(state => state.lead.updating);
  const updateSuccess = useAppSelector(state => state.lead.updateSuccess);

  const handleClose = () => {
    navigate('/lead' + location.search);
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
    if (values.annualRevenue !== undefined && typeof values.annualRevenue !== 'number') {
      values.annualRevenue = Number(values.annualRevenue);
    }

    const entity = {
      ...leadEntity,
      ...values,
      user: users.find(it => it.id.toString() === values.user.toString()),
      customer: customers.find(it => it.id.toString() === values.customer.toString()),
      leadSource: masterStaticTypes.find(it => it.id.toString() === values.leadSource.toString()),
      industryType: masterStaticTypes.find(it => it.id.toString() === values.industryType.toString()),
      leadStatus: masterStaticTypes.find(it => it.id.toString() === values.leadStatus.toString()),
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
          ...leadEntity,
          user: leadEntity?.user?.id,
          customer: leadEntity?.customer?.id,
          leadSource: leadEntity?.leadSource?.id,
          industryType: leadEntity?.industryType?.id,
          leadStatus: leadEntity?.leadStatus?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="crmApp.lead.home.createOrEditLabel" data-cy="LeadCreateUpdateHeading">
            <Translate contentKey="crmApp.lead.home.createOrEditLabel">Create or edit a Lead</Translate>
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
                  id="lead-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('crmApp.lead.name')}
                id="lead-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('crmApp.lead.leadNumber')}
                id="lead-leadNumber"
                name="leadNumber"
                data-cy="leadNumber"
                type="text"
              />
              <ValidatedField
                label={translate('crmApp.lead.annualRevenue')}
                id="lead-annualRevenue"
                name="annualRevenue"
                data-cy="annualRevenue"
                type="text"
              />
              <ValidatedField id="lead-user" name="user" data-cy="user" label={translate('crmApp.lead.user')} type="select">
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="lead-customer" name="customer" data-cy="customer" label={translate('crmApp.lead.customer')} type="select">
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
                id="lead-leadSource"
                name="leadSource"
                data-cy="leadSource"
                label={translate('crmApp.lead.leadSource')}
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
                id="lead-industryType"
                name="industryType"
                data-cy="industryType"
                label={translate('crmApp.lead.industryType')}
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
                id="lead-leadStatus"
                name="leadStatus"
                data-cy="leadStatus"
                label={translate('crmApp.lead.leadStatus')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/lead" replace color="info">
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

export default LeadUpdate;
