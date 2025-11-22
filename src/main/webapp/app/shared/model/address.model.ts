import { ICity } from 'app/shared/model/city.model';
import { IState } from 'app/shared/model/state.model';
import { ICountry } from 'app/shared/model/country.model';
import { ICustomer } from 'app/shared/model/customer.model';
import { ITenant } from 'app/shared/model/tenant.model';

export interface IAddress {
  id?: number;
  addressLine1?: string | null;
  addressLine2?: string | null;
  pincode?: number | null;
  isPrimary?: boolean | null;
  city?: ICity | null;
  state?: IState | null;
  country?: ICountry | null;
  customer?: ICustomer | null;
  tenant?: ITenant | null;
}

export const defaultValue: Readonly<IAddress> = {
  isPrimary: false,
};
