import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './sub-pipeline-open-stage.reducer';

export const SubPipelineOpenStageDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const subPipelineOpenStageEntity = useAppSelector(state => state.subPipelineOpenStage.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="subPipelineOpenStageDetailsHeading">
          <Translate contentKey="crmApp.subPipelineOpenStage.detail.title">SubPipelineOpenStage</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{subPipelineOpenStageEntity.id}</dd>
          <dt>
            <span id="index">
              <Translate contentKey="crmApp.subPipelineOpenStage.index">Index</Translate>
            </span>
          </dt>
          <dd>{subPipelineOpenStageEntity.index}</dd>
          <dt>
            <Translate contentKey="crmApp.subPipelineOpenStage.stage">Stage</Translate>
          </dt>
          <dd>{subPipelineOpenStageEntity.stage ? subPipelineOpenStageEntity.stage.id : ''}</dd>
          <dt>
            <Translate contentKey="crmApp.subPipelineOpenStage.subPipeline">Sub Pipeline</Translate>
          </dt>
          <dd>{subPipelineOpenStageEntity.subPipeline ? subPipelineOpenStageEntity.subPipeline.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/sub-pipeline-open-stage" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/sub-pipeline-open-stage/${subPipelineOpenStageEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default SubPipelineOpenStageDetail;
