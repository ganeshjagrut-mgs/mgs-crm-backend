import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './encryption.reducer';

export const EncryptionDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const encryptionEntity = useAppSelector(state => state.encryption.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="encryptionDetailsHeading">
          <Translate contentKey="crmApp.encryption.detail.title">Encryption</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{encryptionEntity.id}</dd>
          <dt>
            <span id="key">
              <Translate contentKey="crmApp.encryption.key">Key</Translate>
            </span>
          </dt>
          <dd>{encryptionEntity.key}</dd>
          <dt>
            <span id="pin">
              <Translate contentKey="crmApp.encryption.pin">Pin</Translate>
            </span>
          </dt>
          <dd>{encryptionEntity.pin}</dd>
          <dt>
            <Translate contentKey="crmApp.encryption.tenant">Tenant</Translate>
          </dt>
          <dd>{encryptionEntity.tenant ? encryptionEntity.tenant.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/encryption" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/encryption/${encryptionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default EncryptionDetail;
