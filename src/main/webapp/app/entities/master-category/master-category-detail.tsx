import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './master-category.reducer';

export const MasterCategoryDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const masterCategoryEntity = useAppSelector(state => state.masterCategory.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="masterCategoryDetailsHeading">
          <Translate contentKey="crmApp.masterCategory.detail.title">MasterCategory</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{masterCategoryEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="crmApp.masterCategory.name">Name</Translate>
            </span>
          </dt>
          <dd>{masterCategoryEntity.name}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="crmApp.masterCategory.description">Description</Translate>
            </span>
          </dt>
          <dd>{masterCategoryEntity.description}</dd>
          <dt>
            <span id="code">
              <Translate contentKey="crmApp.masterCategory.code">Code</Translate>
            </span>
          </dt>
          <dd>{masterCategoryEntity.code}</dd>
        </dl>
        <Button tag={Link} to="/master-category" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/master-category/${masterCategoryEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default MasterCategoryDetail;
