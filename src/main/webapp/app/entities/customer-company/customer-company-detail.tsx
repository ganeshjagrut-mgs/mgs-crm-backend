import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './customer-company.reducer';

export const CustomerCompanyDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const customerCompanyEntity = useAppSelector(state => state.customerCompany.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="customerCompanyDetailsHeading">
          <Translate contentKey="crmApp.customerCompany.detail.title">CustomerCompany</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{customerCompanyEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="crmApp.customerCompany.name">Name</Translate>
            </span>
          </dt>
          <dd>{customerCompanyEntity.name}</dd>
          <dt>
            <span id="code">
              <Translate contentKey="crmApp.customerCompany.code">Code</Translate>
            </span>
          </dt>
          <dd>{customerCompanyEntity.code}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="crmApp.customerCompany.description">Description</Translate>
            </span>
          </dt>
          <dd>{customerCompanyEntity.description}</dd>
          <dt>
            <span id="website">
              <Translate contentKey="crmApp.customerCompany.website">Website</Translate>
            </span>
          </dt>
          <dd>{customerCompanyEntity.website}</dd>
          <dt>
            <span id="registrationNumber">
              <Translate contentKey="crmApp.customerCompany.registrationNumber">Registration Number</Translate>
            </span>
          </dt>
          <dd>{customerCompanyEntity.registrationNumber}</dd>
        </dl>
        <Button tag={Link} to="/customer-company" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/customer-company/${customerCompanyEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CustomerCompanyDetail;
