import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './pipeline.reducer';

export const PipelineDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const pipelineEntity = useAppSelector(state => state.pipeline.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="pipelineDetailsHeading">
          <Translate contentKey="crmApp.pipeline.detail.title">Pipeline</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{pipelineEntity.id}</dd>
          <dt>
            <span id="pipelineName">
              <Translate contentKey="crmApp.pipeline.pipelineName">Pipeline Name</Translate>
            </span>
          </dt>
          <dd>{pipelineEntity.pipelineName}</dd>
          <dt>
            <span id="totalAmount">
              <Translate contentKey="crmApp.pipeline.totalAmount">Total Amount</Translate>
            </span>
          </dt>
          <dd>{pipelineEntity.totalAmount}</dd>
          <dt>
            <span id="noOfSamples">
              <Translate contentKey="crmApp.pipeline.noOfSamples">No Of Samples</Translate>
            </span>
          </dt>
          <dd>{pipelineEntity.noOfSamples}</dd>
          <dt>
            <span id="correlationId">
              <Translate contentKey="crmApp.pipeline.correlationId">Correlation Id</Translate>
            </span>
          </dt>
          <dd>{pipelineEntity.correlationId}</dd>
          <dt>
            <Translate contentKey="crmApp.pipeline.pipelineOwner">Pipeline Owner</Translate>
          </dt>
          <dd>{pipelineEntity.pipelineOwner ? pipelineEntity.pipelineOwner.id : ''}</dd>
          <dt>
            <Translate contentKey="crmApp.pipeline.customer">Customer</Translate>
          </dt>
          <dd>{pipelineEntity.customer ? pipelineEntity.customer.id : ''}</dd>
          <dt>
            <Translate contentKey="crmApp.pipeline.stageOfPipeline">Stage Of Pipeline</Translate>
          </dt>
          <dd>{pipelineEntity.stageOfPipeline ? pipelineEntity.stageOfPipeline.id : ''}</dd>
          <dt>
            <Translate contentKey="crmApp.pipeline.subPipeline">Sub Pipeline</Translate>
          </dt>
          <dd>{pipelineEntity.subPipeline ? pipelineEntity.subPipeline.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/pipeline" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/pipeline/${pipelineEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PipelineDetail;
