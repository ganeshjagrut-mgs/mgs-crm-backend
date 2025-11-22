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

import { getEntities } from './complaint.reducer';

export const Complaint = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const complaintList = useAppSelector(state => state.complaint.entities);
  const loading = useAppSelector(state => state.complaint.loading);
  const totalItems = useAppSelector(state => state.complaint.totalItems);

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
      <h2 id="complaint-heading" data-cy="ComplaintHeading">
        <Translate contentKey="crmApp.complaint.home.title">Complaints</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="crmApp.complaint.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/complaint/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="crmApp.complaint.home.createLabel">Create new Complaint</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {complaintList && complaintList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="crmApp.complaint.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('complaintNumber')}>
                  <Translate contentKey="crmApp.complaint.complaintNumber">Complaint Number</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('complaintNumber')} />
                </th>
                <th className="hand" onClick={sort('complaintDate')}>
                  <Translate contentKey="crmApp.complaint.complaintDate">Complaint Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('complaintDate')} />
                </th>
                <th className="hand" onClick={sort('recordNumbers')}>
                  <Translate contentKey="crmApp.complaint.recordNumbers">Record Numbers</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('recordNumbers')} />
                </th>
                <th className="hand" onClick={sort('customerContactNumber')}>
                  <Translate contentKey="crmApp.complaint.customerContactNumber">Customer Contact Number</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('customerContactNumber')} />
                </th>
                <th className="hand" onClick={sort('customerContactEmail')}>
                  <Translate contentKey="crmApp.complaint.customerContactEmail">Customer Contact Email</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('customerContactEmail')} />
                </th>
                <th className="hand" onClick={sort('complaintDescription')}>
                  <Translate contentKey="crmApp.complaint.complaintDescription">Complaint Description</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('complaintDescription')} />
                </th>
                <th className="hand" onClick={sort('expectedClosureDate')}>
                  <Translate contentKey="crmApp.complaint.expectedClosureDate">Expected Closure Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('expectedClosureDate')} />
                </th>
                <th className="hand" onClick={sort('rootCause')}>
                  <Translate contentKey="crmApp.complaint.rootCause">Root Cause</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('rootCause')} />
                </th>
                <th className="hand" onClick={sort('complaintStatus')}>
                  <Translate contentKey="crmApp.complaint.complaintStatus">Complaint Status</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('complaintStatus')} />
                </th>
                <th className="hand" onClick={sort('correctiveAction')}>
                  <Translate contentKey="crmApp.complaint.correctiveAction">Corrective Action</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('correctiveAction')} />
                </th>
                <th className="hand" onClick={sort('preventiveAction')}>
                  <Translate contentKey="crmApp.complaint.preventiveAction">Preventive Action</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('preventiveAction')} />
                </th>
                <th className="hand" onClick={sort('complaintClosureDate')}>
                  <Translate contentKey="crmApp.complaint.complaintClosureDate">Complaint Closure Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('complaintClosureDate')} />
                </th>
                <th>
                  <Translate contentKey="crmApp.complaint.customerName">Customer Name</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="crmApp.complaint.complaintRelatedTo">Complaint Related To</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="crmApp.complaint.typeOfComplaint">Type Of Complaint</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {complaintList.map((complaint, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/complaint/${complaint.id}`} color="link" size="sm">
                      {complaint.id}
                    </Button>
                  </td>
                  <td>{complaint.complaintNumber}</td>
                  <td>
                    {complaint.complaintDate ? <TextFormat type="date" value={complaint.complaintDate} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>{complaint.recordNumbers}</td>
                  <td>{complaint.customerContactNumber}</td>
                  <td>{complaint.customerContactEmail}</td>
                  <td>{complaint.complaintDescription}</td>
                  <td>
                    {complaint.expectedClosureDate ? (
                      <TextFormat type="date" value={complaint.expectedClosureDate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{complaint.rootCause}</td>
                  <td>
                    <Translate contentKey={`crmApp.ComplaintStatus.${complaint.complaintStatus}`} />
                  </td>
                  <td>{complaint.correctiveAction}</td>
                  <td>{complaint.preventiveAction}</td>
                  <td>
                    {complaint.complaintClosureDate ? (
                      <TextFormat type="date" value={complaint.complaintClosureDate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {complaint.customerName ? <Link to={`/customer/${complaint.customerName.id}`}>{complaint.customerName.id}</Link> : ''}
                  </td>
                  <td>
                    {complaint.complaintRelatedTo ? (
                      <Link to={`/master-static-type/${complaint.complaintRelatedTo.id}`}>{complaint.complaintRelatedTo.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {complaint.typeOfComplaint ? (
                      <Link to={`/master-static-type/${complaint.typeOfComplaint.id}`}>{complaint.typeOfComplaint.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/complaint/${complaint.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/complaint/${complaint.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                          (window.location.href = `/complaint/${complaint.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
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
              <Translate contentKey="crmApp.complaint.home.notFound">No Complaints found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={complaintList && complaintList.length > 0 ? '' : 'd-none'}>
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

export default Complaint;
