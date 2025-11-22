import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICustomer } from 'app/shared/model/customer.model';
import { getEntities as getCustomers } from 'app/entities/customer/customer.reducer';
import { IMasterStaticType } from 'app/shared/model/master-static-type.model';
import { getEntities as getMasterStaticTypes } from 'app/entities/master-static-type/master-static-type.reducer';
import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { IComplaint } from 'app/shared/model/complaint.model';
import { ComplaintStatus } from 'app/shared/model/enumerations/complaint-status.model';
import { getEntity, updateEntity, createEntity, reset } from './complaint.reducer';

export const ComplaintUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const customers = useAppSelector(state => state.customer.entities);
  const masterStaticTypes = useAppSelector(state => state.masterStaticType.entities);
  const users = useAppSelector(state => state.userManagement.users);
  const complaintEntity = useAppSelector(state => state.complaint.entity);
  const loading = useAppSelector(state => state.complaint.loading);
  const updating = useAppSelector(state => state.complaint.updating);
  const updateSuccess = useAppSelector(state => state.complaint.updateSuccess);
  const complaintStatusValues = Object.keys(ComplaintStatus);

  const handleClose = () => {
    navigate('/complaint' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getCustomers({}));
    dispatch(getMasterStaticTypes({}));
    dispatch(getUsers({}));
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
    values.complaintDate = convertDateTimeToServer(values.complaintDate);
    values.expectedClosureDate = convertDateTimeToServer(values.expectedClosureDate);
    values.complaintClosureDate = convertDateTimeToServer(values.complaintClosureDate);

    const entity = {
      ...complaintEntity,
      ...values,
      complaintRelatedPersons: mapIdList(values.complaintRelatedPersons),
      customerName: customers.find(it => it.id.toString() === values.customerName.toString()),
      complaintRelatedTo: masterStaticTypes.find(it => it.id.toString() === values.complaintRelatedTo.toString()),
      typeOfComplaint: masterStaticTypes.find(it => it.id.toString() === values.typeOfComplaint.toString()),
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
          complaintDate: displayDefaultDateTime(),
          expectedClosureDate: displayDefaultDateTime(),
          complaintClosureDate: displayDefaultDateTime(),
        }
      : {
          complaintStatus: 'OPEN',
          ...complaintEntity,
          complaintDate: convertDateTimeFromServer(complaintEntity.complaintDate),
          expectedClosureDate: convertDateTimeFromServer(complaintEntity.expectedClosureDate),
          complaintClosureDate: convertDateTimeFromServer(complaintEntity.complaintClosureDate),
          customerName: complaintEntity?.customerName?.id,
          complaintRelatedTo: complaintEntity?.complaintRelatedTo?.id,
          typeOfComplaint: complaintEntity?.typeOfComplaint?.id,
          complaintRelatedPersons: complaintEntity?.complaintRelatedPersons?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="crmApp.complaint.home.createOrEditLabel" data-cy="ComplaintCreateUpdateHeading">
            <Translate contentKey="crmApp.complaint.home.createOrEditLabel">Create or edit a Complaint</Translate>
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
                  id="complaint-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('crmApp.complaint.complaintNumber')}
                id="complaint-complaintNumber"
                name="complaintNumber"
                data-cy="complaintNumber"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('crmApp.complaint.complaintDate')}
                id="complaint-complaintDate"
                name="complaintDate"
                data-cy="complaintDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('crmApp.complaint.recordNumbers')}
                id="complaint-recordNumbers"
                name="recordNumbers"
                data-cy="recordNumbers"
                type="text"
              />
              <ValidatedField
                label={translate('crmApp.complaint.customerContactNumber')}
                id="complaint-customerContactNumber"
                name="customerContactNumber"
                data-cy="customerContactNumber"
                type="text"
              />
              <ValidatedField
                label={translate('crmApp.complaint.customerContactEmail')}
                id="complaint-customerContactEmail"
                name="customerContactEmail"
                data-cy="customerContactEmail"
                type="text"
              />
              <ValidatedField
                label={translate('crmApp.complaint.complaintDescription')}
                id="complaint-complaintDescription"
                name="complaintDescription"
                data-cy="complaintDescription"
                type="textarea"
              />
              <ValidatedField
                label={translate('crmApp.complaint.expectedClosureDate')}
                id="complaint-expectedClosureDate"
                name="expectedClosureDate"
                data-cy="expectedClosureDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('crmApp.complaint.rootCause')}
                id="complaint-rootCause"
                name="rootCause"
                data-cy="rootCause"
                type="textarea"
              />
              <ValidatedField
                label={translate('crmApp.complaint.complaintStatus')}
                id="complaint-complaintStatus"
                name="complaintStatus"
                data-cy="complaintStatus"
                type="select"
              >
                {complaintStatusValues.map(complaintStatus => (
                  <option value={complaintStatus} key={complaintStatus}>
                    {translate('crmApp.ComplaintStatus.' + complaintStatus)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('crmApp.complaint.correctiveAction')}
                id="complaint-correctiveAction"
                name="correctiveAction"
                data-cy="correctiveAction"
                type="textarea"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('crmApp.complaint.preventiveAction')}
                id="complaint-preventiveAction"
                name="preventiveAction"
                data-cy="preventiveAction"
                type="textarea"
              />
              <ValidatedField
                label={translate('crmApp.complaint.complaintClosureDate')}
                id="complaint-complaintClosureDate"
                name="complaintClosureDate"
                data-cy="complaintClosureDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="complaint-customerName"
                name="customerName"
                data-cy="customerName"
                label={translate('crmApp.complaint.customerName')}
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
                id="complaint-complaintRelatedTo"
                name="complaintRelatedTo"
                data-cy="complaintRelatedTo"
                label={translate('crmApp.complaint.complaintRelatedTo')}
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
                id="complaint-typeOfComplaint"
                name="typeOfComplaint"
                data-cy="typeOfComplaint"
                label={translate('crmApp.complaint.typeOfComplaint')}
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
                label={translate('crmApp.complaint.complaintRelatedPersons')}
                id="complaint-complaintRelatedPersons"
                data-cy="complaintRelatedPersons"
                type="select"
                multiple
                name="complaintRelatedPersons"
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/complaint" replace color="info">
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

export default ComplaintUpdate;
