import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './master-static-type.reducer';

export const MasterStaticTypeDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const masterStaticTypeEntity = useAppSelector(state => state.masterStaticType.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="masterStaticTypeDetailsHeading">
          <Translate contentKey="crmApp.masterStaticType.detail.title">MasterStaticType</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{masterStaticTypeEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="crmApp.masterStaticType.name">Name</Translate>
            </span>
          </dt>
          <dd>{masterStaticTypeEntity.name}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="crmApp.masterStaticType.description">Description</Translate>
            </span>
          </dt>
          <dd>{masterStaticTypeEntity.description}</dd>
          <dt>
            <span id="displayOrder">
              <Translate contentKey="crmApp.masterStaticType.displayOrder">Display Order</Translate>
            </span>
          </dt>
          <dd>{masterStaticTypeEntity.displayOrder}</dd>
          <dt>
            <span id="isActive">
              <Translate contentKey="crmApp.masterStaticType.isActive">Is Active</Translate>
            </span>
          </dt>
          <dd>{masterStaticTypeEntity.isActive ? 'true' : 'false'}</dd>
        </dl>
        <Button tag={Link} to="/master-static-type" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/master-static-type/${masterStaticTypeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default MasterStaticTypeDetail;
