import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './complaint.reducer';

export const ComplaintDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const complaintEntity = useAppSelector(state => state.complaint.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="complaintDetailsHeading">
          <Translate contentKey="crmApp.complaint.detail.title">Complaint</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{complaintEntity.id}</dd>
          <dt>
            <span id="complaintNumber">
              <Translate contentKey="crmApp.complaint.complaintNumber">Complaint Number</Translate>
            </span>
          </dt>
          <dd>{complaintEntity.complaintNumber}</dd>
          <dt>
            <span id="complaintDate">
              <Translate contentKey="crmApp.complaint.complaintDate">Complaint Date</Translate>
            </span>
          </dt>
          <dd>
            {complaintEntity.complaintDate ? (
              <TextFormat value={complaintEntity.complaintDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="recordNumbers">
              <Translate contentKey="crmApp.complaint.recordNumbers">Record Numbers</Translate>
            </span>
          </dt>
          <dd>{complaintEntity.recordNumbers}</dd>
          <dt>
            <span id="customerContactNumber">
              <Translate contentKey="crmApp.complaint.customerContactNumber">Customer Contact Number</Translate>
            </span>
          </dt>
          <dd>{complaintEntity.customerContactNumber}</dd>
          <dt>
            <span id="customerContactEmail">
              <Translate contentKey="crmApp.complaint.customerContactEmail">Customer Contact Email</Translate>
            </span>
          </dt>
          <dd>{complaintEntity.customerContactEmail}</dd>
          <dt>
            <span id="complaintDescription">
              <Translate contentKey="crmApp.complaint.complaintDescription">Complaint Description</Translate>
            </span>
          </dt>
          <dd>{complaintEntity.complaintDescription}</dd>
          <dt>
            <span id="expectedClosureDate">
              <Translate contentKey="crmApp.complaint.expectedClosureDate">Expected Closure Date</Translate>
            </span>
          </dt>
          <dd>
            {complaintEntity.expectedClosureDate ? (
              <TextFormat value={complaintEntity.expectedClosureDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="rootCause">
              <Translate contentKey="crmApp.complaint.rootCause">Root Cause</Translate>
            </span>
          </dt>
          <dd>{complaintEntity.rootCause}</dd>
          <dt>
            <span id="complaintStatus">
              <Translate contentKey="crmApp.complaint.complaintStatus">Complaint Status</Translate>
            </span>
          </dt>
          <dd>{complaintEntity.complaintStatus}</dd>
          <dt>
            <span id="correctiveAction">
              <Translate contentKey="crmApp.complaint.correctiveAction">Corrective Action</Translate>
            </span>
          </dt>
          <dd>{complaintEntity.correctiveAction}</dd>
          <dt>
            <span id="preventiveAction">
              <Translate contentKey="crmApp.complaint.preventiveAction">Preventive Action</Translate>
            </span>
          </dt>
          <dd>{complaintEntity.preventiveAction}</dd>
          <dt>
            <span id="complaintClosureDate">
              <Translate contentKey="crmApp.complaint.complaintClosureDate">Complaint Closure Date</Translate>
            </span>
          </dt>
          <dd>
            {complaintEntity.complaintClosureDate ? (
              <TextFormat value={complaintEntity.complaintClosureDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="crmApp.complaint.customerName">Customer Name</Translate>
          </dt>
          <dd>{complaintEntity.customerName ? complaintEntity.customerName.id : ''}</dd>
          <dt>
            <Translate contentKey="crmApp.complaint.complaintRelatedTo">Complaint Related To</Translate>
          </dt>
          <dd>{complaintEntity.complaintRelatedTo ? complaintEntity.complaintRelatedTo.id : ''}</dd>
          <dt>
            <Translate contentKey="crmApp.complaint.typeOfComplaint">Type Of Complaint</Translate>
          </dt>
          <dd>{complaintEntity.typeOfComplaint ? complaintEntity.typeOfComplaint.id : ''}</dd>
          <dt>
            <Translate contentKey="crmApp.complaint.complaintRelatedPersons">Complaint Related Persons</Translate>
          </dt>
          <dd>
            {complaintEntity.complaintRelatedPersons
              ? complaintEntity.complaintRelatedPersons.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {complaintEntity.complaintRelatedPersons && i === complaintEntity.complaintRelatedPersons.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/complaint" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/complaint/${complaintEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ComplaintDetail;
