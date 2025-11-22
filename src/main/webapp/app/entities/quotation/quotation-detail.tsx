import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './quotation.reducer';

export const QuotationDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const quotationEntity = useAppSelector(state => state.quotation.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="quotationDetailsHeading">
          <Translate contentKey="crmApp.quotation.detail.title">Quotation</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{quotationEntity.id}</dd>
          <dt>
            <span id="quotationNumber">
              <Translate contentKey="crmApp.quotation.quotationNumber">Quotation Number</Translate>
            </span>
          </dt>
          <dd>{quotationEntity.quotationNumber}</dd>
          <dt>
            <span id="quotationDate">
              <Translate contentKey="crmApp.quotation.quotationDate">Quotation Date</Translate>
            </span>
          </dt>
          <dd>
            {quotationEntity.quotationDate ? (
              <TextFormat value={quotationEntity.quotationDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="referenceNumber">
              <Translate contentKey="crmApp.quotation.referenceNumber">Reference Number</Translate>
            </span>
          </dt>
          <dd>{quotationEntity.referenceNumber}</dd>
          <dt>
            <span id="referenceDate">
              <Translate contentKey="crmApp.quotation.referenceDate">Reference Date</Translate>
            </span>
          </dt>
          <dd>
            {quotationEntity.referenceDate ? (
              <TextFormat value={quotationEntity.referenceDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="estimateDate">
              <Translate contentKey="crmApp.quotation.estimateDate">Estimate Date</Translate>
            </span>
          </dt>
          <dd>
            {quotationEntity.estimateDate ? <TextFormat value={quotationEntity.estimateDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="subject">
              <Translate contentKey="crmApp.quotation.subject">Subject</Translate>
            </span>
          </dt>
          <dd>{quotationEntity.subject}</dd>
          <dt>
            <span id="validity">
              <Translate contentKey="crmApp.quotation.validity">Validity</Translate>
            </span>
          </dt>
          <dd>{quotationEntity.validity ? <TextFormat value={quotationEntity.validity} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="additionalNote">
              <Translate contentKey="crmApp.quotation.additionalNote">Additional Note</Translate>
            </span>
          </dt>
          <dd>{quotationEntity.additionalNote}</dd>
          <dt>
            <span id="discountLevelType">
              <Translate contentKey="crmApp.quotation.discountLevelType">Discount Level Type</Translate>
            </span>
          </dt>
          <dd>{quotationEntity.discountLevelType}</dd>
          <dt>
            <span id="discountType">
              <Translate contentKey="crmApp.quotation.discountType">Discount Type</Translate>
            </span>
          </dt>
          <dd>{quotationEntity.discountType}</dd>
          <dt>
            <span id="discountTypeValue">
              <Translate contentKey="crmApp.quotation.discountTypeValue">Discount Type Value</Translate>
            </span>
          </dt>
          <dd>{quotationEntity.discountTypeValue}</dd>
          <dt>
            <span id="currency">
              <Translate contentKey="crmApp.quotation.currency">Currency</Translate>
            </span>
          </dt>
          <dd>{quotationEntity.currency}</dd>
          <dt>
            <span id="subTotal">
              <Translate contentKey="crmApp.quotation.subTotal">Sub Total</Translate>
            </span>
          </dt>
          <dd>{quotationEntity.subTotal}</dd>
          <dt>
            <span id="grandTotal">
              <Translate contentKey="crmApp.quotation.grandTotal">Grand Total</Translate>
            </span>
          </dt>
          <dd>{quotationEntity.grandTotal}</dd>
          <dt>
            <span id="totalTaxAmount">
              <Translate contentKey="crmApp.quotation.totalTaxAmount">Total Tax Amount</Translate>
            </span>
          </dt>
          <dd>{quotationEntity.totalTaxAmount}</dd>
          <dt>
            <span id="adjustmentAmount">
              <Translate contentKey="crmApp.quotation.adjustmentAmount">Adjustment Amount</Translate>
            </span>
          </dt>
          <dd>{quotationEntity.adjustmentAmount}</dd>
          <dt>
            <span id="statusReason">
              <Translate contentKey="crmApp.quotation.statusReason">Status Reason</Translate>
            </span>
          </dt>
          <dd>{quotationEntity.statusReason}</dd>
          <dt>
            <span id="pdfGenerationStatus">
              <Translate contentKey="crmApp.quotation.pdfGenerationStatus">Pdf Generation Status</Translate>
            </span>
          </dt>
          <dd>{quotationEntity.pdfGenerationStatus}</dd>
          <dt>
            <span id="emailStatus">
              <Translate contentKey="crmApp.quotation.emailStatus">Email Status</Translate>
            </span>
          </dt>
          <dd>{quotationEntity.emailStatus}</dd>
          <dt>
            <span id="emailFailureReason">
              <Translate contentKey="crmApp.quotation.emailFailureReason">Email Failure Reason</Translate>
            </span>
          </dt>
          <dd>{quotationEntity.emailFailureReason}</dd>
          <dt>
            <span id="customParagraph">
              <Translate contentKey="crmApp.quotation.customParagraph">Custom Paragraph</Translate>
            </span>
          </dt>
          <dd>{quotationEntity.customParagraph}</dd>
          <dt>
            <span id="correlationId">
              <Translate contentKey="crmApp.quotation.correlationId">Correlation Id</Translate>
            </span>
          </dt>
          <dd>{quotationEntity.correlationId}</dd>
          <dt>
            <span id="approvedAt">
              <Translate contentKey="crmApp.quotation.approvedAt">Approved At</Translate>
            </span>
          </dt>
          <dd>
            {quotationEntity.approvedAt ? <TextFormat value={quotationEntity.approvedAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="priceDataSource">
              <Translate contentKey="crmApp.quotation.priceDataSource">Price Data Source</Translate>
            </span>
          </dt>
          <dd>{quotationEntity.priceDataSource}</dd>
          <dt>
            <Translate contentKey="crmApp.quotation.user">User</Translate>
          </dt>
          <dd>{quotationEntity.user ? quotationEntity.user.id : ''}</dd>
          <dt>
            <Translate contentKey="crmApp.quotation.customer">Customer</Translate>
          </dt>
          <dd>{quotationEntity.customer ? quotationEntity.customer.id : ''}</dd>
          <dt>
            <Translate contentKey="crmApp.quotation.paymentTerm">Payment Term</Translate>
          </dt>
          <dd>{quotationEntity.paymentTerm ? quotationEntity.paymentTerm.id : ''}</dd>
          <dt>
            <Translate contentKey="crmApp.quotation.quotationStatus">Quotation Status</Translate>
          </dt>
          <dd>{quotationEntity.quotationStatus ? quotationEntity.quotationStatus.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/quotation" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/quotation/${quotationEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default QuotationDetail;
