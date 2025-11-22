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
import { ISubPipelineCloseStage } from 'app/shared/model/sub-pipeline-close-stage.model';
import { getEntity, updateEntity, createEntity, reset } from './sub-pipeline-close-stage.reducer';

export const SubPipelineCloseStageUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const masterStaticTypes = useAppSelector(state => state.masterStaticType.entities);
  const subPipelines = useAppSelector(state => state.subPipeline.entities);
  const subPipelineCloseStageEntity = useAppSelector(state => state.subPipelineCloseStage.entity);
  const loading = useAppSelector(state => state.subPipelineCloseStage.loading);
  const updating = useAppSelector(state => state.subPipelineCloseStage.updating);
  const updateSuccess = useAppSelector(state => state.subPipelineCloseStage.updateSuccess);

  const handleClose = () => {
    navigate('/sub-pipeline-close-stage' + location.search);
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
      ...subPipelineCloseStageEntity,
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
          ...subPipelineCloseStageEntity,
          stage: subPipelineCloseStageEntity?.stage?.id,
          subPipeline: subPipelineCloseStageEntity?.subPipeline?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="crmApp.subPipelineCloseStage.home.createOrEditLabel" data-cy="SubPipelineCloseStageCreateUpdateHeading">
            <Translate contentKey="crmApp.subPipelineCloseStage.home.createOrEditLabel">Create or edit a SubPipelineCloseStage</Translate>
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
                  id="sub-pipeline-close-stage-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('crmApp.subPipelineCloseStage.index')}
                id="sub-pipeline-close-stage-index"
                name="index"
                data-cy="index"
                type="text"
              />
              <ValidatedField
                id="sub-pipeline-close-stage-stage"
                name="stage"
                data-cy="stage"
                label={translate('crmApp.subPipelineCloseStage.stage')}
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
                id="sub-pipeline-close-stage-subPipeline"
                name="subPipeline"
                data-cy="subPipeline"
                label={translate('crmApp.subPipelineCloseStage.subPipeline')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/sub-pipeline-close-stage" replace color="info">
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

export default SubPipelineCloseStageUpdate;
