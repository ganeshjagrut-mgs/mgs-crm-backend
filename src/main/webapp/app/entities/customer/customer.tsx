import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { byteSize, Translate, getPaginationState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './customer.reducer';

export const Customer = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const customerList = useAppSelector(state => state.customer.entities);
  const loading = useAppSelector(state => state.customer.loading);
  const totalItems = useAppSelector(state => state.customer.totalItems);

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
      <h2 id="customer-heading" data-cy="CustomerHeading">
        <Translate contentKey="crmApp.customer.home.title">Customers</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="crmApp.customer.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/customer/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="crmApp.customer.home.createLabel">Create new Customer</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {customerList && customerList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="crmApp.customer.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('name')}>
                  <Translate contentKey="crmApp.customer.name">Name</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('name')} />
                </th>
                <th className="hand" onClick={sort('description')}>
                  <Translate contentKey="crmApp.customer.description">Description</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('description')} />
                </th>
                <th className="hand" onClick={sort('companyCity')}>
                  <Translate contentKey="crmApp.customer.companyCity">Company City</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('companyCity')} />
                </th>
                <th className="hand" onClick={sort('companyArea')}>
                  <Translate contentKey="crmApp.customer.companyArea">Company Area</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('companyArea')} />
                </th>
                <th className="hand" onClick={sort('website')}>
                  <Translate contentKey="crmApp.customer.website">Website</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('website')} />
                </th>
                <th className="hand" onClick={sort('customerName')}>
                  <Translate contentKey="crmApp.customer.customerName">Customer Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('customerName')} />
                </th>
                <th className="hand" onClick={sort('currencyType')}>
                  <Translate contentKey="crmApp.customer.currencyType">Currency Type</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('currencyType')} />
                </th>
                <th className="hand" onClick={sort('maxInvoiceAmount')}>
                  <Translate contentKey="crmApp.customer.maxInvoiceAmount">Max Invoice Amount</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('maxInvoiceAmount')} />
                </th>
                <th className="hand" onClick={sort('gstNo')}>
                  <Translate contentKey="crmApp.customer.gstNo">Gst No</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('gstNo')} />
                </th>
                <th className="hand" onClick={sort('panNo')}>
                  <Translate contentKey="crmApp.customer.panNo">Pan No</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('panNo')} />
                </th>
                <th className="hand" onClick={sort('serviceTaxNo')}>
                  <Translate contentKey="crmApp.customer.serviceTaxNo">Service Tax No</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('serviceTaxNo')} />
                </th>
                <th className="hand" onClick={sort('tanNo')}>
                  <Translate contentKey="crmApp.customer.tanNo">Tan No</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('tanNo')} />
                </th>
                <th className="hand" onClick={sort('customFieldData')}>
                  <Translate contentKey="crmApp.customer.customFieldData">Custom Field Data</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('customFieldData')} />
                </th>
                <th className="hand" onClick={sort('correlationId')}>
                  <Translate contentKey="crmApp.customer.correlationId">Correlation Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('correlationId')} />
                </th>
                <th className="hand" onClick={sort('accountNo')}>
                  <Translate contentKey="crmApp.customer.accountNo">Account No</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('accountNo')} />
                </th>
                <th className="hand" onClick={sort('gstStateName')}>
                  <Translate contentKey="crmApp.customer.gstStateName">Gst State Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('gstStateName')} />
                </th>
                <th className="hand" onClick={sort('gstStateCode')}>
                  <Translate contentKey="crmApp.customer.gstStateCode">Gst State Code</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('gstStateCode')} />
                </th>
                <th className="hand" onClick={sort('isSubmitSampleWithoutPO')}>
                  <Translate contentKey="crmApp.customer.isSubmitSampleWithoutPO">Is Submit Sample Without PO</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('isSubmitSampleWithoutPO')} />
                </th>
                <th className="hand" onClick={sort('isBlock')}>
                  <Translate contentKey="crmApp.customer.isBlock">Is Block</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('isBlock')} />
                </th>
                <th className="hand" onClick={sort('accountType')}>
                  <Translate contentKey="crmApp.customer.accountType">Account Type</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('accountType')} />
                </th>
                <th className="hand" onClick={sort('accountManagement')}>
                  <Translate contentKey="crmApp.customer.accountManagement">Account Management</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('accountManagement')} />
                </th>
                <th className="hand" onClick={sort('revenuePotential')}>
                  <Translate contentKey="crmApp.customer.revenuePotential">Revenue Potential</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('revenuePotential')} />
                </th>
                <th className="hand" onClick={sort('samplePotential')}>
                  <Translate contentKey="crmApp.customer.samplePotential">Sample Potential</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('samplePotential')} />
                </th>
                <th className="hand" onClick={sort('remarks')}>
                  <Translate contentKey="crmApp.customer.remarks">Remarks</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('remarks')} />
                </th>
                <th className="hand" onClick={sort('totalPipeline')}>
                  <Translate contentKey="crmApp.customer.totalPipeline">Total Pipeline</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('totalPipeline')} />
                </th>
                <th className="hand" onClick={sort('type')}>
                  <Translate contentKey="crmApp.customer.type">Type</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('type')} />
                </th>
                <th>
                  <Translate contentKey="crmApp.customer.company">Company</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="crmApp.customer.user">User</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="crmApp.customer.customerType">Customer Type</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="crmApp.customer.customerStatus">Customer Status</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="crmApp.customer.ownershipType">Ownership Type</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="crmApp.customer.industryType">Industry Type</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="crmApp.customer.customerCategory">Customer Category</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="crmApp.customer.paymentTerms">Payment Terms</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="crmApp.customer.invoiceFrequency">Invoice Frequency</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="crmApp.customer.gstTreatment">Gst Treatment</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="crmApp.customer.outstandingPerson">Outstanding Person</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="crmApp.customer.department">Department</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="crmApp.customer.tenat">Tenat</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {customerList.map((customer, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/customer/${customer.id}`} color="link" size="sm">
                      {customer.id}
                    </Button>
                  </td>
                  <td>{customer.name}</td>
                  <td>{customer.description}</td>
                  <td>{customer.companyCity}</td>
                  <td>{customer.companyArea}</td>
                  <td>{customer.website}</td>
                  <td>{customer.customerName}</td>
                  <td>{customer.currencyType}</td>
                  <td>{customer.maxInvoiceAmount}</td>
                  <td>{customer.gstNo}</td>
                  <td>{customer.panNo}</td>
                  <td>{customer.serviceTaxNo}</td>
                  <td>{customer.tanNo}</td>
                  <td>{customer.customFieldData}</td>
                  <td>{customer.correlationId}</td>
                  <td>{customer.accountNo}</td>
                  <td>{customer.gstStateName}</td>
                  <td>{customer.gstStateCode}</td>
                  <td>{customer.isSubmitSampleWithoutPO ? 'true' : 'false'}</td>
                  <td>{customer.isBlock ? 'true' : 'false'}</td>
                  <td>
                    <Translate contentKey={`crmApp.AccountType.${customer.accountType}`} />
                  </td>
                  <td>
                    <Translate contentKey={`crmApp.AccountManagement.${customer.accountManagement}`} />
                  </td>
                  <td>{customer.revenuePotential}</td>
                  <td>{customer.samplePotential}</td>
                  <td>{customer.remarks}</td>
                  <td>{customer.totalPipeline}</td>
                  <td>{customer.type}</td>
                  <td>{customer.company ? <Link to={`/customer-company/${customer.company.id}`}>{customer.company.id}</Link> : ''}</td>
                  <td>{customer.user ? customer.user.id : ''}</td>
                  <td>
                    {customer.customerType ? (
                      <Link to={`/master-static-type/${customer.customerType.id}`}>{customer.customerType.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {customer.customerStatus ? (
                      <Link to={`/master-static-type/${customer.customerStatus.id}`}>{customer.customerStatus.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {customer.ownershipType ? (
                      <Link to={`/master-static-type/${customer.ownershipType.id}`}>{customer.ownershipType.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {customer.industryType ? (
                      <Link to={`/master-static-type/${customer.industryType.id}`}>{customer.industryType.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {customer.customerCategory ? (
                      <Link to={`/master-static-type/${customer.customerCategory.id}`}>{customer.customerCategory.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {customer.paymentTerms ? (
                      <Link to={`/master-static-type/${customer.paymentTerms.id}`}>{customer.paymentTerms.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {customer.invoiceFrequency ? (
                      <Link to={`/master-static-type/${customer.invoiceFrequency.id}`}>{customer.invoiceFrequency.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {customer.gstTreatment ? (
                      <Link to={`/master-static-type/${customer.gstTreatment.id}`}>{customer.gstTreatment.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>{customer.outstandingPerson ? customer.outstandingPerson.id : ''}</td>
                  <td>{customer.department ? <Link to={`/department/${customer.department.id}`}>{customer.department.id}</Link> : ''}</td>
                  <td>{customer.tenat ? <Link to={`/tenant/${customer.tenat.id}`}>{customer.tenat.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/customer/${customer.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/customer/${customer.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                          (window.location.href = `/customer/${customer.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
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
              <Translate contentKey="crmApp.customer.home.notFound">No Customers found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={customerList && customerList.length > 0 ? '' : 'd-none'}>
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

export default Customer;
