import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { byteSize, Translate, TextFormat, getPaginationState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './pipeline-audit.reducer';

export const PipelineAudit = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const pipelineAuditList = useAppSelector(state => state.pipelineAudit.entities);
  const loading = useAppSelector(state => state.pipelineAudit.loading);
  const totalItems = useAppSelector(state => state.pipelineAudit.totalItems);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(pageLocation.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [pageLocation.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = paginationState.sort;
    const order = paginationState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    } else {
      return order === ASC ? faSortUp : faSortDown;
    }
  };

  return (
    <div>
      <h2 id="pipeline-audit-heading" data-cy="PipelineAuditHeading">
        <Translate contentKey="crmApp.pipelineAudit.home.title">Pipeline Audits</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="crmApp.pipelineAudit.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/pipeline-audit/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="crmApp.pipelineAudit.home.createLabel">Create new Pipeline Audit</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {pipelineAuditList && pipelineAuditList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="crmApp.pipelineAudit.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('eventTimestamp')}>
                  <Translate contentKey="crmApp.pipelineAudit.eventTimestamp">Event Timestamp</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('eventTimestamp')} />
                </th>
                <th className="hand" onClick={sort('action')}>
                  <Translate contentKey="crmApp.pipelineAudit.action">Action</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('action')} />
                </th>
                <th className="hand" onClick={sort('rowId')}>
                  <Translate contentKey="crmApp.pipelineAudit.rowId">Row Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('rowId')} />
                </th>
                <th className="hand" onClick={sort('changes')}>
                  <Translate contentKey="crmApp.pipelineAudit.changes">Changes</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('changes')} />
                </th>
                <th className="hand" onClick={sort('correlationId')}>
                  <Translate contentKey="crmApp.pipelineAudit.correlationId">Correlation Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('correlationId')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {pipelineAuditList.map((pipelineAudit, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/pipeline-audit/${pipelineAudit.id}`} color="link" size="sm">
                      {pipelineAudit.id}
                    </Button>
                  </td>
                  <td>
                    {pipelineAudit.eventTimestamp ? (
                      <TextFormat type="date" value={pipelineAudit.eventTimestamp} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{pipelineAudit.action}</td>
                  <td>{pipelineAudit.rowId}</td>
                  <td>{pipelineAudit.changes}</td>
                  <td>{pipelineAudit.correlationId}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/pipeline-audit/${pipelineAudit.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/pipeline-audit/${pipelineAudit.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() =>
                          (window.location.href = `/pipeline-audit/${pipelineAudit.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
                        }
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="crmApp.pipelineAudit.home.notFound">No Pipeline Audits found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={pipelineAuditList && pipelineAuditList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default PipelineAudit;
