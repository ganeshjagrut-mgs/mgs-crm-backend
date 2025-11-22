import { ICustomer } from 'app/shared/model/customer.model';

export interface IMasterCategory {
  id?: number;
  name?: string;
  description?: string | null;
  code?: string | null;
  customers?: ICustomer[] | null;
}

export const defaultValue: Readonly<IMasterCategory> = {};
