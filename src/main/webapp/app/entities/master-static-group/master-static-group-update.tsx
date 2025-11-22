import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IEntityType } from 'app/shared/model/entity-type.model';
import { getEntities as getEntityTypes } from 'app/entities/entity-type/entity-type.reducer';
import { IMasterStaticGroup } from 'app/shared/model/master-static-group.model';
import { MasterStaticGroupEditable } from 'app/shared/model/enumerations/master-static-group-editable.model';
import { getEntity, updateEntity, createEntity, reset } from './master-static-group.reducer';

export const MasterStaticGroupUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const entityTypes = useAppSelector(state => state.entityType.entities);
  const masterStaticGroupEntity = useAppSelector(state => state.masterStaticGroup.entity);
  const loading = useAppSelector(state => state.masterStaticGroup.loading);
  const updating = useAppSelector(state => state.masterStaticGroup.updating);
  const updateSuccess = useAppSelector(state => state.masterStaticGroup.updateSuccess);
  const masterStaticGroupEditableValues = Object.keys(MasterStaticGroupEditable);

  const handleClose = () => {
    navigate('/master-static-group' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getEntityTypes({}));
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
      ...masterStaticGroupEntity,
      ...values,
      entityType: entityTypes.find(it => it.id.toString() === values.entityType.toString()),
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
          editable: 'YES',
          ...masterStaticGroupEntity,
          entityType: masterStaticGroupEntity?.entityType?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="crmApp.masterStaticGroup.home.createOrEditLabel" data-cy="MasterStaticGroupCreateUpdateHeading">
            <Translate contentKey="crmApp.masterStaticGroup.home.createOrEditLabel">Create or edit a MasterStaticGroup</Translate>
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
                  id="master-static-group-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('crmApp.masterStaticGroup.name')}
                id="master-static-group-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('crmApp.masterStaticGroup.description')}
                id="master-static-group-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField
                label={translate('crmApp.masterStaticGroup.uiLabel')}
                id="master-static-group-uiLabel"
                name="uiLabel"
                data-cy="uiLabel"
                type="text"
              />
              <ValidatedField
                label={translate('crmApp.masterStaticGroup.editable')}
                id="master-static-group-editable"
                name="editable"
                data-cy="editable"
                type="select"
              >
                {masterStaticGroupEditableValues.map(masterStaticGroupEditable => (
                  <option value={masterStaticGroupEditable} key={masterStaticGroupEditable}>
                    {translate('crmApp.MasterStaticGroupEditable.' + masterStaticGroupEditable)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                id="master-static-group-entityType"
                name="entityType"
                data-cy="entityType"
                label={translate('crmApp.masterStaticGroup.entityType')}
                type="select"
              >
                <option value="" key="0" />
                {entityTypes
                  ? entityTypes.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/master-static-group" replace color="info">
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

export default MasterStaticGroupUpdate;
