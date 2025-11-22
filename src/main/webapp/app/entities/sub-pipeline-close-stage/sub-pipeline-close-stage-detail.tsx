import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './sub-pipeline-close-stage.reducer';

export const SubPipelineCloseStageDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const subPipelineCloseStageEntity = useAppSelector(state => state.subPipelineCloseStage.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="subPipelineCloseStageDetailsHeading">
          <Translate contentKey="crmApp.subPipelineCloseStage.detail.title">SubPipelineCloseStage</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{subPipelineCloseStageEntity.id}</dd>
          <dt>
            <span id="index">
              <Translate contentKey="crmApp.subPipelineCloseStage.index">Index</Translate>
            </span>
          </dt>
          <dd>{subPipelineCloseStageEntity.index}</dd>
          <dt>
            <Translate contentKey="crmApp.subPipelineCloseStage.stage">Stage</Translate>
          </dt>
          <dd>{subPipelineCloseStageEntity.stage ? subPipelineCloseStageEntity.stage.id : ''}</dd>
          <dt>
            <Translate contentKey="crmApp.subPipelineCloseStage.subPipeline">Sub Pipeline</Translate>
          </dt>
          <dd>{subPipelineCloseStageEntity.subPipeline ? subPipelineCloseStageEntity.subPipeline.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/sub-pipeline-close-stage" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/sub-pipeline-close-stage/${subPipelineCloseStageEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default SubPipelineCloseStageDetail;
