import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICustomerCompany } from 'app/shared/model/customer-company.model';
import { getEntities as getCustomerCompanies } from 'app/entities/customer-company/customer-company.reducer';
import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { IMasterStaticType } from 'app/shared/model/master-static-type.model';
import { getEntities as getMasterStaticTypes } from 'app/entities/master-static-type/master-static-type.reducer';
import { IDepartment } from 'app/shared/model/department.model';
import { getEntities as getDepartments } from 'app/entities/department/department.reducer';
import { ITenant } from 'app/shared/model/tenant.model';
import { getEntities as getTenants } from 'app/entities/tenant/tenant.reducer';
import { IMasterCategory } from 'app/shared/model/master-category.model';
import { getEntities as getMasterCategories } from 'app/entities/master-category/master-category.reducer';
import { ICustomer } from 'app/shared/model/customer.model';
import { AccountType } from 'app/shared/model/enumerations/account-type.model';
import { AccountManagement } from 'app/shared/model/enumerations/account-management.model';
import { getEntity, updateEntity, createEntity, reset } from './customer.reducer';

export const CustomerUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const customerCompanies = useAppSelector(state => state.customerCompany.entities);
  const users = useAppSelector(state => state.userManagement.users);
  const masterStaticTypes = useAppSelector(state => state.masterStaticType.entities);
  const departments = useAppSelector(state => state.department.entities);
  const tenants = useAppSelector(state => state.tenant.entities);
  const masterCategories = useAppSelector(state => state.masterCategory.entities);
  const customerEntity = useAppSelector(state => state.customer.entity);
  const loading = useAppSelector(state => state.customer.loading);
  const updating = useAppSelector(state => state.customer.updating);
  const updateSuccess = useAppSelector(state => state.customer.updateSuccess);
  const accountTypeValues = Object.keys(AccountType);
  const accountManagementValues = Object.keys(AccountManagement);

  const handleClose = () => {
    navigate('/customer' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getCustomerCompanies({}));
    dispatch(getUsers({}));
    dispatch(getMasterStaticTypes({}));
    dispatch(getDepartments({}));
    dispatch(getTenants({}));
    dispatch(getMasterCategories({}));
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
    if (values.maxInvoiceAmount !== undefined && typeof values.maxInvoiceAmount !== 'number') {
      values.maxInvoiceAmount = Number(values.maxInvoiceAmount);
    }
    if (values.revenuePotential !== undefined && typeof values.revenuePotential !== 'number') {
      values.revenuePotential = Number(values.revenuePotential);
    }
    if (values.samplePotential !== undefined && typeof values.samplePotential !== 'number') {
      values.samplePotential = Number(values.samplePotential);
    }
    if (values.totalPipeline !== undefined && typeof values.totalPipeline !== 'number') {
      values.totalPipeline = Number(values.totalPipeline);
    }

    const entity = {
      ...customerEntity,
      ...values,
      masterCategories: mapIdList(values.masterCategories),
      company: customerCompanies.find(it => it.id.toString() === values.company.toString()),
      user: users.find(it => it.id.toString() === values.user.toString()),
      outstandingPerson: users.find(it => it.id.toString() === values.outstandingPerson.toString()),
      customerType: masterStaticTypes.find(it => it.id.toString() === values.customerType.toString()),
      customerStatus: masterStaticTypes.find(it => it.id.toString() === values.customerStatus.toString()),
      ownershipType: masterStaticTypes.find(it => it.id.toString() === values.ownershipType.toString()),
      industryType: masterStaticTypes.find(it => it.id.toString() === values.industryType.toString()),
      customerCategory: masterStaticTypes.find(it => it.id.toString() === values.customerCategory.toString()),
      paymentTerms: masterStaticTypes.find(it => it.id.toString() === values.paymentTerms.toString()),
      invoiceFrequency: masterStaticTypes.find(it => it.id.toString() === values.invoiceFrequency.toString()),
      gstTreatment: masterStaticTypes.find(it => it.id.toString() === values.gstTreatment.toString()),
      department: departments.find(it => it.id.toString() === values.department.toString()),
      tenat: tenants.find(it => it.id.toString() === values.tenat.toString()),
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
          accountType: 'DIRECT',
          accountManagement: 'SELF_MANAGED',
          ...customerEntity,
          company: customerEntity?.company?.id,
          user: customerEntity?.user?.id,
          customerType: customerEntity?.customerType?.id,
          customerStatus: customerEntity?.customerStatus?.id,
          ownershipType: customerEntity?.ownershipType?.id,
          industryType: customerEntity?.industryType?.id,
          customerCategory: customerEntity?.customerCategory?.id,
          paymentTerms: customerEntity?.paymentTerms?.id,
          invoiceFrequency: customerEntity?.invoiceFrequency?.id,
          gstTreatment: customerEntity?.gstTreatment?.id,
          outstandingPerson: customerEntity?.outstandingPerson?.id,
          department: customerEntity?.department?.id,
          tenat: customerEntity?.tenat?.id,
          masterCategories: customerEntity?.masterCategories?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="crmApp.customer.home.createOrEditLabel" data-cy="CustomerCreateUpdateHeading">
            <Translate contentKey="crmApp.customer.home.createOrEditLabel">Create or edit a Customer</Translate>
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
                  id="customer-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField label={translate('crmApp.customer.name')} id="customer-name" name="name" data-cy="name" type="text" />
              <ValidatedField
                label={translate('crmApp.customer.description')}
                id="customer-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField
                label={translate('crmApp.customer.companyCity')}
                id="customer-companyCity"
                name="companyCity"
                data-cy="companyCity"
                type="text"
              />
              <ValidatedField
                label={translate('crmApp.customer.companyArea')}
                id="customer-companyArea"
                name="companyArea"
                data-cy="companyArea"
                type="text"
              />
              <ValidatedField
                label={translate('crmApp.customer.website')}
                id="customer-website"
                name="website"
                data-cy="website"
                type="text"
              />
              <ValidatedField
                label={translate('crmApp.customer.customerName')}
                id="customer-customerName"
                name="customerName"
                data-cy="customerName"
                type="text"
              />
              <ValidatedField
                label={translate('crmApp.customer.currencyType')}
                id="customer-currencyType"
                name="currencyType"
                data-cy="currencyType"
                type="text"
              />
              <ValidatedField
                label={translate('crmApp.customer.maxInvoiceAmount')}
                id="customer-maxInvoiceAmount"
                name="maxInvoiceAmount"
                data-cy="maxInvoiceAmount"
                type="text"
              />
              <ValidatedField label={translate('crmApp.customer.gstNo')} id="customer-gstNo" name="gstNo" data-cy="gstNo" type="text" />
              <ValidatedField label={translate('crmApp.customer.panNo')} id="customer-panNo" name="panNo" data-cy="panNo" type="text" />
              <ValidatedField
                label={translate('crmApp.customer.serviceTaxNo')}
                id="customer-serviceTaxNo"
                name="serviceTaxNo"
                data-cy="serviceTaxNo"
                type="text"
              />
              <ValidatedField label={translate('crmApp.customer.tanNo')} id="customer-tanNo" name="tanNo" data-cy="tanNo" type="text" />
              <ValidatedField
                label={translate('crmApp.customer.customFieldData')}
                id="customer-customFieldData"
                name="customFieldData"
                data-cy="customFieldData"
                type="textarea"
              />
              <ValidatedField
                label={translate('crmApp.customer.correlationId')}
                id="customer-correlationId"
                name="correlationId"
                data-cy="correlationId"
                type="text"
              />
              <ValidatedField
                label={translate('crmApp.customer.accountNo')}
                id="customer-accountNo"
                name="accountNo"
                data-cy="accountNo"
                type="text"
              />
              <ValidatedField
                label={translate('crmApp.customer.gstStateName')}
                id="customer-gstStateName"
                name="gstStateName"
                data-cy="gstStateName"
                type="text"
              />
              <ValidatedField
                label={translate('crmApp.customer.gstStateCode')}
                id="customer-gstStateCode"
                name="gstStateCode"
                data-cy="gstStateCode"
                type="text"
              />
              <ValidatedField
                label={translate('crmApp.customer.isSubmitSampleWithoutPO')}
                id="customer-isSubmitSampleWithoutPO"
                name="isSubmitSampleWithoutPO"
                data-cy="isSubmitSampleWithoutPO"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('crmApp.customer.isBlock')}
                id="customer-isBlock"
                name="isBlock"
                data-cy="isBlock"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('crmApp.customer.accountType')}
                id="customer-accountType"
                name="accountType"
                data-cy="accountType"
                type="select"
              >
                {accountTypeValues.map(accountType => (
                  <option value={accountType} key={accountType}>
                    {translate('crmApp.AccountType.' + accountType)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('crmApp.customer.accountManagement')}
                id="customer-accountManagement"
                name="accountManagement"
                data-cy="accountManagement"
                type="select"
              >
                {accountManagementValues.map(accountManagement => (
                  <option value={accountManagement} key={accountManagement}>
                    {translate('crmApp.AccountManagement.' + accountManagement)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('crmApp.customer.revenuePotential')}
                id="customer-revenuePotential"
                name="revenuePotential"
                data-cy="revenuePotential"
                type="text"
              />
              <ValidatedField
                label={translate('crmApp.customer.samplePotential')}
                id="customer-samplePotential"
                name="samplePotential"
                data-cy="samplePotential"
                type="text"
              />
              <ValidatedField
                label={translate('crmApp.customer.remarks')}
                id="customer-remarks"
                name="remarks"
                data-cy="remarks"
                type="textarea"
              />
              <ValidatedField
                label={translate('crmApp.customer.totalPipeline')}
                id="customer-totalPipeline"
                name="totalPipeline"
                data-cy="totalPipeline"
                type="text"
              />
              <ValidatedField label={translate('crmApp.customer.type')} id="customer-type" name="type" data-cy="type" type="text" />
              <ValidatedField
                id="customer-company"
                name="company"
                data-cy="company"
                label={translate('crmApp.customer.company')}
                type="select"
              >
                <option value="" key="0" />
                {customerCompanies
                  ? customerCompanies.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="customer-user" name="user" data-cy="user" label={translate('crmApp.customer.user')} type="select">
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
                id="customer-customerType"
                name="customerType"
                data-cy="customerType"
                label={translate('crmApp.customer.customerType')}
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
                id="customer-customerStatus"
                name="customerStatus"
                data-cy="customerStatus"
                label={translate('crmApp.customer.customerStatus')}
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
                id="customer-ownershipType"
                name="ownershipType"
                data-cy="ownershipType"
                label={translate('crmApp.customer.ownershipType')}
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
                id="customer-industryType"
                name="industryType"
                data-cy="industryType"
                label={translate('crmApp.customer.industryType')}
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
                id="customer-customerCategory"
                name="customerCategory"
                data-cy="customerCategory"
                label={translate('crmApp.customer.customerCategory')}
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
                id="customer-paymentTerms"
                name="paymentTerms"
                data-cy="paymentTerms"
                label={translate('crmApp.customer.paymentTerms')}
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
                id="customer-invoiceFrequency"
                name="invoiceFrequency"
                data-cy="invoiceFrequency"
                label={translate('crmApp.customer.invoiceFrequency')}
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
                id="customer-gstTreatment"
                name="gstTreatment"
                data-cy="gstTreatment"
                label={translate('crmApp.customer.gstTreatment')}
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
                id="customer-outstandingPerson"
                name="outstandingPerson"
                data-cy="outstandingPerson"
                label={translate('crmApp.customer.outstandingPerson')}
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
                id="customer-department"
                name="department"
                data-cy="department"
                label={translate('crmApp.customer.department')}
                type="select"
              >
                <option value="" key="0" />
                {departments
                  ? departments.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="customer-tenat" name="tenat" data-cy="tenat" label={translate('crmApp.customer.tenat')} type="select">
                <option value="" key="0" />
                {tenants
                  ? tenants.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                label={translate('crmApp.customer.masterCategories')}
                id="customer-masterCategories"
                data-cy="masterCategories"
                type="select"
                multiple
                name="masterCategories"
              >
                <option value="" key="0" />
                {masterCategories
                  ? masterCategories.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/customer" replace color="info">
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

export default CustomerUpdate;
