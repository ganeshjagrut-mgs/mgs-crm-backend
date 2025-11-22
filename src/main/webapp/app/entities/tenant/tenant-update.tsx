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
import { IEncryption } from 'app/shared/model/encryption.model';
import { getEntities as getEncryptions } from 'app/entities/encryption/encryption.reducer';
import { ITenant } from 'app/shared/model/tenant.model';
import { getEntity, updateEntity, createEntity, reset } from './tenant.reducer';

export const TenantUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const encryptions = useAppSelector(state => state.encryption.entities);
  const tenantEntity = useAppSelector(state => state.tenant.entity);
  const loading = useAppSelector(state => state.tenant.loading);
  const updating = useAppSelector(state => state.tenant.updating);
  const updateSuccess = useAppSelector(state => state.tenant.updateSuccess);

  const handleClose = () => {
    navigate('/tenant' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUsers({}));
    dispatch(getEncryptions({}));
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
    if (values.subId !== undefined && typeof values.subId !== 'number') {
      values.subId = Number(values.subId);
    }

    const entity = {
      ...tenantEntity,
      ...values,
      users: mapIdList(values.users),
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
          ...tenantEntity,
          users: tenantEntity?.users?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="crmApp.tenant.home.createOrEditLabel" data-cy="TenantCreateUpdateHeading">
            <Translate contentKey="crmApp.tenant.home.createOrEditLabel">Create or edit a Tenant</Translate>
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
                  id="tenant-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('crmApp.tenant.companyName')}
                id="tenant-companyName"
                name="companyName"
                data-cy="companyName"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('crmApp.tenant.contactPerson')}
                id="tenant-contactPerson"
                name="contactPerson"
                data-cy="contactPerson"
                type="text"
              />
              <ValidatedField label={translate('crmApp.tenant.logo')} id="tenant-logo" name="logo" data-cy="logo" type="text" />
              <ValidatedField label={translate('crmApp.tenant.website')} id="tenant-website" name="website" data-cy="website" type="text" />
              <ValidatedField
                label={translate('crmApp.tenant.registrationNumber')}
                id="tenant-registrationNumber"
                name="registrationNumber"
                data-cy="registrationNumber"
                type="text"
              />
              <ValidatedField label={translate('crmApp.tenant.subId')} id="tenant-subId" name="subId" data-cy="subId" type="text" />
              <ValidatedField
                label={translate('crmApp.tenant.users')}
                id="tenant-users"
                data-cy="users"
                type="select"
                multiple
                name="users"
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/tenant" replace color="info">
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

export default TenantUpdate;
