import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, getPaginationState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './pipeline.reducer';

export const Pipeline = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const pipelineList = useAppSelector(state => state.pipeline.entities);
  const loading = useAppSelector(state => state.pipeline.loading);
  const totalItems = useAppSelector(state => state.pipeline.totalItems);

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
      <h2 id="pipeline-heading" data-cy="PipelineHeading">
        <Translate contentKey="crmApp.pipeline.home.title">Pipelines</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="crmApp.pipeline.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/pipeline/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="crmApp.pipeline.home.createLabel">Create new Pipeline</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {pipelineList && pipelineList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="crmApp.pipeline.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('pipelineName')}>
                  <Translate contentKey="crmApp.pipeline.pipelineName">Pipeline Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('pipelineName')} />
                </th>
                <th className="hand" onClick={sort('totalAmount')}>
                  <Translate contentKey="crmApp.pipeline.totalAmount">Total Amount</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('totalAmount')} />
                </th>
                <th className="hand" onClick={sort('noOfSamples')}>
                  <Translate contentKey="crmApp.pipeline.noOfSamples">No Of Samples</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('noOfSamples')} />
                </th>
                <th className="hand" onClick={sort('correlationId')}>
                  <Translate contentKey="crmApp.pipeline.correlationId">Correlation Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('correlationId')} />
                </th>
                <th>
                  <Translate contentKey="crmApp.pipeline.pipelineOwner">Pipeline Owner</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="crmApp.pipeline.customer">Customer</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="crmApp.pipeline.stageOfPipeline">Stage Of Pipeline</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="crmApp.pipeline.subPipeline">Sub Pipeline</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {pipelineList.map((pipeline, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/pipeline/${pipeline.id}`} color="link" size="sm">
                      {pipeline.id}
                    </Button>
                  </td>
                  <td>{pipeline.pipelineName}</td>
                  <td>{pipeline.totalAmount}</td>
                  <td>{pipeline.noOfSamples}</td>
                  <td>{pipeline.correlationId}</td>
                  <td>{pipeline.pipelineOwner ? pipeline.pipelineOwner.id : ''}</td>
                  <td>{pipeline.customer ? <Link to={`/customer/${pipeline.customer.id}`}>{pipeline.customer.id}</Link> : ''}</td>
                  <td>
                    {pipeline.stageOfPipeline ? (
                      <Link to={`/master-static-type/${pipeline.stageOfPipeline.id}`}>{pipeline.stageOfPipeline.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {pipeline.subPipeline ? <Link to={`/sub-pipeline/${pipeline.subPipeline.id}`}>{pipeline.subPipeline.id}</Link> : ''}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/pipeline/${pipeline.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/pipeline/${pipeline.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                          (window.location.href = `/pipeline/${pipeline.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
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
              <Translate contentKey="crmApp.pipeline.home.notFound">No Pipelines found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={pipelineList && pipelineList.length > 0 ? '' : 'd-none'}>
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

export default Pipeline;
