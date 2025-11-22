import { IAddress } from 'app/shared/model/address.model';
import { ICustomerCompany } from 'app/shared/model/customer-company.model';
import { IUser } from 'app/shared/model/user.model';
import { IMasterStaticType } from 'app/shared/model/master-static-type.model';
import { IDepartment } from 'app/shared/model/department.model';
import { ITenant } from 'app/shared/model/tenant.model';
import { IMasterCategory } from 'app/shared/model/master-category.model';
import { AccountType } from 'app/shared/model/enumerations/account-type.model';
import { AccountManagement } from 'app/shared/model/enumerations/account-management.model';

export interface ICustomer {
  id?: number;
  name?: string | null;
  description?: string | null;
  companyCity?: string | null;
  companyArea?: string | null;
  website?: string | null;
  customerName?: string | null;
  currencyType?: string | null;
  maxInvoiceAmount?: number | null;
  gstNo?: string | null;
  panNo?: string | null;
  serviceTaxNo?: string | null;
  tanNo?: string | null;
  customFieldData?: string | null;
  correlationId?: string | null;
  accountNo?: string | null;
  gstStateName?: string | null;
  gstStateCode?: string | null;
  isSubmitSampleWithoutPO?: boolean | null;
  isBlock?: boolean | null;
  accountType?: keyof typeof AccountType | null;
  accountManagement?: keyof typeof AccountManagement | null;
  revenuePotential?: number | null;
  samplePotential?: number | null;
  remarks?: string | null;
  totalPipeline?: number | null;
  type?: string | null;
  addresses?: IAddress[] | null;
  company?: ICustomerCompany | null;
  user?: IUser | null;
  customerType?: IMasterStaticType | null;
  customerStatus?: IMasterStaticType | null;
  ownershipType?: IMasterStaticType | null;
  industryType?: IMasterStaticType | null;
  customerCategory?: IMasterStaticType | null;
  paymentTerms?: IMasterStaticType | null;
  invoiceFrequency?: IMasterStaticType | null;
  gstTreatment?: IMasterStaticType | null;
  outstandingPerson?: IUser | null;
  department?: IDepartment | null;
  tenat?: ITenant | null;
  masterCategories?: IMasterCategory[] | null;
}

export const defaultValue: Readonly<ICustomer> = {
  isSubmitSampleWithoutPO: false,
  isBlock: false,
};
