import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './entity-type.reducer';

export const EntityTypeDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const entityTypeEntity = useAppSelector(state => state.entityType.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="entityTypeDetailsHeading">
          <Translate contentKey="crmApp.entityType.detail.title">EntityType</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{entityTypeEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="crmApp.entityType.name">Name</Translate>
            </span>
          </dt>
          <dd>{entityTypeEntity.name}</dd>
          <dt>
            <span id="label">
              <Translate contentKey="crmApp.entityType.label">Label</Translate>
            </span>
          </dt>
          <dd>{entityTypeEntity.label}</dd>
        </dl>
        <Button tag={Link} to="/entity-type" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/entity-type/${entityTypeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default EntityTypeDetail;
