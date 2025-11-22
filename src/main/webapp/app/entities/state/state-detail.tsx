import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './state.reducer';

export const StateDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const stateEntity = useAppSelector(state => state.state.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="stateDetailsHeading">
          <Translate contentKey="crmApp.state.detail.title">State</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{stateEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="crmApp.state.name">Name</Translate>
            </span>
          </dt>
          <dd>{stateEntity.name}</dd>
          <dt>
            <span id="code">
              <Translate contentKey="crmApp.state.code">Code</Translate>
            </span>
          </dt>
          <dd>{stateEntity.code}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="crmApp.state.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>{stateEntity.createdAt ? <TextFormat value={stateEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="crmApp.state.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{stateEntity.createdBy}</dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="crmApp.state.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>{stateEntity.updatedAt ? <TextFormat value={stateEntity.updatedAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="updatedBy">
              <Translate contentKey="crmApp.state.updatedBy">Updated By</Translate>
            </span>
          </dt>
          <dd>{stateEntity.updatedBy}</dd>
          <dt>
            <span id="active">
              <Translate contentKey="crmApp.state.active">Active</Translate>
            </span>
          </dt>
          <dd>{stateEntity.active ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="crmApp.state.country">Country</Translate>
          </dt>
          <dd>{stateEntity.country ? stateEntity.country.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/state" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/state/${stateEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default StateDetail;
