import { IUser } from 'app/shared/model/user.model';
import { ICustomer } from 'app/shared/model/customer.model';
import { IMasterStaticType } from 'app/shared/model/master-static-type.model';

export interface ILead {
  id?: number;
  name?: string;
  leadNumber?: string | null;
  annualRevenue?: number | null;
  user?: IUser | null;
  customer?: ICustomer | null;
  leadSource?: IMasterStaticType | null;
  industryType?: IMasterStaticType | null;
  leadStatus?: IMasterStaticType | null;
}

export const defaultValue: Readonly<ILead> = {};
