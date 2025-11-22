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

import { getEntities } from './quotation.reducer';

export const Quotation = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const quotationList = useAppSelector(state => state.quotation.entities);
  const loading = useAppSelector(state => state.quotation.loading);
  const totalItems = useAppSelector(state => state.quotation.totalItems);

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
      <h2 id="quotation-heading" data-cy="QuotationHeading">
        <Translate contentKey="crmApp.quotation.home.title">Quotations</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="crmApp.quotation.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/quotation/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="crmApp.quotation.home.createLabel">Create new Quotation</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {quotationList && quotationList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="crmApp.quotation.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('quotationNumber')}>
                  <Translate contentKey="crmApp.quotation.quotationNumber">Quotation Number</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('quotationNumber')} />
                </th>
                <th className="hand" onClick={sort('quotationDate')}>
                  <Translate contentKey="crmApp.quotation.quotationDate">Quotation Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('quotationDate')} />
                </th>
                <th className="hand" onClick={sort('referenceNumber')}>
                  <Translate contentKey="crmApp.quotation.referenceNumber">Reference Number</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('referenceNumber')} />
                </th>
                <th className="hand" onClick={sort('referenceDate')}>
                  <Translate contentKey="crmApp.quotation.referenceDate">Reference Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('referenceDate')} />
                </th>
                <th className="hand" onClick={sort('estimateDate')}>
                  <Translate contentKey="crmApp.quotation.estimateDate">Estimate Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('estimateDate')} />
                </th>
                <th className="hand" onClick={sort('subject')}>
                  <Translate contentKey="crmApp.quotation.subject">Subject</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('subject')} />
                </th>
                <th className="hand" onClick={sort('validity')}>
                  <Translate contentKey="crmApp.quotation.validity">Validity</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('validity')} />
                </th>
                <th className="hand" onClick={sort('additionalNote')}>
                  <Translate contentKey="crmApp.quotation.additionalNote">Additional Note</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('additionalNote')} />
                </th>
                <th className="hand" onClick={sort('discountLevelType')}>
                  <Translate contentKey="crmApp.quotation.discountLevelType">Discount Level Type</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('discountLevelType')} />
                </th>
                <th className="hand" onClick={sort('discountType')}>
                  <Translate contentKey="crmApp.quotation.discountType">Discount Type</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('discountType')} />
                </th>
                <th className="hand" onClick={sort('discountTypeValue')}>
                  <Translate contentKey="crmApp.quotation.discountTypeValue">Discount Type Value</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('discountTypeValue')} />
                </th>
                <th className="hand" onClick={sort('currency')}>
                  <Translate contentKey="crmApp.quotation.currency">Currency</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('currency')} />
                </th>
                <th className="hand" onClick={sort('subTotal')}>
                  <Translate contentKey="crmApp.quotation.subTotal">Sub Total</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('subTotal')} />
                </th>
                <th className="hand" onClick={sort('grandTotal')}>
                  <Translate contentKey="crmApp.quotation.grandTotal">Grand Total</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('grandTotal')} />
                </th>
                <th className="hand" onClick={sort('totalTaxAmount')}>
                  <Translate contentKey="crmApp.quotation.totalTaxAmount">Total Tax Amount</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('totalTaxAmount')} />
                </th>
                <th className="hand" onClick={sort('adjustmentAmount')}>
                  <Translate contentKey="crmApp.quotation.adjustmentAmount">Adjustment Amount</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('adjustmentAmount')} />
                </th>
                <th className="hand" onClick={sort('statusReason')}>
                  <Translate contentKey="crmApp.quotation.statusReason">Status Reason</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('statusReason')} />
                </th>
                <th className="hand" onClick={sort('pdfGenerationStatus')}>
                  <Translate contentKey="crmApp.quotation.pdfGenerationStatus">Pdf Generation Status</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('pdfGenerationStatus')} />
                </th>
                <th className="hand" onClick={sort('emailStatus')}>
                  <Translate contentKey="crmApp.quotation.emailStatus">Email Status</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('emailStatus')} />
                </th>
                <th className="hand" onClick={sort('emailFailureReason')}>
                  <Translate contentKey="crmApp.quotation.emailFailureReason">Email Failure Reason</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('emailFailureReason')} />
                </th>
                <th className="hand" onClick={sort('customParagraph')}>
                  <Translate contentKey="crmApp.quotation.customParagraph">Custom Paragraph</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('customParagraph')} />
                </th>
                <th className="hand" onClick={sort('correlationId')}>
                  <Translate contentKey="crmApp.quotation.correlationId">Correlation Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('correlationId')} />
                </th>
                <th className="hand" onClick={sort('approvedAt')}>
                  <Translate contentKey="crmApp.quotation.approvedAt">Approved At</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('approvedAt')} />
                </th>
                <th className="hand" onClick={sort('priceDataSource')}>
                  <Translate contentKey="crmApp.quotation.priceDataSource">Price Data Source</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('priceDataSource')} />
                </th>
                <th>
                  <Translate contentKey="crmApp.quotation.user">User</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="crmApp.quotation.customer">Customer</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="crmApp.quotation.paymentTerm">Payment Term</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="crmApp.quotation.quotationStatus">Quotation Status</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {quotationList.map((quotation, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/quotation/${quotation.id}`} color="link" size="sm">
                      {quotation.id}
                    </Button>
                  </td>
                  <td>{quotation.quotationNumber}</td>
                  <td>
                    {quotation.quotationDate ? <TextFormat type="date" value={quotation.quotationDate} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>{quotation.referenceNumber}</td>
                  <td>
                    {quotation.referenceDate ? <TextFormat type="date" value={quotation.referenceDate} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>
                    {quotation.estimateDate ? <TextFormat type="date" value={quotation.estimateDate} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>{quotation.subject}</td>
                  <td>{quotation.validity ? <TextFormat type="date" value={quotation.validity} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{quotation.additionalNote}</td>
                  <td>
                    <Translate contentKey={`crmApp.DiscountLevelTypeEnum.${quotation.discountLevelType}`} />
                  </td>
                  <td>
                    <Translate contentKey={`crmApp.DiscountTypeEnum.${quotation.discountType}`} />
                  </td>
                  <td>{quotation.discountTypeValue}</td>
                  <td>{quotation.currency}</td>
                  <td>{quotation.subTotal}</td>
                  <td>{quotation.grandTotal}</td>
                  <td>{quotation.totalTaxAmount}</td>
                  <td>{quotation.adjustmentAmount}</td>
                  <td>{quotation.statusReason}</td>
                  <td>
                    <Translate contentKey={`crmApp.PDFGenerationStatus.${quotation.pdfGenerationStatus}`} />
                  </td>
                  <td>
                    <Translate contentKey={`crmApp.TestReportEmailStatus.${quotation.emailStatus}`} />
                  </td>
                  <td>{quotation.emailFailureReason}</td>
                  <td>{quotation.customParagraph}</td>
                  <td>{quotation.correlationId}</td>
                  <td>{quotation.approvedAt ? <TextFormat type="date" value={quotation.approvedAt} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>
                    <Translate contentKey={`crmApp.PriceDataSourceEnum.${quotation.priceDataSource}`} />
                  </td>
                  <td>{quotation.user ? quotation.user.id : ''}</td>
                  <td>{quotation.customer ? <Link to={`/customer/${quotation.customer.id}`}>{quotation.customer.id}</Link> : ''}</td>
                  <td>
                    {quotation.paymentTerm ? (
                      <Link to={`/master-static-type/${quotation.paymentTerm.id}`}>{quotation.paymentTerm.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {quotation.quotationStatus ? (
                      <Link to={`/master-static-type/${quotation.quotationStatus.id}`}>{quotation.quotationStatus.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/quotation/${quotation.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/quotation/${quotation.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                          (window.location.href = `/quotation/${quotation.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
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
              <Translate contentKey="crmApp.quotation.home.notFound">No Quotations found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={quotationList && quotationList.length > 0 ? '' : 'd-none'}>
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

export default Quotation;
