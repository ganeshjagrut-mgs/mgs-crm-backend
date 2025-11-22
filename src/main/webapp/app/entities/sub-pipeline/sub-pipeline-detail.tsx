import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './sub-pipeline.reducer';

export const SubPipelineDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const subPipelineEntity = useAppSelector(state => state.subPipeline.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="subPipelineDetailsHeading">
          <Translate contentKey="crmApp.subPipeline.detail.title">SubPipeline</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{subPipelineEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="crmApp.subPipeline.name">Name</Translate>
            </span>
          </dt>
          <dd>{subPipelineEntity.name}</dd>
          <dt>
            <span id="index">
              <Translate contentKey="crmApp.subPipeline.index">Index</Translate>
            </span>
          </dt>
          <dd>{subPipelineEntity.index}</dd>
        </dl>
        <Button tag={Link} to="/sub-pipeline" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/sub-pipeline/${subPipelineEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default SubPipelineDetail;
