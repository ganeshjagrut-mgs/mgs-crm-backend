import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './master-static-group.reducer';

export const MasterStaticGroupDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const masterStaticGroupEntity = useAppSelector(state => state.masterStaticGroup.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="masterStaticGroupDetailsHeading">
          <Translate contentKey="crmApp.masterStaticGroup.detail.title">MasterStaticGroup</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{masterStaticGroupEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="crmApp.masterStaticGroup.name">Name</Translate>
            </span>
          </dt>
          <dd>{masterStaticGroupEntity.name}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="crmApp.masterStaticGroup.description">Description</Translate>
            </span>
          </dt>
          <dd>{masterStaticGroupEntity.description}</dd>
          <dt>
            <span id="uiLabel">
              <Translate contentKey="crmApp.masterStaticGroup.uiLabel">Ui Label</Translate>
            </span>
          </dt>
          <dd>{masterStaticGroupEntity.uiLabel}</dd>
          <dt>
            <span id="editable">
              <Translate contentKey="crmApp.masterStaticGroup.editable">Editable</Translate>
            </span>
          </dt>
          <dd>{masterStaticGroupEntity.editable}</dd>
          <dt>
            <Translate contentKey="crmApp.masterStaticGroup.entityType">Entity Type</Translate>
          </dt>
          <dd>{masterStaticGroupEntity.entityType ? masterStaticGroupEntity.entityType.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/master-static-group" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/master-static-group/${masterStaticGroupEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default MasterStaticGroupDetail;
