import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './customer.reducer';

export const CustomerDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const customerEntity = useAppSelector(state => state.customer.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="customerDetailsHeading">
          <Translate contentKey="crmApp.customer.detail.title">Customer</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{customerEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="crmApp.customer.name">Name</Translate>
            </span>
          </dt>
          <dd>{customerEntity.name}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="crmApp.customer.description">Description</Translate>
            </span>
          </dt>
          <dd>{customerEntity.description}</dd>
          <dt>
            <span id="companyCity">
              <Translate contentKey="crmApp.customer.companyCity">Company City</Translate>
            </span>
          </dt>
          <dd>{customerEntity.companyCity}</dd>
          <dt>
            <span id="companyArea">
              <Translate contentKey="crmApp.customer.companyArea">Company Area</Translate>
            </span>
          </dt>
          <dd>{customerEntity.companyArea}</dd>
          <dt>
            <span id="website">
              <Translate contentKey="crmApp.customer.website">Website</Translate>
            </span>
          </dt>
          <dd>{customerEntity.website}</dd>
          <dt>
            <span id="customerName">
              <Translate contentKey="crmApp.customer.customerName">Customer Name</Translate>
            </span>
          </dt>
          <dd>{customerEntity.customerName}</dd>
          <dt>
            <span id="currencyType">
              <Translate contentKey="crmApp.customer.currencyType">Currency Type</Translate>
            </span>
          </dt>
          <dd>{customerEntity.currencyType}</dd>
          <dt>
            <span id="maxInvoiceAmount">
              <Translate contentKey="crmApp.customer.maxInvoiceAmount">Max Invoice Amount</Translate>
            </span>
          </dt>
          <dd>{customerEntity.maxInvoiceAmount}</dd>
          <dt>
            <span id="gstNo">
              <Translate contentKey="crmApp.customer.gstNo">Gst No</Translate>
            </span>
          </dt>
          <dd>{customerEntity.gstNo}</dd>
          <dt>
            <span id="panNo">
              <Translate contentKey="crmApp.customer.panNo">Pan No</Translate>
            </span>
          </dt>
          <dd>{customerEntity.panNo}</dd>
          <dt>
            <span id="serviceTaxNo">
              <Translate contentKey="crmApp.customer.serviceTaxNo">Service Tax No</Translate>
            </span>
          </dt>
          <dd>{customerEntity.serviceTaxNo}</dd>
          <dt>
            <span id="tanNo">
              <Translate contentKey="crmApp.customer.tanNo">Tan No</Translate>
            </span>
          </dt>
          <dd>{customerEntity.tanNo}</dd>
          <dt>
            <span id="customFieldData">
              <Translate contentKey="crmApp.customer.customFieldData">Custom Field Data</Translate>
            </span>
          </dt>
          <dd>{customerEntity.customFieldData}</dd>
          <dt>
            <span id="correlationId">
              <Translate contentKey="crmApp.customer.correlationId">Correlation Id</Translate>
            </span>
          </dt>
          <dd>{customerEntity.correlationId}</dd>
          <dt>
            <span id="accountNo">
              <Translate contentKey="crmApp.customer.accountNo">Account No</Translate>
            </span>
          </dt>
          <dd>{customerEntity.accountNo}</dd>
          <dt>
            <span id="gstStateName">
              <Translate contentKey="crmApp.customer.gstStateName">Gst State Name</Translate>
            </span>
          </dt>
          <dd>{customerEntity.gstStateName}</dd>
          <dt>
            <span id="gstStateCode">
              <Translate contentKey="crmApp.customer.gstStateCode">Gst State Code</Translate>
            </span>
          </dt>
          <dd>{customerEntity.gstStateCode}</dd>
          <dt>
            <span id="isSubmitSampleWithoutPO">
              <Translate contentKey="crmApp.customer.isSubmitSampleWithoutPO">Is Submit Sample Without PO</Translate>
            </span>
          </dt>
          <dd>{customerEntity.isSubmitSampleWithoutPO ? 'true' : 'false'}</dd>
          <dt>
            <span id="isBlock">
              <Translate contentKey="crmApp.customer.isBlock">Is Block</Translate>
            </span>
          </dt>
          <dd>{customerEntity.isBlock ? 'true' : 'false'}</dd>
          <dt>
            <span id="accountType">
              <Translate contentKey="crmApp.customer.accountType">Account Type</Translate>
            </span>
          </dt>
          <dd>{customerEntity.accountType}</dd>
          <dt>
            <span id="accountManagement">
              <Translate contentKey="crmApp.customer.accountManagement">Account Management</Translate>
            </span>
          </dt>
          <dd>{customerEntity.accountManagement}</dd>
          <dt>
            <span id="revenuePotential">
              <Translate contentKey="crmApp.customer.revenuePotential">Revenue Potential</Translate>
            </span>
          </dt>
          <dd>{customerEntity.revenuePotential}</dd>
          <dt>
            <span id="samplePotential">
              <Translate contentKey="crmApp.customer.samplePotential">Sample Potential</Translate>
            </span>
          </dt>
          <dd>{customerEntity.samplePotential}</dd>
          <dt>
            <span id="remarks">
              <Translate contentKey="crmApp.customer.remarks">Remarks</Translate>
            </span>
          </dt>
          <dd>{customerEntity.remarks}</dd>
          <dt>
            <span id="totalPipeline">
              <Translate contentKey="crmApp.customer.totalPipeline">Total Pipeline</Translate>
            </span>
          </dt>
          <dd>{customerEntity.totalPipeline}</dd>
          <dt>
            <span id="type">
              <Translate contentKey="crmApp.customer.type">Type</Translate>
            </span>
          </dt>
          <dd>{customerEntity.type}</dd>
          <dt>
            <Translate contentKey="crmApp.customer.company">Company</Translate>
          </dt>
          <dd>{customerEntity.company ? customerEntity.company.id : ''}</dd>
          <dt>
            <Translate contentKey="crmApp.customer.user">User</Translate>
          </dt>
          <dd>{customerEntity.user ? customerEntity.user.id : ''}</dd>
          <dt>
            <Translate contentKey="crmApp.customer.customerType">Customer Type</Translate>
          </dt>
          <dd>{customerEntity.customerType ? customerEntity.customerType.id : ''}</dd>
          <dt>
            <Translate contentKey="crmApp.customer.customerStatus">Customer Status</Translate>
          </dt>
          <dd>{customerEntity.customerStatus ? customerEntity.customerStatus.id : ''}</dd>
          <dt>
            <Translate contentKey="crmApp.customer.ownershipType">Ownership Type</Translate>
          </dt>
          <dd>{customerEntity.ownershipType ? customerEntity.ownershipType.id : ''}</dd>
          <dt>
            <Translate contentKey="crmApp.customer.industryType">Industry Type</Translate>
          </dt>
          <dd>{customerEntity.industryType ? customerEntity.industryType.id : ''}</dd>
          <dt>
            <Translate contentKey="crmApp.customer.customerCategory">Customer Category</Translate>
          </dt>
          <dd>{customerEntity.customerCategory ? customerEntity.customerCategory.id : ''}</dd>
          <dt>
            <Translate contentKey="crmApp.customer.paymentTerms">Payment Terms</Translate>
          </dt>
          <dd>{customerEntity.paymentTerms ? customerEntity.paymentTerms.id : ''}</dd>
          <dt>
            <Translate contentKey="crmApp.customer.invoiceFrequency">Invoice Frequency</Translate>
          </dt>
          <dd>{customerEntity.invoiceFrequency ? customerEntity.invoiceFrequency.id : ''}</dd>
          <dt>
            <Translate contentKey="crmApp.customer.gstTreatment">Gst Treatment</Translate>
          </dt>
          <dd>{customerEntity.gstTreatment ? customerEntity.gstTreatment.id : ''}</dd>
          <dt>
            <Translate contentKey="crmApp.customer.outstandingPerson">Outstanding Person</Translate>
          </dt>
          <dd>{customerEntity.outstandingPerson ? customerEntity.outstandingPerson.id : ''}</dd>
          <dt>
            <Translate contentKey="crmApp.customer.department">Department</Translate>
          </dt>
          <dd>{customerEntity.department ? customerEntity.department.id : ''}</dd>
          <dt>
            <Translate contentKey="crmApp.customer.tenat">Tenat</Translate>
          </dt>
          <dd>{customerEntity.tenat ? customerEntity.tenat.id : ''}</dd>
          <dt>
            <Translate contentKey="crmApp.customer.masterCategories">Master Categories</Translate>
          </dt>
          <dd>
            {customerEntity.masterCategories
              ? customerEntity.masterCategories.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {customerEntity.masterCategories && i === customerEntity.masterCategories.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/customer" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/customer/${customerEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CustomerDetail;
