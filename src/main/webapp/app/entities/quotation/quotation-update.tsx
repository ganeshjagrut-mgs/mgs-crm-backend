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
import { IQuotation } from 'app/shared/model/quotation.model';
import { DiscountLevelTypeEnum } from 'app/shared/model/enumerations/discount-level-type-enum.model';
import { DiscountTypeEnum } from 'app/shared/model/enumerations/discount-type-enum.model';
import { PDFGenerationStatus } from 'app/shared/model/enumerations/pdf-generation-status.model';
import { TestReportEmailStatus } from 'app/shared/model/enumerations/test-report-email-status.model';
import { PriceDataSourceEnum } from 'app/shared/model/enumerations/price-data-source-enum.model';
import { getEntity, updateEntity, createEntity, reset } from './quotation.reducer';

export const QuotationUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const customers = useAppSelector(state => state.customer.entities);
  const masterStaticTypes = useAppSelector(state => state.masterStaticType.entities);
  const quotationEntity = useAppSelector(state => state.quotation.entity);
  const loading = useAppSelector(state => state.quotation.loading);
  const updating = useAppSelector(state => state.quotation.updating);
  const updateSuccess = useAppSelector(state => state.quotation.updateSuccess);
  const discountLevelTypeEnumValues = Object.keys(DiscountLevelTypeEnum);
  const discountTypeEnumValues = Object.keys(DiscountTypeEnum);
  const pDFGenerationStatusValues = Object.keys(PDFGenerationStatus);
  const testReportEmailStatusValues = Object.keys(TestReportEmailStatus);
  const priceDataSourceEnumValues = Object.keys(PriceDataSourceEnum);

  const handleClose = () => {
    navigate('/quotation' + location.search);
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
    values.quotationDate = convertDateTimeToServer(values.quotationDate);
    values.referenceDate = convertDateTimeToServer(values.referenceDate);
    values.estimateDate = convertDateTimeToServer(values.estimateDate);
    values.validity = convertDateTimeToServer(values.validity);
    if (values.discountTypeValue !== undefined && typeof values.discountTypeValue !== 'number') {
      values.discountTypeValue = Number(values.discountTypeValue);
    }
    if (values.subTotal !== undefined && typeof values.subTotal !== 'number') {
      values.subTotal = Number(values.subTotal);
    }
    if (values.grandTotal !== undefined && typeof values.grandTotal !== 'number') {
      values.grandTotal = Number(values.grandTotal);
    }
    if (values.totalTaxAmount !== undefined && typeof values.totalTaxAmount !== 'number') {
      values.totalTaxAmount = Number(values.totalTaxAmount);
    }
    if (values.adjustmentAmount !== undefined && typeof values.adjustmentAmount !== 'number') {
      values.adjustmentAmount = Number(values.adjustmentAmount);
    }
    values.approvedAt = convertDateTimeToServer(values.approvedAt);

    const entity = {
      ...quotationEntity,
      ...values,
      user: users.find(it => it.id.toString() === values.user.toString()),
      customer: customers.find(it => it.id.toString() === values.customer.toString()),
      paymentTerm: masterStaticTypes.find(it => it.id.toString() === values.paymentTerm.toString()),
      quotationStatus: masterStaticTypes.find(it => it.id.toString() === values.quotationStatus.toString()),
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
          quotationDate: displayDefaultDateTime(),
          referenceDate: displayDefaultDateTime(),
          estimateDate: displayDefaultDateTime(),
          validity: displayDefaultDateTime(),
          approvedAt: displayDefaultDateTime(),
        }
      : {
          discountLevelType: 'ITEM_LEVEL',
          discountType: 'PERCENTAGE',
          pdfGenerationStatus: 'PENDING',
          emailStatus: 'NOT_SENT',
          priceDataSource: 'MANUAL',
          ...quotationEntity,
          quotationDate: convertDateTimeFromServer(quotationEntity.quotationDate),
          referenceDate: convertDateTimeFromServer(quotationEntity.referenceDate),
          estimateDate: convertDateTimeFromServer(quotationEntity.estimateDate),
          validity: convertDateTimeFromServer(quotationEntity.validity),
          approvedAt: convertDateTimeFromServer(quotationEntity.approvedAt),
          user: quotationEntity?.user?.id,
          customer: quotationEntity?.customer?.id,
          paymentTerm: quotationEntity?.paymentTerm?.id,
          quotationStatus: quotationEntity?.quotationStatus?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="crmApp.quotation.home.createOrEditLabel" data-cy="QuotationCreateUpdateHeading">
            <Translate contentKey="crmApp.quotation.home.createOrEditLabel">Create or edit a Quotation</Translate>
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
                  id="quotation-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('crmApp.quotation.quotationNumber')}
                id="quotation-quotationNumber"
                name="quotationNumber"
                data-cy="quotationNumber"
                type="text"
              />
              <ValidatedField
                label={translate('crmApp.quotation.quotationDate')}
                id="quotation-quotationDate"
                name="quotationDate"
                data-cy="quotationDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('crmApp.quotation.referenceNumber')}
                id="quotation-referenceNumber"
                name="referenceNumber"
                data-cy="referenceNumber"
                type="text"
              />
              <ValidatedField
                label={translate('crmApp.quotation.referenceDate')}
                id="quotation-referenceDate"
                name="referenceDate"
                data-cy="referenceDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('crmApp.quotation.estimateDate')}
                id="quotation-estimateDate"
                name="estimateDate"
                data-cy="estimateDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('crmApp.quotation.subject')}
                id="quotation-subject"
                name="subject"
                data-cy="subject"
                type="text"
              />
              <ValidatedField
                label={translate('crmApp.quotation.validity')}
                id="quotation-validity"
                name="validity"
                data-cy="validity"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('crmApp.quotation.additionalNote')}
                id="quotation-additionalNote"
                name="additionalNote"
                data-cy="additionalNote"
                type="textarea"
              />
              <ValidatedField
                label={translate('crmApp.quotation.discountLevelType')}
                id="quotation-discountLevelType"
                name="discountLevelType"
                data-cy="discountLevelType"
                type="select"
              >
                {discountLevelTypeEnumValues.map(discountLevelTypeEnum => (
                  <option value={discountLevelTypeEnum} key={discountLevelTypeEnum}>
                    {translate('crmApp.DiscountLevelTypeEnum.' + discountLevelTypeEnum)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('crmApp.quotation.discountType')}
                id="quotation-discountType"
                name="discountType"
                data-cy="discountType"
                type="select"
              >
                {discountTypeEnumValues.map(discountTypeEnum => (
                  <option value={discountTypeEnum} key={discountTypeEnum}>
                    {translate('crmApp.DiscountTypeEnum.' + discountTypeEnum)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('crmApp.quotation.discountTypeValue')}
                id="quotation-discountTypeValue"
                name="discountTypeValue"
                data-cy="discountTypeValue"
                type="text"
              />
              <ValidatedField
                label={translate('crmApp.quotation.currency')}
                id="quotation-currency"
                name="currency"
                data-cy="currency"
                type="text"
              />
              <ValidatedField
                label={translate('crmApp.quotation.subTotal')}
                id="quotation-subTotal"
                name="subTotal"
                data-cy="subTotal"
                type="text"
              />
              <ValidatedField
                label={translate('crmApp.quotation.grandTotal')}
                id="quotation-grandTotal"
                name="grandTotal"
                data-cy="grandTotal"
                type="text"
              />
              <ValidatedField
                label={translate('crmApp.quotation.totalTaxAmount')}
                id="quotation-totalTaxAmount"
                name="totalTaxAmount"
                data-cy="totalTaxAmount"
                type="text"
              />
              <ValidatedField
                label={translate('crmApp.quotation.adjustmentAmount')}
                id="quotation-adjustmentAmount"
                name="adjustmentAmount"
                data-cy="adjustmentAmount"
                type="text"
              />
              <ValidatedField
                label={translate('crmApp.quotation.statusReason')}
                id="quotation-statusReason"
                name="statusReason"
                data-cy="statusReason"
                type="text"
              />
              <ValidatedField
                label={translate('crmApp.quotation.pdfGenerationStatus')}
                id="quotation-pdfGenerationStatus"
                name="pdfGenerationStatus"
                data-cy="pdfGenerationStatus"
                type="select"
              >
                {pDFGenerationStatusValues.map(pDFGenerationStatus => (
                  <option value={pDFGenerationStatus} key={pDFGenerationStatus}>
                    {translate('crmApp.PDFGenerationStatus.' + pDFGenerationStatus)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('crmApp.quotation.emailStatus')}
                id="quotation-emailStatus"
                name="emailStatus"
                data-cy="emailStatus"
                type="select"
              >
                {testReportEmailStatusValues.map(testReportEmailStatus => (
                  <option value={testReportEmailStatus} key={testReportEmailStatus}>
                    {translate('crmApp.TestReportEmailStatus.' + testReportEmailStatus)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('crmApp.quotation.emailFailureReason')}
                id="quotation-emailFailureReason"
                name="emailFailureReason"
                data-cy="emailFailureReason"
                type="text"
              />
              <ValidatedField
                label={translate('crmApp.quotation.customParagraph')}
                id="quotation-customParagraph"
                name="customParagraph"
                data-cy="customParagraph"
                type="textarea"
              />
              <ValidatedField
                label={translate('crmApp.quotation.correlationId')}
                id="quotation-correlationId"
                name="correlationId"
                data-cy="correlationId"
                type="text"
              />
              <ValidatedField
                label={translate('crmApp.quotation.approvedAt')}
                id="quotation-approvedAt"
                name="approvedAt"
                data-cy="approvedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('crmApp.quotation.priceDataSource')}
                id="quotation-priceDataSource"
                name="priceDataSource"
                data-cy="priceDataSource"
                type="select"
              >
                {priceDataSourceEnumValues.map(priceDataSourceEnum => (
                  <option value={priceDataSourceEnum} key={priceDataSourceEnum}>
                    {translate('crmApp.PriceDataSourceEnum.' + priceDataSourceEnum)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField id="quotation-user" name="user" data-cy="user" label={translate('crmApp.quotation.user')} type="select">
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
                id="quotation-customer"
                name="customer"
                data-cy="customer"
                label={translate('crmApp.quotation.customer')}
                type="select"
                required
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
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <ValidatedField
                id="quotation-paymentTerm"
                name="paymentTerm"
                data-cy="paymentTerm"
                label={translate('crmApp.quotation.paymentTerm')}
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
                id="quotation-quotationStatus"
                name="quotationStatus"
                data-cy="quotationStatus"
                label={translate('crmApp.quotation.quotationStatus')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/quotation" replace color="info">
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

export default QuotationUpdate;
