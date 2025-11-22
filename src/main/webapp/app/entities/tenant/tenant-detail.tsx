import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './tenant.reducer';

export const TenantDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const tenantEntity = useAppSelector(state => state.tenant.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="tenantDetailsHeading">
          <Translate contentKey="crmApp.tenant.detail.title">Tenant</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{tenantEntity.id}</dd>
          <dt>
            <span id="companyName">
              <Translate contentKey="crmApp.tenant.companyName">Company Name</Translate>
            </span>
          </dt>
          <dd>{tenantEntity.companyName}</dd>
          <dt>
            <span id="contactPerson">
              <Translate contentKey="crmApp.tenant.contactPerson">Contact Person</Translate>
            </span>
          </dt>
          <dd>{tenantEntity.contactPerson}</dd>
          <dt>
            <span id="logo">
              <Translate contentKey="crmApp.tenant.logo">Logo</Translate>
            </span>
          </dt>
          <dd>{tenantEntity.logo}</dd>
          <dt>
            <span id="website">
              <Translate contentKey="crmApp.tenant.website">Website</Translate>
            </span>
          </dt>
          <dd>{tenantEntity.website}</dd>
          <dt>
            <span id="registrationNumber">
              <Translate contentKey="crmApp.tenant.registrationNumber">Registration Number</Translate>
            </span>
          </dt>
          <dd>{tenantEntity.registrationNumber}</dd>
          <dt>
            <span id="subId">
              <Translate contentKey="crmApp.tenant.subId">Sub Id</Translate>
            </span>
          </dt>
          <dd>{tenantEntity.subId}</dd>
          <dt>
            <Translate contentKey="crmApp.tenant.users">Users</Translate>
          </dt>
          <dd>
            {tenantEntity.users
              ? tenantEntity.users.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {tenantEntity.users && i === tenantEntity.users.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/tenant" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/tenant/${tenantEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default TenantDetail;
