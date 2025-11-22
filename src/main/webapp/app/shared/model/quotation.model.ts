import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { ICustomer } from 'app/shared/model/customer.model';
import { IMasterStaticType } from 'app/shared/model/master-static-type.model';
import { DiscountLevelTypeEnum } from 'app/shared/model/enumerations/discount-level-type-enum.model';
import { DiscountTypeEnum } from 'app/shared/model/enumerations/discount-type-enum.model';
import { PDFGenerationStatus } from 'app/shared/model/enumerations/pdf-generation-status.model';
import { TestReportEmailStatus } from 'app/shared/model/enumerations/test-report-email-status.model';
import { PriceDataSourceEnum } from 'app/shared/model/enumerations/price-data-source-enum.model';

export interface IQuotation {
  id?: number;
  quotationNumber?: string | null;
  quotationDate?: dayjs.Dayjs | null;
  referenceNumber?: string | null;
  referenceDate?: dayjs.Dayjs | null;
  estimateDate?: dayjs.Dayjs | null;
  subject?: string | null;
  validity?: dayjs.Dayjs | null;
  additionalNote?: string | null;
  discountLevelType?: keyof typeof DiscountLevelTypeEnum | null;
  discountType?: keyof typeof DiscountTypeEnum | null;
  discountTypeValue?: number | null;
  currency?: string | null;
  subTotal?: number | null;
  grandTotal?: number | null;
  totalTaxAmount?: number | null;
  adjustmentAmount?: number | null;
  statusReason?: string | null;
  pdfGenerationStatus?: keyof typeof PDFGenerationStatus | null;
  emailStatus?: keyof typeof TestReportEmailStatus | null;
  emailFailureReason?: string | null;
  customParagraph?: string | null;
  correlationId?: string | null;
  approvedAt?: dayjs.Dayjs | null;
  priceDataSource?: keyof typeof PriceDataSourceEnum | null;
  user?: IUser | null;
  customer?: ICustomer;
  paymentTerm?: IMasterStaticType | null;
  quotationStatus?: IMasterStaticType | null;
}

export const defaultValue: Readonly<IQuotation> = {};
