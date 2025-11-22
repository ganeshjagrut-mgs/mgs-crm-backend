import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './task.reducer';

export const TaskDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const taskEntity = useAppSelector(state => state.task.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="taskDetailsHeading">
          <Translate contentKey="crmApp.task.detail.title">Task</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{taskEntity.id}</dd>
          <dt>
            <span id="taskType">
              <Translate contentKey="crmApp.task.taskType">Task Type</Translate>
            </span>
          </dt>
          <dd>{taskEntity.taskType}</dd>
          <dt>
            <span id="dueDate">
              <Translate contentKey="crmApp.task.dueDate">Due Date</Translate>
            </span>
          </dt>
          <dd>{taskEntity.dueDate ? <TextFormat value={taskEntity.dueDate} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="taskName">
              <Translate contentKey="crmApp.task.taskName">Task Name</Translate>
            </span>
          </dt>
          <dd>{taskEntity.taskName}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="crmApp.task.status">Status</Translate>
            </span>
          </dt>
          <dd>{taskEntity.status}</dd>
          <dt>
            <span id="taskCompletionDate">
              <Translate contentKey="crmApp.task.taskCompletionDate">Task Completion Date</Translate>
            </span>
          </dt>
          <dd>
            {taskEntity.taskCompletionDate ? (
              <TextFormat value={taskEntity.taskCompletionDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="correlationId">
              <Translate contentKey="crmApp.task.correlationId">Correlation Id</Translate>
            </span>
          </dt>
          <dd>{taskEntity.correlationId}</dd>
          <dt>
            <Translate contentKey="crmApp.task.taskOwner">Task Owner</Translate>
          </dt>
          <dd>{taskEntity.taskOwner ? taskEntity.taskOwner.id : ''}</dd>
          <dt>
            <Translate contentKey="crmApp.task.customer">Customer</Translate>
          </dt>
          <dd>{taskEntity.customer ? taskEntity.customer.id : ''}</dd>
          <dt>
            <Translate contentKey="crmApp.task.relatedTo">Related To</Translate>
          </dt>
          <dd>{taskEntity.relatedTo ? taskEntity.relatedTo.id : ''}</dd>
          <dt>
            <Translate contentKey="crmApp.task.pipeline">Pipeline</Translate>
          </dt>
          <dd>{taskEntity.pipeline ? taskEntity.pipeline.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/task" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/task/${taskEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default TaskDetail;
