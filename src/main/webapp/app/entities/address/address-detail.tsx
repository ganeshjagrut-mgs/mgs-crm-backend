import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './address.reducer';

export const AddressDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const addressEntity = useAppSelector(state => state.address.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="addressDetailsHeading">
          <Translate contentKey="crmApp.address.detail.title">Address</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{addressEntity.id}</dd>
          <dt>
            <span id="addressLine1">
              <Translate contentKey="crmApp.address.addressLine1">Address Line 1</Translate>
            </span>
          </dt>
          <dd>{addressEntity.addressLine1}</dd>
          <dt>
            <span id="addressLine2">
              <Translate contentKey="crmApp.address.addressLine2">Address Line 2</Translate>
            </span>
          </dt>
          <dd>{addressEntity.addressLine2}</dd>
          <dt>
            <span id="pincode">
              <Translate contentKey="crmApp.address.pincode">Pincode</Translate>
            </span>
          </dt>
          <dd>{addressEntity.pincode}</dd>
          <dt>
            <span id="isPrimary">
              <Translate contentKey="crmApp.address.isPrimary">Is Primary</Translate>
            </span>
          </dt>
          <dd>{addressEntity.isPrimary ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="crmApp.address.city">City</Translate>
          </dt>
          <dd>{addressEntity.city ? addressEntity.city.id : ''}</dd>
          <dt>
            <Translate contentKey="crmApp.address.state">State</Translate>
          </dt>
          <dd>{addressEntity.state ? addressEntity.state.id : ''}</dd>
          <dt>
            <Translate contentKey="crmApp.address.country">Country</Translate>
          </dt>
          <dd>{addressEntity.country ? addressEntity.country.id : ''}</dd>
          <dt>
            <Translate contentKey="crmApp.address.customer">Customer</Translate>
          </dt>
          <dd>{addressEntity.customer ? addressEntity.customer.id : ''}</dd>
          <dt>
            <Translate contentKey="crmApp.address.tenant">Tenant</Translate>
          </dt>
          <dd>{addressEntity.tenant ? addressEntity.tenant.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/address" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/address/${addressEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AddressDetail;
