import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './lead.reducer';

export const LeadDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const leadEntity = useAppSelector(state => state.lead.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="leadDetailsHeading">
          <Translate contentKey="crmApp.lead.detail.title">Lead</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{leadEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="crmApp.lead.name">Name</Translate>
            </span>
          </dt>
          <dd>{leadEntity.name}</dd>
          <dt>
            <span id="leadNumber">
              <Translate contentKey="crmApp.lead.leadNumber">Lead Number</Translate>
            </span>
          </dt>
          <dd>{leadEntity.leadNumber}</dd>
          <dt>
            <span id="annualRevenue">
              <Translate contentKey="crmApp.lead.annualRevenue">Annual Revenue</Translate>
            </span>
          </dt>
          <dd>{leadEntity.annualRevenue}</dd>
          <dt>
            <Translate contentKey="crmApp.lead.user">User</Translate>
          </dt>
          <dd>{leadEntity.user ? leadEntity.user.id : ''}</dd>
          <dt>
            <Translate contentKey="crmApp.lead.customer">Customer</Translate>
          </dt>
          <dd>{leadEntity.customer ? leadEntity.customer.id : ''}</dd>
          <dt>
            <Translate contentKey="crmApp.lead.leadSource">Lead Source</Translate>
          </dt>
          <dd>{leadEntity.leadSource ? leadEntity.leadSource.id : ''}</dd>
          <dt>
            <Translate contentKey="crmApp.lead.industryType">Industry Type</Translate>
          </dt>
          <dd>{leadEntity.industryType ? leadEntity.industryType.id : ''}</dd>
          <dt>
            <Translate contentKey="crmApp.lead.leadStatus">Lead Status</Translate>
          </dt>
          <dd>{leadEntity.leadStatus ? leadEntity.leadStatus.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/lead" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/lead/${leadEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default LeadDetail;
