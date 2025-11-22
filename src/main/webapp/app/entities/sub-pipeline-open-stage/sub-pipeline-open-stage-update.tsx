import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IMasterStaticType } from 'app/shared/model/master-static-type.model';
import { getEntities as getMasterStaticTypes } from 'app/entities/master-static-type/master-static-type.reducer';
import { ISubPipeline } from 'app/shared/model/sub-pipeline.model';
import { getEntities as getSubPipelines } from 'app/entities/sub-pipeline/sub-pipeline.reducer';
import { ISubPipelineOpenStage } from 'app/shared/model/sub-pipeline-open-stage.model';
import { getEntity, updateEntity, createEntity, reset } from './sub-pipeline-open-stage.reducer';

export const SubPipelineOpenStageUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const masterStaticTypes = useAppSelector(state => state.masterStaticType.entities);
  const subPipelines = useAppSelector(state => state.subPipeline.entities);
  const subPipelineOpenStageEntity = useAppSelector(state => state.subPipelineOpenStage.entity);
  const loading = useAppSelector(state => state.subPipelineOpenStage.loading);
  const updating = useAppSelector(state => state.subPipelineOpenStage.updating);
  const updateSuccess = useAppSelector(state => state.subPipelineOpenStage.updateSuccess);

  const handleClose = () => {
    navigate('/sub-pipeline-open-stage' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getMasterStaticTypes({}));
    dispatch(getSubPipelines({}));
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
    if (values.index !== undefined && typeof values.index !== 'number') {
      values.index = Number(values.index);
    }

    const entity = {
      ...subPipelineOpenStageEntity,
      ...values,
      stage: masterStaticTypes.find(it => it.id.toString() === values.stage.toString()),
      subPipeline: subPipelines.find(it => it.id.toString() === values.subPipeline.toString()),
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
          ...subPipelineOpenStageEntity,
          stage: subPipelineOpenStageEntity?.stage?.id,
          subPipeline: subPipelineOpenStageEntity?.subPipeline?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="crmApp.subPipelineOpenStage.home.createOrEditLabel" data-cy="SubPipelineOpenStageCreateUpdateHeading">
            <Translate contentKey="crmApp.subPipelineOpenStage.home.createOrEditLabel">Create or edit a SubPipelineOpenStage</Translate>
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
                  id="sub-pipeline-open-stage-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('crmApp.subPipelineOpenStage.index')}
                id="sub-pipeline-open-stage-index"
                name="index"
                data-cy="index"
                type="text"
              />
              <ValidatedField
                id="sub-pipeline-open-stage-stage"
                name="stage"
                data-cy="stage"
                label={translate('crmApp.subPipelineOpenStage.stage')}
                type="select"
                required
              >
                <option value="" key="0" />
                {masterStaticTypes
                  ? masterStaticTypes.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <ValidatedField
                id="sub-pipeline-open-stage-subPipeline"
                name="subPipeline"
                data-cy="subPipeline"
                label={translate('crmApp.subPipelineOpenStage.subPipeline')}
                type="select"
                required
              >
                <option value="" key="0" />
                {subPipelines
                  ? subPipelines.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/sub-pipeline-open-stage" replace color="info">
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

export default SubPipelineOpenStageUpdate;
