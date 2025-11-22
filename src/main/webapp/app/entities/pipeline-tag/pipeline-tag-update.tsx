import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IPipeline } from 'app/shared/model/pipeline.model';
import { getEntities as getPipelines } from 'app/entities/pipeline/pipeline.reducer';
import { IPipelineTag } from 'app/shared/model/pipeline-tag.model';
import { getEntity, updateEntity, createEntity, reset } from './pipeline-tag.reducer';

export const PipelineTagUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const pipelines = useAppSelector(state => state.pipeline.entities);
  const pipelineTagEntity = useAppSelector(state => state.pipelineTag.entity);
  const loading = useAppSelector(state => state.pipelineTag.loading);
  const updating = useAppSelector(state => state.pipelineTag.updating);
  const updateSuccess = useAppSelector(state => state.pipelineTag.updateSuccess);

  const handleClose = () => {
    navigate('/pipeline-tag' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getPipelines({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }

    const entity = {
      ...pipelineTagEntity,
      ...values,
      pipeline: pipelines.find(it => it.id.toString() === values.pipeline.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...pipelineTagEntity,
          pipeline: pipelineTagEntity?.pipeline?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="crmApp.pipelineTag.home.createOrEditLabel" data-cy="PipelineTagCreateUpdateHeading">
            <Translate contentKey="crmApp.pipelineTag.home.createOrEditLabel">Create or edit a PipelineTag</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="pipeline-tag-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('crmApp.pipelineTag.name')}
                id="pipeline-tag-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                id="pipeline-tag-pipeline"
                name="pipeline"
                data-cy="pipeline"
                label={translate('crmApp.pipelineTag.pipeline')}
                type="select"
                required
              >
                <option value="" key="0" />
                {pipelines
                  ? pipelines.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/pipeline-tag" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default PipelineTagUpdate;
