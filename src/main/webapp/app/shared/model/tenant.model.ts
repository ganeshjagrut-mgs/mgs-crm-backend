import { IAddress } from 'app/shared/model/address.model';
import { IUser } from 'app/shared/model/user.model';
import { IEncryption } from 'app/shared/model/encryption.model';

export interface ITenant {
  id?: number;
  companyName?: string;
  contactPerson?: string | null;
  logo?: string | null;
  website?: string | null;
  registrationNumber?: string | null;
  subId?: number | null;
  addresses?: IAddress[] | null;
  users?: IUser[] | null;
  encryption?: IEncryption | null;
}

export const defaultValue: Readonly<ITenant> = {};
