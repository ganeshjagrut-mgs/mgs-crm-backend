import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './pipeline-tag.reducer';

export const PipelineTagDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const pipelineTagEntity = useAppSelector(state => state.pipelineTag.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="pipelineTagDetailsHeading">
          <Translate contentKey="crmApp.pipelineTag.detail.title">PipelineTag</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{pipelineTagEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="crmApp.pipelineTag.name">Name</Translate>
            </span>
          </dt>
          <dd>{pipelineTagEntity.name}</dd>
          <dt>
            <Translate contentKey="crmApp.pipelineTag.pipeline">Pipeline</Translate>
          </dt>
          <dd>{pipelineTagEntity.pipeline ? pipelineTagEntity.pipeline.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/pipeline-tag" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/pipeline-tag/${pipelineTagEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PipelineTagDetail;
